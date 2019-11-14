package com.ak.community.controller;

import com.ak.community.dto.CommentDTO;
import com.ak.community.dto.ResultDTO;
import com.ak.community.exception.CustomizeErrorCode;
import com.ak.community.exception.CustomizeException;
import com.ak.community.mapper.CommentMapper;
import com.ak.community.mapper.QuestionMapper;
import com.ak.community.model.Comment;
import com.ak.community.model.User;
import com.ak.community.service.CommentService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class CommentController {
    @Autowired
    CommentMapper commentMapper;
    @Autowired
    CommentService commentService;

    @Autowired
    QuestionMapper questionMapper;

    @ResponseBody
    @PostMapping("/comment")
    public Object commentController(@RequestBody CommentDTO commentDTO, HttpServletRequest request, Model model){
        Map<String,Object> map=new HashMap<>();
        Object objectUser = request.getSession().getAttribute("user");
        User user = new User();
        if(user==null){
            map.put("code",201);
            return map;
        }
        BeanUtils.copyProperties(objectUser,user);
        Comment comment = new Comment();
        //从页面获取的值
        comment.setParent_id(commentDTO.getParent_id());
        comment.setType(commentDTO.getType());
        comment.setContent(commentDTO.getContent());
        comment.setCommentator(user.getId());
        //自定义设定的值
        comment.setGmt_create(System.currentTimeMillis());
        comment.setGmt_modified(System.currentTimeMillis());
        comment.setLike_count(0L);
        commentService.insertComment(comment);
        map.put("code",200);
        return map;
    }

    //处理二级评论请求
    @GetMapping("/comment/{id}")
    public String secondCommentController(@PathVariable Long id,String comment,HttpServletRequest request,Long parent_id){
        Map<String,Object> map=new HashMap<>();
        Object objectUser = request.getSession().getAttribute("user");
        if(id==null || comment==null || parent_id==null || comment==""){
            request.setAttribute("msg","评论不能为空");
            return "forward:/question/"+id;
        }
        if(objectUser==null){
            request.setAttribute("msg","请先登录！");
            return "forward:/question/"+id;
        }
        User user = new User();
        BeanUtils.copyProperties(objectUser,user);
        Comment newComment = new Comment();
        newComment.setParent_id(parent_id);
        newComment.setContent(comment);
        newComment.setCommentator(user.getId());
        newComment.setType(1);
        newComment.setGmt_create(System.currentTimeMillis());
        newComment.setGmt_modified(System.currentTimeMillis());
        newComment.setLike_count(0L);
        commentService.insertComment(newComment);
        return "redirect:/question/"+id;
    }
    @GetMapping("/deleteComment/{id}/{deleteID}")
    public String deleteComment(@PathVariable Long id, @PathVariable Long deleteID){
        Comment comment = commentMapper.getCommentByID(deleteID);
        if(comment.getType()==1) {
            Long parent_id = commentMapper.getCommentByID(deleteID).getParent_id();
            commentMapper.deleteCommentByID(deleteID);
            commentMapper.downCommentCount(parent_id);
        }else if(comment.getType()==0){
            Long parent_id = commentMapper.getCommentByID(deleteID).getParent_id();
            commentMapper.deleteCommentByID(deleteID);
            questionMapper.downCommentCount(parent_id);
        }
        return "redirect:/question/"+id;
    }
}
