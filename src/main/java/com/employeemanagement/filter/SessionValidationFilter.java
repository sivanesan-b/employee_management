package com.employeemanagement.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class SessionValidationFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession adminSession = request.getSession(false);
        if(adminSession==null || adminSession.getAttribute("admin-logged")==null){
            response.sendRedirect("index.jsp");
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
