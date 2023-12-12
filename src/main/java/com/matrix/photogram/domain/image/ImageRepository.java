package com.matrix.photogram.domain.image;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ImageRepository extends JpaRepository<Image, Integer>{
	// fromUsrId는 로그인한 사용자 id
	@Query(value = "SELECT * FROM image WHERE userId IN (SELECT toUserId FROM subscribe WHERE fromUserId = :principalId) ORDER BY id DESC", nativeQuery=true)
	Page<Image> mStory(@Param("principalId") int principalId, Pageable pageable);

	@Query(value = "SELECT i.* FROM image i INNER JOIN(SELECT imageId, count(imageId) as likeCount FROM likes GROUP BY imageId) c ON i.id = c.imageId ORDER BY likeCount DESC", nativeQuery = true)
	List<Image> mPopular();
}
