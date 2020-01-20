package org.yez.easyweb.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestInLogger implements LogBackLogger{
    
    private RequestInLogger(){}
    
    private static LogBackLogger instant;
    
    public static Logger logger = LoggerFactory.getLogger(RequestInLogger.class);

    @Override
    public void log(String msg) {
        logger.error(msg);
    }
    
    public static LogBackLogger getInstant(){
        if (null == instant){
            instant = new RequestInLogger();
        }
        return instant;
    }
    
}
