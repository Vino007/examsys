package com.cnc.exam.department.service;

import com.cnc.exam.auth.entity.User;
import com.cnc.exam.base.service.BaseService;
import com.cnc.exam.department.entity.Department;
import com.cnc.exam.department.exception.DeptDuplicateException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * Created by zhangyn on 2016/7/28.
 */
public interface DepartmentService extends BaseService<Department, Long> {
    void updateDept(Department department) throws DeptDuplicateException;

    void saveWithCheckDuplicate(Department department, User user) throws DeptDuplicateException;

    Page<Department> findDeptByCondition(Map<String, Object> searchParams,
                                         Pageable pageable);

    Department findByName(String name);

    List<User> findUserByDeptId(Long deptId);
}
