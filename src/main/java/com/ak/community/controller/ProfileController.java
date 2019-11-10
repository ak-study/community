package com.ak.community.controller;

import com.ak.community.dto.QuestionDTO;
import com.ak.community.exception.CustomizeErrorCode;
import com.ak.community.exception.CustomizeException;
import com.ak.community.mapper.QuestionMapper;
import com.ak.community.service.PageService;
import com.ak.community.service.QuestionDTOService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class ProfileController {
    @Autowired
    QuestionDTOService questionDTOService;

    @Autowired
    QuestionMapper questionMapper;

    @Autowired
    PageService pageService;

    @GetMapping("profile/{action}")
    public String profileController(@PathVariable String action, Model model, @RequestParam(defaultValue = "1",value = "pageNum") Integer pageNum,
                                    HttpServletRequest request){
        if(request.getSession().getAttribute("user")==null){
            throw new CustomizeException(CustomizeErrorCode.NO_LOGIN);
        }
        if(action.equals("questions")){
            model.addAttribute("section","question");
            model.addAttribute("sectionName","我的问题");
        }else if(action.equals("newMsg")){
            model.addAttribute("section","newMsg");
            model.addAttribute("sectionName","最新消息");
        }
        pageService.doPage(pageNum,model);
        List<QuestionDTO> questionDTOList = questionDTOService.getQuestionDTOList();
        Integer questionCount=questionMapper.getQuestionCount(questionDTOList.get(0).getUser().getId());
        model.addAttribute("questionCount",questionCount);
        return "profile";
    }
}
