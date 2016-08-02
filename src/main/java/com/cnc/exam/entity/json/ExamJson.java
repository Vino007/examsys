package com.cnc.exam.entity.json;

import java.util.Date;




import com.cnc.exam.exam.entity.Exam;

public class ExamJson {
	private Exam exam;
	private Integer status;//状态
	
	public Exam getExam() {
		return exam;
	}
	public void setExam(Exam exam) {
		this.exam = exam;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	
	
	
}
