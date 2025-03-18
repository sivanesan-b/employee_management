package com.employeemanagement.service;

import com.employeemanagement.model.Employee;

import java.util.List;

public interface EmployeeService {
    boolean isAdmin(String username, String password);
    int addEmployee(Employee employee);
    Employee getEmployee(long employeeId);
    int removeEmployee(long employeeId);
    int updateEmployee(Employee oldEmployee, Employee newEmployee);
    List<Employee> getEmployees();
    List<Employee> getEmployees(int limit);
}
