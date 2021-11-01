package com.mindata.es.superheroeschallenge.config;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AopApplicationConfig {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@AfterThrowing(pointcut = "@annotation(RequestExecutionTime)", throwing = "ex")
	public void apiLogExceptions(JoinPoint joinPoint, Throwable ex) {
		log.error("An exception occurs in{}.{}() with cause {}", joinPoint.getSignature().getDeclaringTypeName(),
				joinPoint.getSignature().getName(), ex.getCause() != null ? ex.getCause().getMessage() : "NULL");
	}

	@Around("@annotation(RequestExecutionTime)")
	public Object logAPiRequests(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();

		var startExecution = System.currentTimeMillis();
		var result = proceedingJoinPoint.proceed();
		var endExecution = startExecution - System.currentTimeMillis();

		log.info("SuperHeroe Challenge API, Execution time of {}.{} in {}",
				methodSignature.getDeclaringType().getSimpleName(), methodSignature.getName(), endExecution);

		return result;

	}
}
