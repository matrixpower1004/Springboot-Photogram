package com.matrix.photogram.web.api;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.matrix.photogram.cofig.auth.PrincipalDetails;
import com.matrix.photogram.domain.comment.Comment;
import com.matrix.photogram.handler.ex.CustomValidationApiException;
import com.matrix.photogram.service.CommentService;
import com.matrix.photogram.web.dto.CMRespDto;
import com.matrix.photogram.web.dto.comment.CommentDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class CommenbtApiController {
	
	private final CommentService commentService;

	@PostMapping("/api/comment")
	public ResponseEntity<?> commentSave(@Valid @RequestBody CommentDto commentDto, BindingResult bindingResult,
			@AuthenticationPrincipal PrincipalDetails principalDetails) {
		//json형태의 data로 받을려면 @RequestBody를 붙여야 한다.
		//x-www-form-urlencoded 포맷으로 받는 데이터만 DTO로 받을 수 있다.
		//현재 로그인한 유저가 댓글을 작성했을 것이므로 세션에 있는 로그인한 유저의 정보를 @AuthenticationPrincipal로 받아온다.
		
		if (bindingResult.hasErrors()) {
			Map<String, String> errorMap = new HashMap<>();
			
			for (FieldError error : bindingResult.getFieldErrors()) {
				errorMap.put(error.getField(), error.getDefaultMessage());
			}
			throw new CustomValidationApiException("유효성검사 실패함", errorMap);
		}
		
		Comment comment = commentService.writeComment(commentDto.getContent(), commentDto.getImageId(), principalDetails.getUser().getId());
		//method 호출시 인자로 넘겨줄 content, imageId, userId가 필요
		return new ResponseEntity<>(new CMRespDto<>(1, "댓글쓰기 성공", comment), HttpStatus.CREATED);
	}
	
	@DeleteMapping("/api/comment/{id}")
	public ResponseEntity<?> commentDelete(@PathVariable int id){
		commentService.removeComment(id);
		return new ResponseEntity<>(new CMRespDto<>(1, "댓글삭제 성공", null), HttpStatus.OK);	
	}
	
}
