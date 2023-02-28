package com.matrix.photogram.web;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.matrix.photogram.domain.user.User;
import com.matrix.photogram.handler.ex.CustomValidationException;
import com.matrix.photogram.service.AuthService;
import com.matrix.photogram.web.dto.auth.SignupDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor // final 필드를 DI할때 사용.
@Controller //1.IoC 2.파일을 리턴하는 컨트롤러
public class AuthController {
	
	private static final Logger log = LoggerFactory.getLogger(AuthController.class);
	
	// 전역변수에 final이 걸려 있으면 무조건 생성자가 실행될 때 또는 객체가 만들어질 때 초기화를 해줘야 함  
	private final AuthService authService;
	
	// Autowired를 붙이거나 생성자를 직접 만들어야 한다.
//	public AuthController(AuthService authService) {
//		this.authService = authService;
//	}
	
	@GetMapping("/auth/signin")
	public String signinForm() {
		return "auth/signin";
	}
	
	@GetMapping("/auth/signup")
	public String signupForm() {
		return "auth/signup";
	}

	// 회원가입버튼 -> /auth/signup -> /auth/signin
	// 회원가입버튼 X -> 동작하지 않은 상태
	@PostMapping("/auth/signup")
	public String signup(@Valid SignupDto signupDto, BindingResult bindingResult) { //key=value(x-www-form-urlencoded) -> 스프링에서 기본적으로 데이터를 받아주는 방식
		//리턴 타입 앞에 @Responsebody가 있으면 Controller이긴 해도 data를 응답한다.
		if (bindingResult.hasErrors()) {
			Map<String, String> errorMap = new HashMap<>();
			
			for (FieldError error : bindingResult.getFieldErrors()) {
				errorMap.put(error.getField(), error.getDefaultMessage());
//				System.out.println("==============================================");
//				System.out.println(error.getDefaultMessage());
//				System.out.println("==============================================");
			}
			throw new CustomValidationException("유효성검사 실패함", errorMap);

		} else {
//			log.info(signupDto.toString());
			// User <- SignupDto
			User user = signupDto.toEntity();
//			log.info(user.toString());
			User userEntity = authService.memberJoin(user);
//			System.out.println(userEntity);
			return "auth/signin"; //회원가입 완료 후 로그인 페이지로 이동
		}

	}
}

