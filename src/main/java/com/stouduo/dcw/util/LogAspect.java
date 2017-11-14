package com.stouduo.dcw.util;

import com.alibaba.fastjson.JSON;
import com.stouduo.dcw.domain.Form;
import com.stouduo.dcw.domain.FormLog;
import com.stouduo.dcw.domain.FormProperty;
import com.stouduo.dcw.domain.FormValue;
import com.stouduo.dcw.repository.FormLogRepository;
import com.stouduo.dcw.repository.FormPropertyRepository;
import com.stouduo.dcw.repository.FormValueRepository;
import com.stouduo.dcw.service.FormService;
import com.stouduo.dcw.vo.FormDetailVO;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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

    @Before("log()")
    @Transactional
    public void before(JoinPoint jp) {
        FormDetailVO formDetailVO = (FormDetailVO) jp.getArgs()[0];
        Form form = formDetailVO.getForm();
        if (!StringUtils.isEmpty(form.getId())) {
            FormDetailVO oldFormDetailVO = formService.getForm(form.getId());
            List<FormProperty> newFormProperties = new ArrayList<>();
            newFormProperties.addAll(formDetailVO.getFormProperties());
            List<FormProperty> oldFormProperties = oldFormDetailVO.getFormProperties();
            List<FormProperty> newProps = new ArrayList<>();
            for (FormProperty formProperty : newFormProperties) {
                if (StringUtils.isEmpty(formProperty.getId()))
                    newProps.add(formProperty);
            }
            newFormProperties.removeAll(newProps);
            if (newProps.size() == 0 && newFormProperties.size() == oldFormProperties.size()) return;
            String newStr = "";
            if (newProps.size() > 0) {
                newStr = "新增";
                for (FormProperty property : newProps) {
                    newStr += "\"" + property.getName() + "\"，";
                }
                newStr = newStr.substring(0, newStr.length() - 1) + "字段";
            }

            FormLog formLog = new FormLog();
            String delStr = "";
            oldFormProperties.removeAll(newFormProperties);
            formPropertyRepository.delete(oldFormProperties);
            delFormVals(form.getId(), oldFormProperties);
            delStr = "刪除";
            for (FormProperty formProperty : oldFormProperties) {
                delStr += "\"" + formProperty.getName() + "\"，";
            }
            delStr = delStr.length()==2?"":delStr.substring(0, delStr.length() - 1) + "字段";
            formLog.setForm(form.getId());
            formLog.setUser(SecurityUtil.getUsername());
            formLog.setOperate(newStr + delStr + "，影响" + formValueRepository.findCountByForm(form.getId()) + "行数据");
            formLog.setOptTime(new Date());
            formLogRepository.save(formLog);
        }
    }

    private void delFormVals(String formId, List<FormProperty> need2DelProps) {
        int index = 0;
        Page<FormValue> formValPages;
        Map<String, String> vals;
        do {
            formValPages = formValueRepository.findAllByForm(formId, new PageRequest(index, 50));
            for (FormValue formValue : formValPages.getContent()) {
                vals = (Map<String, String>) JSON.parse(formValue.getValue());
                for (FormProperty formProperty : need2DelProps) {
                    vals.remove(formProperty.getId());
                }
                formValue.setValue(JSON.toJSONString(vals));
                formValueRepository.save(formValue);
            }
            index++;
        } while (formValPages.hasNext());
    }
}
