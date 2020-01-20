package org.yez.easyweb.module;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.yez.easyweb.entity.ApiInfo;

public class BlankModule extends DataModule{
    
    public BlankModule(JSON outJson, ApiInfo info) {
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
