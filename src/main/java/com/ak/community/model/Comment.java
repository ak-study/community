package com.ak.community.model;


import lombok.Data;

@Data
public class Comment {
    private Long id;
    private Long parent_id;
    private Integer type;
    private Long commentator;
    private Long gmt_create;
    private Long gmt_modified;
    private Long like_count;
    private String content;
    private Integer comment_count;

}
