package com.shiro.controllers;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * author: San Jinhong
 * date: 2018/11/19 15:04
 **/
@Controller
@RequestMapping("/shiro")
public class ShiroHandler {

    @RequestMapping( value = "login")
    public String login(@RequestParam String username,
                        @RequestParam String password){
        Subject currentUser = SecurityUtils.getSubject();

        if(!currentUser.isAuthenticated()){
            UsernamePasswordToken token = new UsernamePasswordToken(username, password);
            token.setRememberMe(true);
            try {
                System.err.println("1:" + token.hashCode());
                currentUser.login(token);
            } catch (AuthenticationException ae){
                System.out.println("登录失败:" + ae.getMessage());
            }
        }
        return "redirect:/list.jsp";
    }
}
