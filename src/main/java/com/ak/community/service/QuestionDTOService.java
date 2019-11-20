package com.ak.community.service;

import com.ak.community.dto.QuestionDTO;
import com.ak.community.exception.CustomizeErrorCode;
import com.ak.community.exception.CustomizeException;
import com.ak.community.exception.ICustomizeErrorCode;
import com.ak.community.mapper.QuestionMapper;
import com.ak.community.mapper.UserMapper;
import com.ak.community.model.Question;
import com.ak.community.model.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionDTOService {
    @Autowired
    QuestionMapper questionMapper;
    @Autowired
    UserMapper userMapper;

    /**
     * 获取所有人的问题列表
     * 通过question里面的creator去user表找对应的id，返回一个user
     * 然后通过questionDTO将question和User拼接
     * @return 返回一个questionDTO列表，其中的questionDTO包含了user信息和对应的question信息
     */
    public List<QuestionDTO> getQuestionDTOList(User objectUser){
        List<Question> questionList;
        if(objectUser!=null){
            questionList=questionMapper.getQuestionByPerson(objectUser.getId());
        }else {
            questionList=questionMapper.getQuestionList();
        }
        List<QuestionDTO> questionDTOS= new ArrayList<QuestionDTO>();
        for (Question question : questionList) {
            User user = userMapper.findUserByID(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            if(question.getDescription().length()>=10) {
                String subDesc = question.getDescription().substring(0, 10);
                questionDTO.setDescription(subDesc);
            }
            questionDTO.setUser(user);
            questionDTOS.add(questionDTO);
        }
        Collections.reverse(questionDTOS);
        return  questionDTOS;
    }

    /**
     * 获取当前用户的问题列表，封装成questionDTO列表
     * @param request 用来获取当前会话
     * @return 封装了当前用户问题集的列表
     */
    public List<QuestionDTO> getQuestionDTOListByPerson(HttpServletRequest request){
        Object objectUser = request.getSession().getAttribute("user");
        User user = new User();
        BeanUtils.copyProperties(objectUser,user);
        List<QuestionDTO> questionDTOS= new ArrayList<QuestionDTO>();
        List<Question> questionByPerson = questionMapper.getQuestionByPerson(user.getId());//获取当前用户的问题列表
        for (Question question : questionByPerson) {
            //封装一个questionDTO列表,里面的用户都是同一个人
            QuestionDTO questionDTO = new QuestionDTO();
            questionDTO.setUser(user);
            BeanUtils.copyProperties(question,questionDTO);
            questionDTOS.add(questionDTO);
        }
        return questionDTOS;
    }
    public QuestionDTO getQuestionDTO(Long id) throws CustomizeException {
        Question questionByID = questionMapper.getQuestionByID(id);
        if(questionByID==null){
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        User user = userMapper.findUserByID(questionByID.getCreator());
        QuestionDTO questionDTO=new QuestionDTO();
        BeanUtils.copyProperties(questionByID,questionDTO);
        questionDTO.setUser(user);
        return  questionDTO;
    }

    /**
     *
     * @param questionDTO 需要查询的问题
     * @return 与该问题相关联的问题集合（通过tag查询）
     */
    public List<QuestionDTO> getRelatedQuestionList(QuestionDTO questionDTO){
        String[] tags= StringUtils.split(questionDTO.getTag(),",");//通过逗号对question里面的tag分隔，获得tag数组
        String regexTag= Arrays.stream(tags).collect(Collectors.joining("|"));//对每个元素添加 | ，组成新字符串
        //封装一个question，设置id和新tag
        Question newQuestion = new Question();
        newQuestion.setId(questionDTO.getId());
        newQuestion.setTag(regexTag);
        //查询返回一个与当前问题相关联的问题数组（通过tag查询）
        List<Question> questions=questionMapper.selectRelated(newQuestion);
        //将questions集合转换成questionDTO集合
        List<QuestionDTO> questionDTOs = questions.stream().map(q -> {
            QuestionDTO questionDTO1 = new QuestionDTO();
            BeanUtils.copyProperties(q,questionDTO1);
            return questionDTO1;
        }).collect(Collectors.toList());
        return  questionDTOs;
    }

    @Transactional
    public void incViewCount(Long id){
        questionMapper.incViewCount(id);
    }


}
