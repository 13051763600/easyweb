package org.yez.easyweb.filter;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;
import org.yez.easyweb.log.RequestInLogger;
import org.yez.easyweb.util.Util;

public class LogFilter implements Filter{
    
    public static final String REQUEST_LOG_UUID = "REQUEST_LOG_UUID";

    private FilterConfig config;
    
    public FilterConfig getConfig() {
        return config;
    }

    public void setConfig(FilterConfig config) {
        this.config = config;
    }

    @Override
    public void destroy() {
        
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        try{
            HttpServletRequest request = (HttpServletRequest)req;
            UUID uuid = UUID.randomUUID();
            request.setAttribute(REQUEST_LOG_UUID, uuid);
            StringBuilder sb = new StringBuilder();
            sb.append(uuid).append("\t");
            sb.append(request.getRequestURI().replace(request.getContextPath(), "")).append("\t");
            sb.append(request.getMethod()).append("\t");
            sb.append(request.getContentType()).append("\t");
            sb.append(request.getCharacterEncoding()).append("\t");
            JSONObject paramJson = new JSONObject();
            paramJson.putAll(Util.prepareParams(request));
            sb.append(paramJson.toJSONString());
            RequestInLogger.getInstant().log(sb.toString());
        }finally{
            chain.doFilter(req, res);
        }
    }

    @Override
    public void init(FilterConfig config) throws ServletException {
        this.config = config;
    }

}
