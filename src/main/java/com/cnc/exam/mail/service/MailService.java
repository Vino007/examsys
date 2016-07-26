package com.cnc.exam.mail.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.cnc.exam.auth.entity.User;
import com.cnc.exam.base.service.BaseService;
import com.cnc.exam.log.entity.LogsEntity;
import com.cnc.exam.mail.entity.MailEntity;

public interface MailService extends  BaseService<MailEntity, Long> {
	
	
}
