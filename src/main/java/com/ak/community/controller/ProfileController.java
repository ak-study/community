package com.ak.community.controller;

import com.ak.community.dto.NotificationDTO;
import com.ak.community.dto.QuestionDTO;
import com.ak.community.exception.CustomizeErrorCode;
import com.ak.community.exception.CustomizeException;
import com.ak.community.mapper.NotificationMapper;
import com.ak.community.mapper.QuestionMapper;
import com.ak.community.model.Notification;
import com.ak.community.model.User;
import com.ak.community.service.NotifyService;
import com.ak.community.service.PageService;
import com.ak.community.service.QuestionDTOService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ProfileController {
    @Autowired
    QuestionDTOService questionDTOService;

    @Autowired
    QuestionMapper questionMapper;

    @Autowired
    PageService pageService;

    @Autowired
    NotificationMapper notificationMapper;

    @Autowired
    NotifyService notifyService;

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
        pageService.doPage(pageNum,model,false,request);
        List<QuestionDTO> questionDTOList = questionDTOService.getQuestionDTOListByPerson(request);
        Integer questionCount=questionMapper.getQuestionCount(questionDTOList.get(0).getUser().getId());
        List<NotificationDTO> notifyDTOList = notifyService.getNotifyDTOList(request);
        Collections.reverse(notifyDTOList);
        Integer newMsg = notifyService.getNewMsg(request);
        request.getSession().setAttribute("newMsg",newMsg);
        model.addAttribute("questionCount",questionCount);
        model.addAttribute("notifyDTOList",notifyDTOList);
        return "profile";
    }

    @GetMapping("notify/{questionID}/{notifyID}")
    public String notifyController(@PathVariable String questionID, @PathVariable Long notifyID){
        notificationMapper.setNotifyStatusByID(notifyID);
        return "redirect:/question/"+questionID;
    }
}
