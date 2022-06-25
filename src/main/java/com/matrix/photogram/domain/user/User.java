package com.matrix.photogram.domain.user;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.matrix.photogram.domain.image.Image;

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
	@GeneratedValue(strategy = GenerationType.IDENTITY) // 번호 증가 전략이 데이터베이스를 따라간다. MySQL=AUTO, 오라클=SEQUENCE, MariaDB=IDENTITY
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
	
	// 한 명의 user는 여러개의 이미지를 만들 수 있다.
	// Image class의 user
	// 나는 연관관계의 주인이 아니다. 그러므로 테이블에 컬럼을 만들지 마.
	// User를 SELECT할 때 해당 User id로 등록된 image들을 다 가져와.
	// Lazy = User를 Select할 때 해당 User id로 등록된 image들을 가져오지마 - 대신 getImages() 함수의 image들이 호출될 때 가져와!!
	// Eager = User를 Select할 때 해당 User id로 등록된 image들을 전부 Join해서 가져와!!
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY) // LAZY = Default 
	@JsonIgnoreProperties({"user"}) // 메시지 컨버터가 Json으로 파싱하여 응답할 때 Image 객체 내부의 user는 무시하고 파싱하라는 의미
	private List<Image> images; // 양방향매핑
	// DB입장에서 하나의 컬럼에 여러개의 값이 들어가는 테이블을 어떻게 만들지?
	
	private LocalDateTime createDate;
	
	@PrePersist // DB에 Insert 되기 직전에 실행
	public void createDate() {
		this.createDate =LocalDateTime.now();
	}
}
