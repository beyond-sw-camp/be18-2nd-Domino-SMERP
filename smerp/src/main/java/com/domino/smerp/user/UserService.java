package com.domino.smerp.user;

import com.domino.smerp.user.dto.request.CreateUserRequest;
import com.domino.smerp.user.dto.request.UpdateUserRequest;
import com.domino.smerp.user.dto.response.UserListResponse;
import com.domino.smerp.user.dto.response.UserResponse;
import java.util.List;

public interface UserService {
    void createUser(CreateUserRequest request);
    List<UserListResponse> findAllUsers();
    void deleteUser(Long userId);
    UserResponse findUserById(Long userId);
    void updateUser(Long userId,UpdateUserRequest request);
}
