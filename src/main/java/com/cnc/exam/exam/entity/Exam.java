package com.cnc.exam.exam.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.alibaba.fastjson.annotation.JSONField;
import com.cnc.exam.base.entity.BaseEntity;
import com.cnc.exam.question.entity.Question;

@Entity
@Table(name="t_exam")
public class Exam extends BaseEntity<Long>{
	@Column(name="no",length=20)
	private String NO;//考试编号
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="start_time")
	private Date startTime;//考试开始时间
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="end_time")
	private Date endTime;//考试结束时间
	
	@Column(name="duration")
	private Integer duration;//考试时长,单位分钟
	
	@Column(name="pass_line")
	private Integer passLine;//及格线
	
	@Column(name="question_number")
	private Integer questionNumber;//题目数量
	
	@Column(name="exam_url",length=200)
	private String examUrl;//考试链接
	
	private Long courseId;//课程id
	
	//题目
	@JSONField(serialize=false)
	@ManyToMany(targetEntity=Question.class,fetch=FetchType.LAZY)
	@JoinTable(name="t_exam_question",joinColumns=@JoinColumn(name="exam_id"),inverseJoinColumns=@JoinColumn(name="question_id"))
	private List<Question> questions=new ArrayList<>();


	@JSONField(serialize=false)
	@OneToMany(mappedBy = "exam")
	private List<ExamUserMid> examUserMids=new ArrayList<>();
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public Integer getPassLine() {
		return passLine;
	}

	public void setPassLine(Integer passLine) {
		this.passLine = passLine;
	}

	public Integer getQuestionNumber() {
		return questionNumber;
	}

	public void setQuestionNumber(Integer questionNumber) {
		this.questionNumber = questionNumber;
	}

	

	public String getExamUrl() {
		return examUrl;
	}

	public void setExamUrl(String examUrl) {
		this.examUrl = examUrl;
	}

	public Long getCourseId() {
		return courseId;
	}

	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}

	public List<Question> getQuestions() {
		return questions;
	}

	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}

	public List<ExamUserMid> getExamUserMids() {
		return examUserMids;
	}

	public void setExamUserMids(List<ExamUserMid> examUserMids) {
		this.examUserMids = examUserMids;
	}

	public String getNO() {
		return NO;
	}

	public void setNO(String nO) {
		NO = nO;
	}
	
	
	
	
}
