package com.stouduo.dcw.events;

import com.stouduo.dcw.service.SMSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class SMSEventLisener implements ApplicationListener<SMSEvent> {
    @Autowired
    private SMSService smsService;

    @Override
    @Async
    public void onApplicationEvent(SMSEvent smsEvent) {
        SMSEventObj obj = (SMSEventObj) smsEvent.getSource();
        try {
            smsService.send(obj.getCode(), obj.getTel());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
