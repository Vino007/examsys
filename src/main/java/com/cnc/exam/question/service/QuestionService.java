package com.cnc.exam.question.service;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.cnc.exam.base.service.BaseService;
import com.cnc.exam.question.entity.Question;

public interface QuestionService extends BaseService<Question, Long>{
	void update(Question question);

	Page<Question> findQuestionByCondition(Map<String, Object> searchParams, Pageable pageable);
}
