package com.ak.community.service;

import com.ak.community.dto.CommentDTO;
import com.ak.community.enums.NotificationStatusEnum;
import com.ak.community.enums.NotificationTypeEnum;
import com.ak.community.exception.CustomizeErrorCode;
import com.ak.community.exception.CustomizeException;
import com.ak.community.mapper.CommentMapper;
import com.ak.community.mapper.NotificationMapper;
import com.ak.community.mapper.QuestionMapper;
import com.ak.community.mapper.UserMapper;
import com.ak.community.model.Comment;
import com.ak.community.model.Notification;
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

    @Autowired
    NotificationMapper notificationMapper;

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
        //如果回复类型为0，则回复问题
        if(comment.getType()==0){
            Question question = questionMapper.getQuestionByID(comment.getParent_id());
            if(question==null){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            questionMapper.incCommentCount(question.getId());//一级评论对应的问题的评论数+1
            commentMapper.insertComment(comment);
            createNotify(comment,question.getCreator(), NotificationTypeEnum.REPLY_QUESTION);//创建通知
        }else{
            //否则回复的就是评论
            Comment parentComment = commentMapper.getCommentByID(comment.getParent_id());
            questionMapper.incCommentCount(parentComment.getParent_id());//二级评论对应的问题的评论数+1
            commentMapper.incCommentCount(comment.getParent_id());//二级评论对应的父评论的评论数+1
            commentMapper.insertComment(comment);//添加二级评论到数据库
            createNotify(comment, parentComment.getCommentator(), NotificationTypeEnum.REPLY_COMMENT);//创建通知
        }

        return true;
    }

    /**
     *
     * @param comment 通知的评论或者问题
     * @param receiver 父评论/问题的创作者
     * @param reply 通知类型
     */
    private void createNotify(Comment comment, Long receiver, NotificationTypeEnum reply) {
        Notification notification = new Notification();
        notification.setType(reply.getType());
        notification.setGmt_create(System.currentTimeMillis());
        notification.setOuterId(comment.getParent_id());//被通知者的评论/问题id
        notification.setNotifier(comment.getCommentator());//通知者的id
        notification.setStatus(NotificationStatusEnum.READ.getStatus());
        notification.setReceiver(receiver);//被通知者的id
        notificationMapper.insertNotification(notification);
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
    public List<CommentDTO> getSecondCommentDTOList(Long id){

        List<Comment> commentList = commentMapper.getCommentList();
        List<CommentDTO>commentDTOList=new ArrayList<CommentDTO>();
        for (Comment comment : commentList) {
            if( comment.getType()==1){
                Comment commentByID = commentMapper.getCommentByID(comment.getParent_id());
                if(commentByID!=null&&commentByID.getParent_id()==id) {
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
