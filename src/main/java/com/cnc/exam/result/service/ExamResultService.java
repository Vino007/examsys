package com.cnc.exam.result.service;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.cnc.exam.base.service.BaseService;
import com.cnc.exam.result.entity.ExamResultEntity;

public interface ExamResultService extends BaseService<ExamResultEntity, Long>{

	public Page<ExamResultEntity> findERByCondition(Map<String, Object> searchParams,Pageable pageable);

	public void update(ExamResultEntity er);
}
