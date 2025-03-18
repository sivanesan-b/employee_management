package com.employeemanagement.servlet;

import com.employeemanagement.config.LoggerConfig;
import com.employeemanagement.dao.EmployeeDao;
import com.employeemanagement.dao.EmployeeDaoImpl;
import com.employeemanagement.service.EmployeeService;
import com.employeemanagement.service.EmployeeServiceImpl;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginServlet extends HttpServlet {
    private EmployeeService employeeService;
    private static final Logger LOGGER = LoggerConfig.getLogger();

    @Override
    public void init(){
        EmployeeDao employeeDao = new EmployeeDaoImpl();
        this.employeeService = new EmployeeServiceImpl(employeeDao);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        LOGGER.log(Level.INFO, "In LoginServlet");
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if(employeeService.isAdmin(username, password)){
            HttpSession adminSession = request.getSession();
            adminSession.setAttribute("admin-logged", username);
            LOGGER.log(Level.INFO,"valid admin");
            response.sendRedirect("dashboard.jsp");
        } else {
            LOGGER.log(Level.INFO,"invalid admin");
            response.sendRedirect("index.jsp");
        }
    }
}
