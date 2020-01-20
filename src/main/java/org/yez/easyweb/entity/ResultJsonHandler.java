package org.yez.easyweb.entity;

import java.util.Map;

import org.json.simple.JSONAware;

public interface ResultJsonHandler {

    public JSONAware handler(BaseEntity info, JSONAware result, Map<String, Object> params);
}
