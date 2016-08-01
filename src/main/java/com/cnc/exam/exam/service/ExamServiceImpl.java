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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.cnc.exam.auth.entity.User;
import com.cnc.exam.auth.repository.UserRepository;
import com.cnc.exam.base.service.AbstractBaseServiceImpl;
import com.cnc.exam.common.MyPage;
import com.cnc.exam.course.entity.Course;
import com.cnc.exam.exam.entity.Exam;
import com.cnc.exam.exam.entity.ExamUserMid;
import com.cnc.exam.exam.entity.UserStatusConstants;
import com.cnc.exam.exam.exception.UserAlreadyHasThisExamException;
import com.cnc.exam.exam.repository.ExamRepository;
import com.cnc.exam.exam.repository.ExamUserMidRepository;
import com.cnc.exam.question.entity.Question;
import com.cnc.exam.question.repository.QuestionRepository;

@Service("examService")
public class ExamServiceImpl extends AbstractBaseServiceImpl<Exam, Long> implements ExamService {
	@Autowired
	private ExamRepository examRepository;
	@Autowired
	private QuestionRepository questionRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ExamUserMidRepository examUserMidRepository;
	
	@Override
	public void update(Exam obj) {
		if (obj == null || obj.getId() == null)
			throw new IllegalArgumentException("id异常");

		Exam obj2 = examRepository.findOne(obj.getId());
		if (obj2 == null)
			throw new NullPointerException("该id对应的实体类不存在");
		try {
			@SuppressWarnings("unchecked")
			Class<Exam> clazz = (Class<Exam>) Class.forName("com.cnc.exam.exam.entity.Exam");
			Method[] methods = clazz.getDeclaredMethods();
			for (Method m : methods) {
				if (m.getName().substring(0, 3).equals("get")) {
					Object value = m.invoke(obj);
					if (value != null && !"".equals(value)) {
						Method setMethod = clazz.getDeclaredMethod(
								"set" + m.getName().substring(3, 4).toUpperCase() + m.getName().substring(4),
								m.getReturnType());
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
	 * 
	 * @param searchParams
	 * @return
	 */
	private Specification<Exam> buildSpecification(final Map<String, Object> searchParams) {
		Specification<Exam> spec = new Specification<Exam>() {
			@Override
			public Predicate toPredicate(Root<Exam> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
				Predicate allCondition = null;

				String no = (String) searchParams.get("no");
			
				Integer passLine = null;
				if (searchParams.get("passLine") != null)
					passLine = Integer.parseInt((String) searchParams.get("passLine"));
				

				if (no != null && !"".equals(no)) {
					Predicate condition = cb.like(root.get("no").as(String.class),
							"%" + searchParams.get("no") + "%");
					if (null == allCondition)
						allCondition = cb.and(condition);
				}

				
				if (passLine != null) {
					Predicate condition = cb.equal(root.get("passLine").as(Integer.class), passLine);
					if (null == allCondition)
						allCondition = cb.and(condition);
					else
						allCondition = cb.and(allCondition, condition);

				}

				return allCondition;
			}

		};
		return spec;
	}

	@Override
	public Page<Exam> findExamByCondition(Map<String, Object> searchParams, Pageable pageable) {

		return examRepository.findAll(buildSpecification(searchParams), pageable);
	}

	/**
	 * 绑定试题,需要将之前绑定的也传过来
	 */
	@Override
	public void bindQuestion(Long examId, Long[] questionIds) {

		if (examId == null || examId < 0)
			throw new IllegalArgumentException("exam id 不合法");
		Exam exam = examRepository.findOne(examId);
		if (exam == null)
			throw new IllegalArgumentException("exam id 对应的记录不存在");
		List<Question> questions = new ArrayList<>();
		for (Long id : questionIds) {
			questions.add(questionRepository.findOne(id));
		}
		exam.setQuestions(questions);
	}
	/**
	 * 自动生成要覆盖之前的结果
	 */
	@Override
	public void autoGenerateExam(Long examId) {
		Exam exam = examRepository.findOne(examId);
		Course course = exam.getCourse();
		int questionNumber = exam.getQuestionNumber();
		List<Question> allQuestions = course.getQuestions();// 获取该考试所属课程的所有考题
		Set<Question> selectQuestions = new HashSet<>();
		Random random = new Random();
		// 随机生成策略
		while (selectQuestions.size() < questionNumber && selectQuestions.size() < allQuestions.size()) {// questionNumber大于课程question怎么办
			// 随机挑一个题目进来
			selectQuestions.add(allQuestions.get(random.nextInt(5000) % allQuestions.size()));
		}
		List<Question> selectQuestionList = new ArrayList<>();
		selectQuestionList.addAll(selectQuestions);
		exam.setQuestions(selectQuestionList);
	}

	@Override
	public void addUser(Long examId, Long[] userIds) throws UserAlreadyHasThisExamException {
		if (userIds == null)
			return;
		Exam exam = examRepository.findOne(examId);
		List<ExamUserMid> examUserMids=new ArrayList<>();
		
		for (long userId : userIds) {
			User user = userRepository.findOne(userId);
			ExamUserMid examUserMid = new ExamUserMid();
			examUserMid.setExam(exam);
			examUserMid.setUser(user);
			examUserMid.setStatus(1);// 默认正常				
			examUserMids.add(examUserMid);		
		}
		examUserMidRepository.save(examUserMids);
		

	}

	@Override
	public void removeUser(Long examId, Long[] userIds) {
		if (userIds == null)
			return;

		for (long userId : userIds) {
			examRepository.removeUser(examId, userId);
		}

	}

	@Override
	public void updateUserStatus(Long examId, Long[] userIds, Integer status) {
		// TODO Auto-generated method stub
		if (userIds == null)
			return;
		for (long userId : userIds) {
			examRepository.updateUserStatus(examId, userId, status);
		}

	}

	@Override
	public MyPage<User> findUsersByStatus(final Long examId, final Integer status, Pageable pageable) {
		//查询
		Specification<ExamUserMid> spec = new Specification<ExamUserMid>() {
			@Override
			public Predicate toPredicate(Root<ExamUserMid> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
				Predicate allCondition = cb.equal(root.get("exam").get("id").as(Long.class), examId);

				if (status == null)
					return allCondition;
				else {
					Predicate condition = cb.equal(root.get("status").as(Integer.class), status);
					allCondition = cb.and(condition);
				}

				return allCondition;
			}

		};
		
		Page<ExamUserMid> midPage = examUserMidRepository.findAll(spec, pageable);
		
		//构造数据
		List<User> users = new ArrayList<>();
		for (ExamUserMid mid : midPage.getContent()) {
			users.add(mid.getUser());
		}
		MyPage<User> userPage = new MyPage<>();
		userPage.setContent(users);
		userPage.setTotalPages(midPage.getTotalPages());
		userPage.setTotalElements(midPage.getTotalElements());
		userPage.setSize(midPage.getSize());

		return userPage;

	}

}
