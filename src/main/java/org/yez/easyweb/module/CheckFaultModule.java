package org.yez.easyweb.module;

import java.util.Map;

import org.json.simple.JSONObject;

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
