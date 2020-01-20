package org.yez.easyweb.entity;

import java.util.Map;

import com.alibaba.fastjson.JSON;

public interface ResultJsonHandler {

    public JSON handler(BaseEntity info, JSON result, Map<String, Object> params);
}
