package com.stouduo.dcw.service;

import com.stouduo.dcw.domain.MailRecord;

public interface MailRecordService {
    MailRecord sendEmail(String mail) throws Exception;

    boolean verify(String token);
}
