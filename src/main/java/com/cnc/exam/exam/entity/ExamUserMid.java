package com.cnc.exam.exam.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.cnc.exam.auth.entity.User;

@Entity
@Table(name="t_exam_user")
public class ExamUserMid {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	@ManyToOne
	@JoinColumn(name = "exam_id")
	private Exam exam;
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	private String status;//请假，免考

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Exam getExam() {
		return exam;
	}

	public void setExam(Exam exam) {
		this.exam = exam;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}
