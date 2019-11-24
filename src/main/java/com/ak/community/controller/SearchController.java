package com.ak.community.controller;

import com.ak.community.dto.QuestionDTO;
import com.ak.community.mapper.QuestionMapper;
import com.ak.community.model.Question;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class SearchController {
    @Autowired
    QuestionMapper questionMapper;

    @GetMapping("/search")
    public String searchController(String search, Model model){
        //用空格将内容分隔，拼接成 内容|内容|内容...
        String[] searchContent= StringUtils.split(search," ");
        String regexSearch= Arrays.stream(searchContent).collect(Collectors.joining("|"));

        List<QuestionDTO> questionDTOS=new ArrayList<>();
        List<Question> questionsByTitle = questionMapper.selectRelatedFromTitle(regexSearch);
        List<Question> questionsByTag = questionMapper.selectRelatedFromTag(regexSearch);
        for (Question question : questionsByTitle) {
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTOS.add(questionDTO);
        }
        for (Question question : questionsByTag) {
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTOS.add(questionDTO);
        }
        model.addAttribute("questions",questionDTOS);
        return "index";
    }
}
