package com.stouduo.dcw.util;

import com.stouduo.dcw.domain.Form;
import com.stouduo.dcw.domain.FormLog;
import com.stouduo.dcw.domain.FormProperty;
import com.stouduo.dcw.repository.FormLogRepository;
import com.stouduo.dcw.repository.FormPropertyRepository;
import com.stouduo.dcw.repository.FormValueRepository;
import com.stouduo.dcw.service.FormService;
import com.stouduo.dcw.vo.FormDetailVO;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

@Component
@Aspect
public class LogAspect {
    @Autowired
    private FormService formService;
    @Autowired
    private FormValueRepository formValueRepository;
    @Autowired
    private FormLogRepository formLogRepository;
    @Autowired
    private FormPropertyRepository formPropertyRepository;

    @Pointcut("@annotation(com.stouduo.dcw.util.Log)")
    public void log() {
    }

    @After("log()")
    @Transactional
    public void after(JoinPoint jp) {
        FormDetailVO formDetailVO = (FormDetailVO) jp.getArgs()[0];
        Form form = formDetailVO.getForm();
        if (!StringUtils.isEmpty(form.getId())) {
            FormDetailVO oldFormDetailVO = formService.getForm(form.getId());
            List<FormProperty> newFormProperties = formDetailVO.getFormProperties();
            List<FormProperty> oldFormProperties = oldFormDetailVO.getFormProperties();
            if (newFormProperties.size() == oldFormProperties.size()) return;
            List<FormProperty> formProperties;
            FormLog formLog = new FormLog();
            String opt = "";
            if (newFormProperties.size() > oldFormProperties.size()) {
                newFormProperties.removeAll(oldFormProperties);
                opt = "新增";
                formProperties = newFormProperties;
            } else {
                oldFormProperties.removeAll(newFormProperties);
                formProperties = oldFormProperties;
                formPropertyRepository.delete(formProperties);
                opt = "刪除";
            }
            for (FormProperty formProperty : formProperties) {
                opt += "\"" + formProperty.getName() + "\"，";
            }
            opt = opt.substring(0, opt.length() - 1) + "字段，影响" + formValueRepository.findCountByForm(form.getId()) + "行数据";
            formLog.setForm(form.getId());
            formLog.setUser(SecurityUtil.getUsername());
            formLog.setOperate(opt);
            formLog.setOptTime(new Date());
            formLogRepository.save(formLog);
        }
    }
}
