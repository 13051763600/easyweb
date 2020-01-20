package org.yez.easyweb;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.yez.easyweb.entity.ApiInfo;
import org.yez.easyweb.log.SystemLogger;
import org.yez.easyweb.module.BackModule;
import org.yez.easyweb.module.CheckFaultModule;
import org.yez.easyweb.module.SystemErrorModule;
import org.yez.easyweb.module.UnSupportModule;
import org.yez.easyweb.printer.JsonPrinter;
import org.yez.easyweb.printer.Printer;
import org.yez.easyweb.start.IntefaceInfoCache;
import org.yez.easyweb.start.LocalInterfaceInfoCache;
import org.yez.easyweb.util.Util;
import org.yez.easyweb.validator.ParameterValidator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
public class Controller {
    
    @ResponseBody
    @RequestMapping("/**")
    public void doRend(HttpServletRequest request, HttpServletResponse response){
        response.setCharacterEncoding("UTF-8");
        String contextName = request.getContextPath();
        String uri = request.getRequestURI();
        String method = request.getMethod();
        BackModule modul = doRender(request, uri.replaceFirst(contextName, ""), method);
        Printer printer = new JsonPrinter();
        printer.print(request, response, modul);
    }

    private BackModule doRender(HttpServletRequest request, String uri, String method) {
        JSON outJson = null;
        IntefaceInfoCache cache = LocalInterfaceInfoCache.getInstant();
        ApiInfo info = cache.getInfo(uri, method);
        if (null == info){
            return new UnSupportModule(uri, method);
        }
        try{
            Map<String, Object> params = Util.prepareParams(request);
            ParameterValidator validator = new ParameterValidator();
            Map<String, String> validatorResult 
                    = ParameterValidator.isValidate(validator.validate(uri, method, params));
            if (null != validatorResult && validatorResult.size() > 0) {
                return new CheckFaultModule(validatorResult);
            }
            Executor executor = new Executor(request, params, info);
            if (info.cache()){
                outJson = executor.executeWithCache();
            } else {
                outJson = executor.execute();
            }
            return info.createBackModul(outJson);
        }catch (Throwable e){
            e.printStackTrace();
            SystemLogger.getInstant().log(e.getMessage());
            return new SystemErrorModule(e);
        }
    }
}
