package com.matrix.photogram.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.matrix.photogram.domain.subscribe.SubscribeRepository;
import com.matrix.photogram.handler.ex.CustomApiException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class SubscribeService {

	private final SubscribeRepository subscribeRepository;
	
	@Transactional
	public void subscribe(int fromuserId, int toUserId) {
		// 1. save할 때 repository 객체가 필요하다
		// 2. int 타입인 fromUserId와 toUserId로는 Subscribe 객체를 만들 수 없다. 
		// 3. 그래서 native query를 사용하는게 좋다. 더 쉽고 간단하게 구현 할 수 있다.
		// 터지면 나중에 Exception 처리하는 게 낫다
		try {
			subscribeRepository.mSubscribe(fromuserId, toUserId);
		} catch (Exception e) {
			throw new CustomApiException("이미 구독을 하였습니다.");
		}
	}
	
	@Transactional
	public void unSubscribe(int fromuserId, int toUserId) {
		subscribeRepository.mUnSubscribe(fromuserId, toUserId);
	}
}
