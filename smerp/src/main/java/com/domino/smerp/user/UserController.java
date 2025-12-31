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
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<Void> createUser(
        @Valid @RequestBody final CreateUserRequest request
    ) {
        userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<PageResponse<UserListResponse>> searchUsers(
        @RequestParam(required = false) final String name,
        @RequestParam(required = false) final String deptTitle,
        @PageableDefault(size = 20, sort = "userId", direction = Sort.Direction.DESC)
        Pageable pageable
    ) {
        return ResponseEntity.ok(
            userService.searchUsers(name, deptTitle, pageable)
        );
    }

    @GetMapping("/{enpNo}")
    public ResponseEntity<UserResponse> findUserById(
        @PathVariable final String enpNo
    ) {
        return ResponseEntity.ok(
            userService.findUserByEnpNo(enpNo)
        );
    }

    @PatchMapping("/{enpNo}")
    public ResponseEntity<Void> updateUser(
        @PathVariable final String enpNo,
        @Valid @RequestBody final UpdateUserRequest request
    ) {
        userService.updateUser(enpNo, request);
        return ResponseEntity.noContent().build(); // 204
    }

    @DeleteMapping("/{enpNo}")
    public ResponseEntity<Void> deleteUser(
        @PathVariable final String enpNo
    ) {
        userService.deleteUser(enpNo);
        return ResponseEntity.noContent().build(); // 204
    }
}

