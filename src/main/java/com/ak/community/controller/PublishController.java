package com.ak.community.controller;

import com.ak.community.cache.TagCache;
import com.ak.community.exception.CustomizeErrorCode;
import com.ak.community.exception.CustomizeException;
import com.ak.community.mapper.QuestionMapper;
import com.ak.community.mapper.UserMapper;
import com.ak.community.model.Question;
import com.ak.community.model.User;
import com.ak.community.service.PublishService;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import sun.security.provider.MD2;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;
import java.util.Map;

@Controller
public class PublishController {
    @Autowired
    QuestionMapper questionMapper;

    @Autowired
    PublishService publishService;

    //处理访问页面
    @GetMapping("/publish")
    public String publishController(Model model){
        model.addAttribute("tags",TagCache.get());
        return "publish";
    }

    //处理发布页面
    @PostMapping("/publish")
    public String doPublish(Question question, HttpServletRequest request, Model model){

        Object user = request.getSession().getAttribute("user");
        User newUser = new User();
        newUser= (User) user;
        model.addAttribute("title", question.getTitle());
        model.addAttribute("description", question.getDescription());
        model.addAttribute("tag", question.getTag());
        model.addAttribute("tags",TagCache.get());
        boolean truePublish = publishService.isTruePublish(question, model,newUser);
        if(!truePublish){
            return "/publish";
        }
        if(user==null){
            model.addAttribute("error","用户未登录");
            return "/publish";
        }else{
            publishService.createOrUpdate(question.getId(),question,newUser);
        }
        return "redirect:/";
    }

    //处理编辑页面
    @GetMapping("/publish/{id}")
    public String edit(@PathVariable Long id, Model model,HttpServletRequest request){
        Question question = questionMapper.getQuestionByID(id);
        Object user = request.getSession().getAttribute("user");
        User newUser = new User();
        newUser= (User) user;
        if(question==null){
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        if(question.getCreator()!=newUser.getId()){
            throw new CustomizeException(CustomizeErrorCode.INVALID_OPERATION);
        }
        model.addAttribute("title", question.getTitle());
        model.addAttribute("description", question.getDescription());
        model.addAttribute("tag", question.getTag());
        model.addAttribute("id",question.getId());
        model.addAttribute("tags",TagCache.get());
        return "/publish";
    }
}
