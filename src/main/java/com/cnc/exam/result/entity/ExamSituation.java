package com.cnc.exam.result.entity;

import java.sql.Timestamp;

public class ExamSituation {
	private long examId;
	private int total;
	private int passCount ;
	private int unpassCount;
	private Double passPercent;
	private Timestamp time;
	public ExamSituation(long examId, int total, int passCount,
			int unpassCount, Double passPercent, Timestamp time) {
		super();
		this.examId = examId;
		this.total = total;
		this.passCount = passCount;
		this.unpassCount = unpassCount;
		this.passPercent = passPercent;
		this.time = time;
	}
	public long getExamId() {
		return examId;
	}
	public void setExamId(long examId) {
		this.examId = examId;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public int getPassCount() {
		return passCount;
	}
	public void setPassCount(int passCount) {
		this.passCount = passCount;
	}
	public int getUnpassCount() {
		return unpassCount;
	}
	public void setUnpassCount(int unpassCount) {
		this.unpassCount = unpassCount;
	}
	public Double getPassPercent() {
		return passPercent;
	}
	public void setPassPercent(Double passPercent) {
		this.passPercent = passPercent;
	}
	public Timestamp getTime() {
		return time;
	}
	public void setTime(Timestamp time) {
		this.time = time;
	}
	public ExamSituation() {
		super();
	}
}
