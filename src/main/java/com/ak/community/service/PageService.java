package com.ak.community.service;

import com.ak.community.dto.QuestionDTO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;

@Service
public class PageService {
    @Autowired
    QuestionDTOService questionDTOService;
    public void doPage(Integer pageNum, Model model){
        Page<Object> page = PageHelper.startPage(pageNum, 6);
        List<QuestionDTO> questionDTOList = questionDTOService.getQuestionDTOList();
        PageInfo<QuestionDTO> pageInfo = new PageInfo<>(questionDTOList,6);
        pageInfo.setPages(page.getPages());//总页数
        pageInfo.setTotal(page.getTotal());//总条数
        boolean hasNextPage=pageNum<pageInfo.getPages();
        boolean hasPrePage=pageNum>1;
        model.addAttribute("pageNum",pageNum);
        model.addAttribute("hasPrePage",hasPrePage);
        model.addAttribute("hasNextPage",hasNextPage);
        model.addAttribute("nextPage",pageNum+1);
        model.addAttribute("prePage",pageNum-1);
        model.addAttribute("pageInfo",pageInfo);
        model.addAttribute("questions",questionDTOList);
    }
}
