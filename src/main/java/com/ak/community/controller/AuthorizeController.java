package com.ak.community.controller;

import com.ak.community.dto.AccessTokenDTO;
import com.ak.community.dto.GithubUser;
import com.ak.community.mapper.UserMapper;
import com.ak.community.model.User;
import com.ak.community.provider.GithubProvider;
import org.h2.engine.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import sun.misc.Request;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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
        GithubUser githubUser = githubProvider.getUser(accessToken);
        if(githubUser!=null){
            //登录成功
            User user = new User();
            //通过UUID，获取一个随机字符串当作token
            String token = UUID.randomUUID().toString();
            //设置用户属性
            user.setToken(token);
            user.setName(githubUser.getName());
            user.setAccountid(String.valueOf(githubUser.getId()));
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            //添加该用户到数据库
            userMapper.insertUser(user);
            System.out.println(token);
            //用过浏览器的响应，添加cookie
            response.addCookie(new Cookie("token",token));
            return "redirect:index";
        }else{
            //登录失败
            return "redirect:index";
        }
    }
}
