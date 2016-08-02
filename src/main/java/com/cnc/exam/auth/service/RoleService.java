package com.cnc.exam.auth.service;


import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.cnc.exam.auth.entity.Role;
import com.cnc.exam.auth.entity.User;
import com.cnc.exam.auth.exception.RoleDuplicateException;
import com.cnc.exam.base.service.BaseService;

public interface RoleService extends  BaseService<Role, Long> {


	/**
	 * 建立角色与资源之间的联系
	 * @param roleId
	 * @param resourceIds
	 */
	public void connectRoleAndResource(Long roleId,Long ...resourceIds );
	public void disconnnectRoleAndResource(Long roledId,Long... resourceIds);
	public void clearAllRoleAndResourceConnection(Long roleId);
	public void update(Role role);
	public void saveWithCheckDuplicate(Role role,User user) throws RoleDuplicateException;
	public Page<Role> findRoleByCondition(Map<String, Object> searchParams,
			Pageable pageable);
	public void connectRoleAndCategory(Long roleId, Long... categoryIds);
	public void disconnectRoleAndCategory(Long roleId, Long... categoryIds);
	public void clearAllRoleAndCategoryConnection(Long roleId);
}
