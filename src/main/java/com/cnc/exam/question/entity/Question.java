package com.cnc.exam.question.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.cnc.exam.base.entity.BaseEntity;

@Entity
@Table(name="t_question")
public class Question extends BaseEntity<Long>{
	/**
	 * 题干内容
	 */
	@Column(name="content",length=500)
	private String content;
	/**
	 * 题干图片url
	 */
	@Column(name="content_img_url",length=255)
	private String contentImgageUrl;
	/**
	 * 选项
	 */
	@Column(name="choices",length=255)
	private String choices="";
	/**
	 * 答案
	 */
	@Column(name="answer",length=255)
	private String answer="";
	/**
	 * 题目类型 1.单选 2.多选 3.填空 4.判断
	 */
	@Column(name="type")
	private Integer type=1;
	/**
	 * 通过率
	 */
	@Column(name="passing_rate")
	private float passingRate=0;
	/**
	 * 是否已考？
	 */
	@Column(name="is_exam")
	private boolean isExam=Boolean.FALSE;
	/**
	 * 是否在线
	 */
	@Column(name="is_online")
	private boolean isOnline=Boolean.TRUE;
	
	@Column(name="course_id")
	private Long courseId;
	
	
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
	public boolean getIsExam() {
		return isExam;
	}
	public void setIsExam(boolean isExam) {
		this.isExam = isExam;
	}
	public boolean getIsOnline() {
		return isOnline;
	}
	public void setIsOnline(boolean isOnline) {
		this.isOnline = isOnline;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getContentImgageUrl() {
		return contentImgageUrl;
	}
	public void setContentImgageUrl(String contentImgageUrl) {
		this.contentImgageUrl = contentImgageUrl;
	}
	public Long getCourseId() {
		return courseId;
	}
	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}
}
