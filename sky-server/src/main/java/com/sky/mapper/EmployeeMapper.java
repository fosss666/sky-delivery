package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface EmployeeMapper {

    /**
     * 根据用户名查询员工
     *
     * @param username
     * @return
     */
    @Select("select * from sky_take_out.employee where username = #{username}")
    Employee getByUsername(String username);

    /**
     * 新增员工
     */
    @Insert("insert into sky_take_out.employee(name, username, password, phone, sex, id_number, create_time, update_time, " +
            "create_user, update_user)values (#{employee.name},#{employee.username},#{employee.password},#{employee.phone}," +
            "#{employee.sex},#{employee.idNumber},#{employee.createTime},#{employee.updateTime},#{employee.createUser}," +
            "#{employee.updateUser})")
    @AutoFill(value = OperationType.INSERT)
    void insert(@Param("employee") Employee employee);

    /**
     * 模糊查询
     */
    Page<Employee> searchPage(@Param("page") EmployeePageQueryDTO employeePageQueryDTO);

    /**
     * 启用禁用员工账号
     */
    void updateStatus(@Param("status") int status, @Param("id") Long id);

    /**
     * 编辑员工信息
     *
     * @param employee
     */
    @AutoFill(value = OperationType.UPDATE)
    void updateEmployee(@Param("employee") Employee employee);

    /**
     * 根据id查询员工
     */
    Employee getById(@Param("id") Long id);
}
