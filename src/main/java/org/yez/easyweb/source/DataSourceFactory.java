package org.yez.easyweb.source;

import org.yez.easyweb.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class DataSourceFactory {
    
    public static final String ENV_LOCAL = "local";

    private static DataSourceFactory instant;
    
    public static final String SERVER_ENV = "SERVER_ENV";
    
    private static Map<String, DataSource> cache = new HashMap<String, DataSource>();

    public DataSource getDataSource(String name){
        return cache.get(name);
    }
    
    public static DataSourceFactory getInstant(){
        if (null == instant){
            reload();
        }
        return instant;
    }

    private static void reload() {
        instant = new DataSourceFactory();
        Properties prop = new Properties();
        String env = System.getenv(SERVER_ENV);
        env = StringUtils.isNotEmpty(env) ? env : ENV_LOCAL;
//        InputStream in = DataSourceFactory.class.getResourceAsStream("dataSource." + env + ".properties");
        InputStream in =null;
        try {
			File file = new File(URLDecoder.decode(
			        DataSourceFactory.class.getResource(
			                "/datasource/dataSource." + env + ".properties")
                            .getFile(), "UTF-8"));
			if(file.exists()){
				in = new FileInputStream(file);
			}
        } catch (Exception e1) {
			e1.printStackTrace();
		}
        try {
            prop.load(in);
            String[] names = prop.getProperty("dataSource.name").split(",");
            for (String name : names){
                String className = prop.getProperty(name + ".className");
                Class clz = Class.forName(className);
                DataSource dataSource = (DataSource)clz.newInstance();
                Field[] fields = clz.getDeclaredFields();
                for (Field field : fields){
                    if (field.getAnnotation(Initialize.class) == null){
                        continue;
                    }
                    String methodName = "set" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1, field.getName().length());
                    Method method = clz.getDeclaredMethod(methodName, field.getType());
                    String value = prop.getProperty(name+".class." + field.getName());
                    if (StringUtils.isNotEmpty(value)){
                        method.invoke(dataSource, value);
                    }
                }
                Method initMethod = clz.getDeclaredMethod("init");
                if (null != initMethod){
                    initMethod.invoke(dataSource);
                }
                cache.put(name, dataSource);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }
}
