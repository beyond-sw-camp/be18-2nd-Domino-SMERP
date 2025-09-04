package com.domino.smerp.auth;

import com.domino.smerp.auth.dto.UserLoginRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
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
    }

    @PostMapping("/logout")
    public void logout(HttpSession session) {
        authService.logout(session);
        log.info("Logout Success ID ${}",session.getId());
    }

    @GetMapping("/me")
    public String whoAmI(HttpSession session) {
        String loginId = (String) session.getAttribute("loginId");
        String realName = (String) session.getAttribute("name");
        String role = (String) session.getAttribute("role");

        log.info("현재 로그인: {}, 이름: {}, 권한: {}", loginId, realName, role);
        return String.format("Hello %s (%s) with role %s", realName, loginId, role);
    }
}
