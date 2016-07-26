package com.cnc.exam.log.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.cnc.exam.base.repository.BaseRepository;
import com.cnc.exam.log.entity.LogsEntity;

/**
 * 日志管理dao
 * @author guoy1
 *
 */
public interface LogsRepository extends BaseRepository<LogsEntity, Long> {
	
}
