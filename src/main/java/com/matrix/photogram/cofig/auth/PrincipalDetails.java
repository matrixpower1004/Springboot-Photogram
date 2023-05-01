package com.matrix.photogram.cofig.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.matrix.photogram.domain.user.User;

import lombok.Data;

@Data
public class PrincipalDetails implements UserDetails, OAuth2User {

	private static final long serialVersionUID = 1L;

	private User user;
	private Map<String, Object> attributes;

	//일반 로그인에 사용할 생성자
	public PrincipalDetails(User user) {
		this.user = user;
	}
	
	//OAuth2 로그인 했을 때 사용할 생성자
	public PrincipalDetails(User user, Map<String, Object> attributes) {
		this.user = user;
	}
	
	// 권한 : 한개가 아닐 수 있음. (3개 이상의 권한이 있을 수 있음)
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// 권한을 가져오는 함수. 권한은 user가 들고 있음
		// 자바는 함수가 1급객체가 아니기 때문에 매개변수에 함수를 넣을 수 없다.
		// 인테페이스는 넣을 수 있고, 인터페이스가 함수를 들고 있으면 인터페이스를 넣어야 한다.
		// 함수를 파라메터로 전달하고 싶으면 무조건 자바에서는 인터페이스를 넘겨야 한다. (혹은 class object)
		
		Collection<GrantedAuthority> collector = new ArrayList<>();
		collector.add(() -> { // add안에 함수를 넣고 싶은게 목적임
				return user.getRole();
		}); // 람다식으로 함수를 넘겨준다
		return collector;
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() { // 이 계정이 만료된 계정인지 여부
		return true; // false일 경우 로그인 불가
	}

	@Override
	public boolean isAccountNonLocked() { // 계정 잠금 여부
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() { // 비밀번호 유효 시간 만기 여부
		return true;
	}

	@Override
	public boolean isEnabled() { // 계정 활성화 여부
		return true;
	}

	//OAuth2로 로그인을 했다면 attributes 정보들이 존재한다.
	@Override
	public Map<String, Object> getAttributes() {
		// TODO Auto-generated method stub
		return attributes; //페이스북에서 넘어온 유저 정보 {id, name, email}
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
//		return (String) attributes.get("name");
		return attributes.get("name").toString();
	}

}
