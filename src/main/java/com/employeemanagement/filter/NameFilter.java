package com.employeemanagement.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Pattern;

public class NameFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        PrintWriter out = response.getWriter();
        String employeeName = request.getParameter("name")==null?"":request.getParameter("name");
        if(employeeName.length()>2 &&
                Pattern.matches("[a-zA-Z. ]+$", employeeName)){
            filterChain.doFilter(servletRequest, servletResponse);
        }
        else {
            out.println("{ \"message\": \"Enter a valid Name\" }");
        }
    }
}