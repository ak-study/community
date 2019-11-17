package com.ak.community.controller;

import com.ak.community.dto.CommentDTO;
import com.ak.community.dto.QuestionDTO;
import com.ak.community.mapper.QuestionMapper;
import com.ak.community.service.CommentService;
import com.ak.community.service.NotifyService;
import com.ak.community.service.QuestionDTOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class QuestionController {
    @Autowired
    QuestionDTOService questionDTOService;

    @Autowired
    QuestionMapper questionMapper;

    @Autowired
    CommentService commentService;

    @Autowired
    NotifyService notifyService;

    @GetMapping("/question/{id}")
    public String questionController(@PathVariable Long id, Model model, HttpServletRequest request) {
        Integer newMsg = notifyService.getNewMsg(request);
        request.getSession().setAttribute("newMsg",newMsg);
        QuestionDTO questionDTO = questionDTOService.getQuestionDTO(id);
        List<QuestionDTO> relatedQuestionList = questionDTOService.getRelatedQuestionList(questionDTO);//相关问题列表
        List<CommentDTO> commentDTOList = commentService.getCommentDTOList(id);//一级评论列表
        List<CommentDTO> secondCommentDTOList = commentService.getSecondCommentDTOList(id);//二级评论列表
        questionDTOService.incViewCount(id);
        model.addAttribute("question",questionDTO);
        model.addAttribute("comments",commentDTOList);
        model.addAttribute("secondCommentList",secondCommentDTOList);
        model.addAttribute("relatedQuestionList",relatedQuestionList);
        return "question";
    }
}
