package com.matrix.photogram.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.matrix.photogram.domain.comment.Comment;
import com.matrix.photogram.domain.comment.CommentRepository;
import com.matrix.photogram.domain.image.Image;
import com.matrix.photogram.domain.user.User;
import com.matrix.photogram.domain.user.UserRepository;
import com.matrix.photogram.handler.ex.CustomApiException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CommentService {
	
	private final CommentRepository commentRepository;
	private final UserRepository userRepository;

	//댓글 쓰기할 때는 어떤 사람이 작성했는지 알아야 하고
	@Transactional
	public Comment writeComment(String content, int imageId, int userId) {
		
		//Tip(객체를 만들 때 id값만 담아서 insert할 수 있다)
		//대신 return시에 image객체는 id 값만 가지고 있는 빈 객체를 return받는다.
		Image image = new Image();
		image.setId(imageId);
		
		User userEntity = userRepository.findById(userId).orElseThrow(()->{
			return new CustomApiException("유저 아이디를 찾을 수 없습니다.");
		});
		
		Comment comment = new Comment();
		comment.setContent(content);
		comment.setImage(image);
		comment.setUser(userEntity);
		
		return commentRepository.save(comment);
	}
	
	@Transactional
	public void removeComment(int id) {
		try {
			commentRepository.deleteById(id);
		} catch (Exception e) {
			throw new CustomApiException(e.getMessage());
		}
		//customException : HTML file을 리턴하는 Controller
		//customApiException : Data를 리턴하는 Controller
	}
}
