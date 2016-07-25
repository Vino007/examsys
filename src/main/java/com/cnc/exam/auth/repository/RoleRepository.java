package com.cnc.exam.auth.repository;


import com.cnc.exam.auth.entity.Role;
import com.cnc.exam.base.repository.BaseRepository;

public interface RoleRepository extends BaseRepository<Role, Long>{
	public Role findByName(String name);
	public void deleteAssociateById(Long... roleIds);
}
