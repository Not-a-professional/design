package com.liwei.design.controller;

import com.liwei.design.model.Share;
import com.liwei.design.model.User;
import com.liwei.design.model.ticketShare;
import com.liwei.design.model.ticketVolume;
import com.liwei.design.repo.ShareRepository;
import com.liwei.design.repo.UserRepository;
import com.liwei.design.repo.ticketShareRepository;
import com.liwei.design.repo.ticketVolumeRepository;
import com.liwei.design.service.FileService;
import com.liwei.design.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private ticketShareRepository tsRepo;
    @Autowired
    private ShareRepository sRepo;
    @Autowired
    private ticketVolumeRepository tvRepo;
    @Autowired
    private UserRepository uRepo;
    @Autowired
    private FileService fileService;

    @RequestMapping("/ticketShare")
    public ModelAndView ticketShare() {
        ModelAndView modelAndView = new ModelAndView("admin/ticketShare");
        modelAndView.addObject("title", "审核分享");
        return  modelAndView;
    }

    @RequestMapping("/ticketVolume")
    public ModelAndView ticketVolume() {
        ModelAndView modelAndView = new ModelAndView("admin/ticketVolume");
        modelAndView.addObject("title", "审核容量");
        return  modelAndView;
    }

    @RequestMapping("/getTicketVolume")
    @ResponseBody
    public Map<String, Object> getTicketShare(Pageable pageable,
            HttpServletRequest request, @RequestParam String username) {
        SecurityContextImpl securityContextImpl = (SecurityContextImpl) request
                .getSession().getAttribute("SPRING_SECURITY_CONTEXT");
        Authentication authentication = securityContextImpl.getAuthentication();
        String user = authentication.getName();
        List<ticketVolume> list = new ArrayList<ticketVolume>();
        list = tvRepo.findAllByUserAndStatus(user, "%" + username + "%", pageable);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("total", list.size());
        map.put("rows", list);
        return map;
    }

    @RequestMapping("/getTicketShare")
    @ResponseBody
    public Map<String, Object> getTicketVolume(Pageable pageable,
                                              HttpServletRequest request, @RequestParam String username) {
        SecurityContextImpl securityContextImpl = (SecurityContextImpl) request
            .getSession().getAttribute("SPRING_SECURITY_CONTEXT");
        Authentication authentication = securityContextImpl.getAuthentication();
        String user = authentication.getName();
        List<ticketShare> list = new ArrayList<ticketShare>();
        list = tsRepo.findAllByUser(user, "%" + username + "%", pageable);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("total", list.size());
        map.put("rows", list);
        return map;
    }

    @RequestMapping("/getList")
    @ResponseBody
    public Map<String, Object> getAdminFileList(String path) {
        Map<String, Object> map = new HashMap<>();
        List<Map<String, String>> list = fileService.getAdminFileList(path);
        map.put("total", list.size());
        map.put("rows", list);
        return map;
    }

    @RequestMapping("/shareAuth")
    @ResponseBody
    public Map<String, String> auth(String id) {
        Map<String, String> map = new HashMap<String, String>();
        ticketShare ts = tsRepo.getOne(Long.valueOf(id));
        Share s = new Share();
        s.setPath(ts.getPath());
        s.setUser(ts.getUser());
        s.setDate(new SimpleDateFormat("yyyyMMdd").format(new Date()));
        s.setDownload(0);
        s.setExpire(ts.getExpire());
        String html = "分享申请" + ts.getPath() + "已通过！";
        if (ts.getSpath() != null) {
            s.setSecret(ts.getSecret());
            s.setSpath(ts.getSpath());
            html = html + "私密分享路径为localhost:8080/s/" + ts.getSpath()
                + "，私密分享提取码为" + ts.getSecret();
        }
        User user = uRepo.findAllByUsername(ts.getUser());
        try {
            MailService.sendHtmlMail(user.getEmail(), "分享申请审核结果", html);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            map.put("fail", "fail");
            return map;
        } catch (MessagingException e) {
            e.printStackTrace();
            map.put("fail", "fail");
            return map;
        }
        sRepo.saveAndFlush(s);
        tsRepo.updateById(Long.valueOf(id));
        map.put("res", "success");
        return map;
    }

    @RequestMapping("/shareUnauth")
    @ResponseBody
    public Map<String, String> unAuth(String id) {
        Map<String, String> map = new HashMap<String, String>();
        ticketShare ticketShare = tsRepo.findOne(Long.valueOf(id));
        User user = uRepo.findAllByUsername(ticketShare.getUser());
        try {
            MailService.sendHtmlMail(user.getEmail(), "分享申请审核结果", "抱歉，您的分享申请" + ticketShare.getPath()
                + "未通过！");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            map.put("fail", "fail");
            return map;
        } catch (MessagingException e) {
            e.printStackTrace();
            map.put("fail", "fail");
            return map;
        }
        tsRepo.updateById(Long.valueOf(id));
        map.put("res", "success");
        return map;
    }

    @RequestMapping("/volumeAuth")
    @ResponseBody
    public Map<String, String> authVolume(String id) {
        Map<String, String> map = new HashMap<String, String>();
        ticketVolume tv = tvRepo.findOne(Long.valueOf(id));

        User user = uRepo.findAllByUsername(tv.getUser());
        try {
            MailService.sendHtmlMail(user.getEmail(), "容量申请审核结果", "您的容量申请已通过，您的总容量为："
                + user.getVolume() + "kb，已使用容量为：" + user.getUsedVolume() + "kb");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            map.put("fail", "fail");
            return map;
        } catch (MessagingException e) {
            e.printStackTrace();
            map.put("fail", "fail");
            return map;
        }
        tv.setStatus("1");
        tvRepo.saveAndFlush(tv);

        user.setVolume(user.getVolume().add(BigDecimal.valueOf(2048)));
        uRepo.saveAndFlush(user);
        map.put("res", "success");
        return map;
    }

    @RequestMapping("/unAuthVolume")
    @ResponseBody
    public Map<String, String> unAuthVolume(String id) {
        Map<String, String> map = new HashMap<String, String>();
        ticketVolume ticketVolume = tvRepo.findOne(Long.valueOf(id));
        User user = uRepo.findAllByUsername(ticketVolume.getUser());
        try {
            MailService.sendHtmlMail(user.getEmail(), "容量申请审核结果", "抱歉，您的容量申请未通过");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            map.put("fail", "fail");
            return map;
        } catch (MessagingException e) {
            e.printStackTrace();
            map.put("fail", "fail");
            return map;
        }
        ticketVolume tv = tvRepo.findOne(Long.valueOf(id));
        tv.setStatus("1");
        tvRepo.saveAndFlush(tv);
        map.put("res", "success");
        return map;
    }

}
