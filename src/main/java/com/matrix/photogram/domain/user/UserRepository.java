package com.matrix.photogram.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

// 어노테이션이 없어도 JpaRepository를 상속하면 IoC등록이 자동으로 된다.
public interface UserRepository extends JpaRepository<User, Integer>{
	// JPA query method (Query Creation)
	User findByUsername(String username);
}
