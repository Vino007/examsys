package com.cnc.exam.base.repository;

import java.io.Serializable;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import com.cnc.exam.base.entity.BaseEntity;
@NoRepositoryBean
public interface BaseRepository<T extends BaseEntity<PK>,PK extends Serializable> extends JpaRepository<T, PK>,JpaSpecificationExecutor<T>{
	public Page<T> findAll(Pageable pageable);
	
	
	 
}
