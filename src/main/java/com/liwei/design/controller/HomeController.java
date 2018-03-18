package com.liwei.design.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {
    @RequestMapping(value = "/home")
    public ModelAndView home() {
        ModelAndView home = new ModelAndView("home");
        home.addObject("title","主页");
        return home;
    }

    @RequestMapping(value = "/")
    public ModelAndView common() {
        ModelAndView home = new ModelAndView("redirect:/home");
        home.addObject("title","主页");
        return home;
    }
}
