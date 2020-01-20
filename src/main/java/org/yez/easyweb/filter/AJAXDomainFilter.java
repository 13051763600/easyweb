package org.yez.easyweb.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.yez.easyweb.util.StringUtils;

public class AJAXDomainFilter implements Filter{
    
    @Value("${filter.ajaxDomain.accessAllowOrigin}")
    private String accessAllowOrigin;
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		   HttpServletResponse res = (HttpServletResponse)response;
		   res.setContentType("text/html;charset=UTF-8");  
		   res.setHeader("Access-Control-Allow-Origin",  StringUtils.isNotEmpty(this.accessAllowOrigin) ? this.accessAllowOrigin : "*");
		   res.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE,PUT");  
		   res.setHeader("Access-Control-Max-Age", "0");  
		   res.setHeader("Access-Control-Allow-Headers", "Origin, No-Cache, X-Requested-With, "
		           + "If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With,userId,token,isFront");  
		   res.setHeader("Access-Control-Allow-Credentials", "true");  
		   res.setHeader("XDomainRequestAllowed","1");  
		   chain.doFilter(request, res);
	}

	@Override
	public void destroy() {
		
	}
	
}
