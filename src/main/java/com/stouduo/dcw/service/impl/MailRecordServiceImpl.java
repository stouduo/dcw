package com.stouduo.dcw.service.impl;

import com.stouduo.dcw.domain.MailRecord;
import com.stouduo.dcw.repository.MailRecordRepository;
import com.stouduo.dcw.service.MailRecordService;
import com.stouduo.dcw.util.MD5Util;
import com.stouduo.dcw.util.MailUtil;
import freemarker.ext.beans.HashAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class MailRecordServiceImpl implements MailRecordService {
    @Autowired
    private MailRecordRepository mailRecordRepository;

    @Override
    @Transactional
    public MailRecord sendEmail(String email) throws Exception {
        MailRecord mailRecord = new MailRecord();
        mailRecord.setCreateTime(new Date());
        mailRecord.setEmail(email);
        mailRecord.setToken(MD5Util.getToken(email));
        mailRecord.setInvalid(false);
        Map<String, Object> model = new HashMap<>();
        model.put("email", "email");
        model.put("token", mailRecord.getToken());
        MailUtil.sendTemplateMail(email, "/resource/templates/fmtpls/signup.tpl", model);
        return mailRecordRepository.save(mailRecord);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean verify(String token) {
        MailRecord record = mailRecordRepository.findByToken(token);
        boolean invalid = record.getInvalid();
        GregorianCalendar gc = new GregorianCalendar();
        gc.add(5, -1);
        record.setInvalid(true);
        mailRecordRepository.save(record);
        return record != null && !invalid && gc.before(record.getCreateTime());
    }
}
