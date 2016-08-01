package com.cnc.exam.log.entity;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.cnc.exam.auth.entity.User;
import com.cnc.exam.base.entity.BaseEntity;

/**
 * 日志记录实体类
 * @author guoy1
 *
 */
@Entity
@Table(name = "t_logs")
public class LogsEntity extends BaseEntity<Long> {
	@ManyToOne(cascade=CascadeType.ALL,fetch=FetchType.EAGER)
	@JoinColumn(name="user_id")
	private User user;
	
	@Column(name = "op_type")
	private int opType;//日志类别1.课程设置日志 2.考试日志 3.管理员日志 4.学员日志
	
	@Column(name = "op_content")
	private String content;//内容
	
	@Column(name = "op_date")
	private Timestamp timestamp;//时间戳

	public LogsEntity(User user, int opType, String content, Timestamp timestamp) {
		super();
		this.user = user;
		this.opType = opType;
		this.content = content;
		this.timestamp = timestamp;
	}

	public LogsEntity() {
		super();
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public int getOpType() {
		return opType;
	}

	public void setOpType(int opType) {
		this.opType = opType;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

}
