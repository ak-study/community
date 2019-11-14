package com.ak.community.mapper;

import lombok.Data;

@Data
public class Comment {
    private Integer id;
    private Long parent_id;
    private Integer type;
    private Integer commentator;
    private Long gmt_create;
    private Long gmt_modified;
    private Long like_count;
    private String content;

}
