package com.ak.community.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;

@Controller
public class index {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @GetMapping("index")
    public String indexController(){
        return "index";
    }
}
