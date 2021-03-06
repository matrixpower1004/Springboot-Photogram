package com.matrix.photogram.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.matrix.photogram.cofig.auth.PrincipalDetails;
import com.matrix.photogram.domain.image.Image;
import com.matrix.photogram.domain.image.ImageRepository;
import com.matrix.photogram.web.dto.image.ImageUploadDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor // DI
@Service
public class ImageService {
	
	private final ImageRepository imageRepository;
	
	@Value("${file.path}") // application.yml의 file: path에 적은 경로
	private String uploadFolder; 
	
	@Transactional
	public void photoUpload(ImageUploadDto imageUploadDto, PrincipalDetails principalDetails) {
		UUID uuid = UUID.randomUUID(); // uuid. 몇십억분의 일 확률로 중복될 가능성 있음
		String imageFileName = uuid + "_" + imageUploadDto.getFile().getOriginalFilename(); // 1.jpg (실제 파일 이름)
		System.out.println("이미지 파일 이름 : " + imageFileName);
		
		Path imageFilePath = Paths.get(uploadFolder+imageFileName);
		// 통신, I/O -> 예외가 발생할 수 있다.
		try {
			Files.write(imageFilePath, imageUploadDto.getFile().getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// image 테이블에 저장
		// imageUpload를 image에 넣는 로직이 필요
		Image image = imageUploadDto.toEntity(imageFileName, principalDetails.getUser());
//		Image imageEntity = imageRepository.save(image);
		imageRepository.save(image); // 어짜피 void이므로 변수에 저장할 필요 없음
		// save 할 때 imageUploadDto를 넣을 수 없고 image 객체를 넣어야 한다.
		
		// System.out에 이런 코드는 좋지 않다. 처음에 테스할 때만 사용하고 모두 주석처리하거나 삭제한다.
//		System.out.println(imageEntity);
	}

}
