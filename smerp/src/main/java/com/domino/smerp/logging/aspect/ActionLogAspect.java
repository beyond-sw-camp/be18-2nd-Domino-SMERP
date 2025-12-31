package com.domino.smerp.logging.aspect;

import com.domino.smerp.logging.provider.ActionLogEntityProvider;
import com.domino.smerp.logging.provider.ActionLogEntityProviderRegistry;
import com.domino.smerp.logging.snapshot.UserSnapshot;
import com.domino.smerp.logging.annotation.ActionLoggable;
import com.domino.smerp.logging.domain.ActionLog;
import com.domino.smerp.logging.repository.ActionLogRepository;
import com.domino.smerp.user.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@RequiredArgsConstructor
@Aspect
@Component
public class ActionLogAspect {

    private final ActionLogRepository repository;
    private final ObjectMapper objectMapper;
    private final ActionLogEntityProviderRegistry providerRegistry;

    @PersistenceContext
    private EntityManager em;

    @Around("@annotation(actionLoggable)")
    public Object logAction(
        ProceedingJoinPoint joinPoint,
        ActionLoggable actionLoggable
    ) throws Throwable {

        MethodSignature signature =
            (MethodSignature) joinPoint.getSignature();

        EvaluationContext context =
            buildContext(signature, joinPoint.getArgs());

        String action = actionLoggable.action();
        String entity = actionLoggable.entity();

        HttpServletRequest request =
            ((ServletRequestAttributes)
                RequestContextHolder.getRequestAttributes())
                .getRequest();

        String actor = getPrincipal();
        String clientIp = request.getRemoteAddr();

        String entityId = null;
        String beforeJson = null;

        if (!"CREATE".equals(action)) {
            entityId = parseSpel(actionLoggable.entityId(), context);
            beforeJson = toJson(loadSnapshot(entity, entityId));
        }

        try {
            Object result = joinPoint.proceed();
            em.flush();

            if ("CREATE".equals(action)) {
                context.setVariable("result", result);
                entityId = parseSpel(actionLoggable.entityId(), context);
            }

            String afterJson =
                "DELETE".equals(action) ? null
                    : toJson(loadSnapshot(entity, entityId));

            repository.save(ActionLog.builder()
                .action(action)
                .entity(entity)
                .entityId(entityId)
                .success(true)
                .beforeData(beforeJson)
                .afterData(afterJson)
                .actor(actor)
                .clientIp(clientIp)
                .timestamp(LocalDateTime.now())
                .build()
            );

            return result;

        } catch (Exception ex) {

            repository.save(ActionLog.builder()
                .action(action)
                .entity(entity)
                .entityId(entityId)
                .success(false)
                .beforeData(beforeJson)
                .failReason(ex.getMessage())
                .actor(actor)
                .clientIp(clientIp)
                .timestamp(LocalDateTime.now())
                .build()
            );

            throw ex;
        }
    }

    private Object loadSnapshot(String entity, String entityId) {
        if (entityId == null) return null;

        ActionLogEntityProvider provider =
            providerRegistry.getProvider(entity);

        return provider != null
            ? provider.loadSnapshot(entityId)
            : null;
    }

    private EvaluationContext buildContext(
        MethodSignature sig, Object[] args
    ) {
        EvaluationContext ctx = new StandardEvaluationContext();
        String[] names = sig.getParameterNames();
        for (int i = 0; i < names.length; i++) {
            ctx.setVariable(names[i], args[i]);
        }
        return ctx;
    }

    private String parseSpel(String spel, EvaluationContext ctx) {
        return new SpelExpressionParser()
            .parseExpression(spel)
            .getValue(ctx, String.class);
    }

    private String getPrincipal() {
        Authentication auth =
            SecurityContextHolder.getContext().getAuthentication();
        return auth != null ? auth.getName() : "SYSTEM";
    }

    private String toJson(Object obj) {
        try {
            return obj == null ? null : objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            return null;
        }
    }
}


