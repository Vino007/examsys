package com.cnc.exam.auth.service;


import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.cnc.exam.auth.entity.Resource;
import com.cnc.exam.auth.entity.User;
import com.cnc.exam.auth.exception.ResourceDuplicateException;
import com.cnc.exam.auth.exception.RoleDuplicateException;
import com.cnc.exam.base.service.BaseService;



public interface ResourceService extends  BaseService<Resource, Long> {

	void update(Resource resource);

	void saveWithCheckDuplicate(Resource resource,User user) throws ResourceDuplicateException;

	Page<Resource> findResourceByCondition(Map<String, Object> searchParams,
			Pageable pageable);
	Resource findByName(String name);
	/**
	 * 获取侧边栏资源
	 * @param currentUser
	 * @return
	 */
	List<Resource> getSidebarResourceByUser(User currentUser);
	/**
	 * 获取按钮资源
	 * @param currentUser
	 * @return
	 */
	List<Resource> getButtonResourceByUser(User currentUser);
}
