package com.domino.smerp.user;

import com.domino.smerp.common.dto.PageResponse;
import com.domino.smerp.user.dto.request.CreateUserRequest;
import com.domino.smerp.user.dto.request.UpdateUserRequest;
import com.domino.smerp.user.dto.response.UserListResponse;
import com.domino.smerp.user.dto.response.UserResponse;
import org.springframework.data.domain.Pageable;

public interface UserService {
    void createUser(CreateUserRequest request);
    PageResponse<UserListResponse> searchUsers(String name, String deptTitle, Pageable pageable);
    void deleteUser(Long userId);
    UserResponse findUserByEnpNo(String enpNo);
    void updateUser(String enpNo,UpdateUserRequest request);;
}
