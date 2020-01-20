package org.yez.easyweb.printer;

import org.yez.easyweb.filter.LogFilter;
import org.yez.easyweb.log.ResponseOutLogger;
import org.yez.easyweb.module.BackModule;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class AbstractPrinter implements Printer{
    
    public abstract String buildOutContext(BackModule modul);

    @Override
    public void print(final HttpServletRequest request, final HttpServletResponse response, BackModule module) {
        try {
            final String outContext = buildOutContext(module);
            new Runnable() {
                @Override
                public void run() {
                    writeOutLog(request, response, outContext);
                }
            }.run();
            //response.setContentType("applicationContext/json");
            response.getOutputStream().write(outContext.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void writeOutLog(HttpServletRequest request, HttpServletResponse response, String outJson) {
        try{
            StringBuilder sb = new StringBuilder();
            sb.append(request.getAttribute(LogFilter.REQUEST_LOG_UUID)).append("\t");
            sb.append(request.getRequestURI().replace(request.getContextPath(), "")).append("\t");
            sb.append(request.getMethod()).append("\t");
            sb.append(response.getContentType()).append("\t");
            sb.append(response.getCharacterEncoding()).append("\t");
            sb.append(outJson);
            ResponseOutLogger.getInstant().log(sb.toString());
        }catch (Throwable e){}
    }

}
