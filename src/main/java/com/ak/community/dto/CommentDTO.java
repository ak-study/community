package com.ak.community.dto;

import com.ak.community.model.User;
import lombok.Data;

@Data
public class CommentDTO {
    private String content;
    private Long parent_id;
    private Integer type;
    private Long id;
    private Long commentator;
    private Long gmt_create;
    private Long gmt_modified;
    private Long like_count;
    private Integer comment_count;
    private User user;

}
