package org.yez.easyweb.util;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

public class Util {

    public static boolean isNumeric(String str){ 
        Pattern pattern = Pattern.compile("[0-9]*"); 
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false; 
        } 
        return true; 
     }
    
    public static Map<String, Object> prepareParams(HttpServletRequest request){
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, String[]> params = request.getParameterMap();
        for (String key : params.keySet()){
            String value = params.get(key)[0];
            result.put(key, value);
        }
        return result;
    }
}
