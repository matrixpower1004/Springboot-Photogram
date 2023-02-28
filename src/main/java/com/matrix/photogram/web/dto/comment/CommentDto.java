package com.matrix.photogram.web.dto.comment;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

//@NotNull = null값만 체크
//@NotEmpty = null, ""(빈값) 체크
//@NotBlack = null, "", " "(빈공백) 체크

@Data
public class CommentDto {
	//imageId와 content에 값이 전달되는 것을 방지 
	@NotBlank
	private String content;
	@NotNull 
	private int imageId;
	
	//toEntity가 필요 없다.
}
