package com.domino.smerp.log.audit;

import org.hibernate.envers.RevisionListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class RevListener implements RevisionListener {
    @Override
    public void newRevision(Object revisionEntity) {
        RevInfo rev = (RevInfo) revisionEntity;
        // SecurityContext에서 사용자명 꺼내기 (미로그인 환경이면 'system')
        String actor = "";
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            actor = auth.getName();
        }
        rev.setActor(actor);
    }
}
