package org.yez.easyweb.cache.redis;

public class NoKeyGeneratorConfigException extends Exception{

    /**
     * 
     */
    private static final long serialVersionUID = -4639641927062841695L;

    public NoKeyGeneratorConfigException(String message) {
        super(message);
    }
    
    public NoKeyGeneratorConfigException(){}
}
