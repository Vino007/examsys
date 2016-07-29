package com.cnc.exam.result.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import com.cnc.exam.result.entity.ExamResultEntity;
import com.cnc.exam.result.repository.ExamResultRepository;

@Transactional
@Service("examResultService")
public class ExamResultServiceImpl extends
		AbstractBaseServiceImpl<ExamResultEntity, Long> implements
		ExamResultService {

	@Autowired
	private ExamResultRepository examResultRepository;

	private Specification<ExamResultEntity> buildSpecification(
			final Map<String, Object> searchParams) {

		Specification<ExamResultEntity> spec = new Specification<ExamResultEntity>() {
			@Override
			public Predicate toPredicate(Root<ExamResultEntity> root,
					CriteriaQuery<?> cq, CriteriaBuilder cb) {
				Predicate allCondition = null;
				String examId = (String) searchParams.get("examId");
				String userId = (String) searchParams.get("userId");
				String isPass = (String) searchParams.get("isPass");
				String deptId = (String) searchParams.get("deptId");
				String createTimeRange=(String) searchParams.get("createTime");
				if (examId != null && !"".equals(examId)) {
					Predicate condition = cb.like(root.get("exam").get("id").as(String.class), "%"+ searchParams.get("examId") + "%");
					if (null == allCondition)
						allCondition = cb.and(condition);
					else
						allCondition = cb.and(allCondition, condition);
				}
				if (userId != null && !"".equals(userId)) {
					Predicate condition = cb.like(root.get("user").get("id").as(String.class), "%"+ searchParams.get("userId") + "%");
					if (null == allCondition)
						allCondition = cb.and(condition);
					else
						allCondition = cb.and(allCondition, condition);

				}
				if (isPass != null && !"".equals(isPass)) {
					Predicate condition = cb.like(root.get("isPass").as(String.class), "%"+ searchParams.get("isPass") + "%");
					if (null == allCondition)
						allCondition = cb.and(condition);
					else
						allCondition = cb.and(allCondition, condition);
				}
				if (deptId != null && !"".equals(deptId)) {
					Predicate condition = cb.like(root.get("user").get("department").get("id").as(String.class), "%"+ searchParams.get("deptId") + "%");
					if (null == allCondition)
						allCondition = cb.and(condition);
					else
						allCondition = cb.and(allCondition, condition);
				}
				
				if(createTimeRange!=null&&!"".equals(createTimeRange)){			
					String createTimeStartStr=createTimeRange.split(" - ")[0]+":00:00:00";
					String createTimeEndStr=createTimeRange.split(" - ")[1]+":23:59:59";
					SimpleDateFormat format=new SimpleDateFormat("MM/dd/yyyy:hh:mm:ss");
					System.out.println(createTimeStartStr);
					try {
						Date createTimeStart = format.parse(createTimeStartStr);
						Date createTimeEnd=format.parse(createTimeEndStr);
						Predicate condition=cb.between(root.get("createTime").as(Date.class),createTimeStart, createTimeEnd);
						if(null==allCondition)
							allCondition=cb.and(condition);
						else
							allCondition=cb.and(allCondition,condition);
					} catch (ParseException e) {
						e.printStackTrace();
						Logger log =LoggerFactory.getLogger(this.getClass());
					}
				}	
				return allCondition;
			}
		};
		return spec;
	}

	@Override
	public Page<ExamResultEntity> findERByCondition(
			Map<String, Object> searchParams, Pageable pageable) {
		return examResultRepository.findAll(buildSpecification(searchParams),
				pageable);
	}
	
	@Override
	public void update(ExamResultEntity obj){
		ExamResultEntity obj2=examResultRepository.findOne(obj.getId());
		try {
			@SuppressWarnings("unchecked")
			Class<ExamResultEntity> clazz=(Class<ExamResultEntity>) Class.forName("com.cnc.exam.result.entity.ExamResultEntity");
			Method[] methods=clazz.getDeclaredMethods();
			for(Method m:methods){
				 if(m.getName().substring(0, 3).equals("get")){					
					Object value=m.invoke(obj);
					if(value!=null){
					Method setMethod=clazz.getDeclaredMethod("set"+m.getName().substring(3, 4).toUpperCase()+m.getName().substring(4),m.getReturnType());
					setMethod.invoke(obj2, value);
					}
				}		
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
	}
}
