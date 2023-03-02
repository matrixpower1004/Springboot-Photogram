package com.matrix.photogram.handler.aop;

import java.util.HashMap;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.matrix.photogram.handler.ex.CustomValidationApiException;
import com.matrix.photogram.handler.ex.CustomValidationException;

@Component //RestController, Service 등이 Component를 상속해서 만들어진 구현체.
@Aspect //
public class ValidationAdvice {

	@Around("execution(* com.matrix.photogram.web.api.*Controller.*(..))")
	public Object apiAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		
//		System.out.println("web api controller =================================");
		Object[] args = proceedingJoinPoint.getArgs();
		for (Object arg : args) {
			if(arg instanceof BindingResult) {
				BindingResult bindingResult = (BindingResult) arg; //다운캐스팅
				
				if (bindingResult.hasErrors()) {
					Map<String, String> errorMap = new HashMap<>();
					
					for (FieldError error : bindingResult.getFieldErrors()) {
						errorMap.put(error.getField(), error.getDefaultMessage());
					}
					throw new CustomValidationApiException("유효성검사 실패함", errorMap);
					//throw를 던지는 순간 밑의 코드들은 무효화된다.
				}
			}
		}
		
		//proceedingJoinPoint -> Around에서 선언한 controller의 모든 메서드에 접근할 할 수 있는 변수. ex)UserController의 profile() 메서드
		//profile() 메서드보다 먼저 실행된다.
		
		return proceedingJoinPoint.proceed(); //profile() 메서드가 실행된다
	}
	
	@Around("execution(* com.matrix.photogram.web.*Controller.*(..))")
	public Object advice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		
//		System.out.println("web controller =================================");
		Object[] args = proceedingJoinPoint.getArgs();
		for (Object arg : args) {
			if(arg instanceof BindingResult) {
				BindingResult bindingResult = (BindingResult) arg; //다운캐스팅
				
				if (bindingResult.hasErrors()) {
					Map<String, String> errorMap = new HashMap<>();
					
					for (FieldError error : bindingResult.getFieldErrors()) {
						errorMap.put(error.getField(), error.getDefaultMessage());
					}
					throw new CustomValidationException("유효성검사 실패함", errorMap);
				}
			}
		}
		
		return proceedingJoinPoint.proceed();
	}
}
