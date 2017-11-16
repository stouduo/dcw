package com.stouduo.dcw.events;

import com.stouduo.dcw.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class MailEventLisener implements ApplicationListener<MailEvent> {
    @Autowired
    private MailService mailService;

    @Override
    @Async
    public void onApplicationEvent(MailEvent mailEvent) {
        MailEventObj mailEventObj = (MailEventObj) mailEvent.getSource();
        try {
            mailService.sendTemplateMail(mailEventObj.getEmail(), mailEventObj.getTplPath(), mailEventObj.getModel());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
