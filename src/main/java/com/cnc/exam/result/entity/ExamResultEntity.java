package com.cnc.exam.result.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;

import com.cnc.exam.auth.entity.User;
import com.cnc.exam.base.entity.BaseEntity;
import com.cnc.exam.exam.entity.Exam;

/**
 * 考试结果实体类
 * @author guoy1
 *
 */
@Entity
@Table(name = "t_exam_results")
public class ExamResultEntity extends BaseEntity<Long>{
	@ManyToOne(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	@JoinColumn(name="user_id")
	private User user;
	
	@ManyToOne(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	@JoinColumn(name="exam_id")
	private Exam exam;
	
	@Column(name = "score", length = 5)
	private long score;
	
	@Column(name = "is_pass", length = 2)
	private int isPass;
	
	@Column(name = "performance")
	private String performance;//答题情况

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

	public ExamResultEntity(long score, int isPass,
			String performance) {
		super();
		this.score = score;
		this.isPass = isPass;
		this.performance = performance;
	}

	public Exam getExam() {
		return exam;
	}

	public void setExam(Exam exam) {
		this.exam = exam;
	}

	public ExamResultEntity(User user, Exam exam, long score, int isPass,
			String performance) {
		super();
		this.user = user;
		this.exam = exam;
		this.score = score;
		this.isPass = isPass;
		this.performance = performance;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	}
