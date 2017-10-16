package com.stouduo.dcw.service.impl;

import com.alibaba.fastjson.JSON;
import com.stouduo.dcw.domain.Const;
import com.stouduo.dcw.domain.Form;
import com.stouduo.dcw.domain.FormProperty;
import com.stouduo.dcw.domain.FormValue;
import com.stouduo.dcw.repository.FormPropertyRepository;
import com.stouduo.dcw.repository.FormRepository;
import com.stouduo.dcw.repository.FormValueRepository;
import com.stouduo.dcw.service.FormService;
import com.stouduo.dcw.util.SecurityUtil;
import com.stouduo.dcw.vo.DesktopVO;
import com.stouduo.dcw.vo.FormDetailVO;
import com.stouduo.dcw.vo.FormVO;
import com.stouduo.dcw.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.*;

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
    public DesktopVO getAllForms(String username, int curPage, int pageSize) {
        DesktopVO desktopVO = new DesktopVO();
        desktopVO.setTotalFormCount(formRepository.findFormCountByAuthor(username));
        desktopVO.setLabelNullCount(formRepository.findByLabelIsNull(username));
        return desktopVO;
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
        if (form.getAuthor() != SecurityUtil.getUsername())
            formRepository.updateViewCount(formId);
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
            form.setSubmitPrivilege(Const.PEOPLE_OF_NONE);
            form.setCollectFlag(true);
            form.setResultShow(Const.PEOPLE_OF_NONE);
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

    @Override
    public ResultVO getResult(String formId, int curPage, int pageSize) {
        Form form = formRepository.findOne(formId);
        if (form == null) return null;
        ResultVO resultVO = new ResultVO();
        List<FormProperty> formProperties = formPropertyRepository.findAllByForm(formId);
        String showProperties = "";
        for (FormProperty formproperty : formProperties) {
            if (formproperty.getReultShow()) showProperties += formproperty.getName() + ",";
        }
        Page<FormValue> page = formValueRepository.findByContent(formId, "", new PageRequest(curPage, pageSize, Sort.Direction.DESC, "creattime"));
        List<FormValue> formValues = page.getContent();
        Map<String, String> value;
        if (!StringUtils.isEmpty(showProperties)) {
            showProperties = showProperties.substring(0, showProperties.length() - 1);
            for (FormValue formValue : formValues) {
                value = (Map<String, String>) JSON.parse(formValue.getValue());
                Iterator<Map.Entry<String, String>> iterator = value.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<String, String> entry = iterator.next();
                    if (showProperties.contains(entry.getKey())) {
                        iterator.remove();
                    }
                }
                formValue.setValue(JSON.toJSONString(value));
            }
            page = new PageImpl<FormValue>(formValues, new PageRequest(curPage, pageSize), page.getTotalElements());
        }
        formRepository.updateResultViewCount(formId);
        resultVO.setForm(form);
        resultVO.setFormValues(page);
        return resultVO;
    }

    @Override
    public void editForm(Form form) throws Exception {
        Form temp = formRepository.findOne(form.getId());
        Field[] fields = Form.class.getDeclaredFields();
        Object value;
        for (Field field : fields) {
            field.setAccessible(true);
            value = field.get(form);
            if (!ObjectUtils.isEmpty(value)) {
                field.set(temp, value);
            }
        }
        formRepository.save(temp);
    }

    @Override
    public FormVO myForms(String username, int curPage, int pageSize) {
        FormVO formVO = new FormVO();
        Page<Form> page = formRepository.findAllByAuthorAndDel(username, false, new PageRequest(curPage - 1, pageSize));
        List<Form> forms = page.getContent();
        List<Map<String, Object>> formList = new ArrayList<>();
        Map<String, Object> formMap;
        for (Form form : forms) {
            formMap = new HashMap<>();
            formMap.put("formValueCount", formValueRepository.findFormValueCount(form.getId()));
            formMap.put("form", form);
            formList.add(formMap);
        }
        formVO.setFormList(formList);
        formVO.setTotalPages(page.getTotalPages());
        return formVO;
    }

    @Override
    public void delForm(String id, String type) {
        if (type.equals("delFormValues")) {
            formValueRepository.delFormValues(id);
        } else {
            formRepository.delForm(id);
        }
    }

}
