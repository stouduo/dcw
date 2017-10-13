package com.stouduo.dcw.service;

import java.util.Map;

public interface MailService {
    void sendSimpleMail(String from, String to, String subject, String content) throws Exception;

    void sendSimpleMail(String to, String subject, String content) throws Exception;

    void sendHtmlMail(String from, String to, String subject, String html) throws Exception;

    void sendHtmlMail(String to, String subject, String html) throws Exception;

    void sendTemplateMail(String from, String to, String tplPath, Map<String, Object> model) throws Exception;

    void sendTemplateMail(String to, String tplPath, Map<String, Object> model) throws Exception;

    void sendAttachmentsMail(String from, String to, String subject, String content, String filePath, String fileName);

    void sendAttachmentsMail(String to, String subject, String content, String filePath, String fileName);
}
