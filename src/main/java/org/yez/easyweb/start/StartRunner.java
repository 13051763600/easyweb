package org.yez.easyweb.start;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.yez.easyweb.source.DataSourceFactory;

@Component
public class StartRunner implements CommandLineRunner{

    @Override
    public void run(String... args) throws Exception {
        DataSourceFactory.getInstant();
        LocalInterfaceInfoCache.getInstant();
    }
}
