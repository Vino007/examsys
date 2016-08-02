package com.cnc.exam.exam.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.cnc.exam.base.repository.BaseRepository;
import com.cnc.exam.exam.entity.Exam;

public interface ExamRepository extends BaseRepository<Exam, Long>{
	@Modifying
	@Query("delete from ExamUserMid e where e.exam.id=?1 and e.user.id=?2")
	public void removeUser(long examId,long userId);
	@Modifying
	@Query("update ExamUserMid e set e.status=?3 where e.exam.id=?1 and e.user.id=?2 ")
	public void updateUserStatus(long examId,long userId,int status);
	
	@Query("select e.status from ExamUserMid e  where e.exam.id=?1 and e.user.id=?2 ")
	public Integer findUserStatus(long examId,long userId);
	

}
