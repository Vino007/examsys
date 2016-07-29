package com.cnc.exam.common;

import java.util.List;

public class MyPage<T> {
	private List<T> content;
	private long totalPages ;
	private long totalElements;
	private int size;
	public List<T> getContent() {
		return content;
	}
	public void setContent(List<T> content) {
		this.content = content;
	}
	public long getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(long totalPages) {
		this.totalPages = totalPages;
	}
	public long getTotalElements() {
		return totalElements;
	}
	public void setTotalElements(long totalElements) {
		this.totalElements = totalElements;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	
	
	
}
