package org.yez.easyweb.module;

public class UnSupportModule extends BackModule{

    public UnSupportModule(String uri, String method) {
        this.setCode("404");
        this.setMessage("不支持的接口：url:" + uri + ", method:" + method);
    }

    @Override
    public String getBackMessage() {
        return getBaseBackJson().toJSONString();
    }
    
}
