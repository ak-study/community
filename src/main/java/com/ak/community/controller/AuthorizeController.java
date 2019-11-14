package com.ak.community.controller;

import com.ak.community.dto.AccessTokenDTO;
import com.ak.community.dto.GithubUserDTO;
import com.ak.community.mapper.UserMapper;
import com.ak.community.model.User;
import com.ak.community.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
public class AuthorizeController {
    @Autowired
    GithubProvider githubProvider;

    @Value("${github.clientid}")
    private String clientid;
    @Value("${github.clientsecret}")
    private String clientsecret;
    @Value("${github.clienturi}")
    private String clienturi;

    @Autowired
    UserMapper userMapper;

    @Transactional
    @GetMapping("/callback")
    public String callback(@RequestParam(name="code")String code,
                           @RequestParam(name="state")String state,
                           HttpServletRequest request,
                           HttpServletResponse response){
        //和github网站交互，需要传带参的连接，设置参数属性（具体看application.properties）
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setClient_id(clientid);
        accessTokenDTO.setClient_secret(clientsecret);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setState(state);
        accessTokenDTO.setRedirect_uri(clienturi);
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        GithubUserDTO githubUserDTO = githubProvider.getUser(accessToken);
        if(githubUserDTO !=null){
            //登录成功
            //判断该用户在数据库是否已经存在
            Long id = githubUserDTO.getId();
            User userByAccountID = userMapper.findUserByAccountID(String.valueOf(id));
            if(userByAccountID!=null){
                response.addCookie(new Cookie("token",userByAccountID.getToken()));
                return "redirect:/";
            }
            //如果不存在，则通过githubUser封装一个User，存入数据库
            User user = new User();
            //通过UUID，获取一个随机字符串当作token
            String token = UUID.randomUUID().toString();
            //设置用户属性
            user.setToken(token);
            user.setBio(githubUserDTO.getBio());
            user.setName(githubUserDTO.getName());
            user.setAccountid(String.valueOf(githubUserDTO.getId()));
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            user.setAvatarUrl(githubUserDTO.getAvatarUrl());
            //添加该用户到数据库
            userMapper.insertUser(user);
            //用过浏览器的响应，添加cookie
            response.addCookie(new Cookie("token",token));
            return "redirect:/";
        }else{
            //登录失败
            return "redirect:/";
        }
    }

}
