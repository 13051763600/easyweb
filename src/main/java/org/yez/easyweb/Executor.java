package org.yez.easyweb;

import org.json.simple.JSONAware;
import org.json.simple.JSONObject;
import org.springframework.cache.annotation.Cacheable;
import org.yez.easyweb.entity.ApiInfo;
import org.yez.easyweb.entity.BaseEntity;
import org.yez.easyweb.entity.ResultInfo;
import org.yez.easyweb.source.Template;
import org.yez.easyweb.source.mybatis.Page;
import org.yez.easyweb.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class Executor {
    
    public Executor(HttpServletRequest request,  Map<String, Object> params, ApiInfo info){
        this.request = request;
        this.params = params;
        this.info = info;
    }

    private HttpServletRequest request;
    private Map<String, Object> params;
    private ApiInfo info;
    
    public Map<String, Object> getParams() {
        return params;
    }
    public void setParams(Map<String, Object> params) {
        this.params = params;
    }
    public HttpServletRequest getRequest() {
        return request;
    }
    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }
    public ApiInfo getInfo() {
        return info;
    }
    public void setInfo(ApiInfo info) {
        this.info = info;
    }
    
    @Cacheable(value="userT", keyGenerator="wiselyKeyGenerator")
    public JSONAware executeWithCache(){
        return this.execute();
    }
    
    public JSONAware execute(){
        JSONAware result = null;
        //当只有一个返回结果时
        if (this.info.getResult().length == 1){
            result = executeResultInfo(this.info.getResult()[0]);
        }else if (this.info.getResult().length > 1){
            //当有多个返回结果集时
            result = new JSONObject();
            for (ResultInfo resultInfo : this.info.getResult()){
                ((JSONObject)result).put(resultInfo.getName(), executeResultInfo(resultInfo));
            }
        }else {
            result = new JSONObject();
        }
        // 对返回结果做处理
        if (this.info.hasHandler()) {
            result = this.info.invokeHandler(this.info, result, params);
        } 
        return result;
    }
    
    private JSONAware executeResultInfo(ResultInfo resultInfo) {
        Template template = resultInfo.getTemplate();
        Page page = resultInfo.getPageInfo().getPage(this.request);
        if (null == template) {
            throw new RuntimeException("获取名称为" + resultInfo.getDataSourceName() + "的template失败！");
        }
        JSONAware result = template.select(resultInfo, prepareParams(params, resultInfo), page);
        if (resultInfo.hasHandler()) {
            result = resultInfo.invokeHandler(resultInfo, result, params);
        }
        return result;
    }

	private Map<String, Object> prepareParams(Map<String, Object> map, BaseEntity info) {
		final Map<String, Object> param = new HashMap<String, Object>();
		param.putAll(map);
		if (StringUtils.isNotEmpty(info.getParamHandler())){
			try {
				Class clz = Class.forName(info.getParamHandler());
				clz.getMethod("handler", new Class[]{Map.class})
						.invoke(clz.newInstance(), param);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			}
			
		}
		return param;
	}
} 
