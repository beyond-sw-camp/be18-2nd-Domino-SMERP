package com.domino.smerp.auth;

import jakarta.servlet.http.HttpSession;

public interface AuthService {
    void login(String loginId, String password, HttpSession session);

    void logout(HttpSession session);
}
