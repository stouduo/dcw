package com.stouduo.dcw.service.impl;

import com.stouduo.dcw.domain.Const;
import com.stouduo.dcw.domain.Form;
import com.stouduo.dcw.domain.FormProperty;
import com.stouduo.dcw.repository.FormPropertyRepository;
import com.stouduo.dcw.repository.FormRepository;
import com.stouduo.dcw.repository.FormValueRepository;
import com.stouduo.dcw.service.FormService;
import com.stouduo.dcw.util.SecurityUtil;
import com.stouduo.dcw.vo.FormDetailVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class FormServiceImpl implements FormService {
    @Autowired
    private FormRepository formRepository;
    @Autowired
    private FormPropertyRepository formPropertyRepository;
    @Autowired
    private FormValueRepository formValueRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Form> getAllForms(String username) {
        return formRepository.findAllByAuthor(username);
    }

    @Override
    @Transactional(readOnly = true)
    public FormDetailVO formDetail(String formId) {
        FormDetailVO formDetailVO = new FormDetailVO();
        formDetailVO.setForm(formRepository.findOne(formId));
        formDetailVO.setFormProperties(formPropertyRepository.findAllByForm(formId));
        formDetailVO.setFormValueCount(formValueRepository.findFormValueCount(formId));
        formDetailVO.setFormValues(formValueRepository.findAllByForm(formId));
        formDetailVO.setTodaySubmitCount(formValueRepository.findTodayFormValueCount(formId));
        return formDetailVO;
    }


    @Override
    public void editDetail(Form form) {
        Form temp = formRepository.findOne(form.getId());
        temp.setCollectFlag(form.getCollectFlag());
        temp.setLabels(form.getLabels());
        temp.setSubmitPrivilege(form.getSubmitPrivilege());
        temp.setResultShow(form.getResultShow());
        temp.setSubmitCountLimited(form.getSubmitCountLimited());
        temp.setLastModifyTime(new Date());
        formRepository.save(temp);
    }

    @Override
    public FormDetailVO getForm(String formId) {
        Form form = formRepository.findOne(formId);
        if (form == null) return null;
        FormDetailVO formDetailVO = new FormDetailVO();
        formDetailVO.setForm(form);
        formDetailVO.setFormProperties(formPropertyRepository.findAllByForm(formId));
        return formDetailVO;
    }

    @Override
    public void edit(FormDetailVO formDetailVO) {
        Form form = formDetailVO.getForm();
        Date now = new Date();
        List<FormProperty> formProperties = formDetailVO.getFormProperties();
        if (form == null || StringUtils.isEmpty(form.getId())) {
            form = new Form();
            form.setSubmitCountLimited(1);
            form.setSubmitPrivilege(Const.PEOPLE_OF_ALL);
            form.setCollectFlag(true);
            form.setResultShow(Const.SHOW_NONE);
            form.setAuthor(SecurityUtil.getUsername());
            form.setCreateTime(now);
            form = formRepository.save(form);
            for (FormProperty formProperty : formProperties) {
                formProperty.setForm(form.getId());
                formProperty.setReultShow(true);
            }
        } else {
            form = formRepository.findOne(form.getId());
            form.setLastModifyTime(now);
            formRepository.save(form);
        }
        formPropertyRepository.save(formProperties);
    }

}
