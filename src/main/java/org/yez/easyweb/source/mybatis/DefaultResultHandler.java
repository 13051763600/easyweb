package org.yez.easyweb.source.mybatis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;

public class DefaultResultHandler{
    
    private List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
    private ResultHandler resultHandler = new Handler();
    

    public List<Map<String, Object>> getResult(){
        return this.result;
    }

    public ResultHandler getResultHandler() {
        return resultHandler;
    }
    
    private class Handler implements ResultHandler{
        @Override
        public void handleResult(ResultContext context) {
            result.add((Map<String, Object>)context.getResultObject());
        }
    }
}
