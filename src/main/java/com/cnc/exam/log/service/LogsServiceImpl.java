package com.cnc.exam.log.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cnc.exam.auth.entity.User;
import com.cnc.exam.base.service.AbstractBaseServiceImpl;
import com.cnc.exam.base.service.BaseService;
import com.cnc.exam.log.entity.LogsEntity;
import com.cnc.exam.mail.entity.MailEntity;
@Transactional
@Service("logsService")
public class LogsServiceImpl extends AbstractBaseServiceImpl<LogsEntity, Long>  implements LogsService{
	
	
}
