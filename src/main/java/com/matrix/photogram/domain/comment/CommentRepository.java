package com.matrix.photogram.domain.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CommentRepository extends JpaRepository<Comment, Integer>{

//	@Modifying
//	@Query(value = "INSERT INTO comment(content, imageId, userId, createDate) VALUES(:content, :imageId, :userId, now())", nativeQuery = true)
//	Comment mSave(String content, int imageId, int userId);	//inset한 후 insert된 객체를 다시 리턴을 받아야 한다.

//	Native Query로는 Comment객체를 리턴받을 수 없으므로 JPARepository가 들고 있는 save를 return받아야 한다.
}
