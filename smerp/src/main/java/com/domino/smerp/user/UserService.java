package com.domino.smerp.user;

import com.domino.smerp.common.dto.PageResponse;
import com.domino.smerp.user.dto.request.CreateUserRequest;
import com.domino.smerp.user.dto.request.UpdateUserRequest;
import com.domino.smerp.user.dto.response.UserListResponse;
import com.domino.smerp.user.dto.response.UserResponse;
import org.springframework.data.domain.Pageable;

public interface UserService {
    User createUser(final CreateUserRequest request);

    PageResponse<UserListResponse> searchUsers(final String name, final String deptTitle, final Pageable pageable);

    void deleteUser(final String enpNo);

    UserResponse findUserByEnpNo(final String enpNo);

    void updateUser(final String enpNo,final UpdateUserRequest request);
}
