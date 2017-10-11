package com.stouduo.dcw.service;

import com.stouduo.dcw.domain.MailRecord;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

public interface MailRecordService {
    MailRecord sendEmail(String mail) throws Exception;

    @Transactional
    MailRecord sendEmail(String email, Map<String, Object> model, String tplPath) throws Exception;

    boolean verify(String token);
}
