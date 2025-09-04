package com.domino.smerp.auth;

import com.domino.smerp.user.User;
import com.domino.smerp.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByLoginId(username)
                                  .orElseThrow(() -> new UsernameNotFoundException("해당 유저 없음"));
        return new CustomUserDetails(user);
    }
}
