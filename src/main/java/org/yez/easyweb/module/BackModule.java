package org.yez.easyweb.module;

import org.json.simple.JSONObject;

public abstract class BackModule {
    
    private String code;
    private String message;
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public abstract String getBackMessage();
    
    JSONObject getBaseBackJson(){
        JSONObject result = new JSONObject();
        result.put("code", this.getCode());
        result.put("message", this.getMessage());
        return result;
    }
    
}
