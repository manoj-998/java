package com.example.aopdemo.aspect;

import com.example.aopdemo.annotation.TrackExecution;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {

    @Pointcut("execution(* com.example.aopdemo.service..*(..))")
    public void serviceLayer() {
    }

    @Before("serviceLayer()")
    public void logBefore(JoinPoint joinPoint) {
        System.out.println("----- @Before -----");
        System.out.println("Method: " + joinPoint.getSignature().toShortString());
        System.out.println("Arguments: " + Arrays.toString(joinPoint.getArgs()));
    }

    @After("serviceLayer()")
    public void logAfter(JoinPoint joinPoint) {
        System.out.println("----- @After -----");
        System.out.println("Finished: " + joinPoint.getSignature().toShortString());
    }

    @AfterReturning(pointcut = "serviceLayer()", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        System.out.println("----- @AfterReturning -----");
        System.out.println("Method: " + joinPoint.getSignature().toShortString());
        System.out.println("Returned value: " + result);
    }

    @AfterThrowing(pointcut = "serviceLayer()", throwing = "exception")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable exception) {
        System.out.println("----- @AfterThrowing -----");
        System.out.println("Method: " + joinPoint.getSignature().toShortString());
        System.out.println("Exception: " + exception.getMessage());
    }

    @Around("@annotation(trackExecution)")
    public Object trackExecution(
            ProceedingJoinPoint joinPoint,
            TrackExecution trackExecution) throws Throwable {

        long start = System.currentTimeMillis();

        System.out.println("----- @Around Before -----");
        System.out.println("Operation: " + trackExecution.operation());

        try {
            return joinPoint.proceed();
        } finally {
            long executionTime = System.currentTimeMillis() - start;

            System.out.println("Execution time: " + executionTime + " ms");
            System.out.println("----- @Around After -----");
        }
    }
}
