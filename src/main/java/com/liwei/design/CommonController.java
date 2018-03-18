package com.liwei.design;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice(basePackages = "com.liwei.design.controller")
public class CommonController {
    @ModelAttribute(value = "Username")
    public String getUsername(HttpServletRequest request) {
        SecurityContextImpl securityContextImpl = (SecurityContextImpl) request
                .getSession().getAttribute("SPRING_SECURITY_CONTEXT");
        if (securityContextImpl == null) {
            return "";
        }
        Authentication authentication = securityContextImpl.getAuthentication();
        String username = authentication.getName();
        return username;
    }

    @ModelAttribute(value = "Auth")
    public String getAuth(HttpServletRequest request) {
        SecurityContextImpl securityContextImpl = (SecurityContextImpl) request
                .getSession().getAttribute("SPRING_SECURITY_CONTEXT");
        if (securityContextImpl == null) {
            return "";
        }
        Authentication authentication = securityContextImpl.getAuthentication();
        String auth = authentication.getAuthorities().toString();
        return auth;
    }
}
