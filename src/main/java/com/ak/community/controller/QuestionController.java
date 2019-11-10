package com.ak.community.controller;

import com.ak.community.dto.QuestionDTO;
import com.ak.community.service.QuestionDTOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class QuestionController {
    @Autowired
    QuestionDTOService questionDTOService;

    @GetMapping("/question/{id}")
    public String questionController(@PathVariable Integer id, Model model)  {
        QuestionDTO questionDTO = questionDTOService.getQuestionDTO(id);
        model.addAttribute("question",questionDTO);
        return "question";
    }
}
