package com.matrix.photogram.domain.subscribe;

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
		@UniqueConstraint(
			name = "subscribe_uk",
			columnNames = { "fromUserId", "toUserId" } // DB테이블의 실제 컬럼명
		)
	}
)
public class Subscribe {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@JoinColumn(name = "fromUserId") // 이렇게 컬럼명 만들어! 니 맘대로 만들지 말고!!
	@ManyToOne
	private User fromUser;

	@JoinColumn(name = "toUserId")
	@ManyToOne
	private User ToUser;

	private LocalDateTime createDate;

	@PrePersist
	public void createDate() {
		this.createDate = LocalDateTime.now();
	}

}
