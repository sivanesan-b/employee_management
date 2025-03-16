package com.employeemanagement.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

public class IdFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        PrintWriter out = response.getWriter();
        String employeeId = request.getParameter("id")==null?"":request.getParameter("id");
        try{
            if(employeeId.isEmpty() || Long.parseLong(employeeId)<1){
                throw new NumberFormatException();
            }
            filterChain.doFilter(servletRequest, servletResponse);
        }
        catch (NumberFormatException ex){
            out.println("{ \"message\": \"Enter a valid ID & ID can't be Negative or Zero\" }");
        }
    }
}