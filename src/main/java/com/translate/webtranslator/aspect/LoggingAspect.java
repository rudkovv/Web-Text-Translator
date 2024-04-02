package com.translate.webtranslator.aspect;

import java.util.Arrays;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * The logging aspect class that provides logging functionality for annotated methods.
 * It is responsible for logging method entry, exit, exceptions, and return values.
 */
@Aspect
@Component
public class LoggingAspect {
    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);
    
    /**
     * Pointcut to match all methods in the com.translate.webtranslator.exception package.
     */
    @Pointcut("execution(* com.translate.webtranslator.exception.*.*(..))")
    public void allExceptionMethods() {
    }

    /**
     * Pointcut to match methods annotated with @AspectAnnotation.
     */
    @Pointcut("@annotation(AspectAnnotation)")
    public void forAspectAnnotation() { }

    /**
     * Advice executed before methods annotated with @AspectAnnotation.
     * Logs method entry and arguments.
     *
     * @param joinPoint The join point representing the method execution.
     */
    @Before(value = "forAspectAnnotation()")
    public void logBefore(final JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        String methodName = joinPoint.toString() + " "
                + joinPoint.getSignature().getName();
        logger.info(">> {}() - {}", methodName, Arrays.toString(args));
    }

    /**
     * Advice executed after methods annotated with @AspectAnnotation.
     * Logs method exit and return value.
     *
     * @param joinPoint The join point representing the method execution.
     * @param result    The return value of the method.
     */
    @AfterReturning(value = "forAspectAnnotation()", returning = "result")
    public void logAfter(final JoinPoint joinPoint, final Object result) {
        String methodName = joinPoint.toString();
        logger.info("<< {}() - {}", methodName, result);
    }
    
    /**
     * Advice executed after methods in the com.translate.webtranslator.exception package.
     * Logs method exit and return value in case of exceptions.
     *
     * @param joinPoint The join point representing the method execution.
     * @param result    The return value of the method.
     */
    @AfterReturning(value = "allExceptionMethods()", returning = "result")
    public void logExceptionAfter(final JoinPoint joinPoint, final Object result) {
        String methodName = joinPoint.toString();
        logger.error("<< {}() - {}", methodName, result);
    }

    /**
     * Advice executed when exceptions are thrown from methods annotated with @AspectAnnotation.
     * Logs the exception.
     *
     * @param joinPoint The join point representing the method execution.
     * @param exception The thrown exception.
     */
    @AfterThrowing(pointcut = "forAspectAnnotation()", throwing = "exception")
    public void logException(final JoinPoint joinPoint, final Throwable exception) {
        String methodName = joinPoint.toString();
        logger.error("<< {}() - {}", methodName, exception.getMessage());
    }
}