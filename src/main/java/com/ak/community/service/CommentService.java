package com.ak.community.service;

import com.ak.community.dto.CommentDTO;
import com.ak.community.exception.CustomizeErrorCode;
import com.ak.community.exception.CustomizeException;
import com.ak.community.mapper.CommentMapper;
import com.ak.community.mapper.QuestionMapper;
import com.ak.community.mapper.UserMapper;
import com.ak.community.model.Comment;
import com.ak.community.model.Question;
import com.ak.community.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentService {
    @Autowired
    CommentMapper commentMapper;

    @Autowired
    QuestionMapper questionMapper;

    @Autowired
    UserMapper userMapper;

    //@Transactional为该方法添加事务机制
    @Transactional
    public boolean insertComment(Comment comment){
        //非空处理
        if(comment==null){
            throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
        }else if(comment.getType()==null){
            throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_WRONG);
        }else if(comment.getParent_id()==null){
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }else if(comment.getContent()==null){
            throw new CustomizeException(CustomizeErrorCode.CONTENT_IS_EMPTY);
        }
        Question question = questionMapper.getQuestionByID(comment.getParent_id());
        //如果回复类型为0，则回复问题
        if(comment.getType()==0){
            if(question==null){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            questionMapper.incCommentCount(question.getId());//该问题的评论数+1
            commentMapper.insertComment(comment);
        }else{
            //否则回复的就是评论
            Question question1 = questionMapper.getQuestionByID(commentMapper.getCommentByID(comment.getParent_id()).getParent_id());
            questionMapper.incCommentCount(question1.getId());
            commentMapper.incCommentCount(comment.getParent_id());
            commentMapper.insertComment(comment);
        }

        return true;
    }

    //获取传入问题id的所有评论
    //这里的ID是一级评论的id
    public List<CommentDTO> getCommentDTOList(Long id){

        List<Comment> commentList = commentMapper.getCommentList();
        List<CommentDTO>commentDTOList=new ArrayList<CommentDTO>();
        for (Comment comment : commentList) {
            if(comment.getParent_id()==id && comment.getType()==0){
                CommentDTO commentDTO=new CommentDTO();
                Long commentator = comment.getCommentator();
                User user = userMapper.findUserByID(commentator);
                commentDTO.setUser(user);
                BeanUtils.copyProperties(comment,commentDTO);
                commentDTOList.add(commentDTO);
            }
        }
        return  commentDTOList;
    }
    //获取所有二级评论列表
    //这里的ID是一级评论的id
    public List<CommentDTO> getSecondCommentList(Long id){

        List<Comment> commentList = commentMapper.getCommentList();
        List<CommentDTO>commentDTOList=new ArrayList<CommentDTO>();
        for (Comment comment : commentList) {
            if( comment.getType()==1){
                Comment commentByID = commentMapper.getCommentByID(comment.getParent_id());
                if(commentByID.getParent_id()==id) {
                    CommentDTO commentDTO = new CommentDTO();
                    Long commentator = comment.getCommentator();
                    User user = userMapper.findUserByID(commentator);
                    commentDTO.setUser(user);
                    BeanUtils.copyProperties(comment, commentDTO);
                    commentDTOList.add(commentDTO);
                }
            }
        }
        return  commentDTOList;
    }
}
