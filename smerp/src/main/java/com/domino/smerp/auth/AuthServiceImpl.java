package com.domino.smerp.auth;

import com.domino.smerp.common.exception.CustomException;
import com.domino.smerp.common.exception.ErrorCode;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.Manager;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    @Override
    public void login(String loginId, String password, HttpSession session) {
        if (loginId == null || loginId.isEmpty() || password == null || password.isEmpty()) {
            throw new CustomException(ErrorCode.INVALID_LOGIN);
        }
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginId, password)
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
        if (authentication.getPrincipal() instanceof CustomUserDetails customUserDetails) {
            session.setAttribute("loginId", customUserDetails.getUsername());
            session.setAttribute("name", customUserDetails.getName());
            session.setAttribute("role", customUserDetails.getAuthorities().toString());
        }
    }

    @Override
    public void logout(HttpSession session) {
        session.invalidate();
        SecurityContextHolder.getContext().setAuthentication(null);
    }
}
