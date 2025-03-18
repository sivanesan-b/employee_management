package com.employeemanagement.dao;

import com.employeemanagement.model.Employee;

import java.util.List;

public interface EmployeeDao {
    boolean isAdmin(String username, String password);
    Employee addEmployee(Employee employee);
    Employee getEmployee(long employeeId);
    int removeEmployee(long employeeId);
    int updateEmployee(Employee oldEmployee, Employee newEmployee);
    List<Employee> getEmployees();
    List<Employee> getEmployees(int limit);
}
