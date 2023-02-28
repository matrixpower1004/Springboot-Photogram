package com.matrix.photogram.domain.comment;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;

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

public class Comment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(length = 100,nullable = false)
	private String content;
	
	//한 명의 유저는 댓글을 여러개 작성할 수 있다. User 1 : N Comment
	//Many to One의 기본 fetch 전략은 EAGER
	//DB에서 select했을 때 딸려오는 게 여러개면 DB에 부담이 될 수 있다. 이때는 LAZY 전략을 고려해봐야 한다.
	//댓글의 유저정보를 들고올 때 images 정보는 필요가 없음 
	@JsonIgnoreProperties({"images"})
	@JoinColumn(name = "userId")
	@ManyToOne(fetch = FetchType.EAGER)
	private User user;		//댓글을 누가 작성했는지.
	
	//한 개의 이미지에는 댓글이 여러개 있을 수 있음. Image 1 : N Comment
	@JoinColumn(name = "imageId")
	@ManyToOne(fetch = FetchType.EAGER)
	private Image image;	//어떤 이미지에 댓글을 작성했는지?
	
	private LocalDateTime createDate;	//댓글을 언제 작성했는지
	
	@PrePersist
	public void createDate() {
		this.createDate = LocalDateTime.now();
	}

}
