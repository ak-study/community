package com.ak.community.controller;

import com.ak.community.dto.CommentDTO;
import com.ak.community.dto.QuestionDTO;
import com.ak.community.mapper.QuestionMapper;
import com.ak.community.model.User;
import com.ak.community.service.CommentService;
import com.ak.community.service.QuestionDTOService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import sun.misc.Request;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
public class QuestionController {
    @Autowired
    QuestionDTOService questionDTOService;

    @Autowired
    QuestionMapper questionMapper;

    @Autowired
    CommentService commentService;

    @GetMapping("/question/{id}")
    public String questionController(@PathVariable Long id, Model model, HttpServletRequest request)  {
        List<CommentDTO> commentDTOList = commentService.getCommentDTOList(id);
        List<CommentDTO> secondCommentList = commentService.getSecondCommentList(id);
        QuestionDTO questionDTO = questionDTOService.getQuestionDTO(id);
        questionDTOService.incViewCount(id);
        model.addAttribute("question",questionDTO);
        model.addAttribute("comments",commentDTOList);
        model.addAttribute("secondCommentList",secondCommentList);
        return "question";
    }
}
