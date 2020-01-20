package org.yez.easyweb.source.mybatis;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.session.SqlSession;
import org.yez.easyweb.entity.ApiInfo;
import org.yez.easyweb.entity.ResultInfo;
import org.yez.easyweb.source.Template;

public class MybatisTemplate implements Template {
    
    private SqlSession session;
    
    public MybatisTemplate(SqlSession session){
        this.session = session;
    }
    
    public SqlSession getSession() {
        return session;
    }
    public void setSession(SqlSession session) {
        this.session = session;
    }

    @Override
    public JSON select(ResultInfo info, Map<String, Object> params, Page page) {
        if (null == this.session){
            throw new RuntimeException("Mybatis session can't be null");
        }
        JSON json = null;
        try{
            if (info.isPaging() && page.getPageSize() > 0) {
                PageHelper.startPage(page.getPageNum(), page.getPageSize());
                session.selectList(info.getSql(), params);
                page = PageHelper.endPage();
                json = new JSONObject();
                ((JSONObject) json).put(info.getPageInfo().getTotalName(), page.getTotal());
                ((JSONObject) json).put(info.getDataName(), page.getResult());
            } else {
                DefaultResultHandler defaultHandler = new DefaultResultHandler();
                session.select(info.getSql(), params, defaultHandler.getResultHandler());
                if (ApiInfo.RETURN_TYPE_LIST.equals(info.getResultType())) {
                    json = new JSONArray();
                    ((JSONArray) json).addAll(defaultHandler.getResult());
                } else {
                    List<Map<String, Object>> result = defaultHandler.getResult();
                    if (null == result || result.size() == 0 || null == defaultHandler.getResult().get(0)) {
                        json = new JSONObject();
                    } else {
                        json = new JSONObject(defaultHandler.getResult().get(0));
                    }
                }
            }
        }finally{
            if (null != this.session){
                session.close();
            }
        }
        return json;
    }
    
//    public static void main(String[] args) {
//        Map<String, Object> map = new HashMap<String, Object>();
//        map.put("adsf", "adsfasdf");
//        map.put("5676", 32423);
//        map.put("ddff", null);
//        map.put("DATE", new Date(System.currentTimeMillis()));
//        JSONObject json = new JSONObject();
//        json.putAll(map);
//        System.out.println(json.toString());
//    }
}
