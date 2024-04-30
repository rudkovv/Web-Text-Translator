package com.translate.webtranslator.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.translate.webtranslator.counter.RequestCounter;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class RequestCounterAspect {
	
	private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);
	
	@Pointcut("@annotation(RequestCounterAnnotation)")
	public void methodForRequestCounter() {
		
	}
	
	@Before("methodForRequestCounter()")
	public void requestCounterIncrementAndLogIt(final JoinPoint joinPoint) {
		RequestCounter.increment();
		logger.info("Increment requestCounter from {}.{}()." 
				+ "Current value of requestCounter is {}",
                joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName(),
                RequestCounter.getCount());
	}
}