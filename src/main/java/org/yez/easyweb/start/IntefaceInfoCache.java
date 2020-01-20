package org.yez.easyweb.start;

import org.yez.easyweb.entity.ApiInfo;

public interface IntefaceInfoCache {
    
    public ApiInfo getInfo(String uri, String method);
    
}
