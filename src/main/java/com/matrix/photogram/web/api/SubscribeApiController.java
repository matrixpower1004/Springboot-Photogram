package com.matrix.photogram.web.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.matrix.photogram.cofig.auth.PrincipalDetails;
import com.matrix.photogram.handler.ex.CustomApiException;
import com.matrix.photogram.service.SubscribeService;
import com.matrix.photogram.web.dto.CMRespDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class SubscribeApiController {
	
	private final SubscribeService subscribeService;
	
	@PostMapping("/api/subscribe/{toUserId}")
	public ResponseEntity<?> subscribe(@AuthenticationPrincipal PrincipalDetails principalDetails, @PathVariable int toUserId) {
		// 현재 로그인한 사람이 ToUserId를 구독하겠다는 의미
		subscribeService.subscribe(principalDetails.getUser().getId(), toUserId);
		return new ResponseEntity<>(new CMRespDto<>(1, "구독하기 성공", null), HttpStatus.OK);
	}
	
	@DeleteMapping("/api/subscribe/{toUserId}") // ToUserId의 구독을 취소
	public ResponseEntity<?> unSubscribe(@AuthenticationPrincipal PrincipalDetails principalDetails, @PathVariable int toUserId) {
		subscribeService.unSubscribe(principalDetails.getUser().getId(), toUserId);
		return new ResponseEntity<>(new CMRespDto<>(1, "구독취소하기 성공", null), HttpStatus.OK);
	}
}
