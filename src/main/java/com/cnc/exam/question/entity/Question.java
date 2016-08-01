package com.cnc.exam.question.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.alibaba.fastjson.annotation.JSONField;
import com.cnc.exam.base.entity.BaseEntity;
import com.cnc.exam.course.entity.Course;
import com.cnc.exam.exam.entity.Exam;

@Entity
@Table(name="t_question")
public class Question extends BaseEntity<Long>{
	
	@Column(name="no",length=20)
	private String no;//题目编号
	/**
	 * 题干内容
	 */
	@Column(name="content",length=500)
	private String content;
	/**
	 * 题干图片url
	 */
	@Column(name="content_img_url",length=255)
	private String contentImgageUrl="";
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
	private Float passingRate=Float.valueOf(0);
	/**
	 * 是否已考？
	 */
	@Column(name="is_exam")
	private Boolean isExam=Boolean.FALSE;
	/**
	 * 是否在线
	 */
	@Column(name="is_online")
	private Boolean isOnline=Boolean.TRUE;
	
	//所属课程
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "course_id")
	private Course course;
	//使用过该考题的考试
	@JSONField(serialize=false)
	@ManyToMany(targetEntity=Exam.class,mappedBy="questions")
	private List<Exam> exams=new ArrayList<Exam>();
	
	
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
	
	public Float getPassingRate() {
		return passingRate;
	}
	public void setPassingRate(Float passingRate) {
		this.passingRate = passingRate;
	}
	public Boolean getIsExam() {
		return isExam;
	}
	public void setIsExam(Boolean isExam) {
		this.isExam = isExam;
	}
	public Boolean getIsOnline() {
		return isOnline;
	}
	public void setIsOnline(Boolean isOnline) {
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
	
	public List<Exam> getExams() {
		return exams;
	}
	public void setExams(List<Exam> exams) {
		this.exams = exams;
	}
	
	public Course getCourse() {
		return course;
	}
	public void setCourse(Course course) {
		this.course = course;
	}
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	
	
}
