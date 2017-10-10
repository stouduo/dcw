package com.stouduo.dcw.service.impl;

import com.stouduo.dcw.domain.FormValue;
import com.stouduo.dcw.repository.FormValueRepository;
import com.stouduo.dcw.service.FormValueService;
import com.stouduo.dcw.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.util.Date;

@Service
public class FormValueServiceImpl implements FormValueService {
    @Autowired
    private FormValueRepository formValueRepository;

    @Override
    public void save(FormValue formValue) {
        Date now = new Date();
        formValue.setLastModifyTime(now);
        if (StringUtils.isEmpty(formValue.getId())) {
            formValue.setCreateTime(now);
            formValue.setAuthor(SecurityUtil.getUsername());
        }
        formValue.setLastModifyPerson(SecurityUtil.getUsername());
        formValueRepository.save(formValue);
    }

    @Override
    public void delete(String formValueId) {
        formValueRepository.delete(formValueId);
    }

    public Page<FormValue> formDatas(String content, String formId, int asc, PageRequest page) {
        return formValueRepository.findByContent(formId, content, new Sort(asc == 1 ? Sort.Direction.ASC : Sort.Direction.DESC, "createtime"), page);
    }
}
