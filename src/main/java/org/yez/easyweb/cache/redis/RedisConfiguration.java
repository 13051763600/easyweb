package org.yez.easyweb.cache.redis;

import com.alibaba.fastjson.JSONArray;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.yez.easyweb.Executor;
import org.yez.easyweb.cache.redis.keygenerator.CacheKeyGenerator;
import org.yez.easyweb.entity.ApiInfo;
import org.yez.easyweb.filter.LogFilter;
import org.yez.easyweb.log.SystemLogger;
import org.yez.easyweb.util.MD5Util;
import org.yez.easyweb.util.StringUtils;

import java.lang.reflect.Method;
import java.util.*;

@Configuration
@EnableCaching
public class RedisConfiguration extends CachingConfigurerSupport{
    
    public static final String DEFAULT_KEY = "CACHE_DEFAULT_KEY";

    @Bean
    public KeyGenerator wiselyKeyGenerator(){
        return new KeyGenerator() {
            @Override
            public Object generate(Object target, Method method, Object... params) {
                Executor exec = (Executor)target;
                ApiInfo info = exec.getInfo();
                if (info == null){
                    return DEFAULT_KEY;
                }
                if (null != info.getCacheKeyGenerator() && info.getCacheKeyGenerator().trim().length() > 0){
                    try {
                        return info.invokeCacheKeyGenerator(exec.getParams());
                    } catch (NoKeyGeneratorConfigException e) {
                        SystemLogger.getInstant().log(e.getMessage());
                        return new DefaultKeyGenerator().generate(info, exec.getParams());
                    }
                } else {
                    return new DefaultKeyGenerator().generate(info, exec.getParams());
                }
            }
        };
    }
    
    class DefaultKeyGenerator implements CacheKeyGenerator {
        
        @Override
        public Object generate(ApiInfo info, Map<String, Object> param) {
            if (null == param){
                param = new HashMap<String, Object>();
            }
            StringBuilder sb = new StringBuilder();
            sb.append(info.getUri()).append("_");
            sb.append(info.getMethod());
            List<Object> valueList = new ArrayList<Object>();
            valueList.addAll(param.values());
            if (StringUtils.isNotEmpty(info.getExceptKey())){
                //去掉不需要的参数
                valueList.removeAll(Arrays.asList(info.getExceptKey().split(",")));
            }
            //去掉日志中的UUID
            valueList.remove(param.get(LogFilter.REQUEST_LOG_UUID));
            String paramStr = MD5Util.stringMD5(JSONArray.toJSONString(valueList));
            sb.append("_").append(paramStr);
            return sb.toString();
        }
        
    }

    @Bean
    public RedisTemplate<String, String> redisTemplate(
            RedisConnectionFactory factory) {
        StringRedisTemplate template = new StringRedisTemplate(factory);
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        template.setValueSerializer(jackson2JsonRedisSerializer);
        template.afterPropertiesSet();
        return template;
    }
}
