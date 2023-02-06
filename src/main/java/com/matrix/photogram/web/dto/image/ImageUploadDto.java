package com.matrix.photogram.web.dto.image;

import org.springframework.web.multipart.MultipartFile;

import com.matrix.photogram.domain.image.Image;
import com.matrix.photogram.domain.user.User;

import lombok.Data;

@Data
public class ImageUploadDto {
	
	private MultipartFile file; // Multipart에는 @NotBlank 지원 불가
	private String caption;
	
	public Image toEntity(String postImageUrl, User user) {
		return Image.builder()
				.caption(caption)
				.postImageUrl(postImageUrl)
				.user(user) // Image객체는 유저 정보가 필요. 어떤 유저가 사진을 저장했는지 알아야 함
				.build();
	}
}
