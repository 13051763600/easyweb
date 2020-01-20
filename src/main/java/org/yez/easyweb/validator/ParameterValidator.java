package org.yez.easyweb.validator;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.yez.easyweb.util.StringUtils;

public class ParameterValidator {

	private File confFile ;
	
	private Map<String, InterfaceConf> configuration = new HashMap<String, InterfaceConf>();
	
	public ParameterValidator(){
		URL confURL = ParameterValidator.class.getClassLoader().getResource("validation/param-validation.xml");
		
		if(confURL != null){
			try {
				this.confFile = new File(URLDecoder.decode(confURL.getFile(),"UTF-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		init();
	}
	
	public ParameterValidator(String confFile){
		this.confFile = new File(confFile);
		init();
	}
	
	private void init(){
		try {
			readConfigFile();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void readConfigFile() throws Exception{
		SAXReader saxReader = new SAXReader();
		Document document = saxReader.read(confFile);
		
		configuration.clear();
		
		List interfaces = document.selectNodes("//config/interface");
		for(Object node : interfaces){
			if(node instanceof Element){
				Element interfaceNode = (Element)node;
				InterfaceConf interfaceConf = new InterfaceConf();
				
				String uri = interfaceNode.attributeValue("uri");
				interfaceConf.setUri(uri);
				
				String method = interfaceNode.attributeValue("method");
				interfaceConf.setMethod(method);
				
				List paramNodes = interfaceNode.selectNodes("./param");
				for(Object paramNode : paramNodes){
					if(paramNode instanceof Element){
						Element param = (Element)paramNode;
						ParamConf paramConf = new ParamConf();
						
						String name = param.attributeValue("name");
						paramConf.setName(name);
						
						String type = param.attributeValue("type");
						if(StringUtils.isNotEmpty(type)){
							paramConf.setType(type);
							switch(type){
								case "regex" : {
									Node regExp = param.selectSingleNode("./regex");
									paramConf.setRegex(regExp.getText().trim());
									break;
								}
								case "date" : {
									Node dateFormat = param.selectSingleNode("./date-format");
									paramConf.setDateFormat(dateFormat.getText().trim());
									break;
								}
								case "md5" : {
									Node ruleClass =  param.selectSingleNode("./rule-class");
									paramConf.setRuleClass(ruleClass.getText().trim());
									break;
								}
								
								case "not-null" : {
									break;
								}
								default : {
									throw new Exception("Unknown config type : " + type);
								}
							}
						}
						interfaceConf.getParamMap().put(paramConf.getName(), paramConf);
					}
				}
				
				configuration.put(interfaceConf.getUri() + "_" + interfaceConf.getMethod(), interfaceConf);
			}
		}
	}
	
	public Map<String, ParameterValidResult> validate(String uri, String method, Map<String,Object> params) throws Exception{
		Map<String, ParameterValidResult> resultMap = new HashMap<String, ParameterValidResult>();
		if(params != null){
			InterfaceConf interfaceConf = configuration.get(uri + "_" + method);
			
			if(interfaceConf != null){
				for(Entry<String,ParamConf> entry : interfaceConf.getParamMap().entrySet()){
					ParameterValidResult result = new ParameterValidResult();
					String paramKey = entry.getKey();
					String paramValue = (String)params.get(entry.getKey());
					if(paramValue != null){
						ParamConf paramConf = entry.getValue();
						if(paramConf == null){
							result.setSuccess(true);
						}else{
							this.validateParameter(paramKey, paramValue, paramConf, result, params);
						}
						
					}else{
						result.setSuccess(false);
						result.setErrMsg("Missing parameter[" + paramKey + "]");
					}
					
					resultMap.put(paramKey, result);
				}
			}
		}
		return resultMap;
	}
	
	public static Map<String, String> isValidate(Map<String, ParameterValidResult> resultMap){
		if (null == resultMap || resultMap.isEmpty()){
			return null;
		}
		Map<String, String> temp = new HashMap<String, String>();
		for (String key : resultMap.keySet()){
			ParameterValidResult paramResult = resultMap.get(key);
			if (null != paramResult && !paramResult.isSuccess()){
				temp.put(key, paramResult.getErrMsg());
			}
		}
		return temp;
	}
	
	public ParameterValidResult validate(String uri, String method, String param, String value, Map<String,Object> params) throws Exception{
		ParameterValidResult result = new ParameterValidResult();
		InterfaceConf interfaceConf = configuration.get(uri + "_" + method);
		if(interfaceConf == null){
			result.setSuccess(true);
		}else{
			ParamConf paramConf = interfaceConf.getParamMap().get(param);
			if(paramConf == null){
				result.setSuccess(true);
			}else{
				this.validateParameter(param, value, paramConf, result, params);
			}
		}
		return result;
	}
	
	private void validateParameter(String param, String value, ParamConf paramConf, ParameterValidResult result, Map<String,Object> params) throws Exception{
		switch(paramConf.getType()) {
			case "regex" : {
				if(value == null){
					result.setSuccess(false);
					result.setErrMsg("value["+value+"] of parameter[" + param + "] is null");
					break;
				}
				
				Pattern pattern = Pattern.compile(paramConf.getRegex());
				Matcher matcher = pattern.matcher(value);
				boolean success = matcher.matches();
				result.setSuccess(success);
				if(!success){
					result.setErrMsg("value["+value+"] of parameter[" + param + "] not match pattern[" + paramConf.getRegex() + "]");
				}
				break;
			}
			case "date" : {
				if(value == null){
					result.setSuccess(false);
					result.setErrMsg("value["+value+"] of parameter[" + param + "] is null");
					break;
				}
				DateFormat format = new SimpleDateFormat(paramConf.getDateFormat());
				boolean success = true;
				try{
					format.parse(value);
				}catch(Exception e){
					success = false;
				}
				result.setSuccess(success);
				if(!success){
					result.setErrMsg("value["+value+"] of parameter[" + param + "] not match date format [" + paramConf.getDateFormat() + "]");
				}
				break;
			}
			
			case "not-null" : {
				if(StringUtils.isNotEmpty(value)){
					result.setSuccess(true);
				}else{
					result.setSuccess(false);
					result.setErrMsg("value["+value+"] of parameter[" + param + "] is empty");
				}
				break;
			}
			
			case "md5" : {
				if(StringUtils.isNotEmpty(value)){
					String ruleClass = paramConf.getRuleClass();
					Class<MD5Rule> rule = (Class<MD5Rule>) Class.forName(ruleClass);
					MD5Rule md5 = rule.newInstance();
					String newMD5 = md5.encrypt(params);
					if(newMD5.equals(value)){
						result.setSuccess(true);
					}else{
						result.setSuccess(false);
						result.setErrMsg("value["+value+"] of parameter[" + param + "] not match md5 rule");
					}
					
				}else{
					result.setSuccess(false);
					result.setErrMsg("value["+value+"] of parameter[" + param + "] is empty");
				}
				break;
			}
			
			default : {
				throw new Exception("Unknown config type : " + paramConf.getType());
			}
		}
	}
	
	public void reconfig(){
		try {
			readConfigFile();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public File getConfFile() {
		return confFile;
	}

	public void setConfFile(File confFile) {
		this.confFile = confFile;
	}
	
//	public static void main(String[] args) {
//		ParameterValidator validator = new ParameterValidator();
//		try {
//			ParameterValidResult r1 = validator.validate("test", "GET", "name", "");
//			ParameterValidResult r2 = validator.validate("test", "GET", "phone", "1234567");
//			ParameterValidResult r3 = validator.validate("test", "GET", "date", "20161116");
//			
//			System.out.println(r1.isSuccess() + "=" + r1.getErrMsg());
//			System.out.println(r2.isSuccess() + "=" + r2.getErrMsg());
//			System.out.println(r3.isSuccess() + "=" + r3.getErrMsg());
//			
//			
//			System.out.println("============================================");
//			Map<String,Object> params = new HashMap<String,Object>();
//			params.put("phone", "1111111");
//			params.put("date", "2016-11-16");
//			
//			Map<String,ParameterValidResult> rm = validator.validate("test", "GET", params);
//			for(Entry<String,ParameterValidResult> entry : rm.entrySet()){
//				System.out.println(entry.getKey() + " : " + entry.getValue().isSuccess() + "=" + entry.getValue().getErrMsg());
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
}
