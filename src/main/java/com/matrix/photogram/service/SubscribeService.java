package com.matrix.photogram.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.qlrm.mapper.JpaResultMapper; // qlrm은 spring boot에서 제공해주는 라이브러리가 아님.
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.matrix.photogram.domain.subscribe.SubscribeRepository;
import com.matrix.photogram.handler.ex.CustomApiException;
import com.matrix.photogram.web.dto.subscribe.SubscribeDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class SubscribeService {

	private final SubscribeRepository subscribeRepository;
	private final EntityManager em; // Repository는 EntityManager를 구현해서 만들어져 있는 구현체
	
	@Transactional(readOnly = true)
	public List<SubscribeDto> getSubscribeList(int paincipalId, int pageUserId) {
		
		// 쿼리 준비
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT u.id, u.username, u.profileImageUrl, "); // 끝에 꼭 한 칸 띄우기
		sb.append("if ((SELECT 1 FROM subscribe WHERE fromUserId = ? AND toUserId = u.id), 1, 0) subscribeState, ");
		sb.append("if ((?=u.id), 1, 0) equalUserState ");
		sb.append("FROM user u INNER JOIN subscribe s ");
		sb.append("ON u.id = s.toUserId ");
		sb.append("WHERE s.fromUserId = ?"); // 세미콜론 첨부하면 안됨. 꼭 뺄것.
		
		// 1.물픔표 : paincipalId (로그인한 id)
		// 2.물음표 : paincipalId 
		// 3.물음표 : pageUserId
		
		// 쿼리 완성
		Query query = em.createNativeQuery(sb.toString()) // import javax.persistence.Query -> 임포트시 주의!!
				.setParameter(1, paincipalId)
				.setParameter(2, paincipalId)
				.setParameter(3, pageUserId);
		
		// 쿼리 실행(qlrm library 필요 = DTO에 DB결과를 매핑하기 위해서)
		JpaResultMapper result = new JpaResultMapper();
		List<SubscribeDto> subscribeDtos = result.list(query, SubscribeDto.class);
		return subscribeDtos; // 구독 리스트
	}
	
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
