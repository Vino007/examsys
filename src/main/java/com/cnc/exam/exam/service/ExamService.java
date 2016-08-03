package com.cnc.exam.exam.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.cnc.exam.auth.entity.User;
import com.cnc.exam.base.service.BaseService;
import com.cnc.exam.common.MyPage;
import com.cnc.exam.entity.json.ExamJson;
import com.cnc.exam.entity.json.UserJson;
import com.cnc.exam.exam.entity.Exam;
import com.cnc.exam.exam.exception.UserStatusErrorException;
import com.cnc.exam.exam.exception.UserAlreadyHasThisExamException;
import com.cnc.exam.result.entity.ExamResultEntity;

public interface ExamService extends BaseService<Exam, Long> {

	Page<Exam> findExamByCondition(Map<String, Object> searchParams, Pageable pageable);

	void update(Exam exam);

	void bindQuestion(Long examId, Long[] questionIds);
	/**
	 * 根据examId生成考卷，随机从数据库中选择questionNumber道考题
	 * @param examId
	 */
	void autoGenerateExam(Long examId);
	/**
	 * 添加考试人员
	 * @param examId
	 * @param userIds
	 * @throws UserAlreadyHasThisExamException
	 */
	void addUser(Long examId, Long[] userIds) throws UserAlreadyHasThisExamException;
	/**
	 * 删除考试人员
	 * @param examId
	 * @param userIds
	 */
	void removeUser(Long examId, Long[] userIds);
	/**
	 * 修改考试人员状态
	 */
	void updateUserStatus(Long examId,Long[] userIds,Integer status);
	/**
	 * 根据状态查询考试人员
	 * @param examId
	 * @param userIds
	 * @param status
	 */
	MyPage<UserJson> findUsersByStatus(Long examId, Integer status, Pageable pageable);
	

	/**
	 * 
	 * @param userId
	 * @param examId
	 * @param performances 用户的回答
	 * @param isMock 是否是模拟考试
	 * @return
	 * @throws UserStatusErrorException 
	 */
	ExamResultEntity judgeExam(Long userId, Long examId, String[] performances, boolean isMock) throws UserStatusErrorException;

	MyPage<ExamJson> findExamByUser(Long userId, Pageable pageable);
	MyPage<Exam> findExamByUser2(Long userId, Pageable pageable);
	void autoGenerateMockExam(Long examId);

	void bindMockQuestion(Long examId, Long[] questionIds);
	

	void deleteExam(Long[] examIds);

	void clearConnection(Long[] examIds);
}
