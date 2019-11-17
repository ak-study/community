package com.ak.community.service;

import com.ak.community.dto.NotificationDTO;
import com.ak.community.enums.NotificationTypeEnum;
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

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Service
public class NotifyService {
    @Autowired
    NotificationMapper notificationMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    QuestionMapper questionMapper;

    @Autowired
    CommentMapper commentMapper;

    public List<NotificationDTO> getNotifyDTOList(HttpServletRequest request){
        Object objectUser = request.getSession().getAttribute("user");
        User user = new User();
        BeanUtils.copyProperties(objectUser,user);
        List<Notification> notifyList = notificationMapper.getNotifyListByID(user.getId());
        List<NotificationDTO> notificationDTOList=new ArrayList<>();
        for (Notification notification : notifyList) {
            NotificationDTO notificationDTO = new NotificationDTO();
            Integer type = notification.getType();
            if(type.equals(NotificationTypeEnum.REPLY_QUESTION.getType())){
                NotificationDTO notifyDTO = createNotifyDTO(notification, notificationDTO, NotificationTypeEnum.REPLY_QUESTION, true);
                notificationDTOList.add(notifyDTO);
            }else{
                NotificationDTO notifyDTO = createNotifyDTO(notification, notificationDTO, NotificationTypeEnum.REPLY_COMMENT, false);
                notificationDTOList.add(notifyDTO);
            }
        }
        return  notificationDTOList;
    }

    /**
     * 创建评论传输类
     * @param notification 当前用户被通知的所有问题和评论列表
     * @param notificationDTO 当前用户被通知的所有问题和评论列表，其中将评论人id和被评论的问题转换成名字
     * @param notificationTypeEnum 评论类型（评论了评论还是问题）
     * @param questionOrComment 判断是评论了评论还是问题,true是问题，false是评论
     */
    private NotificationDTO createNotifyDTO(Notification notification, NotificationDTO notificationDTO,
                                 NotificationTypeEnum notificationTypeEnum,boolean questionOrComment) {
        Long notifierID = notification.getNotifier();
        Long outerId = notification.getOuterId();
        User notifier = userMapper.findUserByID(notifierID);
        BeanUtils.copyProperties(notification,notificationDTO);
        if(questionOrComment){
            Question notifyQuestion = questionMapper.getQuestionByID(outerId);
            if(notification!=null){
                notificationDTO.setOuterTitle(notifyQuestion.getTitle());
            }
        }else{
            Comment notifyComment = commentMapper.getCommentByID(outerId);
            if(notifyComment!=null){
                notificationDTO.setOuterTitle(notifyComment.getContent());
                notificationDTO.setOuterId(commentMapper.getCommentByID(notification.getOuterId()).getParent_id());
            }
        }
        notificationDTO.setNotifierName(notifier.getName());
        notificationDTO.setTypeName(notificationTypeEnum.getName());
        return notificationDTO;
    }

    public Integer getNewMsg(HttpServletRequest request){
        Object objectUser = request.getSession().getAttribute("user");
        Integer newMsgCount=0;
        if(objectUser==null){
            return newMsgCount;

        }
        User user = new User();
        BeanUtils.copyProperties(objectUser,user);
        List<Notification> notifyList = notificationMapper.getNotifyListByID(user.getId());
        for (Notification notification : notifyList) {
            if (notification.getStatus().equals(1)){
                newMsgCount++;
            }
        }
        return newMsgCount;
    }
}
