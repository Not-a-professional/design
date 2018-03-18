package com.liwei.design.controller;

import com.liwei.design.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService us;

    @RequestMapping("/file")
    public ModelAndView file() {
        ModelAndView file = new ModelAndView("user/myFile");
        file.addObject("title","文件");
        return file;
    }

    @RequestMapping("/share")
    public ModelAndView share() {
        ModelAndView share = new ModelAndView("user/myShare");
        share.addObject("title", "分享");
        return share;
    }

    @RequestMapping("/getFriend")
    @ResponseBody
    public List<String> getFirend(HttpServletRequest request) {
        return us.getFriend(request);
    }
}
