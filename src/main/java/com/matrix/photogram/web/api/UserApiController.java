package com.matrix.photogram.web.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.matrix.photogram.cofig.auth.PrincipalDetails;
import com.matrix.photogram.domain.user.User;
import com.matrix.photogram.handler.ex.CustomValidationApiException;
import com.matrix.photogram.service.SubscribeService;
import com.matrix.photogram.service.UserService;
import com.matrix.photogram.web.dto.CMRespDto;
import com.matrix.photogram.web.dto.subscribe.SubscribeDto;
import com.matrix.photogram.web.dto.user.UserUpdateDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class UserApiController {
	
	// 회원 수정을 해야하므로 DI
	private final UserService userService;
	private final SubscribeService subscribeService;
	
	//스토리에 보여질 사진을 업로드 할 때는 사진과 함께 caption도 입력받아야 하므로 DTO를 만들어서
	//DTO 내부에 MultiparFile로 받았지만(ImageUploadDto.java)
	//지금은 사진의 image file만 받으면 된다. -> 단 변수 이름이 중요.
	//profile.jsp에서 form 태그 내부의 name 태그에 선언되어 있는 값을 정확히 변수명으로 사용해야 값을 받아올 수 있다.
	@PutMapping("/api/user/{principalId}/profileImageUrl")
	public ResponseEntity<?> profileImageUrlUpdate(@PathVariable int principalId, MultipartFile profileImageFile,
			@AuthenticationPrincipal PrincipalDetails principalDetails) {	//회원 사진이 변경되면 session의 값이 변경되어야 한다.
		User userEntity = userService.userProfieImageUpdate(principalId, profileImageFile);
		principalDetails.setUser(userEntity);	//세션 변경
		return new ResponseEntity<>(new CMRespDto<>(1, "프로필 사진 변경 성공", null), HttpStatus.OK);
	}
	
	@GetMapping("/api/user/{pageUserId}/subscribe") // 해당 페이지의 주인 정보
	public ResponseEntity<?> subscribeList(@PathVariable int pageUserId,
			@AuthenticationPrincipal PrincipalDetails principalDetails) {
		
		List<SubscribeDto> subscribeDto = subscribeService.getSubscribeList(principalDetails.getUser().getId(), pageUserId);
		
		return new ResponseEntity<>(new CMRespDto<>(1, "구독자 정보 리스트 가져오기 성공", subscribeDto), HttpStatus.OK);
	}
	
	
	@PutMapping("/api/user/{id}")
	public CMRespDto<?> update(@PathVariable int id,
			@Valid UserUpdateDto userUpdateDto, BindingResult bindingResult, // 꼭 @Valid가 적혀있는 다음 파라메터에 적어야 함.
			@AuthenticationPrincipal PrincipalDetails principalDetails) { // js에서 보내는 값들을 받기 위한 data transfer object가 필요함
		
		if (bindingResult.hasErrors()) {
			Map<String, String> errorMap = new HashMap<>();
			
			for (FieldError error : bindingResult.getFieldErrors()) {
				errorMap.put(error.getField(), error.getDefaultMessage());
			}
			throw new CustomValidationApiException("유효성검사 실패함", errorMap);
		} else {
			User userEntity = userService.memberUpdate(id, userUpdateDto.toEntity());
			principalDetails.setUser(userEntity); // 세션정보변경
			return new CMRespDto<>(1,"회원수정완료", userEntity); // 응답시에 userEntity의 모든 getter 함수가 호출되고 JSON으로 파싱하여 응답한다.
		}
	}
}
