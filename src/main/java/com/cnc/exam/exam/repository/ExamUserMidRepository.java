package com.cnc.exam.exam.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.cnc.exam.auth.entity.User;
import com.cnc.exam.exam.entity.ExamUserMid;

public interface ExamUserMidRepository  extends JpaRepository<ExamUserMid, Long>,JpaSpecificationExecutor<ExamUserMid>{
	//public Page<ExamUserMid> findAll(Pageable pageable);
	/*@Query("select ExamUserMid from ExamUserMid e where e.exam.id=?1 and e.status=?2  ")
	public Page<User> findUserByStatus(long examId,int status,Pageable pageable);*/
	
/*	@Query("select e from ExamUserMid e where e.exam.id=?1 and e.status=?2  ")
	public Page<ExamUserMid> findByStatus(long examId,int status,Pageable pageable);*/
	@Modifying
	@Query("update ExamUserMid e set e.status=?3 where e.exam.id=?1 and e.user.id=?2")
	void updateExamUserStatus(long examId,long userId,int status);
	@Query("select e from ExamUserMid e  where e.exam.id=?1 and e.user.id=?2")
	ExamUserMid examUserIsExist(long examId,long userId);
	/*@Modifying
	@Query("insert into ExamUserMid  (exam_id, status, user_id) values (?1, ?3, ?2)")
	void insertExamUser(long examId,long userId,int status);*/
}
