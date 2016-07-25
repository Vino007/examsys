package com.cnc.exam.auth.repository;


import org.springframework.data.jpa.repository.Query;

import com.cnc.exam.auth.entity.Resource;
import com.cnc.exam.base.repository.BaseRepository;

public interface ResourceRepository extends BaseRepository<Resource, Long>{
	
	public Resource findByName(String name);
	public void deleteAssociateById(Long... resourceIds);
}
