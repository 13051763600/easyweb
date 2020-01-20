package org.yez.easyweb.module;

import org.json.simple.JSONArray;
import org.json.simple.JSONAware;
import org.json.simple.JSONObject;
import org.yez.easyweb.entity.ApiInfo;

public class BlankModule extends DataModule{
    
    public BlankModule(JSONAware outJson, ApiInfo info) {
        super(outJson, info);
    }

    @Override
    public String getBackMessage() {
        ApiInfo info = this.getInfo();
        if (null == this.getJson()){
            if (this.getInfo().getResult().length == 1
                    && null != info.getResult()[0]
                    && ApiInfo.RETURN_TYPE_LIST.equals(info.getResult()[0].getResultType())) {
                return new JSONArray().toJSONString();
            } else {
                return new JSONObject().toJSONString();
            }
        } else {
            return this.getJson().toJSONString();
        }
    }

}
