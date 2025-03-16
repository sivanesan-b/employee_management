package com.employeemanagement.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Pattern;

public class PhoneNumberFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        PrintWriter out = response.getWriter();
        String employeePhoneNo = request.getParameter("phone")==null?"":request.getParameter("phone");
        if(Pattern.matches("^(?!.*000000000)[6-9]\\d{9}$", employeePhoneNo)) {
            filterChain.doFilter(servletRequest, servletResponse);
        }
        else {
            out.println("{ \"message\": \"Invalid Phone Number\" }");
        }
    }
}