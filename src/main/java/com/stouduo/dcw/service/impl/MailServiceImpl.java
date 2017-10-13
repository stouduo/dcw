package com.stouduo.dcw.service.impl;

import com.stouduo.dcw.service.MailService;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.Map;

@Service
public class MailServiceImpl implements MailService {

    @Autowired
    private JavaMailSender mailSender; //自动注入的Bean

    @Value("${spring.mail.username}")
    private String Sender; //读取配置文件中的参数

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;  //自动注入

    @Override
    public void sendSimpleMail(String from, String to, String subject, String content) throws Exception {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        mailSender.send(message);
    }

    @Override
    public void sendSimpleMail(String to, String subject, String content) throws Exception {
        sendSimpleMail(Sender, to, subject, content);
    }

    @Override
    public void sendHtmlMail(String from, String to, String subject, String html) throws Exception {
        MimeMessage message = null;
        message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(from);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(html, true);
        mailSender.send(message);
    }

    @Override
    public void sendHtmlMail(String to, String subject, String html) throws Exception {
        sendHtmlMail(Sender, to, subject, html);
    }

    @Override
    public void sendTemplateMail(String from, String to, String tplPath, Map<String, Object> model) throws Exception {
        Template template = freeMarkerConfigurer.getConfiguration().getTemplate(tplPath + ".ftl");
        sendHtmlMail(from, to, (String) model.get("subject"), FreeMarkerTemplateUtils.processTemplateIntoString(template, model));
    }

    @Override
    public void sendTemplateMail(String to, String tplPath, Map<String, Object> model) throws Exception {
        sendTemplateMail(Sender, to, tplPath, model);
    }

    @Override
    public void sendAttachmentsMail(String from, String to, String subject, String content, String filePath, String fileName) {
        MimeMessage message = null;
        try {
            message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content);
            //注意项目路径问题，自动补用项目路径
            FileSystemResource file = new FileSystemResource(new File(filePath));
            //加入邮件
            helper.addAttachment(fileName, file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mailSender.send(message);
    }

    @Override
    public void sendAttachmentsMail(String to, String subject, String content, String filePath, String fileName) {
        sendAttachmentsMail(Sender, subject, content, filePath, fileName);
    }
}
