package com.cnc.exam.mail.repository;

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

/**
 * 邮件模板dao实现类
 * @author guoy1
 *
 */

public class MailRepositoryImpl {
	@PersistenceContext
	private EntityManager em;

	
	
}
