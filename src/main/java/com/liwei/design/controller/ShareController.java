package com.liwei.design.controller;

import com.liwei.design.model.Share;
import com.liwei.design.repo.ShareRepository;
import com.liwei.design.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.List;

@Controller
public class ShareController {
    @Autowired
    private ShareRepository sr;
    @Autowired
    private FileService fileService;

    @RequestMapping("/s/{sPath}")
    public ModelAndView sShare(@PathVariable String sPath, @RequestParam String secret) {
        ModelAndView modelAndView = new ModelAndView();
        String path = sr.getBysPath(sPath, secret);
        modelAndView.addObject("rootPath", path);
        return modelAndView;
    }

    @RequestMapping("/otherShare")
    public ModelAndView otherShare(String name) {
        ModelAndView modelAndView = new ModelAndView("user/otherShare");
        modelAndView.addObject("name", name);
        return modelAndView;
    }

    @RequestMapping("/getOtherShare")
    @ResponseBody
    public List<String> getOtherShare(String name) {
        return sr.getListByUserAndSpath(name);
    }

    @RequestMapping("/share/download")
    public void responseDownload(HttpServletResponse response, @RequestParam String path,
                                 String other) throws UnsupportedEncodingException {
        fileService.downloadFile(response, path, other);
    }
}
