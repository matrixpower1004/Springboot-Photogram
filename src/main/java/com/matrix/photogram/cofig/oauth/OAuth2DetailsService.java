package com.matrix.photogram.cofig.oauth;

import java.util.Map;
import java.util.UUID;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.matrix.photogram.cofig.auth.PrincipalDetails;
import com.matrix.photogram.domain.user.User;
import com.matrix.photogram.domain.user.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class OAuth2DetailsService extends DefaultOAuth2UserService {

	private final UserRepository userRepository;
	
//	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
//		System.out.println("OAuth2 서비스 탐");
		OAuth2User oauth2User = super.loadUser(userRequest);
		//받아온 회원정보로 회원가입을 하려면 User Object에 넣어야 하므로 userRepository가 필요하다.  
		System.out.println(oauth2User.getAttributes());
		
		Map<String, Object> userInfo = oauth2User.getAttributes();

		//한 번 로그인해서 DB에 회원 정보가 저장되었다면 다시 DB에 저장되어서는 안 된다.
		String username = String.valueOf("facebook_" + userInfo.get("id"));
		String password = String.valueOf(new BCryptPasswordEncoder().encode(UUID.randomUUID().toString()));
		String email = String.valueOf(userInfo.get("email"));
		String name = String.valueOf(userInfo.get("name"));
		
		User userEntity = userRepository.findByUsername(username);
		if (userEntity == null) { //페이스북 최초 로그인
			//username은 페이스북을 통해서 로그인 하므로 우리에게 크게 중요하지는 않지만 중복되지 않게 만들어야 한다.
			//password는 무조건 ByCrypt로 암호화가 되어  들어가야 한다.
			User user = User.builder()
					.username(username)
					.password(password)
					.email(email)
					.name(name)
					.role("ROLE_USER")
					.build();
			
			//Type이 맞지 않아서 에러 발생
			//애플리케이션에서 OAuth로 로그인 했는지, 일반 로그인인지 구분하지 않는다면 굳이 넣어줄 필요는 없다.  
			return new PrincipalDetails(userRepository.save(user), oauth2User.getAttributes());
			//userRepository를 OAuth2User type으로 저장해야 한다.
		} else { //이미 페이스북 계정으로 회원가입이 되어 있다는 뜻
			return new PrincipalDetails(userEntity, oauth2User.getAttributes());
		}
	}

}
