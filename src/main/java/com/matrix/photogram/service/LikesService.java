package com.matrix.photogram.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.matrix.photogram.domain.likes.LikesRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class LikesService {
	private final LikesRepository likesRepository;
	
	//DB에 영향을 줄 때는 항상 트랜잭션을 걸어줘야 한다.
	@Transactional
	public void toLikes(int imageId, int principalId) {
		//쿼리를 작성할 때 Likes 모델에서 idmageid와 userid를 들고와서 넣어야 한다.
		//LikesRepository에 nativeQuery를 작성하고 호출.
		likesRepository.mLikes(imageId, principalId);
		
	}
	
	@Transactional
	public void backLikes(int imageId, int principalId) {
		likesRepository.mUnLikes(imageId, principalId);
	}
}
