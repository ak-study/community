package com.ak.community.controller;

import com.ak.community.mapper.QuestionMapper;
import com.ak.community.mapper.UserMapper;
import com.ak.community.model.Question;
import com.ak.community.model.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
public class PublishController {
    @Autowired
    QuestionMapper questionMapper;

    @GetMapping("/publish")
    public String publishController(){
        return "publish";
    }

    @PostMapping("/publish")
    public String doPublish(Question question, HttpServletRequest request, Model model){
        Object user = request.getSession().getAttribute("user");
        User newUser = new User();
        newUser= (User) user;
        model.addAttribute("title", question.getTitle());
        model.addAttribute("description", question.getDescription());
        model.addAttribute("tag", question.getTag());

        if (StringUtils.isBlank(question.getTitle())) {
            model.addAttribute("error", "标题不能为空");
            return "publish";
        }
        if (StringUtils.isBlank(question.getDescription())) {
            model.addAttribute("error", "问题补充不能为空");
            return "publish";
        }
        if (StringUtils.isBlank(question.getTag())) {
            model.addAttribute("error", "标签不能为空");
            return "publish";
        }
        if(user==null){
            request.getSession().setAttribute("error","用户未登录");
            System.out.println("用户未登录");
            return "/publish";
        }else{
            question.setCreator(newUser.getId());
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            questionMapper.insertQuestion(question);
        }
        return "redirect:/";
    }
}
