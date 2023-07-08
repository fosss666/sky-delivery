package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
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
    @Select("select * from employee where username = #{username}")
    Employee getByUsername(String username);

    /**
     * 新增员工
     */
    @Insert("insert into sky_take_out.employee(name, username, password, phone, sex, id_number, create_time, update_time, " +
            "create_user, update_user)values (#{employee.name},#{employee.username},#{employee.password},#{employee.phone}," +
            "#{employee.sex},#{employee.idNumber},#{employee.createTime},#{employee.updateTime},#{employee.createUser}," +
            "#{employee.updateUser})")
    void insert(@Param("employee") Employee employee);

    /**
     * 模糊查询
     */
    Page<Employee> searchPage(@Param("page") EmployeePageQueryDTO employeePageQueryDTO);
}
