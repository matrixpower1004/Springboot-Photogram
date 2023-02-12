package com.matrix.photogram.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.matrix.photogram.cofig.auth.PrincipalDetails;
import com.matrix.photogram.domain.image.Image;
import com.matrix.photogram.domain.image.ImageRepository;
import com.matrix.photogram.web.dto.image.ImageUploadDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ImageService {
	
	private final ImageRepository imageRepository;
	
	// @Transactional을 걸어서 세션을 컨트롤러단 까지 끌고 오는것은 중요하다.
	@Transactional(readOnly=true) // 영속성 컨텍스트 변경 감지를 해서, 더티체킹, flush(DB에 반영) X
	public Page<Image> imageStory(int principalId, Pageable pageable) {
		Page<Image> images = imageRepository.mStory(principalId, pageable);
		
		//2번유저(cos)로그인
		//images에 좋아요 상태 담기
		images.forEach((image)->{
			
			image.setLikeCount(image.getLikes().size());
			
			image.getLikes().forEach((like) -> {
				//해당 이미지에 좋아요 한 사람들을 찾아서 현재 로그인한 사람이 좋아요 한 것인지 비교
				if(like.getUser().getId() == principalId) {	
					image.setLikeState(true);
				}
			});
			
		});
		return images;
	}
	
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

	@Transactional(readOnly = true)
	public List<Image> popularPhoto() {
		return imageRepository.mPopular();
	}

}
