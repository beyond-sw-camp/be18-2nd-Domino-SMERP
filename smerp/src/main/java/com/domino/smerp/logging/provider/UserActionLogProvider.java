package com.domino.smerp.logging.provider;

import com.domino.smerp.logging.snapshot.UserSnapshot;
import com.domino.smerp.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserActionLogProvider implements ActionLogEntityProvider {

    private final UserRepository userRepository;

    @Override
    public String getEntity() {
        return "USER";
    }

    @Override
    public Object loadSnapshot(String entityId) {
        return userRepository.findByEmpNo(entityId)
            .map(UserSnapshot::from)
            .orElse(null);
    }
}
