package com.ak.community.controller;

import com.ak.community.mapper.UserMapper;
import com.ak.community.model.User;
import com.ak.community.service.QuestionDTOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Controller
public class index {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    UserMapper userMapper;

    @Autowired
    QuestionDTOService questionDTOService;

    @GetMapping("/")
    public String indexController(HttpServletRequest request, Model model){
        //获取cookie数组，查询名字为token的值，然后根据该值查找数据库对应的用户，如果查找到则显示登陆状态
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if(cookie.getName().equals("token")){
                String token = cookie.getValue();
                User user = userMapper.findByToken(token);
                //System.out.println("获取的token值"+token);
                if(user!=null){
                    request.getSession().setAttribute("user",user);

                }
                break;
            }
        }
        model.addAttribute("questions",questionDTOService.getQuestionDTOList());
        return "index";
    }
}
