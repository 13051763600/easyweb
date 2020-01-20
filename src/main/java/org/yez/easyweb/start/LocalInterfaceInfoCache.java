package org.yez.easyweb.start;

import java.io.*;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.yez.easyweb.entity.ApiInfo;
import org.yez.easyweb.entity.ResultInfo;
import org.yez.easyweb.source.mybatis.PageInfo;
import org.yez.easyweb.util.StringUtils;

public class LocalInterfaceInfoCache implements IntefaceInfoCache{
    
    private static LocalInterfaceInfoCache instant;
    
    private static final String interfacePath = "infconfig";
    
    @Value("${cache.redis.default}")
    private String defaultCache;
    
    private static final Map<String, ApiInfo> cache = new HashMap<String, ApiInfo>();
    
    private LocalInterfaceInfoCache() {
        init();
    }

    private void init() {
        File root;
		try {
			root = new File(URLDecoder.decode(Thread.currentThread().getContextClassLoader().getResource("").getPath(), "UTF-8") + "/" + interfacePath);
			initProperties(root);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
    }

    private void initProperties(File root) {
        if (null == root || root.listFiles() == null){
            return;
        }
        for(File file : root.listFiles()){
            if (file.isDirectory()){
                initProperties(file);
            }else if (file.isFile() && file.getName().endsWith("properties")){
                Properties prop = new Properties();
                try {
                    InputStream in = new FileInputStream(file);
                    InputStreamReader isr = new InputStreamReader(in, "utf-8");
                    prop.load(isr);
                    ApiInfo info = new ApiInfo();
                    info.setMethod(prop.getProperty("method"));
                    info.setUri(prop.getProperty("uri"));
                    info.setName(prop.getProperty("name"));
                    info.setResult(initResultInfo(prop, prop.getProperty("result")));
                    info.setJsonHandler(prop.getProperty("jsonHandler"));
                    String isCache = prop.getProperty("cache");
                    //默认开启缓存模式
                    info.setCache(StringUtils.isNotEmpty(isCache) ? isCache : this.defaultCache );
                    info.setCacheKeyGenerator(prop.getProperty("cacheKeyGenerator"));
                    info.setBackModul(prop.getProperty("backModul"));
                    info.setExceptKey(prop.getProperty("cacheKeyGenerator.exceptKey"));
                    if (info.getUri() == null || info.getMethod() == null
                            || info.getUri().length() == 0 || info.getMethod().length() == 0){
                        continue;
                    }
                    LocalInterfaceInfoCache.cache.put(info.getUri() + "_" + info.getMethod(),  info);
                } catch (IOException e) {
                    e.printStackTrace();
                } 
            }
        }
    }

    private ResultInfo[] initResultInfo(Properties prop, String keys) {
        if (!StringUtils.isNotEmpty(keys)){
            return new ResultInfo[0];
        }
        String[] keyArray = keys.split(",");
        ResultInfo[] resultArray = new ResultInfo[keyArray.length];
        for (int i = 0; i < keyArray.length; i++){
            ResultInfo result = new ResultInfo();
            result.setName(keyArray[i]);
            result.setDataSourceName(prop.getProperty(keyArray[i] + ".dataSourceName"));
            result.setPaging(prop.getProperty(keyArray[i] + ".paging"));
            result.setJsonHandler(prop.getProperty(keyArray[i] + ".jsonHandler"));
            result.setPageInfo(initPageInfo(prop, keyArray[i] + ".pageInfo"));
            result.setSql(prop.getProperty(keyArray[i] + ".sql"));
            result.setResultType(prop.getProperty(keyArray[i] + ".resultType"));
            result.setDataName(prop.getProperty(keyArray[i] + ".dataName"));
            result.setParamHandler(prop.getProperty(keyArray[i] + ".paramHandler"));
            resultArray[i] = result;
        }
        return resultArray;
    }

    private PageInfo initPageInfo(Properties prop, String key) {
        PageInfo pageInfo = new PageInfo();
        pageInfo.setPageNumName(prop.getProperty(key + ".pageNumName"));
        pageInfo.setPageSizeName(prop.getProperty(key + ".pageSizeName"));
        pageInfo.setTotalName(prop.getProperty(key + ".totalName"));
        return pageInfo;
    }

    public static IntefaceInfoCache getInstant(){
        if (null == instant){
            instant = new LocalInterfaceInfoCache();
        }
        return instant;
    }
    
    @Override
    public ApiInfo getInfo(String uri, String method) {
        if (cache.size() == 0){
            init();
        }
        ApiInfo info = cache.get(uri + "_" + method);
        return null == info ? null : info.clone();
    }

}
