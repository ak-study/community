package com.ak.community.dto;

import lombok.Data;

@Data
public class PageDTO {
    public boolean hasNextPage(Integer pages,Integer pageNum){
        return pageNum<pages;
    }
    public Integer nextPage(Integer pageNum){
        return pageNum+1;
    }
    public boolean hasPrePage(Integer pageNum){
        return pageNum>1;
    }
    public Integer prePage(Integer pageNum){
        return pageNum-1;
    }

}
