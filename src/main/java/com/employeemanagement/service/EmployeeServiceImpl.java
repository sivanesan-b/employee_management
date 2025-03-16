package com.employeemanagement.service;

import com.employeemanagement.config.LoggerConfig;
import com.employeemanagement.dao.EmployeeDao;
import com.employeemanagement.model.AddressTypes;
import com.employeemanagement.model.Employee;

import java.util.logging.Level;
import java.util.logging.Logger;

public class EmployeeServiceImpl implements EmployeeService{
    private final EmployeeDao employeeDao;
    private static final Logger LOGGER = LoggerConfig.getLogger();

    public boolean isAdmin(String username, String password){
        return employeeDao.isAdmin(username, password);
    }

    public int addEmployee(Employee employee){
        LOGGER.log(Level.INFO,"Employee Service layer reached for add Employee");
        Employee addedEmployee = employeeDao.addEmployee(employee);

        if(addedEmployee == null){
            return -1;
        }else if(addedEmployee.getId()!=0){
            return 1;
        } else {
            return 0;
        }
    }

    public Employee getEmployee(long employeeId){
        LOGGER.log(Level.INFO,"Employee service layer reached for get employee");
        return employeeDao.getEmployee(employeeId);
    }

    public int removeEmployee(long employeeId) {
        LOGGER.log(Level.INFO, "Employee service layer reached for remove employee");
        return employeeDao.removeEmployee(employeeId);
    }

    public int updateEmployee(Employee oldEmployee, Employee newEmployee){
        LOGGER.log(Level.INFO, "Employee service layer reached for remove employee");
        if(oldEmployee.getPermanentAddress().equals(oldEmployee.getCurrentAddress())){
            oldEmployee.getPermanentAddress().setAddressType(AddressTypes.BOTH_PERMANENT_AND_CURRENT);
        }else{
            oldEmployee.getPermanentAddress().setAddressType(AddressTypes.PERMANENT);
            oldEmployee.getCurrentAddress().setAddressType(AddressTypes.CURRENT);
        }
        return employeeDao.updateEmployee(oldEmployee, newEmployee);
    }

    public EmployeeServiceImpl(EmployeeDao employeeDao){
        this.employeeDao = employeeDao;
    }
}
