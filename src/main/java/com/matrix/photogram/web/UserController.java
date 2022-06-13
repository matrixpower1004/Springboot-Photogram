package com.matrix.photogram.web;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.matrix.photogram.cofig.auth.PrincipalDetails;

@Controller
public class UserController {

	@GetMapping("/user/{id}")
	public String profile(@PathVariable int id) {
		return "user/profile";
	}
	
	@GetMapping("/user/{id}/update")
	public String update(@PathVariable int id,
			@AuthenticationPrincipal PrincipalDetails principalDetails) {
		// 1. 추천
		System.out.println("세션 정보 : " + principalDetails.getUser());
		
		// 2. 극혐 : 세션에 접근하는 복잡한 방법
		// Authentication auth = SecurityContextHolder.getContext().getAuthentication(); 
		// PrincipalDetails mprinDetails = (PrincipalDetails) auth.getPrincipal();
		// System.out.println("직접 찾은 세션 정보 : " + mprinDetails.getUser());
		
		// 접근주체, 인증주체 -> 인증되는 사용자의 오브젝트 name으로 사용하기 좋다.
		// model.addAttribute("principal", principalDetails.getUser());
		return "user/update";
	}
}
