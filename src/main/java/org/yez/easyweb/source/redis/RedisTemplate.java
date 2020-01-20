package org.yez.easyweb.source.redis;

import java.util.Map;

import org.json.simple.JSONAware;
import org.json.simple.JSONObject;

import org.yez.easyweb.entity.ResultInfo;
import org.yez.easyweb.source.Template;
import org.yez.easyweb.source.mybatis.Page;
import org.yez.easyweb.util.StringUtils;
import redis.clients.jedis.Jedis;

public class RedisTemplate implements Template {
    
    public static final String REDIS_REQUEST_KEY = "key";
    
    private Jedis jedis;

    public RedisTemplate(Jedis jedis) {
        this.jedis = jedis;
    }

    @Override
    public JSONAware select(ResultInfo info, Map<String, Object> params, Page page) {
        JSONObject result = new JSONObject();
        String key = (String)params.get(REDIS_REQUEST_KEY);
        if (!StringUtils.isNotEmpty(key)){
            return result;
        }
        if (null == this.jedis){
            throw new RuntimeException("jedis connection is not Initialized");
        }
        String value = this.jedis.get(key);
        result.put(info.getName(), value);
        return result;
    }

    public void setJedis(Jedis jedis) {
        this.jedis = jedis;
    }

}
