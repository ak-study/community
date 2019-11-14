package com.ak.community.dto;

import com.ak.community.exception.CustomizeErrorCode;
import lombok.Data;

@Data
public class ResultDTO {
    private String message;
    private Integer code;
    public ResultDTO(CustomizeErrorCode customizeErrorCode) {
        this.message = customizeErrorCode.getMessage();
        this.code=customizeErrorCode.getCode();
    }
}
