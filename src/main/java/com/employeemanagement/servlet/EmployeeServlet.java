package com.employeemanagement.servlet;

import com.employeemanagement.config.LoggerConfig;
import com.employeemanagement.dao.EmployeeDao;
import com.employeemanagement.dao.EmployeeDaoImpl;
import com.employeemanagement.model.Employee;
import com.employeemanagement.service.EmployeeService;
import com.employeemanagement.service.EmployeeServiceImpl;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EmployeeServlet extends HttpServlet {
    private static final Logger LOGGER = LoggerConfig.getLogger();
    private EmployeeService employeeService;

    @Override
    public void init(){
        EmployeeDao employeeDao = new EmployeeDaoImpl();
        employeeService = new EmployeeServiceImpl(employeeDao);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");

        Employee employee = new Employee();
        employee.setName(request.getParameter("name"));
        employee.setPhone(request.getParameter("phone"));
        employee.getPermanentAddress().setStreetName(request.getParameter("permanent-street"));
        employee.getPermanentAddress().setState(request.getParameter("permanent-state"));
        employee.getCurrentAddress().setStreetName(request.getParameter("current-street"));
        employee.getCurrentAddress().setState(request.getParameter("current-state"));

        int isEmployeeAdded = employeeService.addEmployee(employee);
        if(isEmployeeAdded == -1){
            LOGGER.log(Level.WARNING,"Employee with this Phone Number already exists");
            out.println("{\"message\": \"Employee with this Phone Number already exists\"}");
        } else if (isEmployeeAdded==1) {
            LOGGER.log(Level.INFO,"Employee added Successfully");
            out.println("{\"message\": \"Employee added Successfully\"}");
        } else {
            LOGGER.log(Level.SEVERE, "Something went wrong, unable to add employee");
            out.println("{\"error\": \"Something went wrong, unable to add employee\"}");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");

        String action = request.getParameter("action");
        LOGGER.log(Level.INFO, "In get method via " + action + " action");

        switch (action) {
            case "get": {
                long employeeId = Long.parseLong(request.getParameter("id"));
                Employee employee = employeeService.getEmployee(employeeId);
                if (employee == null) {
                    LOGGER.log(Level.WARNING, "No Employee found for the ID: " + employeeId);
                    out.println("{\"message\": \"No Employee found for the ID: " + employeeId + "\"}");
                } else if (employee.getId() == 0) {
                    LOGGER.log(Level.SEVERE, "Something went wrong.");
                    out.println("{\"error\": \"Something went wrong, Unable to get Employee Details\"}");
                } else {
                    LOGGER.log(Level.INFO, "Employee fetched");
                    out.println(employee);
                }
                break;
            }
            case "remove": {
                long employeeId = Long.parseLong(request.getParameter("id"));
                int rows = employeeService.removeEmployee(employeeId);
                if (rows == 1) {
                    LOGGER.log(Level.INFO, "Employee deleted successfully");
                    out.println("{ \"message\": \"Employee deleted successfully\" }");
                } else if (rows == 0) {
                    LOGGER.log(Level.WARNING, "No Employee found for the ID: " + employeeId);
                    out.println("{ \"message\": \"No Employee found for the ID: " + employeeId + "\" }");
                } else if (rows == -1) {
                    LOGGER.log(Level.SEVERE, "Something went wrong in employee deletion");
                    out.println("{ \"message\": \"Something went wrong in employee deletion\" }");
                } else {
                    LOGGER.log(Level.SEVERE, "Employee deleted, " + rows + " rows(s) deleted");
                    out.println("{ \"message\": \"Employee deleted, " + rows + " row(s) deleted\" }");
                }
                break;
            }
            case "update": {
                long employeeId = Long.parseLong(request.getParameter("id"));
                Employee employee = employeeService.getEmployee(employeeId);
                if (employee == null) {
                    LOGGER.log(Level.WARNING, "No Employee found for the ID: " + employeeId);
                    out.println("{ \"message\": \"No Employee found for the ID: " + employeeId + "\" }");
                } else {
                    int rows = updateEmployee(request, employee);
                    if(rows == 2 || rows==3){
                        LOGGER.log(Level.INFO, "Update Successful");
                        out.println("{ \"message\": \"Update Successful\" }");
                    }else {
                        LOGGER.log(Level.WARNING,"Something went wrong in updating");
                        out.println("{ \"message\": \"Something went wrong in updating\" }");
                    }
                }
                break;
            }
        }
    }

    private int updateEmployee(HttpServletRequest request, Employee oldEmployee){
        String employeeName = request.getParameter("name")==null ? oldEmployee.getName() : request.getParameter("name");
        String employeePhoneNo = request.getParameter("phone")==null ? oldEmployee.getPhone() : request.getParameter("phone");
        String permanentStreet = request.getParameter("permanent-street")==null ?
                oldEmployee.getPermanentAddress().getStreetName() : request.getParameter("permanent-street");
        String permanentState = request.getParameter("permanent-state")==null ?
                oldEmployee.getPermanentAddress().getState() : request.getParameter("permanent-state");
        String currentStreet = request.getParameter("current-street")==null ?
                oldEmployee.getCurrentAddress().getStreetName() : request.getParameter("current-street");
        String currentState = request.getParameter("current-state")==null ?
                oldEmployee.getCurrentAddress().getState() : request.getParameter("current-state");

        Employee newEmployee = new Employee();
        newEmployee.setId(oldEmployee.getId());
        newEmployee.setName(employeeName);
        newEmployee.setPhone(employeePhoneNo);
        newEmployee.getPermanentAddress().setStreetName(permanentStreet);
        newEmployee.getPermanentAddress().setState(permanentState);
        newEmployee.getCurrentAddress().setStreetName(currentStreet);
        newEmployee.getCurrentAddress().setState(currentState);

        int rows = employeeService.updateEmployee(oldEmployee, newEmployee);
        LOGGER.log(Level.INFO, "Employee Updated " + rows + " affected");
        return rows;
    }
}