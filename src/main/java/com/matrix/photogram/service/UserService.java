package com.matrix.photogram.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.matrix.photogram.domain.subscribe.SubscribeRepository;
import com.matrix.photogram.domain.user.User;
import com.matrix.photogram.domain.user.UserRepository;
import com.matrix.photogram.handler.ex.CustomApiException;
import com.matrix.photogram.handler.ex.CustomException;
import com.matrix.photogram.handler.ex.CustomValidationApiException;
import com.matrix.photogram.web.api.SubscribeApiController;
import com.matrix.photogram.web.dto.user.UserProfileDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {

	private final UserRepository userRepository;
	private final SubscribeRepository subscribeRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Value("${file.path}") // application.yml의 file: path에 적은 경로
	private String uploadFolder; 

	
	@Transactional
	public User userProfieImageUpdate(int principalId, MultipartFile profileImageFile) {
		UUID uuid = UUID.randomUUID(); // uuid. 몇십억분의 일 확률로 중복될 가능성 있음
		String imageFileName = uuid + "_" + profileImageFile.getOriginalFilename(); //profileImageFile 자체가 file이므로 .getFile()은 필요없다.
		System.out.println("이미지 파일 이름 : " + imageFileName);
		
		Path imageFilePath = Paths.get(uploadFolder+imageFileName);
		// 통신, I/O -> 예외가 발생할 수 있다.
		try {
			Files.write(imageFilePath, profileImageFile.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		User userEntity = userRepository.findById(principalId).orElseThrow(()->{
			return new CustomApiException("유저를 찾을 수 없습니다.");
		});
		userEntity.setProfileImageUrl(imageFileName);
		
		return userEntity;
	}//더티체킹으로 업데이트 된다. 
	
	@Transactional(readOnly = true) // select할 때도 @Transactional을 걸어 주면 좋다.
	public UserProfileDto memberProfile(int pageUserId, int principalId) { // 해당 페이지의 주인 id.
		// localhost:8080/user/3번으로 이동하면 3번 유저가 올린 사진이 나와야 한다. 로그인한 사람의 사진이 아님
		UserProfileDto dto = new UserProfileDto();
		// SELECT * FROM image WHERE userId=:userId;
		// 해당 유저 id로 검색이 안 될 수도 있기 때문에 Optional로 처리를 해줘야 한다.
		User userEntity = userRepository.findById(pageUserId).orElseThrow(()-> {
			throw new CustomException("해당 프로필 페이지는 없는 페이지입니다.");
		});
		dto.setUser(userEntity);
		dto.setPageOwnerState(pageUserId == principalId);
		dto.setImageCount(userEntity.getImages().size());
		
		int subscribeState = subscribeRepository.mSubscribeState(principalId, pageUserId);
		int subscribeCount = subscribeRepository.mSubscribeCount(pageUserId);
		
		dto.setSubscribeState(subscribeState == 1);
		dto.setSubscribeCount(subscribeCount);
		
		//이미지파일 마다 좋아요 카운트 추가하기
		userEntity.getImages().forEach((image) -> {
			image.setLikeCount(image.getLikes().size());
		});
		
		return dto;
	}
	
	@Transactional
	public User memberUpdate(int id, User user) {
		// 영속화
		// 1.무조건 찾았다. 걱정마get() 2.못찾았어 익셉션 발동시킬께 orElseThrow()
		User userEntity = userRepository.findById(id).orElseThrow(()->{
			return new CustomValidationApiException("찾을 수 없는 id입니다.");
			});
		// 2.영속화된 오브젝트를 수정 - 더티체킹(업데이트 완료)
		userEntity.setName(user.getName());
		String rawPassword = user.getPassword();
		String encPassword = bCryptPasswordEncoder.encode(rawPassword);
		
		userEntity.setPassword(encPassword);
		userEntity.setBio(user.getBio());
		userEntity.setWebsite(user.getWebsite());
		userEntity.setPhone(user.getPhone());
		userEntity.setGender(user.getGender());
		return userEntity;
	}

}
