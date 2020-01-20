package org.yez.easyweb.printer;

import org.springframework.stereotype.Component;
import org.yez.easyweb.module.BackModule;

@Component
public class JsonPrinter extends AbstractPrinter{

    @Override
    public String buildOutContext(BackModule modul) {
        return modul.getBackMessage();
    }
    
}
