package com.liwei.design;

import com.google.code.kaptcha.servlet.KaptchaServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.ServletException;

@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {
    @Bean
    public ServletRegistrationBean servletRegistrationBean() throws ServletException{
        ServletRegistrationBean servlet = new ServletRegistrationBean(
                new KaptchaServlet(),"/kaptcha.jpg");
        servlet.addInitParameter("kaptcha.border", "yes");//无边框
        servlet.addInitParameter("kaptcha.border.color", "105,179,90");
        servlet.addInitParameter("kaptcha.textproducer.font.color", "blue");
        servlet.addInitParameter("kaptcha.image.width", "125");
        servlet.addInitParameter("kaptcha.image.height", "45");
        servlet.addInitParameter("kaptcha.textproducer.char.length", "4");
        servlet.addInitParameter("kaptcha.textproducer.font.names", "宋体,楷体,微软雅黑");
        return servlet;
    }
}
