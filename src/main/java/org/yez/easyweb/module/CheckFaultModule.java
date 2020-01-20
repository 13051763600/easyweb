package org.yez.easyweb.module;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

public class CheckFaultModule extends BackModule{

    public CheckFaultModule(Map<String, String> validatorResult) {
        this.setCode("401");
        this.setMessage(JSONObject.toJSONString(validatorResult));
    }

    @Override
    public String getBackMessage() {
        return getBaseBackJson().toJSONString();
    }
}
