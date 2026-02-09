package com.siddardha.redisDemonstration.AOP;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class ExecutionTimeAspect {

    private final static Logger log = LoggerFactory.getLogger(ExecutionTimeAspect.class);

    @Around("execution(public * com.siddardha.redisDemonstration.Service..*(..))")
    public Object logExecutionTime(ProceedingJoinPoint pjp) throws Throwable {
        Long start = System.nanoTime();
        try {
            return pjp.proceed();
        } finally {
            long durationMs = (System.nanoTime() - start) / 1_000_000;
            // toShortString → ClassName.method(..)
            log.info("{} executed in {} ms", pjp.getSignature().toShortString(), durationMs);
        }
    }
}
