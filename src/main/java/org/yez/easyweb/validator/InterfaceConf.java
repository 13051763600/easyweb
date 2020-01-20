package org.yez.easyweb.validator;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class InterfaceConf {
	
	private String uri ;
	
	private String method;

	private Map<String, ParamConf> paramMap = new HashMap();

	public Map<String, ParamConf> getParamMap() {
		return paramMap;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("{").append("uri:\"").append(uri).append("method:\"").append(method).append("\", paramMap:[");
		int i = 0;
		for(Entry<String,ParamConf> param : paramMap.entrySet()){
			builder.append("key:\"").append(param.getKey()).append("\",value:").append(param.getValue().toString());
			if(i < paramMap.size() - 1){
				builder.append(",");
			}
			i++;
		}
		builder.append("]}");
		return builder.toString();
	}
	
}
