package com.ak.community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ProfileController {
    @GetMapping("profile/{action}")
    public String profileController(@PathVariable String action, Model model){
        if(action.equals("questions")){
            model.addAttribute("section","question");
            model.addAttribute("sectionName","我的问题");
            return "profile";
        }else if(action.equals("newMsg")){
            model.addAttribute("section","newMsg");
            model.addAttribute("sectionName","最新消息");
            return "profile";
        }
        return "profile";
    }
}
