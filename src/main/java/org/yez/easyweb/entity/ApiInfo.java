package org.yez.easyweb.entity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.json.simple.JSONAware;
import org.yez.easyweb.cache.redis.NoKeyGeneratorConfigException;
import org.yez.easyweb.cache.redis.RedisConfiguration;
import org.yez.easyweb.cache.redis.keygenerator.CacheKeyGenerator;
import org.yez.easyweb.module.BackModule;
import org.yez.easyweb.module.DataModule;
import org.yez.easyweb.util.StringUtils;

public class ApiInfo extends BaseEntity implements Cloneable, Serializable {
    
    public static final String RETURN_TYPE_LIST = "list";
    public static final String RETURN_TYPE_OBJECT = "object";

    /**
     * 
     */
    private static final long serialVersionUID = -1683006096746593552L;
    private String uri;
    private String method;
    private String cache;
    private String cacheKeyGenerator;
    private ResultInfo[] result;
    private String backModul;
    private String exceptKey;
    
    public String getUri() {
        return uri;
    }
    public void setUri(String uri) {
        this.uri = uri;
    }
    public String getMethod() {
        return method;
    }
    public void setMethod(String method) {
        this.method = method;
    }
    public ResultInfo[] getResult() {
        return result.clone();
    }
    public void setResult(ResultInfo[] result) {
        this.result = result;
    }
    public String getCache() {
        return cache;
    }
    public void setCache(String cache) {
        this.cache = cache;
    }
    public String getCacheKeyGenerator() {
        return cacheKeyGenerator;
    }
    public void setCacheKeyGenerator(String cacheKeyGenerator) {
        this.cacheKeyGenerator = cacheKeyGenerator;
    }
    public String getBackModul() {
        return backModul;
    }
    public void setBackModul(String backModul) {
        this.backModul = backModul;
    }
    
    public ApiInfo clone() {
        ByteArrayOutputStream byteOut = null;   
        ObjectOutputStream objOut = null;   
        ByteArrayInputStream byteIn = null;   
        ObjectInputStream objIn = null;   
           
        try {   
            byteOut = new ByteArrayOutputStream();    
            objOut = new ObjectOutputStream(byteOut);    
            objOut.writeObject(this);   
  
            byteIn = new ByteArrayInputStream(byteOut.toByteArray());   
            objIn = new ObjectInputStream(byteIn);   
               
            return (ApiInfo) objIn.readObject();
        } catch (IOException e) {   
            throw new RuntimeException("Clone Object failed in IO.",e);      
        } catch (ClassNotFoundException e) {   
            throw new RuntimeException("Class not found.",e);      
        } finally{   
            try{   
                byteIn = null;   
                byteOut = null;   
                if(objOut != null) objOut.close();      
                if(objIn != null) objIn.close();      
            }catch(IOException e){      
            }      
        }   
    }
    
    /**
     * 是否开启缓存
     * 
     * @return
     */
    public boolean cache() {
        return null != this.cache && Boolean.valueOf(this.cache);
    }
    
    /**
     * 调用接口配置的缓存KeyGenerator，
     * 详情见com.gomeplus.bi.api.cache.redis.keygenerator.CacheKeyGenerator
     * 
     * @param params 请求参数
     * @return
     * @throws NoKeyGeneratorConfigException
     */
    public Object invokeCacheKeyGenerator(Map<String, Object> params) throws NoKeyGeneratorConfigException {
        if (null == this.cacheKeyGenerator || this.cacheKeyGenerator.trim().length() == 0){
            throw new NoKeyGeneratorConfigException("接口：url:" + uri + ", method:" + method + "没有初始化KeyGenerator");
        }
        try {
            Class clz = Class.forName(this.cacheKeyGenerator);
            CacheKeyGenerator generator = (CacheKeyGenerator)clz.newInstance();
            return generator.generate(this, params);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return RedisConfiguration.DEFAULT_KEY;
    }
    
    public BackModule createBackModul(JSONAware outJson) {
        if (!StringUtils.isNotEmpty(this.backModul)){
            return new DataModule(outJson, this);
        }
        try {
            Class clz = Class.forName(this.backModul);
            Constructor constructor = clz.getConstructor(JSONAware.class, ApiInfo.class);
            return (BackModule)constructor.newInstance(outJson, this);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public String getExceptKey() {
        return exceptKey;
    }
    public void setExceptKey(String exceptKey) {
        this.exceptKey = exceptKey;
    } 
    
    
}
