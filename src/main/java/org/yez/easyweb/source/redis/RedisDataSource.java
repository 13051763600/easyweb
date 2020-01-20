package org.yez.easyweb.source.redis;

import org.yez.easyweb.source.DataSource;
import org.yez.easyweb.source.Initialize;
import org.yez.easyweb.source.Template;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisDataSource implements DataSource {
    
    private JedisPool pool;
    //Redis服务器IP
    @Initialize
    private String host;
    
    //Redis的端口号
    @Initialize
    private int port = 6379;
    //访问密码
    @Initialize
    private String password;
    //可用连接实例的最大数目，默认值为8；
    //如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
    @Initialize
    private int max_active = 1024; 
    //控制一个pool最多有多少个状态为idle(空闲的)的jedis实例，默认值也是8。
    @Initialize
    private int max_idle = 200;
    //等待可用连接的最大时间，单位毫秒，默认值为-1，表示永不超时。如果超过等待时间，则直接抛出JedisConnectionException；
    @Initialize
    private int max_wait = 10000;
    @Initialize
    private int timeout = 10000;
    
    //在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
    @Initialize
    private boolean test_on_borrow = true; 
    
    private RedisTemplate template;

    @Override
    public Template getTemplate() {
        if (this.template == null){
            this.template = new RedisTemplate(pool.getResource());
        }
        return this.template;
    }

    @Override
    public boolean close() {
        try{
            if (null != this.pool){
                pool.close();
            }
            return true;
        }catch (Exception e){
            return false;
        }
    }
    
    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getMax_active() {
        return max_active;
    }

    public void setMax_active(int max_active) {
        this.max_active = max_active;
    }

    public int getMax_idle() {
        return max_idle;
    }

    public void setMax_idle(int max_idle) {
        this.max_idle = max_idle;
    }

    public int getMax_wait() {
        return max_wait;
    }

    public void setMax_wait(int max_wait) {
        this.max_wait = max_wait;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public boolean isTest_on_borrow() {
        return test_on_borrow;
    }

    public void setTest_on_borrow(boolean test_on_borrow) {
        this.test_on_borrow = test_on_borrow;
    }

    @Override
    public void init() {
        try {
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxTotal(this.max_active);
            config.setMaxIdle(this.max_idle);
            config.setMaxWaitMillis(this.max_wait);
            config.setTestOnBorrow(this.test_on_borrow);
            this.pool = new JedisPool(config, this.host, this.port, this.timeout, this.password);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
