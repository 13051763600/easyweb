package org.yez.easyweb.module;

public class SystemErrorModule extends BackModule{
    
    private Throwable throwable;

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    public SystemErrorModule(Throwable e) {
        this.setCode("500");
        this.setMessage("Server Exception");
        this.throwable = e;
    }

    @Override
    public String getBackMessage() {
        return getBaseBackJson().toJSONString();
    }

}
