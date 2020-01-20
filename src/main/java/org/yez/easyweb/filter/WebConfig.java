package org.yez.easyweb.filter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CharacterEncodingFilter;

import org.yez.easyweb.util.StringUtils;

@Configuration
public class WebConfig {
    
    @Value("${filter.charset}")
    private String charSet;
    @Value("${filter.forceEncoding}")
    private String forceEncoding;
    @Value("${filter.request.forceEncoding}")
    private String forceRequestEncoding;
    @Value("${filter.response.forceEncoding}")
    private String forceResponseEncoding;
    
    @Bean  
    public FilterRegistrationBean logFilterRegistrationBean(){  
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();  
        LogFilter logFilter = new LogFilter();
        filterRegistrationBean.setFilter(logFilter);  
        filterRegistrationBean.setEnabled(true);
        filterRegistrationBean.addUrlPatterns("/*");  
        return filterRegistrationBean;  
    }
    
    @Bean  
    public FilterRegistrationBean charSetFilterRegistrationBean(){  
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();  
        CharacterEncodingFilter charSetFilter = new CharacterEncodingFilter();
        charSetFilter.setEncoding(StringUtils.isNotEmpty(this.forceEncoding) ? this.charSet : "UTF-8");
        if (StringUtils.isNotEmpty(this.forceEncoding)){
            charSetFilter.setForceEncoding(Boolean.valueOf(this.forceEncoding));;
        }
        if (StringUtils.isNotEmpty(this.forceRequestEncoding)){
            charSetFilter.setForceRequestEncoding(Boolean.valueOf(this.forceRequestEncoding));;
        }
        if (StringUtils.isNotEmpty(this.forceResponseEncoding)){
            charSetFilter.setForceResponseEncoding(Boolean.valueOf(forceResponseEncoding));;
        }
        filterRegistrationBean.setFilter(charSetFilter);
        filterRegistrationBean.setEnabled(true);
        filterRegistrationBean.addUrlPatterns("/*");  
        return filterRegistrationBean;  
    }
    
    @Bean  
    public FilterRegistrationBean ajaxDomainFilterRegistrationBean(){  
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();  
        AJAXDomainFilter ajaxDomainFilter = new AJAXDomainFilter();
        filterRegistrationBean.setFilter(ajaxDomainFilter);  
        filterRegistrationBean.setEnabled(true);
        filterRegistrationBean.addUrlPatterns("/*");  
        return filterRegistrationBean;  
    }
}
