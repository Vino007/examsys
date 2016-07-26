package com.cnc.exam.result.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.cnc.exam.base.entity.BaseEntity;

/**
 * 考试结果实体类
 * @author guoy1
 *
 */
@Entity
@Table(name = "t_exam_results")
public class ExamResultEntity extends BaseEntity<Long>{
	@Column(name = "user_id", length = 100)
	private long userId;
	
	@Column(name = "exam_id", length = 100)
	private long examId;
	
	@Column(name = "score", length = 5)
	private long score;
	
	@Column(name = "is_pass", length = 2)
	private int isPass;
	
	@Column(name = "performance")
	private String performance;//答题情况

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getExamId() {
		return examId;
	}

	public void setExamId(long examId) {
		this.examId = examId;
	}

	public long getScore() {
		return score;
	}

	public void setScore(long score) {
		this.score = score;
	}

	public int getIsPass() {
		return isPass;
	}

	public void setIsPass(int isPass) {
		this.isPass = isPass;
	}

	public String getPerformance() {
		return performance;
	}

	public void setPerformance(String performance) {
		this.performance = performance;
	}

	public ExamResultEntity() {
		super();
	}

	public ExamResultEntity(long userId, long examId, long score, int isPass,
			String performance) {
		super();
		this.userId = userId;
		this.examId = examId;
		this.score = score;
		this.isPass = isPass;
		this.performance = performance;
	}
	
}
