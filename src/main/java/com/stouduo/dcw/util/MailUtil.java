package com.stouduo.dcw.util;

import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.Map;

public class MailUtil {
    @Autowired
    private static JavaMailSender mailSender; //自动注入的Bean

    @Value("${spring.mail.username}")
    private static String Sender; //读取配置文件中的参数

    @Autowired
    private static FreeMarkerConfigurer freeMarkerConfigurer;  //自动注入

    public static void sendSimpleMail(String from, String to, String subject, String content) throws Exception {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        mailSender.send(message);
    }

    public static void sendSimpleMail(String to, String subject, String content) throws Exception {
        sendSimpleMail(Sender, to, subject, content);
    }

    public static void sendHtmlMail(String from, String to, String subject, String html) {
        MimeMessage message = null;
        try {
            message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(html, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mailSender.send(message);
    }

    public static void sendHtmlMail(String to, String subject, String html) {
        sendHtmlMail(Sender, to, subject, html);
    }

    public static void sendTemplateMail(String from, String to, String tplPath, Map<String, Object> model) throws Exception {
        Template template = freeMarkerConfigurer.getConfiguration().getTemplate(tplPath);
        sendHtmlMail(from, to, (String) model.get("subj" +
                "ect"), FreeMarkerTemplateUtils.processTemplateIntoString(template, model));
    }

    public static void sendTemplateMail(String to, String tplPath, Map<String, Object> model) throws Exception {
        sendTemplateMail(Sender, to, tplPath, model);
    }

    public static void sendAttachmentsMail(String from, String to, String subject, String content, String filePath, String fileName) {
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

    public static void sendAttachmentsMail(String to, String subject, String content, String filePath, String fileName) {
        sendAttachmentsMail(Sender, subject, content, filePath, fileName);
    }
}
