package com.matrix.photogram.web;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.matrix.photogram.cofig.auth.PrincipalDetails;
import com.matrix.photogram.domain.user.User;
import com.matrix.photogram.service.UserService;
import com.matrix.photogram.web.dto.user.UserProfileDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class UserController {

	private final UserService userService;
	
	@GetMapping("/user/{pageUserId}")
	public String profile(@PathVariable int pageUserId, Model model,
			@AuthenticationPrincipal PrincipalDetails principalDetails) {
		// user/profile 페이지로 갈 때 images들을 가지고 가야 된다. -> UserService가 필요함
		UserProfileDto dto = userService.memberProfile(pageUserId, principalDetails.getUser().getId());
		// 내가 회원 프로필 페이지로 갈 때 사진데이터만 들고 오는게 아니라 회원 정보, 이미지 정보, 게시물 정보, 구독 정보를 들고 와야 한다.
		// User 내부에는 user 정보만 있지 이미지파일에 대한 정보는 없다.
		// user/id 페이지로 이동할 때 로그인한 사용자의 id와 비교하여 같은면 '사진등록', 다르면 '구독하기' 버튼 노출
		model.addAttribute("dto", dto);
		return "user/profile";
	}
	
	@GetMapping("/user/{id}/update")
	public String update(@PathVariable int id,
			@AuthenticationPrincipal PrincipalDetails principalDetails) {
		// 1. 추천
		// System.out.println("세션 정보 : " + principalDetails.getUser());
		// No session 에러가 나면 두가지를 체크 ;
		//		- @Transactional 안붙여서 세션영역을 컨트롤러 단까지 끌고 가지 못해서 Lazy 로딩이 안되서 발생
		// 		- System.out을 어디서 잘못하지 않았는지(무한참조) 체크 -> 매우 위험한 방법
		
		// 2. 극혐 : 세션에 접근하는 복잡한 방법
		// Authentication auth = SecurityContextHolder.getContext().getAuthentication(); 
		// PrincipalDetails mprinDetails = (PrincipalDetails) auth.getPrincipal();
		// System.out.println("직접 찾은 세션 정보 : " + mprinDetails.getUser());
		
		// 접근주체, 인증주체 -> 인증되는 사용자의 오브젝트 name으로 사용하기 좋다.
		// model.addAttribute("principal", principalDetails.getUser());
		return "user/update";
	}
}
