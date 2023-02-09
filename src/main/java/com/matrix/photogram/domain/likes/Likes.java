package com.matrix.photogram.domain.likes;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.matrix.photogram.domain.image.Image;
import com.matrix.photogram.domain.user.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(
	uniqueConstraints = {
		@UniqueConstraint(	//unique 제약조건
			name = "likes_uk",
			columnNames = { "imageId", "userId" }	//중복 unique key로 묶은 것
			//한 유저가 '좋아요'한 이미지를 중복으로 좋아요 할 수 없도록
		)
	}
)
public class Likes {	//N
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	//무한참조 오류 발생. Image.java에 @JsonIgnoreProperties 애너테이션 추가로 해결
	@JoinColumn(name = "imageId")
	@ManyToOne
	private Image image;	//1. 하나의 이미지는 여러번 좋아요를 받을 수 있음
	
	//Likes 데이터를 json으로 보낼 때 User 내부에 images 데이터가 다시 포함되어 무한참조가 되지 않도록
	@JsonIgnoreProperties({"images"})
	@JoinColumn(name = "userId")
	@ManyToOne
	private User user;		//1. 한 명의 유저는 좋아요를 여러번 할 수 있음 
	private LocalDateTime createDate;

	@PrePersist
	public void createDate() {
		this.createDate = LocalDateTime.now();
	}
}
