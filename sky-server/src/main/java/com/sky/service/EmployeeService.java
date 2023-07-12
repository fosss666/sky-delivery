package com.sky.service;

import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.dto.PasswordEditDTO;
import com.sky.entity.Employee;
import com.sky.result.PageResult;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);
    /**
     * 新增员工
     */
    void save(EmployeeDTO employeeDTO);
    /**
     * 分页查询
     *
     */
    PageResult searchPage(EmployeePageQueryDTO employeePageQueryDTO);
    /**
     * 启用禁用员工账号
     */
    void updateStatus(int status, Long id);
    /**
     * 编辑员工信息
     */
    void updateEmployee(EmployeeDTO employeeDTO);
    /**
     * 根据id查询员工
     */
    Employee getById(Long id);
    /**
     * 修改密码
     * @return
     */
    boolean editPassword(PasswordEditDTO passwordEditDTO);
}
