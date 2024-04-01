package com.sky.service;

import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
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
     * @param empDTO
     */
    void save(EmployeeDTO empDTO);

    PageResult page(EmployeePageQueryDTO employeePageQueryDTO);

    void startOrStop(Integer status, Long id);

    Employee getById(Long id);

    /**
     * 修改员工
     * @param employeeDTO
     */
    void update(EmployeeDTO employeeDTO);
}
