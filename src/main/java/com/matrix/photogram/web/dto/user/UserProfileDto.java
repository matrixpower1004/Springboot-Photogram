package com.matrix.photogram.web.dto.user;

import com.matrix.photogram.domain.user.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserProfileDto {
	private boolean pageOwnerState; // jstl에서 앞에 is가 붙으면 파싱이 제대로 안되는 버그가 있음
	private int imageCount; // 최종데이터를 만들어서 View로 전달하는 게 좋다. view에서 연산이 일어나는 것은 좋은 방법이 아님.
	private User user;
}
