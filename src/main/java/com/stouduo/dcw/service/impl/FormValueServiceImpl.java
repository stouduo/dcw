package com.stouduo.dcw.service.impl;

import com.alibaba.fastjson.JSON;
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
import com.stouduo.dcw.vo.FormDetailVO;
import com.stouduo.dcw.vo.FormValueRestVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;

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
        for (String id : formValueId.split(","))
            formValueRepository.delFormValues(id);
    }

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public FormValueRestVO formDatas(String formId, String content, String startTime, String endTime, int asc, int pageSize, int curPage) {
        Page<FormValue> page = formValueRepository.findByContent(formId, content, startTime, endTime, new PageRequest(curPage, pageSize, asc == 1 ? Sort.Direction.ASC : Sort.Direction.DESC, "createtime"));
        FormValueRestVO restVO = new FormValueRestVO();
        List<FormValue> formValues = page.getContent();
        List<Map<String, String>> formValuesMap = new ArrayList<>();
        Map<String, String> formValueMap;
        for (FormValue formValue : formValues) {
            formValueMap = new HashMap<>();
            formValueMap.put("author", formValue.getAuthor());
            formValueMap.put("createTime", sdf.format(formValue.getCreateTime()));
            formValueMap.put("lastModifyTime", sdf.format(formValue.getLastModifyTime()));
            formValueMap.put("lastModifyPerson", formValue.getLastModifyPerson());
            formValueMap.put("browser", formValue.getBrowser());
            formValueMap.put("os", formValue.getOs());
            formValueMap.put("submitIP", formValue.getSubmitIP());
            formValueMap.putAll((Map<String, String>) JSON.parse(formValue.getValue()));
            formValuesMap.add(formValueMap);
        }
        restVO.setCode(200);
        restVO.setCount(page.getTotalElements());
        restVO.setMsg("");
        restVO.setData(formValuesMap);
        return restVO;
    }

    @Override
    public void outport(String formId, String content, String startTime, String endTime, int asc, int pageSize, int curPage) throws ExcelException {
        Form form = formRepository.findOne(formId);
        List<FormValue> formValues;
        if (pageSize != 0 || curPage != 0) {
            formValues = formValueRepository.findByContent(formId, content, startTime, endTime, new PageRequest(curPage, pageSize, asc == 1 ? Sort.Direction.ASC : Sort.Direction.DESC, "createtime")).getContent();
        } else {
            formValues = formValueRepository.findByContent(formId, content, startTime, endTime, new Sort(asc == 1 ? Sort.Direction.ASC : Sort.Direction.DESC, "createTime"));
        }
        List<String> fieldNames = formPropertyRepository.findByForm(formId);
        fieldNames.add("表单创建人");
        fieldNames.add("表单最后修改人");
        fieldNames.add("表单创建时间");
        fieldNames.add("表单最后修改时间");
        fieldNames.add("浏览器");
        fieldNames.add("操作系统");
        fieldNames.add("操作IP");
        ExcelUtil.listToExcel(formValues, "Sheet1", fieldNames, form.getTitle());
    }

    @Override
    public void importExcel(MultipartFile file, String formId) throws Exception {
        List<FormValue> results = ExcelUtil.excelToList(file.getInputStream(), "Sheet1", formPropertyRepository.findByForm(formId), null, formId);
        formValueRepository.save(results);
    }

    @Override
    public FormDetailVO myFormData(String formId) {
        FormDetailVO formDetailVO = new FormDetailVO();
        formDetailVO.setForm(formRepository.findOne(formId));
        formDetailVO.setFormProperties(formPropertyRepository.findAllByForm(formId));
        return formDetailVO;
    }
}
