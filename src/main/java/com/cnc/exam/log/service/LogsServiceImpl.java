package com.cnc.exam.log.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cnc.exam.base.service.AbstractBaseServiceImpl;
import com.cnc.exam.base.service.BaseService;
import com.cnc.exam.log.entity.LogsEntity;
import com.cnc.exam.log.repository.LogsRepository;
import com.cnc.exam.mail.entity.MailEntity;

@Transactional
@Service("logsService")
public class LogsServiceImpl extends AbstractBaseServiceImpl<LogsEntity, Long>
		implements LogsService {

	@Autowired
	private LogsRepository logsRepository;

	private Specification<LogsEntity> buildSpecification(
			final Map<String, Object> searchParams) {

		Specification<LogsEntity> spec = new Specification<LogsEntity>() {
			@Override
			public Predicate toPredicate(Root<LogsEntity> root,
					CriteriaQuery<?> cq, CriteriaBuilder cb) {
				Predicate allCondition = null;
				String type = (String) searchParams.get("opType");
				if (type != null && !"".equals(type)) {
					Predicate condition = cb.like(
							root.get("opType").as(String.class), "%"
									+ searchParams.get("opType") + "%");
					if (null == allCondition)
						allCondition = cb.and(condition);
					else
						allCondition = cb.and(allCondition, condition);
				}
				return allCondition;
			}
		};
		return spec;
	}

	@Override
	public Page<LogsEntity> findLogsByCondition(
			Map<String, Object> searchParams, Pageable pageable) {
		return logsRepository.findAll(buildSpecification(searchParams),
				pageable);
	}
}
