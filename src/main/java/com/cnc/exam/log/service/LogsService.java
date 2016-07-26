package com.cnc.exam.log.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.cnc.exam.auth.entity.User;
import com.cnc.exam.base.service.BaseService;
import com.cnc.exam.log.entity.LogsEntity;
import com.cnc.exam.mail.entity.MailEntity;

public interface LogsService extends  BaseService<LogsEntity, Long> {

	/**
	 * 根据条件查询
	 * @param searchParams
	 * @param pageable
	 * @return
	 */
	public Page<LogsEntity> findLogsByCondition(Map<String,Object> searchParams, Pageable pageable);
}
