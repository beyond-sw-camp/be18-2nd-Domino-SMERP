package com.domino.smerp.user;

import com.domino.smerp.common.dto.PageResponse;
import com.domino.smerp.user.dto.request.CreateUserRequest;
import com.domino.smerp.user.dto.request.UpdateUserRequest;
import com.domino.smerp.user.dto.response.UserListResponse;
import com.domino.smerp.user.dto.response.UserResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createUser(@Valid @RequestBody final CreateUserRequest request) {

        userService.createUser(request);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<UserListResponse> searchUsers(@RequestParam(required = false) String name,
        @RequestParam(required = false) String deptTitle,@PageableDefault(size = 20, sort = "userId", direction = Sort.Direction.DESC)
        Pageable pageable) {

        return userService.searchUsers(name, deptTitle,pageable);
    }

    @GetMapping("/{userId}")
    public UserResponse findUserById(@PathVariable final Long userId) {

        return userService.findUserById(userId);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable final Long userId) {

        userService.deleteUser(userId);
    }

    @PatchMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateUser(@PathVariable final Long userId,
        @Valid @RequestBody final UpdateUserRequest request) {

        userService.updateUser(userId, request);
    }
}
