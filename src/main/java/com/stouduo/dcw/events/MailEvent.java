package com.stouduo.dcw.events;

import org.springframework.context.ApplicationEvent;

public class MailEvent extends ApplicationEvent {
    public MailEvent(Object source) {
        super(source);
    }
}
