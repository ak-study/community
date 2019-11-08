package com.ak.community.service;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.ak.community.dto.QuestionDTO;
import com.ak.community.mapper.QuestionMapper;
import com.ak.community.mapper.UserMapper;
import com.ak.community.model.Question;
import com.ak.community.model.User;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionDTOService {
    @Autowired
    QuestionMapper questionMapper;
    @Autowired
    UserMapper userMapper;

    /**
     * 通过question里面的creator去user表找对应的id，返回一个user
     * 然后通过questionDTO将question和User拼接
     * @return 返回一个questionDTO列表，其中的questionDTO包含了user信息和对应的question信息
     */
    public List<QuestionDTO> getQuestionDTOList(){
        List<Question> questionList = questionMapper.getQuestionList();
        List<QuestionDTO> questionDTOS= new ArrayList<QuestionDTO>();
        for (Question question : questionList) {
            User user = userMapper.findNameByID(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOS.add(questionDTO);
        }
        return  questionDTOS;
    }
}
