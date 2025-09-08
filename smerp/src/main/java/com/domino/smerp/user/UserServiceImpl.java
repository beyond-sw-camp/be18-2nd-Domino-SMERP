package com.domino.smerp.user;

import com.domino.smerp.client.Client;
import com.domino.smerp.client.ClientRepository;
import com.domino.smerp.common.encrypt.SsnEncryptor;
import com.domino.smerp.common.exception.CustomException;
import com.domino.smerp.common.exception.ErrorCode;
import com.domino.smerp.user.constants.UserRole;
import com.domino.smerp.user.dto.request.CreateUserRequest;
import com.domino.smerp.user.dto.request.UpdateUserRequest;
import com.domino.smerp.user.dto.response.UserListResponse;
import com.domino.smerp.user.dto.response.UserResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;
    private final SsnEncryptor ssnEncryptor;

    @Override
    @Transactional
    public void createUser(final CreateUserRequest request) {

        String encryptedSsn = ssnEncryptor.encryptSsn(request.getSsn());
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
        }
        if (userRepository.existsByPhone(request.getPhone())) {
            throw new CustomException(ErrorCode.DUPLICATE_PHONE);
        }
        if (userRepository.existsByLoginId(request.getLoginId())) {
            throw new CustomException(ErrorCode.DUPLICATE_LOGINID);
        }
        if (userRepository.existsBySsn(encryptedSsn)) {
            throw new CustomException(ErrorCode.DUPLICATE_SSN);
        }

        Client client = null;
        if (request.getClientId() != null) {
            client = clientRepository.findById(request.getClientId())
                                     .orElseThrow(
                                         () -> new CustomException(ErrorCode.CLIENT_NOT_FOUND));
        }

        String empNo = generateEmpNo(request.getHireDate());

        User user = User.builder()
                        .name(request.getName())
                        .email(request.getEmail())
                        .phone(request.getPhone())
                        .address(request.getAddress())
                        .ssn(encryptedSsn)
                        .loginId(request.getLoginId())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .hireDate(request.getHireDate())
                        .fireDate(
                            request.getFireDate() != null ? request.getFireDate() : null)
                        .deptTitle(request.getDeptTitle())
                        .role(UserRole.valueOf(request.getRole()))
                        .empNo(empNo)
                        .client(client)
                        .build();

        userRepository.save(user);
    }


    @Override
    @Transactional(readOnly = true)
    public List<UserListResponse> findAllUsers() {

        List<User> allUser = userRepository.findAll();

        return allUser.stream()
                      .map(users -> UserListResponse.builder()
                                                    .name(users.getName())
                                                    .email(users.getEmail())
                                                    .address(users.getAddress())
                                                    .phone(users.getPhone())
                                                    .deptTitle(users.getDeptTitle())
                                                    .role(String.valueOf(users.getRole()))
                                                    .build())
                      .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteUser(final Long UserId) {

        User user = userRepository.findById(UserId)
                                  .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        userRepository.deleteById(UserId);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse findUserById(final Long userId) {

        User user = userRepository.findById(userId)
                                  .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Client client = user.getClient();

        return UserResponse.builder()
                           .userId(user.getUserId())
                           .name(user.getName())
                           .email(user.getEmail())
                           .phone(user.getPhone())
                           .address(user.getAddress())
                           .ssn(ssnEncryptor.decryptSsn(user.getSsn()))
                           .hireDate(user.getHireDate())
                           .fireDate(user.getFireDate())
                           .loginId(user.getLoginId())
                           .deptTitle(user.getDeptTitle())
                           .role(user.getRole())
                           .empNo(user.getEmpNo())
                           .clientName(client != null ? client.getCompanyName() : "거래처 아님")
                           .build();
    }

    @Override
    @Transactional
    public void updateUser(final Long userId, final UpdateUserRequest request) {

        User user = userRepository.findById(userId)
                                  .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        user.updateUser(request);
    }

    private String generateEmpNo(LocalDate hireDate) {

        String yearMonth = hireDate.format(DateTimeFormatter.ofPattern("yyyyMM"));

        String lastEmpNo = userRepository.findLastEmpNoByYearMonth(yearMonth);

        int nextSeq = 1;
        if (lastEmpNo != null) {
            nextSeq = Integer.parseInt(lastEmpNo.substring(6)) + 1;
        }

        return String.format("%s%03d", yearMonth, nextSeq);
    }
}
