package com.cnc.exam.question.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.cnc.exam.base.entity.BaseEntity;

@Entity
@Table(name="t_subjects")
public class Question extends BaseEntity<Long>{
	@Column(name="description",length=255)
	private String description="";
	@Column(name="choices",length=255)
	private String choices="";
	@Column(name="answer",length=255)
	private String answer="";
	@Column(name="type")
	private Integer type=1;
	@Column(name="passing_rate")
	private float passingRate=0;
	@Column(name="is_exam")
	private boolean isExam=Boolean.FALSE;
	@Column(name="is_online")
	private boolean isOnline=Boolean.TRUE;
	@Column(name="course_id")
	private Long courseId;
	
	public String getDescription() {
		
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getChoices() {
		return choices;
	}
	public void setChoices(String choices) {
		this.choices = choices;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public float getPassingRate() {
		return passingRate;
	}
	public void setPassingRate(float passingRate) {
		this.passingRate = passingRate;
	}
	public boolean isExam() {
		return isExam;
	}
	public void setExam(boolean isExam) {
		this.isExam = isExam;
	}
	public boolean getIsOnline() {
		return isOnline;
	}
	public void setIsOnline(boolean isOnline) {
		this.isOnline = isOnline;
	}
	
}
