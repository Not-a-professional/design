package com.liwei.design.controller;

import com.liwei.design.model.Share;
import com.liwei.design.model.User;
import com.liwei.design.model.ticketShare;
import com.liwei.design.model.ticketVolume;
import com.liwei.design.repo.ShareRepository;
import com.liwei.design.repo.UserRepository;
import com.liwei.design.repo.ticketShareRepository;
import com.liwei.design.repo.ticketVolumeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
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

    @RequestMapping("/getTicketShare")
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

    @RequestMapping("/getTicketVolume")
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

    @RequestMapping("/shareAuth")
    @ResponseBody
    public Map<String, String> auth(String id) {
        Map<String, String> map = new HashMap<String, String>();
        tsRepo.updateById(Long.valueOf(id));
        ticketShare ts = tsRepo.getOne(Long.valueOf(id));
        Share s = new Share();
        s.setPath(ts.getPath());
        s.setUser(ts.getUser());
        s.setDate(new SimpleDateFormat("yyyyMMdd").format(new Date()));
        s.setDownload(0);
        if (ts.getSpath() != null) {
            s.setSecret(ts.getSecret());
            s.setSpath(ts.getSpath());
        }
        sRepo.saveAndFlush(s);
        map.put("res", "success");
        return map;
    }

    @RequestMapping("/shareUnauth")
    @ResponseBody
    public Map<String, String> unAuth(String id) {
        Map<String, String> map = new HashMap<String, String>();
        tsRepo.deleteById(Long.valueOf(id));
        map.put("res", "success");
        return map;
    }

    @RequestMapping("/volumeAuth")
    @ResponseBody
    public Map<String, String> authVolume(String id) {
        Map<String, String> map = new HashMap<String, String>();
        ticketVolume tv = tvRepo.findOne(Long.valueOf(id));
        tv.setStatus("1");
        tvRepo.saveAndFlush(tv);

        User user = uRepo.findAllByUsername(tv.getUser());
        user.setVolume(user.getVolume().add(BigDecimal.valueOf(2048)));
        uRepo.saveAndFlush(user);
        map.put("res", "success");
        return map;
    }

    @RequestMapping("/unAuthVolume")
    @ResponseBody
    public Map<String, String> unAuthVolume(String id) {
        Map<String, String> map = new HashMap<String, String>();
        tvRepo.delete(Long.valueOf(id));
        map.put("res", "success");
        return map;
    }
}
