package com.domino.smerp.log.audit;

import com.domino.smerp.common.support.SpringContext;
import com.domino.smerp.log.ActionType;
import com.domino.smerp.log.Log;
import com.domino.smerp.log.repository.LogRepository;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.WeakHashMap;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuditLogEntityListener {
    //TODO 개인정보 마스킹
    //스냅샷
    private static final ThreadLocal<Map<Object, String>> SNAPSHOTS =
        ThreadLocal.withInitial(WeakHashMap::new);

    private LogRepository logs() { return SpringContext.getBean(LogRepository.class); }

    private String actor() {
        String actor = "system";
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && auth.getName() != null) {
            actor = auth.getName();
        }
        return actor;
    }

    private String entityName(Object entity) { return entity.getClass().getSimpleName(); }


    private String toLogString(Object entity) {
        if (entity == null) return null;

        return entityName(entity) + ":" + entity.toString();
    }

    @PostLoad
    public void onLoad(Object entity) {
        SNAPSHOTS.get().put(entity, toLogString(entity));
    }

    @PostPersist
    public void afterCreate(Object entity) {
        Log log = Log.builder()
                     .actor(actor())
                     .action(ActionType.CREATE)
                     .entity(entityName(entity))
                     .doAt(LocalDateTime.now())
                     .beforeData(null)
                     .afterData(toLogString(entity))
                     .build();
        logs().save(log);

        SNAPSHOTS.get().put(entity, log.getAfterData());
    }

    @PreUpdate
    public void beforeUpdate(Object entity) { }

    @PostUpdate
    public void afterUpdate(Object entity) {
        String before = SNAPSHOTS.get().get(entity);
        String after  = toLogString(entity);

        Log log = Log.builder()
                     .actor(actor())
                     .action(ActionType.UPDATE)
                     .entity(entityName(entity))
                     .doAt(LocalDateTime.now())
                     .beforeData(before)
                     .afterData(after)
                     .build();
        logs().save(log);

        SNAPSHOTS.get().put(entity, after);
    }

    @PreRemove
    public void beforeDelete(Object entity) {
        SNAPSHOTS.get().putIfAbsent(entity, toLogString(entity));
    }

    @PostRemove
    public void afterDelete(Object entity) {
        String before = SNAPSHOTS.get().get(entity);

        Log log = Log.builder()
                     .actor(actor())
                     .action(ActionType.DELETE)
                     .entity(entityName(entity))
                     .doAt(LocalDateTime.now())
                     .beforeData(before)
                     .afterData(null)
                     .build();
        logs().save(log);

        SNAPSHOTS.get().remove(entity);
    }
}
