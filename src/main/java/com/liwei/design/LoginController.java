package com.liwei.design;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.liwei.design.model.User;
import com.liwei.design.repo.UserRepository;
import com.liwei.design.service.FileService;
import com.liwei.design.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Controller
public class LoginController {
    @Autowired
    private UserRepository uRepo;
    @Autowired
    private FileService fs;
    private final static String CHECK_CODE = "checkCode";
    private final static String FORGET_NAME = "forgetName";


    @RequestMapping("/vertifyKaptcha")
    @ResponseBody
    public Map<String, String> vertifyKaptcha(@RequestParam String code, HttpServletRequest request){
        String captchaId = (String) request.getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY);
        Map<String, String> res = new HashMap<String, String>();
        if (!captchaId.equals(code)) {
            res.put("res", "fail");
        } else {
            res.put("res", "success");
        }
        return res;
    }

    @RequestMapping(value = {"/login","/logout"}, method = RequestMethod.GET)
    public ModelAndView index() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) {
            ModelAndView login = new ModelAndView("login");
            login.addObject("title","欢迎使用本系统");
            return login;
        } else {
            RedirectView redirectView = new RedirectView("/home");
            ModelAndView home = new ModelAndView(redirectView);
            return home;
        }
    }

    @RequestMapping("/checkRegName")
    @ResponseBody
    public String checkRegName(String regname) {
        if (uRepo.findOne(regname) != null) {
            return "false";
        } else {
            return "true";
        }
    }

    @RequestMapping("/checkEmail")
    @ResponseBody
    public String checkEmail(String email) {
        if (uRepo.findByEmail(email).isEmpty()) {
            return "true";
        } else {
            return "false";
        }
    }

    @RequestMapping("/register")
    @ResponseBody
    public Map<String, String> register(String name, String password, String email, String hobby) {
        User user = new User();
        user.setAuth("USER");
        user.setUsername(name);
        user.setPassword(password);
        user.setEmail(email);
        user.setVolume(BigDecimal.valueOf(2048));
        user.setUsedVolume(BigDecimal.valueOf(0));
        if (hobby.contains("tiyu")) {
            user.setTiyu("1");
        }
        if (hobby.contains("renwen")) {
            user.setRenwen("1");
        }
        if (hobby.contains("yishu")) {
            user.setYishu("1");
        }
        if (hobby.contains("lvyou")) {
            user.setLvyou("1");
        }
        uRepo.saveAndFlush(user);

        fs.createDir("/" + user.getUsername());
        if (hobby.contains("tiyu")) {
            fs.createDir("/" + user.getUsername() + "/" + "体育");
        }
        if (hobby.contains("renwen")) {
            fs.createDir("/" + user.getUsername() + "/" + "人文");
        }
        if (hobby.contains("yishu")) {
            fs.createDir("/" + user.getUsername() + "/" + "艺术");
        }
        if (hobby.contains("lvyou")) {
            fs.createDir("/" + user.getUsername() + "/" + "旅游");
        }

        Map<String, String> res = new HashMap<String, String>();
        res.put("res", "success");
        return res;
    }

    @RequestMapping("/forgetPassword")
    @ResponseBody
    public Map<String, String> forgetPassword(HttpServletRequest request, String name) 
        throws UnsupportedEncodingException, MessagingException {
        String index = "123456789zxcvbnmasdfghjklqwertyuiopZXCVBNMASDFGHJKLQWERTYUIOP";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 6; ++i) {
            sb.append(index.charAt(random.nextInt(index.length())));
        }

        String checkCode = sb.toString();
        HttpSession session = request.getSession();

        session.setAttribute(CHECK_CODE, checkCode);
        session.setAttribute(FORGET_NAME, name);

        User user = uRepo.findAllByUsername(name);
        String html = "验证码:" + checkCode;
        MailService.sendHtmlMail(user.getEmail(), "找回密码提示", html);
        
        Map<String, String> res = new HashMap<String, String>();
        res.put("success", "success");
        return res;
    }
    
    @RequestMapping("/vertifyCheckCode")
    @ResponseBody
    public Map<String, String> vertifyCheckCode(String checkCode, HttpServletRequest request) {
        HttpSession session = request.getSession();
        String code = (String) session.getAttribute(CHECK_CODE);
        if (code.equals(checkCode)) {
            session.removeAttribute(CHECK_CODE);
            Map<String, String> res = new HashMap<String, String>();
            res.put("success", "success");
            return res;
        } else {
            Map<String, String> res = new HashMap<String, String>();
            res.put("fail", "fail");
            return res;
        }
    }

    @RequestMapping("/findPassword")
    @ResponseBody
    public Map<String, String> findPassword(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String name = (String) session.getAttribute(FORGET_NAME);
        User user = uRepo.findAllByUsername(name);
        Map<String, String> res = new HashMap<>();
        if (user != null) {
            res.put("password", user.getPassword());
        }
        session.removeAttribute(FORGET_NAME);
        return res;
    }
}
