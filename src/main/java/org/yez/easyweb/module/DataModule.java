package org.yez.easyweb.module;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.yez.easyweb.entity.ApiInfo;

public class DataModule extends BackModule{
    
    private JSON json;
    private ApiInfo info;

    public ApiInfo getInfo() {
        return info;
    }


    public void setInfo(ApiInfo info) {
        this.info = info;
    }


    public JSON getJson() {
        return json;
    }


    public void setJson(JSON json) {
        this.json = json;
    }


    public DataModule(JSON outJson, ApiInfo info) {
        this.setCode("200");
        this.setMessage("成功");
        this.json = outJson;
        this.info = info;
    }


    @Override
    public String getBackMessage() {
        JSONObject result = getBaseBackJson();
        result.put(info.getName(), this.json);
        return result.toJSONString();
    }

}
