package com.cnc.exam.auth.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.cnc.exam.auth.entity.User;
import com.cnc.exam.base.repository.BaseRepository;

public interface UserRepository extends BaseRepository<User, Long> {
	public User findByUsername(String username);
	public Page<User> getUsersByCondition(User user,Pageable pageable);
}
