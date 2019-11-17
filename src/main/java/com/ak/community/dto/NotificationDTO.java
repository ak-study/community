package com.ak.community.dto;

import lombok.Data;

@Data
public class NotificationDTO {
    private Long id;
    private Long gmt_create;
    private Long notifier;
    private Long outerId;
    private Integer type;
    private Integer status;

    private String notifierName;
    private String outerTitle;
    private String typeName;
}
