package org.yez.easyweb.source.RDBMS;

import java.sql.SQLException;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.yez.easyweb.source.DataSource;
import org.yez.easyweb.source.Template;

public class RDBMSDataSource implements DataSource {
    
    private static javax.sql.DataSource pool;
    
    static{
        pool = new ComboPooledDataSource();//默认读取c3p0中默认配置
    }
    
    @Override
    public Template getTemplate() {
        try {
            RDBMSTemplete template = new RDBMSTemplete(pool.getConnection());
            return template;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean close() {
        return true;
    }

    @Override
    public void init() {
        
    }

}
