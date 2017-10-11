package com.stouduo.dcw.service.impl;

import com.stouduo.dcw.domain.Form;
import com.stouduo.dcw.domain.FormValue;
import com.stouduo.dcw.repository.FormPropertyRepository;
import com.stouduo.dcw.repository.FormRepository;
import com.stouduo.dcw.repository.FormValueRepository;
import com.stouduo.dcw.service.FormValueService;
import com.stouduo.dcw.util.ControllerUtil;
import com.stouduo.dcw.util.ExcelException;
import com.stouduo.dcw.util.ExcelUtil;
import com.stouduo.dcw.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;
import org.thymeleaf.util.StringUtils;

import java.io.File;
import java.util.Date;
import java.util.List;

import static com.stouduo.dcw.util.ExcelUtil.excelToList;

@Service
@Transactional
public class FormValueServiceImpl implements FormValueService {
    @Autowired
    private FormValueRepository formValueRepository;
    @Autowired
    private FormRepository formRepository;
    @Autowired
    private FormPropertyRepository formPropertyRepository;

    @Override
    public void save(FormValue formValue) {
        Date now = new Date();
        formValue.setLastModifyTime(now);
        if (StringUtils.isEmpty(formValue.getId())) {
            formValue.setCreateTime(now);
            formValue.setAuthor(SecurityUtil.getUsername());
        }
        String[] clientMsg = ControllerUtil.getUserAgent();
        formValue.setBrowser(clientMsg[0]);
        formValue.setOs(clientMsg[1]);
        formValue.setSubmitIP(ControllerUtil.getIpAddress());
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

    @Override
    public void outport(String formId, String content, int asc, int pageSize, int curPage) throws ExcelException {
        Form form = formRepository.findOne(formId);
        List<FormValue> formValues;
        if (pageSize != 0 || curPage != 0) {
            formValues = formValueRepository.findByContent(formId, content, new Sort(asc == 1 ? Sort.Direction.ASC : Sort.Direction.DESC, "createtime"), new PageRequest(curPage, pageSize)).getContent();
        } else {
            formValues = formValueRepository.findByContent(formId, content, new Sort(asc == 1 ? Sort.Direction.ASC : Sort.Direction.DESC, "createtime"));
        }
        List<String> fieldNames = formPropertyRepository.findByForm(formId);
        fieldNames.add("表单创建人");
        fieldNames.add("表单最后修改人");
        fieldNames.add("表单创建时间");
        fieldNames.add("表单最后修改时间");
        fieldNames.add("浏览器");
        fieldNames.add("操作系统");
        fieldNames.add("操作");
        ExcelUtil.listToExcel(formValues, "Sheet1", fieldNames, form.getTitle());
    }

    @Override
    public void importExcel(String filePath, String formId) throws Exception {
        List<FormValue> results = ExcelUtil.excelToList(filePath, "Sheet1", formPropertyRepository.findByForm(formId), null, formId);
        File file = ResourceUtils.getFile(filePath);
        if (file.exists()) file.delete();
        formValueRepository.save(results);
    }
}
