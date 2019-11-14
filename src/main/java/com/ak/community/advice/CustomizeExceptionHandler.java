package com.ak.community.advice;

import com.ak.community.exception.CustomizeErrorCode;
import com.ak.community.exception.CustomizeException;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * 自定义错误处理类
 * 如果是网页访问，出现异常则返回错误页面，如果是其他客户端访问则返回json数据
 */
@ControllerAdvice
public class CustomizeExceptionHandler {
    @ExceptionHandler(Exception.class)
    String handle(Exception e, Model model, HttpServletRequest request, HttpServletResponse response) {
        HttpStatus status=getStatus(request);//获取错误状态码
        model.addAttribute("status",status);//传入错误状态码
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("msg", e.getMessage());

        //给request设置错误码，则来到这个页面处理的时候进入定制页面（CustomErrorAttributes）
        request.setAttribute("javax.servlet.error.status_code",status.value());//给request域传入错误状态码，否者无法进入定制错误页面
        request.setAttribute("msg",map);//给request域传入自己的map，在自定义页面接收

        //如果接收的异常类为自定义异常，抛出自定义错误，否则抛出系统异常
        if (e instanceof CustomizeException) {
            model.addAttribute("message", e.getMessage());
        } else {
            model.addAttribute("message", CustomizeErrorCode.SYS_ERROR.getMessage());
        }
        return "forward:/error";
    }
    //获取错误状态码
    private HttpStatus getStatus(HttpServletRequest request){
        Integer statusCode=(Integer) request.getAttribute("javax.servlet.error.status_code");
        if(statusCode==null){
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return HttpStatus.valueOf(statusCode);
    }
}
