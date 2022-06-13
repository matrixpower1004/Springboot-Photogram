package com.matrix.photogram.cofig.auth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.matrix.photogram.domain.user.User;
import com.matrix.photogram.domain.user.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service // IoC
public class PrincipalDetailsService implements UserDetailsService {
	
	private final UserRepository userRepository;
	
	// 1. 패스워드는 알아서 체킹하니까 신경쓸 필요 없다.
	// 2. 리턴이 잘되면 자동으로 UserDetails 타입을 세션으로 만든다.
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// 패스워드는 시큐리티가 알아서 비교를 해준다. 우리가 할 일은 username이 있는지 체크해서 UserDetails로 리턴만 해주면 된다.
		User userEntity = userRepository.findByUsername(username);
		
		if (userEntity == null) {
			return null;
		} else {
			return new PrincipalDetails(userEntity); // 이 부분이 세션에 저장이 되면 나중에 유저오브젝트 데이터를 우리가 활용할 수 있다.
		}
	}
}
