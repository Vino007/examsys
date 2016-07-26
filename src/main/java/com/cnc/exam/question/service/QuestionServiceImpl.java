package com.cnc.exam.question.service;

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

import com.cnc.exam.auth.entity.User;
import com.cnc.exam.base.service.AbstractBaseServiceImpl;
import com.cnc.exam.question.entity.Question;
import com.cnc.exam.question.repository.QuestionRepository;

@Service("questionService")
public class QuestionServiceImpl extends AbstractBaseServiceImpl<Question, Long> implements QuestionService{
	@Autowired
	private QuestionRepository questionRepository;
	@Override
	public void update(Question obj) {
		if(obj==null||obj.getId()==null)  return;
		
		Question obj2=questionRepository.findOne(obj.getId());
			try {
				@SuppressWarnings("unchecked")
				Class<Question> clazz=(Class<Question>) Class.forName("com.cnc.exam.question.entity.Question");
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	/**
	 * 条件查询，type,content,isOnline
	 * @param searchParams
	 * @return
	 */
    private Specification<Question> buildSpecification(final Map<String,Object> searchParams) {
        Specification<Question> spec = new Specification<Question>(){           
			@Override
			public Predicate toPredicate(Root<Question> root,
				CriteriaQuery<?> cq, CriteriaBuilder cb) {
				Predicate allCondition = null;
				String content=(String) searchParams.get("content");
				Integer type=Integer.parseInt((String) searchParams.get("type")) ;
				Boolean isOnline=(Boolean)searchParams.get("content");
				if(content!=null&&!"".equals(content)){
					Predicate condition=cb.like(root.get("content").as(String.class),"%"+searchParams.get("content")+"%");
					if(null==allCondition)
						allCondition=cb.and(condition);					
					}
				if(type!=null){
					Predicate condition=cb.equal(root.get("type").as(Integer.class),searchParams.get("type"));
					if(null==allCondition)
						allCondition=cb.and(condition);
					else
						allCondition=cb.and(allCondition,condition);
					
					}
				if(isOnline!=null){
					Predicate condition=cb.equal(root.get("isOnline").as(Boolean.class),searchParams.get("isOnline"));
					if(null==allCondition)
						allCondition=cb.and(condition);
					else
						allCondition=cb.and(allCondition,condition);
					
					}
															
				return allCondition;
			}
        	
        };
        return spec;
    }
    
    
	@Override
	public Page<Question> findQuestionByCondition(Map<String,Object> searchParams, Pageable pageable) {
	
		return questionRepository.findAll(buildSpecification(searchParams), pageable);
	}
	
}
