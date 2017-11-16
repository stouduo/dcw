package com.stouduo.dcw.events;

import org.springframework.context.ApplicationEvent;

public class SMSEvent extends ApplicationEvent {
    public SMSEvent(Object source) {
        super(source);
    }
}
