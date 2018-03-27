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
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ShareController {
    @Autowired
    private ShareRepository sr;
    @Autowired
    private FileService fileService;

    //TODO 有点奇怪
    @RequestMapping("/s/{sPath}")
    public ModelAndView sShare(@PathVariable String sPath) {
        ModelAndView modelAndView = new ModelAndView("user/sShare");
        List<Share> list = sr.getBysPath(sPath);
        Share s = null;
        if (!list.isEmpty()) {
            s = list.get(0);
        }
        modelAndView.addObject("sPath", sPath);
        modelAndView.addObject("title", s == null ? "" : s.getUser() + "的私密分享");
        return modelAndView;
    }

    @RequestMapping("/s/check")
    @ResponseBody
    public Map<String, String> checkSecretShare(String sPath, String secret) {
        String path = sr.getBysPathAndSecret(sPath, secret);
        Map<String, String> res = new HashMap<>();
        if (path != null) {
            res.put("res", "success");
            res.put("path", path);
        } else {
            res.put("res", "fail");
        }
        return res;
    }

    @RequestMapping("/share/otherShare")
    public ModelAndView otherShare(String name) {
        ModelAndView modelAndView = new ModelAndView("user/otherShare");
        modelAndView.addObject("name", name);
        return modelAndView;
    }

    @RequestMapping("/share/getOtherShare")
    @ResponseBody
    public List<String> getOtherShare(String name) {
        return sr.getListByUserAndSpath(name);
    }

    @RequestMapping("/share/download")
    public void responseDownload(HttpServletResponse response, @RequestParam String path,
                                 String other) throws UnsupportedEncodingException {
        fileService.downloadFile(response, path, other);
    }

    @RequestMapping("/share/downloadZip")
    public void responseDownloadZip(HttpServletResponse response, @RequestParam String url,
                                 String other) throws IOException {
        fileService.downloadFolder(url, response, other);
    }
}
