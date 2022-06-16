package com.matrix.photogram.cofig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity //해당 파일로 시큐리티를 활성화 
@Configuration //IoC
public class SecurityConfig extends WebSecurityConfigurerAdapter{

	@Bean // SecurityConfig가 IoC에 등록될 때 @Bean 어노테이션을 읽어서 메모리에 들고 있음 -> DI해서 쓰기만 하면 된다
	public BCryptPasswordEncoder encode() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		//super 삭제 - 기존 시큐리티가 가지고 있는 기능이 다 비활성화
		http.csrf().disable();
		http.authorizeRequests()
			.antMatchers("/", "/user/**", "/image/**", "/subscribe/**", "/comment/**", "/api/*.*").authenticated() //인증이 필요한 요청
			.anyRequest().permitAll() //그게 아닌 모든 요청은 허용
			.and()
			.formLogin()
			.loginPage("/auth/signin") // GET
			.loginProcessingUrl("/auth/signin")// POST -> 스프링 시큐리티가 로그인 프로세스 진행
			.defaultSuccessUrl("/");
	}
}
