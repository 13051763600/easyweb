package org.yez.easyweb;

import org.apache.ibatis.logging.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class BootStrap extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(BootStrap.class, args);
    }

    /**
     * 实现SpringBootServletInitializer可以让spring-boot项目在web容器中运行
     *
      * @param builder
     * @return
     */
    @Override  
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {  
        builder.sources(this.getClass());  
        return super.configure(builder);  
    } 
}
