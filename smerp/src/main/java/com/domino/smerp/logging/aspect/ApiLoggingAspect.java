package com.domino.smerp.logging.aspect;

import com.domino.smerp.logging.domain.ApiLog;
import com.domino.smerp.logging.repository.ApiLogRepository;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
@RequiredArgsConstructor
public class ApiLoggingAspect {

    private final ApiLogRepository apiLogRepository;

    @Around("execution(* com.domino.smerp..*Controller.*(..))")
    public Object logApi(ProceedingJoinPoint joinPoint) throws Throwable {

        long start = System.currentTimeMillis();

        ServletRequestAttributes attrs =
            (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        HttpServletRequest request = attrs.getRequest();

        Authentication authentication =
            SecurityContextHolder.getContext().getAuthentication();

        String principal =
            (authentication != null) ? authentication.getName() : "UNKNOWN";

        Object result = joinPoint.proceed();

        long duration = System.currentTimeMillis() - start;

        ApiLog apiLog = ApiLog.builder()
            .method(request.getMethod())
            .uri(request.getRequestURI())
            .status(200) // 정상 흐름만 기록
            .duration(duration)
            .clientIp(request.getRemoteAddr())
            .principal(principal)
            .timestamp(LocalDateTime.now())
            .build();

        apiLogRepository.save(apiLog);

        return result;
    }
}
