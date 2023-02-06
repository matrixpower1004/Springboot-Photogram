package com.matrix.photogram.domain.image;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
public class Image { // N : 1 , 한명의 유저는 몇 개의 이미지를 등록할 수 있나? user 1 : image n
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String caption; // 오늘 나 너무 피곤해!!
	private String postImageUrl; // 사진을 전송받아서 그 사진을 서버에 특정 폴더에 저장 - DB에 그 저장된 경로를 insert
	
	@JsonIgnoreProperties({"images"}) // user 정보가 들고 있는 images는 필요가 없음
	@JoinColumn(name="userId")
	@ManyToOne(fetch=FetchType.EAGER) // 이미지를 select하면 join해서 User정보를 같이 들고 온다
	private User user;
	/* 사진을 누가 업로드 했는지 알아야 하니까 
	Object 자체를 DB에 저장할수는 없다. 이대로 저장하면 FK로 저장됨
	Table을 만들었으면 당연히 repository가 필요함.
	하나의 이미지는 몇 명의 유저가 만들어 낼 수 있나? 1 : 1 */
	
	//이미지 좋아요
	
	//댓글
	
	private LocalDateTime createDate;

	@PrePersist
	public void createDate() { // DB에는 항상 시간이 들어가야 한다. 데이터가 언제 들어갔는지
		this.createDate = LocalDateTime.now();
	}

	// 오브젝트를 콘솔에 출력할 때 문제가 될 수 있어서 User 부분을 출력되지 않게 함.
//	@Override
//	public String toString() {
//		return "Image [id=" + id + ", caption=" + caption + ", postImageUrl=" + postImageUrl
//				+ ", createDate=" + createDate + "]";
//	}
}
