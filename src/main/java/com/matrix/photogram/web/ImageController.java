package com.matrix.photogram.web;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.matrix.photogram.cofig.auth.PrincipalDetails;
import com.matrix.photogram.handler.ex.CustomValidationException;
import com.matrix.photogram.service.ImageService;
import com.matrix.photogram.web.dto.image.ImageUploadDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class ImageController {

	private final ImageService imageService;
	
	@GetMapping({"/", "/image/story"})
	public String story() {
		return "image/story";
	}
	
	@GetMapping("/image/popular")
	public String popular() {
		return "image/popular";
	}
	
	@GetMapping("/image/upload")
	public String upload() {
		return "image/upload";
	}
	
	@PostMapping("/image")
	public String imageUpload(ImageUploadDto imageUploadDto, @AuthenticationPrincipal PrincipalDetails principalDetails) {
		
		if (imageUploadDto.getFile().isEmpty()) {
			throw new CustomValidationException("이미지가 첨부되지 않았습니다.", null);
		}
		// Image에서 caption과 postImage 파일을 받아와야 함
		// 서비스 호출
		imageService.photoUpload(imageUploadDto, principalDetails);
		return "redirect:/user/"+principalDetails.getUser().getId();
		// 이미지 업로드 완료 후 현재 로그인한 유저의 id 프로필 페이지로 돌아가기 -> /user/id
	}
}
