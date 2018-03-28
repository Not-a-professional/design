package com.liwei.design.controller;

import com.liwei.design.othermodel.hotShare;
import com.liwei.design.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    @RequestMapping("/editor")
    public ModelAndView editor(String path) {
        ModelAndView editor = new ModelAndView("user/editor");
        editor.addObject("title", "云笔记");
        editor.addObject("editorPath", path);
        return editor;
    }

    @RequestMapping("/getHotShare")
    @ResponseBody
    public List<hotShare> getHotShare(HttpServletRequest request) {
        return us.getHotShare(request);
    }

    @RequestMapping("/volumeApply")
    public ModelAndView volumeApply() {
        ModelAndView volumeApply = new ModelAndView("user/volumeApply");
        volumeApply.addObject("title", "申请容量");
        return volumeApply;
    }

    @RequestMapping("/volumeReason")
    @ResponseBody
    public Map<String, String> volumeReason(HttpServletRequest request, String reason) {
        return us.volumeReason(request, reason);
    }
}
