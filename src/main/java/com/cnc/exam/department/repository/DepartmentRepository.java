package com.cnc.exam.department.repository;

import com.cnc.exam.base.repository.BaseRepository;
import com.cnc.exam.department.entity.Department;

/**
 * Created by zhangyn on 2016/7/28.
 */
public interface DepartmentRepository extends BaseRepository<Department, Long> {
    Department findByDeptName(String deptName);
}
