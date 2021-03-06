package com.cnc.exam.auth.entity;

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

import com.alibaba.fastjson.annotation.JSONField;
import com.cnc.exam.base.entity.BaseEntity;
import com.cnc.exam.course.entity.CourseCategory;

@Entity
@Table(name="t_role")
public class Role extends BaseEntity<Long> {
	@Column(name="name",length=100)
	private String name;
	@Column(name="description",length=200)
	private String description;
	@Column(name="available")
	private Boolean available=Boolean.TRUE;
	
	@JSONField(serialize=false)
	@ManyToMany(targetEntity=Resource.class)
	@JoinTable(name="t_role_resource",joinColumns=@JoinColumn(name="role_id"),inverseJoinColumns=@JoinColumn(name="resource_id"))
	private Set<Resource> resources=new HashSet<Resource>();
	
	@JSONField(serialize=false)
	@ManyToMany(targetEntity=User.class,mappedBy="roles")
	private Set<User> users=new HashSet<User>();

	@ManyToMany(targetEntity=CourseCategory.class)
	@JoinTable(name="t_role_category",joinColumns=@JoinColumn(name="role_id"),inverseJoinColumns=@JoinColumn(name="category_id"))
	private Set<CourseCategory> categories=new HashSet<CourseCategory>();

	public Role() {
	}
	
	public Role(String mark, String name) {
	
		this.name = name;
	}

	public Set<CourseCategory> getCategories() {
		return categories;
	}
	public void setCategories(Set<CourseCategory> categories) {
		this.categories = categories;
	}
	public Set<User> getUsers() {
		return users;
	}
	public void setUsers(Set<User> users) {
		this.users = users;
	}
	public Set<Resource> getResources() {
		return resources;
	}
	public void setResources(Set<Resource> resources) {
		this.resources = resources;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Boolean getAvailable() {
		return available;
	}
	public void setAvailable(Boolean available) {
		this.available = available;
	}
	
	
	
}
