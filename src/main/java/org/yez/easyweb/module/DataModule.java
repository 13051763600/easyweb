package org.yez.easyweb.module;

import org.json.simple.JSONAware;
import org.json.simple.JSONObject;
import org.yez.easyweb.entity.ApiInfo;

public class DataModule extends BackModule{
    
    private JSONAware json;
    private ApiInfo info;

    public ApiInfo getInfo() {
        return info;
    }


    public void setInfo(ApiInfo info) {
        this.info = info;
    }


    public JSONAware getJson() {
        return json;
    }


    public void setJson(JSONAware json) {
        this.json = json;
    }


    public DataModule(JSONAware outJson, ApiInfo info) {
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
