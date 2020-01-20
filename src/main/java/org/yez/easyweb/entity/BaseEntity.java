package org.yez.easyweb.entity;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import com.alibaba.fastjson.JSON;

public class BaseEntity implements Cloneable, Serializable{
    /**
     * 
     */
    private static final long serialVersionUID = -6654023787499911168L;
    
    private String name;
    private String jsonHandler;
    private String paramHandler;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getJsonHandler() {
        return jsonHandler;
    }
    public void setJsonHandler(String jsonHandler) {
        this.jsonHandler = jsonHandler;
    }

    public boolean hasHandler() {
        return null != this.jsonHandler && this.jsonHandler.trim().length() > 0;
    }
    
    public JSON invokeHandler(BaseEntity info, JSON result, Map<String, Object> params) {
        try {
            Class handlerClass = Class.forName(this.jsonHandler);
            Object handler = handlerClass.newInstance();
            Method method = handlerClass.getMethod("handler", BaseEntity.class, JSON.class, Map.class);
            result = (JSON)method.invoke(handler, info, result, params);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return result;
    }
	public String getParamHandler() {
		return paramHandler;
	}
	public void setParamHandler(String paramHandler) {
		this.paramHandler = paramHandler;
	}
    
}
