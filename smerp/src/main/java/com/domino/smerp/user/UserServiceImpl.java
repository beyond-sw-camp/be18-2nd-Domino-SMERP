package com.domino.smerp.user;

import com.domino.smerp.client.Client;
import com.domino.smerp.client.ClientRepository;
import com.domino.smerp.user.constants.UserRole;
import com.domino.smerp.user.dto.request.CreateUserRequest;
import com.domino.smerp.user.dto.request.UpdateUserRequest;
import com.domino.smerp.user.dto.response.UserListResponse;
import com.domino.smerp.user.dto.response.UserResponse;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ClientRepository clientRepository;

    @Override
    @Transactional
    public void createUser(CreateUserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("중복된 이메일");
        }
        if (userRepository.existsByPhone(request.getPhone())) {
            throw new IllegalArgumentException("중복된 전화번호");
        }
        if (userRepository.existsByLoginId(request.getLoginId())) {
            throw new IllegalArgumentException("중복된 아이디");
        }
        if (userRepository.existsBySsn(request.getSsn())) {
            throw new IllegalArgumentException("중복된 주민번호");
        }

        Client client = null;

        if (request.getClientId() != null) {
            client = clientRepository.findById(request.getClientId())
                                     .orElseThrow(
                                         () -> new IllegalArgumentException("없는 클라이언트"));
        }

        User user = User.builder()
                        .name(request.getName())
                        .email(request.getEmail())
                        .phone(request.getPhone())
                        .address(request.getAddress())
                        .ssn(request.getSsn())
                        .loginId(request.getLoginId())
                        .password(request.getPassword())
                        .hireDate(LocalDate.parse(request.getHireDate()))
                        .fireDate(
                            request.getFireDate() != null ? LocalDate.parse(request.getFireDate())
                                : null)
                        .deptTitle(request.getDeptTitle())
                        .role(UserRole.valueOf(request.getRole()))
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
    public void deleteUser(Long UserId) {
        userRepository.deleteById(UserId);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse findUserById(Long userId) {
        User user = userRepository.findById(userId)
                                  .orElseThrow(() -> new IllegalArgumentException("해당유저 없음"));

        Client client = user.getClient();

        return UserResponse.builder()
                           .userId(user.getUserId())
                           .name(user.getName())
                           .email(user.getEmail())
                           .phone(user.getPhone())
                           .address(user.getAddress())
                           .ssn(user.getSsn())
                           .hireDate(user.getHireDate())
                           .fireDate(user.getFireDate())
                           .loginId(user.getLoginId())
                           .deptTitle(user.getDeptTitle())
                           .role(user.getRole())
                           .clientName(client != null ? client.getCompanyName() : "거래처 아님")
                           .build();
    }

    @Override
    @Transactional
    public void updateUser(Long userId,UpdateUserRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("해당유저 없음"));
        user.updateUser(request);
    }
}
