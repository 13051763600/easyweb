package org.yez.easyweb.cache.redis.keygenerator;

import org.yez.easyweb.entity.ApiInfo;

import java.util.Map;

public interface CacheKeyGenerator {

    public Object generate(ApiInfo info, Map<String, Object> param);
}
