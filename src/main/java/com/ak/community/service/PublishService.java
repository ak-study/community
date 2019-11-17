package com.ak.community.service;

import com.ak.community.cache.TagCache;
import com.ak.community.exception.CustomizeErrorCode;
import com.ak.community.exception.CustomizeException;
import com.ak.community.mapper.QuestionMapper;
import com.ak.community.model.Question;
import com.ak.community.model.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

@Service
public class PublishService {
    @Autowired
    QuestionMapper questionMapper;

    @Transactional
    public void createOrUpdate(Long id,Question question,User newUser){
        if(id!=null){
            questionMapper.updateQuestionByID(question);
        }else{
            question.setCreator(newUser.getId());
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            questionMapper.insertQuestion(question);
        }
    }
    public boolean isTruePublish(Question question,Model model,User user){
        String s = TagCache.filterInvalid(question.getTag());
        if(question.getCreator()!=user.getId()){
            throw new CustomizeException(CustomizeErrorCode.INVALID_OPERATION);
        }
        if (StringUtils.isBlank(question.getTitle())) {
            model.addAttribute("error", "标题不能为空");
            return false;
        }
        if (StringUtils.isBlank(question.getDescription())) {
            model.addAttribute("error", "问题补充不能为空");
            return false;
        }
        if (StringUtils.isBlank(question.getTag())) {
            model.addAttribute("error", "标签不能为空");
            return false;
        }
        if(StringUtils.isNotBlank(s)){
            model.addAttribute("error", "输入非法标签:"+s);
            return false;
        }
        return true;
    }
}
