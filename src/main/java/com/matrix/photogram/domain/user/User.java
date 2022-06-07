package com.matrix.photogram.domain.user;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//JPA - Java Persistence API(자바로 데이터를 영구적으로 저장(DB)할 수 있는 API를 제공)

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity // DB에 테이블을 생성
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO) // 번호 증가 전략이 데이터베이스를 따라간다. MySQL=AUTO, 오라클=SEQUENCE, MariaDB=IDENTITY
	private int id;

	@Column(length=20, unique=true) // 스키마(table)이 변경된것이기 때문에 단순 저장만해서는 반영되지 않는다.
	private String username;
	@Column(nullable=false)
	private String password;
	@Column(nullable=false)
	private String name;
	private String website; // 웹사이트
	private String bio; // 자기소개
	@Column(nullable=false)
	private String email;
	private String phone;
	private String gender;
	
	private String profileImageUrl; // 사진
	private String role; // 권한
	
	private LocalDateTime createDate;
	
	@PrePersist //DB에 Insert 되기 직전에 실행
	public void createDate() {
		this.createDate =LocalDateTime.now();
	}
}
