package org.yez.easyweb.source;

import java.util.Map;

import com.alibaba.fastjson.JSON;
import org.yez.easyweb.entity.ResultInfo;
import org.yez.easyweb.source.mybatis.Page;

public interface Template {
    
    public JSON select(ResultInfo info, Map<String, Object> params, Page page);
    
}
