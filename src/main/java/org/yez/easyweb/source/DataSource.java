package org.yez.easyweb.source;

public interface DataSource {

    public Template getTemplate();
    
    public boolean close();
    
    public void init();
}
