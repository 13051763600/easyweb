package org.yez.easyweb.source;

import java.util.Map;

import org.json.simple.JSONAware;
import org.yez.easyweb.entity.ResultInfo;
import org.yez.easyweb.source.mybatis.Page;

public interface Template {
    
    public JSONAware select(ResultInfo info, Map<String, Object> params, Page page);
    
}
