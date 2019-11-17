package com.ak.community.model;

import lombok.Data;

@Data
public class Notification {
    private Long id;
    private Long gmt_create;
    private Long notifier;
    private Integer status;
    private Integer type;
    private Long outerId;
    private Long receiver;
}
