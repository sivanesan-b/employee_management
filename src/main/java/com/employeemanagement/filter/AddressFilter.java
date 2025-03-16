package com.employeemanagement.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Pattern;

public class AddressFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        PrintWriter out = response.getWriter();

        String permanentStreet = request.getParameter("permanent-street") == null ? "" : request.getParameter("permanent-street");
        String permanentState = request.getParameter("permanent-state") == null ? "" : request.getParameter("permanent-state");
        String currentStreet = request.getParameter("current-street") == null ? "" : request.getParameter("current-street");
        String currentState = request.getParameter("current-state") == null ? "" : request.getParameter("current-state");

        String streetMatcherRegex = "^(?!\\d+$)[a-zA-Z0-9 ,.-]{3,}$";
        String stateMatcherRegex = "^[A-Za-z]+(?: [A-Za-z,]+)*$";

        if(permanentStreet.isEmpty() ||
                permanentState.isEmpty() ||
                !Pattern.matches(stateMatcherRegex, permanentState) ||
                !Pattern.matches(streetMatcherRegex, permanentStreet)){
            out.println("{ \"message\": \"Permanent Address is Invalid\" }");
            return;
        } if (currentStreet.isEmpty() || currentState.isEmpty()||
                !Pattern.matches(streetMatcherRegex, currentStreet) ||
                !Pattern.matches(stateMatcherRegex, currentState)){
            out.println("{ \"message\": \"Current Address is Invalid\" }");
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}