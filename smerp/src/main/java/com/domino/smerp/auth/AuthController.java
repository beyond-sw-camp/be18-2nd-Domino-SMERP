package com.domino.smerp.auth;

import com.domino.smerp.auth.dto.UserLoginRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final AuthService authService;
    @PostMapping("/login")
    public void login(@RequestBody UserLoginRequest request, HttpSession session) {
        authService.login(request.getLoginId(),request.getPassword(),session);
        log.info("Login Success ID ${}",request.getLoginId());
        log.info("Login Success PASSWORD ${}",request.getPassword());
    }

    @PostMapping("/logout")
    public void logout(HttpSession session) {
        authService.logout(session);
        log.info("Logout Success ID ${}",session.getId());
        log.info("Logout Success PASSWORD ${}",session.getId());
    }
}
