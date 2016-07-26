package com.cnc.exam.mail.entity;

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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.cnc.exam.base.entity.BaseEntity;

/**
 * 邮件模板实体类
 * @author guoy1
 *
 */
@Entity
@Table(name = "t_mail")
public class MailEntity extends BaseEntity<Long> {
	
	@Column(name = "op_content")
	private String content;//内容
	
	@Column(name = "op_type")
	private String opType;

	public MailEntity() {
		super();
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getOpType() {
		return opType;
	}

	public void setOpType(String opType) {
		this.opType = opType;
	}

	public MailEntity(String content, String opType) {
		super();
		this.content = content;
		this.opType = opType;
	}
	
}
