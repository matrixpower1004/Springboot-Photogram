package com.matrix.photogram.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import com.matrix.photogram.handler.ex.CustomApiException;
import com.matrix.photogram.handler.ex.CustomException;
import com.matrix.photogram.handler.ex.CustomValidationApiException;
import com.matrix.photogram.handler.ex.CustomValidationException;
import com.matrix.photogram.util.Script;
import com.matrix.photogram.web.dto.CMRespDto;

@RestController
@ControllerAdvice // 모든 컨트롤러에서 발생하는 에러를 낚아챔
public class ControllerExceptionHandler {
	
	// https://jeong-pro.tistory.com/195 -> ExceptionHandler, ControllerAdvice
	@ExceptionHandler(CustomValidationException.class)
	public String validationException(CustomValidationException e) { // 제네릭타입 리턴할때는 ?로 적는게 간편함
		// CMrespDto, Script비교
		// 1.클라이언트에게 응답할 때는 Script가 좋음.
		// 2.Ajax통신 - CMRespDto
		// 3.Android통신 - CMRespDto
		if (e.getErrorMap() == null) {
			return Script.back(e.getMessage());
		} else {
			return Script.back(e.getErrorMap().toString());
		}
	}
	
	@ExceptionHandler(CustomException.class)
	public String validationException(CustomException e) {
		return Script.back(e.getMessage().toString());
	}
	
	
	@ExceptionHandler(CustomValidationApiException.class)
	public ResponseEntity<?> validationApiException(CustomValidationApiException e) { // 제네릭타입 리턴할때는 ?로 적는게 간편함
		return new ResponseEntity<>(new CMRespDto<>(
				-1, e.getMessage(), e.gerErrorMap()), HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(CustomApiException.class)
	public ResponseEntity<?> apiException(CustomApiException e) { // 제네릭타입 리턴할때는 ?로 적는게 간편함
		return new ResponseEntity<>(new CMRespDto<>(
				-1, e.getMessage(),null), HttpStatus.BAD_REQUEST);
	}
}
