package com.ak.community.controller;

import com.ak.community.dto.QuestionDTO;
import com.ak.community.mapper.UserMapper;
import com.ak.community.model.User;
import com.ak.community.service.PageService;
import com.ak.community.service.QuestionDTOService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
public class IndexController {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    UserMapper userMapper;

    @Autowired
    QuestionDTOService questionDTOService;

    @Autowired
    PageService pageService;

    @GetMapping("/")
    public String indexController(Model model,@RequestParam(defaultValue = "1",value = "pageNum") Integer pageNum){
        pageService.doPage(pageNum,model);
        return "index";

    }

    @GetMapping("/signOut")
    public String signOut(HttpServletRequest request, HttpServletResponse response){
        request.getSession().removeAttribute("user");
        Cookie cookie=new Cookie("token",null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        request.getSession().setAttribute("msg","退出成功");
        return "redirect:/";
    }

    @GetMapping("/text")
    public String text(){
        return "login";
    }


}
