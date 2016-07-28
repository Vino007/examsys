package com.cnc.exam.exam.service;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.cnc.exam.base.service.BaseService;
import com.cnc.exam.exam.entity.Exam;

public interface ExamService extends BaseService<Exam, Long> {

	Page<Exam> findExamByCondition(Map<String, Object> searchParams, Pageable pageable);

	void update(Exam exam);

	void bindQuestion(Long examId, Long[] questionIds);
	/**
	 * 根据examId生成考卷，随机从数据库中选择questionNumber道考题
	 * @param examId
	 */
	void autoGenerateExam(Long examId);
}
