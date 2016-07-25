package com.cnc.exam.auth.service;



import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.cnc.exam.auth.entity.Resource;
import com.cnc.exam.auth.entity.Role;
import com.cnc.exam.auth.entity.User;
import com.cnc.exam.auth.exception.ResourceDuplicateException;
import com.cnc.exam.auth.repository.ResourceRepository;
import com.cnc.exam.base.service.AbstractBaseServiceImpl;
@Service("resourceService")
@Transactional
public class ResourceServiceImpl extends AbstractBaseServiceImpl<Resource, Long>  implements ResourceService {
	@Autowired
	@Qualifier("resourceRepository")
	private ResourceRepository resourceRepository;

	public ResourceRepository getResourceRepository() {
		return resourceRepository;
	}

	public void setResourceRepository(ResourceRepository resourceRepository) {
		this.resourceRepository = resourceRepository;
	}
	
	@Override
	public void update(Resource resource) {
		Resource resource2=resourceRepository.findOne(resource.getId());
		if(resource.getType()!=null&&!resource.getType().equals("")){
			resource2.setType(resource.getType());
		}
		if(resource.getParentId()!=null){
			resource2.setParentId(resource.getParentId());
		}
		if(resource.getUrl()!=null){//允许为空字符串
			resource2.setUrl(resource.getUrl());
		}
		if(resource.getPriority()!=null){
			resource2.setPriority(resource.getPriority());
		}
		if(resource.getAvailable()!=null){
			resource2.setAvailable(resource.getAvailable());
		}
		if(resource.getPermission()!=null){
			resource2.setPermission(resource.getPermission());
		}
	}

	@Override
	public void saveWithCheckDuplicate(Resource resource,User user)
			throws ResourceDuplicateException {
		if(resourceRepository.findByName(resource.getName())!=null)
			throw new ResourceDuplicateException();
		else{
			resource.setCreateTime(new Date());
			//创建人id
			resource.setCreatorName(user.getUsername());
			resourceRepository.save(resource);
			
			
		}
		
	}
	 /**
     * 创建动态查询条件组合.
     */
    private Specification<Resource> buildSpecification(final Map<String,Object> searchParams) {
    	
		
        Specification<Resource> spec = new Specification<Resource>(){           
			@Override
			public Predicate toPredicate(Root<Resource> root,
				CriteriaQuery<?> cq, CriteriaBuilder cb) {
				Predicate allCondition = null;
				String name=(String) searchParams.get("name");			
				String createTimeRange=(String) searchParams.get("createTimeRange");
				if(name!=null&&!"".equals(name)){
					Predicate condition=cb.like(root.get("name").as(String.class),"%"+searchParams.get("name")+"%");
					if(null==allCondition)
						allCondition=cb.and(condition);//此处初始化allCondition,若按cb.and(allCondition,condition)这种写法，会导致空指针
					else
						allCondition=cb.and(allCondition,condition);
					}
				
											
				if(createTimeRange!=null&&!"".equals(createTimeRange)){			
					String createTimeStartStr=createTimeRange.split(" - ")[0]+":00:00:00";
					String createTimeEndStr=createTimeRange.split(" - ")[1]+":23:59:59";
					SimpleDateFormat format=new SimpleDateFormat("MM/dd/yyyy:hh:mm:ss");
					try {
						Date createTimeStart = format.parse(createTimeStartStr);
						Date createTimeEnd=format.parse(createTimeEndStr);
						Predicate condition=cb.between(root.get("createTime").as(Date.class),createTimeStart, createTimeEnd);
						if(null==allCondition)
							allCondition=cb.and(condition);//此处初始化allCondition,若按cb.and(allCondition,condition)这种写法，会导致空指针
						else
							allCondition=cb.and(allCondition,condition);
						
					} catch (ParseException e) {
						e.printStackTrace();
						Logger log =LoggerFactory.getLogger(this.getClass());
						log.error("createTime 转换时间出错");
					}
					
				
				}					
				return allCondition;
			}
        	
        };
        return spec;
    }
	@Override
	public Page<Resource> findResourceByCondition(
			Map<String, Object> searchParams, Pageable pageable) {
		return resourceRepository.findAll(buildSpecification(searchParams), pageable);
	}

	@Override
	public Resource findByName(String name) {
		
		return resourceRepository.findByName(name);
	}

	@Override
	public void delete(Long... ids) {
		// TODO Auto-generated method stub
		resourceRepository.deleteAssociateById(ids);
		super.delete(ids);
	}
	/**
	 * 不显示监控系统！！
	 */
	@Override
	public List<Resource> getSidebarResourceByUser(User currentUser) {
		List<Resource> menuResources=new ArrayList<Resource>();	
	
		for(Role role:currentUser.getRoles()){
			Set<Resource> resources=role.getResources();			
			for(Resource res : resources){
				if(res!=null&&res.getType().equals("menu")&&!res.getName().equals("监控系统")){
					menuResources.add(res);
				}
			}			
		}
		Collections.sort(menuResources, new Comparator<Resource>() {
			@Override
			public int compare(Resource o1, Resource o2) {
				//o1>o2则返回正数O1=O2返回0，小于返回负数
				return o1.getPriority().compareTo(o2.getPriority());
			}
		});
		return menuResources;
	}
	
	@Override
	public List<Resource> getButtonResourceByUser(User currentUser) {
		List<Resource> buttonResources=new ArrayList<Resource>();	
		for(Role role:currentUser.getRoles()){
			Set<Resource> resources=role.getResources();			
			for(Resource res : resources){
				if(res!=null&&!res.getType().equals("menu")){
					buttonResources.add(res);
				}
			}			
		}		
		
		return buttonResources;
	}



	

}
