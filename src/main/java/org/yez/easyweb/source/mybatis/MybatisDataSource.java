package org.yez.easyweb.source.mybatis;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLDecoder;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.yez.easyweb.source.DataSource;
import org.yez.easyweb.source.Initialize;
import org.yez.easyweb.source.Template;

public class MybatisDataSource implements DataSource {

    @Initialize
    private String filePath;
    private SqlSessionFactory sessionFactory;
    
    @Override
    public Template getTemplate() {
        return new MybatisTemplate(getSessionFactory().openSession());
    }
    
    private SqlSessionFactory getSessionFactory() {
        if (null == filePath){
            throw new RuntimeException("Mybatis source file can't be null");
        }
        if (null == sessionFactory){
            File file;
			try {
				file = new File(URLDecoder.decode(Thread.currentThread().getContextClassLoader().getResource("").getPath(),"UTF-8") + "/" + filePath);
			
				InputStream inputStream = new FileInputStream(file);
                sessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
        }
        return sessionFactory;
    }

    @Override
    public boolean close() {
        return true;
    }
    public String getFilePath() {
        return filePath;
    }
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void init() {
        
    }
}
