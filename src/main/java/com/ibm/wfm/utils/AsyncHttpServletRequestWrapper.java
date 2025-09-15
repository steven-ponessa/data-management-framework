package com.ibm.wfm.utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import java.util.*;

public class AsyncHttpServletRequestWrapper extends HttpServletRequestWrapper {
    private final String requestURI;
    private final String method;
    private final Map<String, String[]> parameterMap;

    public AsyncHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
        this.requestURI = request.getRequestURI();
        this.method = request.getMethod();
        this.parameterMap = new HashMap<>(request.getParameterMap());
    }

    @Override
    public String getRequestURI() {
        return requestURI;
    }

    @Override
    public String getMethod() {
        return method;
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        return parameterMap;
    }

    @Override
    public String getParameter(String name) {
        String[] values = parameterMap.get(name);
        return (values != null && values.length > 0) ? values[0] : null;
    }

    @Override
    public String[] getParameterValues(String name) {
        return parameterMap.get(name);
    }

    @Override
    public Enumeration<String> getParameterNames() {
        return Collections.enumeration(parameterMap.keySet());
    }
}
