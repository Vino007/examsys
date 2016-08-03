package com.cnc.exam.result.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.cnc.exam.base.repository.BaseRepository;
import com.cnc.exam.result.entity.ExamResultEntity;

/**
 * 考试结果dao
 * @author guoy1
 *
 */
public interface ExamResultRepository extends BaseRepository<ExamResultEntity, Long>{

	@Query("from ExamResultEntity er where er.exam.course.id=?1")
	public List<ExamResultEntity> getEntities(long courseId);
	

	
	
	
	@Modifying
	@Query("delete ExamResultEntity e  where e.exam.id=?1")
	void deleteExam(long examId);

	@Modifying
	@Query("delete from ExamResultEntity er where er.id=?1")
	public void deleteEntity(long examresultid);

	
}
