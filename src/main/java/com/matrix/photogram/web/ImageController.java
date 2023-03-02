package com.matrix.photogram.web;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.matrix.photogram.cofig.auth.PrincipalDetails;
import com.matrix.photogram.domain.image.Image;
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
	
	//API로 구현한다면 이유가 있어야 - 브라우저에서의 요청이 아닌 안드로이드, iOS 요청인 경우
	@GetMapping("/image/popular")
	public String popular(Model model) {	//model에 인기 사진들을 담아가야 함
		//api는 데이터를 리턴!! ajax를 써야 하는 경우 api controller를 만든다.
		
		List<Image> images = imageService.popularPhoto();
		model.addAttribute("images", images);
		
		return "image/popular";		//페이지를 만들어서 리턴
	}
	
	@GetMapping("/image/upload")
	public String upload() {
		return "image/upload";
	}
	
	@PostMapping("/image")
	public String imageUpload(ImageUploadDto imageUploadDto, @AuthenticationPrincipal PrincipalDetails principalDetails) {
		
		//공통처리 불가능한 부분
		if (imageUploadDto.getFile().isEmpty()) {
			throw new CustomValidationException("이미지가 첨부되지 않았습니다.", null);
		}
		
		// Image에서 caption과 postImage 파일을 받아와야 함
		imageService.photoUpload(imageUploadDto, principalDetails);
		return "redirect:/user/"+principalDetails.getUser().getId();
		// 이미지 업로드 완료 후 현재 로그인한 유저의 id 프로필 페이지로 돌아가기 -> /user/id
	}
}
