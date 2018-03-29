package com.liwei.design.service;

import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

@Service
public class MailService {
  private static final String HOST = "smtp.163.com";
  private static final Integer PORT = 25;
  private static final String USERNAME = "shuliweeei@163.com";
//  private static final String PASSWORD = "wwwliwei001com";
  private static final String EMAILFROM = "shuliweeei@163.com";
  private static JavaMailSenderImpl mailSender = createMailSender();

  private MailService() {

  }

  /**
   * 邮件发送器
   *
   * @return 配置好的工具
   */
  private static JavaMailSenderImpl createMailSender() {
    JavaMailSenderImpl sender = new JavaMailSenderImpl();
    sender.setHost(HOST);
    sender.setPort(PORT);
    sender.setUsername(USERNAME);
    sender.setPassword("wwwliwei001com");
    sender.setDefaultEncoding("Utf-8");
    Properties p = new Properties();
    p.setProperty("mail.smtp.timeout", "25000");
    p.setProperty("mail.smtp.auth", "false");
    sender.setJavaMailProperties(p);
    return sender;
  }

  /**
   * 发送邮件
   *
   * @param to 接受人
   * @param subject 主题
   * @param html 发送内容
   * @throws javax.mail.MessagingException 异常
   * @throws UnsupportedEncodingException 异常
   */
  private static void sendHtmlMail(String to, String subject, String html)
      throws UnsupportedEncodingException, javax.mail.MessagingException {
    MimeMessage mimeMessage = mailSender.createMimeMessage();
    // 设置utf-8或GBK编码，否则邮件会有乱码
    MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
    messageHelper.setFrom(EMAILFROM, "自由网盘提示邮件");
    messageHelper.setTo(to);
    messageHelper.setSubject(subject);
    messageHelper.setText(html, true);
    mailSender.send(mimeMessage);
  }

}
