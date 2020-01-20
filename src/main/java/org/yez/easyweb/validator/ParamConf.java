package org.yez.easyweb.validator;

public class ParamConf {

	private String name;
	
	private String type;
	
	private String regex;
	
	private String dateFormat;
	
	private String ruleClass;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getRegex() {
		return regex;
	}

	public void setRegex(String regex) {
		this.regex = regex;
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}
	
	public String getRuleClass() {
		return ruleClass;
	}

	public void setRuleClass(String ruleClass) {
		this.ruleClass = ruleClass;
	}

	@Override
	public String toString() {
		return "{name:\"" + name + "\",type:\"" + type + "\",regex:\"" + regex + "\",dateFormat:\"" + dateFormat + "\",ruleClass:\"" + ruleClass + "\"}";
	}
	
	
}
