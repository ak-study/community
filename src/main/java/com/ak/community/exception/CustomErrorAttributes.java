package com.ak.community.exception;

import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

@Component
public class CustomErrorAttributes  extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, boolean includeStackTrace) {

        Map<String, Object> errorAttributes = super.getErrorAttributes(webRequest, includeStackTrace);
        Map<String, Object> map = (Map<String, Object>) webRequest.getAttribute("msg", 0);//0标识从request中获取
        errorAttributes.put("msg",map);
        return errorAttributes;
    }
}
