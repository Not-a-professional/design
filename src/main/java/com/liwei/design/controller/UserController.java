package com.liwei.design.controller;

import com.liwei.design.model.Trash;
import com.liwei.design.othermodel.hotShare;
import com.liwei.design.service.FileService;
import com.liwei.design.service.UserService;
import gui.ava.html.image.generator.HtmlImageGenerator;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j
@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService us;
    @Autowired
    private FileService fileService;

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

    @RequestMapping("/trash")
    public ModelAndView trash() {
        ModelAndView trash = new ModelAndView("user/trash");
        trash.addObject("title", "回收站");
        return trash;
    }

    @RequestMapping("/pic_vid")
    public ModelAndView pictureVideo(String path) {
        ModelAndView mav = new ModelAndView("/user/pic_vid");
        mav.addObject("title","预览");
        mav.addObject("pvPath", path);
        return mav;
    }

    @RequestMapping("/editor")
    public ModelAndView editor(String path) {
        ModelAndView editor = new ModelAndView("user/editor");
        editor.addObject("title", "云笔记");
        editor.addObject("editorPath", path);
        return editor;
    }

    @RequestMapping("/getTrashList")
    @ResponseBody
    public Map<String, Object> getTrashList(HttpServletRequest request, Pageable pageable) {
        Map<String, Object> map = new HashMap<String, Object>();
        List<Trash> list = us.getTrashList(request, pageable);
        map.put("total", list.size());
        map.put("rows", list);
        return map;
    }

    @RequestMapping("/getFriend")
    @ResponseBody
    public List<String> getFirend(HttpServletRequest request) {
        return us.getFriend(request);
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

    @RequestMapping("/htmlToImage")
    @ResponseBody
    public void htmlToImage(String path, HttpServletResponse response) throws IOException, InterruptedException {
        HtmlImageGenerator generator = new HtmlImageGenerator();
        try {
            generator.loadHtml((String) fileService.getEditorHtml(path).get("res"));
            Thread.sleep(100);
        } catch (IOException e) {
            log.debug(e);
        }
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        OutputStream os = response.getOutputStream();
        BufferedImage image = generator.getBufferedImage();
        Thread.sleep(200);
        ImageIO.write(image, "jpeg", os);
        os.close();
    }
}
