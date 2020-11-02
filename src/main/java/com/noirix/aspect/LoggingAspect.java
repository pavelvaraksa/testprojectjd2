package com.noirix.aspect;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.util.HashMap;
import java.util.Map;

@Component
@Aspect
public class LoggingAspect {
    private static final Logger log = Logger.getLogger(LoggingAspect.class);

    @Pointcut("execution(* com.noirix.repository..*(..))")
    public void aroundRepositoryPointcut() {
    }

    StopWatch stopWatch = new StopWatch();
    Integer callCounter = 0;

    @Around("aroundRepositoryPointcut()")
    public Object countTime(ProceedingJoinPoint joinPoint) throws Throwable {
        stopWatch.start();

        log.info("Method " + joinPoint.getSignature().getName() + " start");
        Object proceed = joinPoint.proceed();
        log.info("Method " + joinPoint.getSignature().getName() + " finished");

        Map<String, Integer> countCalls = new HashMap<>();
        String loggingAspect = joinPoint.getSignature().getName();
        countCalls.put(loggingAspect, 0);
        countCalls.put(loggingAspect, countCalls.get(loggingAspect) + 1);

        log.info("Method and count calls : " + countCalls);
        log.info("Total calls : " + ++callCounter);

        stopWatch.stop();
        log.info("Time : " + stopWatch.getTotalTimeSeconds());

        return proceed;
    }
}






