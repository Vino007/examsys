package com.cnc.exam.log.repository;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.cnc.exam.auth.entity.User;
import com.cnc.exam.log.entity.LogsEntity;

/**
 * criteria学习视频  http://www.jikexueyuan.com/course/1449.html
 * http://blog.csdn.net/dracotianlong/article/details/28445725
 * spring data会自动选择该实现类作为UserRepository的补充
 * @author Joker
 *
 */

public class LogsRepositoryImpl {
	@PersistenceContext
	private EntityManager em;

	
	
}
