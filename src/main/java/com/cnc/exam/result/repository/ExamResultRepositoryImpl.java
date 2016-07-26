package com.cnc.exam.result.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


/**
 * 实现类
 * @author guoy1
 *
 */
public class ExamResultRepositoryImpl {
	@PersistenceContext
	private EntityManager em;
}
