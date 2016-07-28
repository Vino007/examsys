package com.cnc.exam.exam.service;

import java.lang.reflect.InvocationTargetException;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.cnc.exam.base.service.AbstractBaseServiceImpl;
import com.cnc.exam.exam.entity.Exam;
import com.cnc.exam.exam.repository.ExamRepository;
import com.cnc.exam.question.entity.Question;
import com.cnc.exam.question.repository.QuestionRepository;

@Service("examService")
public class ExamServiceImpl extends AbstractBaseServiceImpl<Exam, Long> implements ExamService{
	@Autowired
	private ExamRepository examRepository;
	@Autowired
	private QuestionRepository questionRepository;
	@Override
	public void update(Exam obj) {
		if(obj==null||obj.getId()==null)  throw new IllegalArgumentException("id异常");
		
		Exam obj2=examRepository.findOne(obj.getId());
		if(obj2==null) throw new NullPointerException("该id对应的实体类不存在");
			try {
				@SuppressWarnings("unchecked")
				Class<Exam> clazz=(Class<Exam>) Class.forName("com.cnc.exam.exam.entity.Exam");
				Method[] methods=clazz.getDeclaredMethods();
				for(Method m:methods){
					 if(m.getName().substring(0, 3).equals("get")){					
						Object value=m.invoke(obj);
						if(value!=null&&!"".equals(value)){
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
	 * 条件查询
	 * @param searchParams
	 * @return
	 */
    private Specification<Exam> buildSpecification(final Map<String,Object> searchParams) {
        Specification<Exam> spec = new Specification<Exam>(){           
			@Override
			public Predicate toPredicate(Root<Exam> root,
				CriteriaQuery<?> cq, CriteriaBuilder cb) {
				Predicate allCondition = null;
				String content=(String) searchParams.get("content");
				Integer type=null;
				Boolean isOnline=null;
				if(searchParams.get("type")!=null)
					type=Integer.parseInt((String) searchParams.get("type")) ;
				if(searchParams.get("isOnline")!=null)					
					isOnline=Boolean.valueOf((String) searchParams.get("isOnline"));
				
				if(content!=null&&!"".equals(content)){
					Predicate condition=cb.like(root.get("content").as(String.class),"%"+searchParams.get("content")+"%");
					if(null==allCondition)
						allCondition=cb.and(condition);					
					}
				if(type!=null){
					Predicate condition=cb.equal(root.get("type").as(Integer.class),type);
					if(null==allCondition)
						allCondition=cb.and(condition);
					else
						allCondition=cb.and(allCondition,condition);
					
					}
				//实体类基本类型需要使用包装器类
				if(isOnline!=null){
					Predicate condition=cb.equal(root.get("isOnline").as(Boolean.class),isOnline);
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
	public Page<Exam> findExamByCondition(Map<String,Object> searchParams, Pageable pageable) {
	
		return examRepository.findAll(buildSpecification(searchParams), pageable);
	}
	/**
	 * 绑定试题,需要将之前绑定的也传过来
	 */
	@Override
	public void bindQuestion(Long examId,Long[] questionIds) {

		if(examId==null||examId<0) throw new IllegalArgumentException("exam id 不合法");
		Exam exam=examRepository.findOne(examId);
		if(exam==null) throw new IllegalArgumentException("exam id 对应的记录不存在");
		List<Question> questions=new ArrayList<>();
		for(Long id:questionIds){
			questions.add(questionRepository.findOne(id));
		}
		exam.setQuestions(questions);
	}

	@Override
	public void autoGenerateExam(Long examId) {
		//根据课程id查出课程所拥有的题库
		Exam exam=examRepository.findOne(examId);
		int questionNumber=exam.getQuestionNumber();
		List<Question> questions=new ArrayList<>();
		List<Question> allQuestions=questionRepository.findAll();//这里要根据课程来找出对应的问题
		Set<Question> selectQuestions=new HashSet<>();
		Random random=new Random();
		while(selectQuestions.size()<questionNumber&&selectQuestions.size()<allQuestions.size()){//questionNumber大于课程question怎么办
			//随机挑一个题目进来
			selectQuestions.add(allQuestions.get(random.nextInt(5000)%allQuestions.size()));
		}
		List<Question> selectQuestionList=new ArrayList<>();
		selectQuestionList.addAll(selectQuestions);
		exam.setQuestions(selectQuestionList);
	}
	
}