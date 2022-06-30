package com.matrix.photogram.web.dto.subscribe;

import java.math.BigInteger;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SubscribeDto {
	private int id;
	private String username;
	private String profileImageUrl;
	private BigInteger subscribeState;
	private BigInteger equalUserState; // 현재 페이지의 주인이 로그인한 사용자와 같은 사람인지 여부 체크
	// MariaDB는 int로 TRUE 값을 리턴받지 못한다.(버그?) 그래서 Integer를 사용
	// MySQL에서는 Integer를 BigInteger로 변경해야 정상 동작함
}
