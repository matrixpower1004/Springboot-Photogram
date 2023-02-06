package com.matrix.photogram.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.matrix.photogram.domain.user.User;
import com.matrix.photogram.domain.user.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service // 1.IoC 2. 트랜잭션 관리
public class AuthService {
	
	private final UserRepository userRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Transactional // Write(Insert, Update, Delete)
	public User memberJoin(User user) { // user -> 외부에서 통신을 통해서 받은 데이터를 User Object에 담은 것
		//회원가입 진행
		String rawPassword = user.getPassword();
		String encPassword = bCryptPasswordEncoder.encode(rawPassword);
		user.setPassword(encPassword);
		user.setRole("ROLE_USER"); // 관리자 ROLE_ADMIN
		User userEntity = userRepository.save(user); //userEntity -> DB에 있는 데이터를 User Object에 담은 것
		return userEntity;
	}
}
