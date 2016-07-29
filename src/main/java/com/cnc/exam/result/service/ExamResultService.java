package com.cnc.exam.result.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.cnc.exam.auth.entity.User;
import com.cnc.exam.base.service.BaseService;
import com.cnc.exam.result.entity.ExamResultEntity;

public interface ExamResultService extends BaseService<ExamResultEntity, Long>{

	public Page<ExamResultEntity> findERByCondition(Map<String, Object> searchParams,Pageable pageable);

	public void update(ExamResultEntity er);
	
	public void saveToExcel(String path) throws FileNotFoundException;
}
