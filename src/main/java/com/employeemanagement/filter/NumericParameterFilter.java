package com.employeemanagement.filter;

import com.employeemanagement.config.LoggerConfig;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NumericParameterFilter implements Filter {
    private static final Logger LOGGER = LoggerConfig.getLogger();
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        PrintWriter out = response.getWriter();
        String employeeId = request.getParameter("id")==null?"":request.getParameter("id");
        String queryLimit = request.getParameter("limit")==null?"": request.getParameter("limit");

        // by passing this filter for getting all employee records
        if("getAll".equals(request.getParameter("action"))){
            filterChain.doFilter(request, response);
            return;
        }
        // parsing employee id from the request
        try{
            if((employeeId.isEmpty() || Long.parseLong(employeeId)<1) && queryLimit.isEmpty()){
                throw new NumberFormatException();
            } else {
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }
        } catch (NumberFormatException ex){
            LOGGER.log(Level.WARNING, "Invalid ID format");
            out.println("{ \"message\": \"Enter a valid ID & ID can't be Negative or Zero\" }");
        }
        // parsing query limit from the request
        try{
            if((queryLimit.isEmpty() || Integer.parseInt(queryLimit)<0) && employeeId.isEmpty()){
                throw new NumberFormatException();
            } else {
                filterChain.doFilter(servletRequest, servletResponse);
            }
        } catch (NumberFormatException ex){
            LOGGER.log(Level.WARNING, "Invalid limit");
            out.println("{ \"message\": \"Enter a valid limit, 0 for all records\" }");
        }
    }
}