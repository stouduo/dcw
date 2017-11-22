package com.stouduo.dcw.service.impl;

import com.alibaba.fastjson.JSON;
import com.stouduo.dcw.domain.Form;
import com.stouduo.dcw.domain.FormProperty;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
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
    @Value("${import.prop.limited:uploadfeild}")
    private String importPropLimited;

    @Override
    public void save(FormValue formValue) {
        Date now = new Date();
        if (StringUtils.isEmpty(formValue.getId())) {
            formValue.setCreateTime(now);
            formValue.setAuthor(SecurityUtil.getUsername());
            String[] clientMsg = ControllerUtil.getUserAgent();
            formValue.setBrowser(clientMsg[0]);
            formValue.setOs(clientMsg[1]);
            formValue.setSubmitIP(ControllerUtil.getIpAddress());
        } else {
            String value = formValue.getValue();
            formValue = formValueRepository.findOne(formValue.getId());
            formValue.setValue(value);
        }
        formValue.setLastModifyTime(now);
        formValue.setLastModifyPerson(SecurityUtil.getUsername());
        formValueRepository.save(formValue);
    }

    @Override
    public void delete(String formValueId) {
        for (String id : formValueId.split(","))
            formValueRepository.delFormValues(id);
    }

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public FormValueRestVO formDatas(String formId, String content, String startTime, Date endTime, int asc, int pageSize, int curPage) throws ParseException {
        Page<FormValue> page = formValueRepository.findByContent(formId, content, sdf.parse(startTime), endTime, new PageRequest(curPage, pageSize, asc == 1 ? Sort.Direction.ASC : Sort.Direction.DESC, "createTime"));
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
            formValueMap.put("id", formValue.getId());
            formValueMap.putAll((Map<String, String>) JSON.parse(formValue.getValue()));
            formValuesMap.add(formValueMap);
        }
        restVO.setCode(0);
        restVO.setCount(page.getTotalElements());
        restVO.setMsg("");
        restVO.setData(formValuesMap);
        return restVO;
    }

    @Override
    public void outport(HttpServletResponse response, String formId, String content, String startTime, Date endTime, int asc, int pageSize, int curPage) throws Exception {
        Form form = formRepository.findOne(formId);
        List<FormValue> formValues;
        if (curPage != -1) {
            formValues = formValueRepository.findByContent(formId, content, sdf.parse(startTime), endTime, new PageRequest(curPage, pageSize, asc == 1 ? Sort.Direction.ASC : Sort.Direction.DESC, "createTime")).getContent();
        } else {
            formValues = formValueRepository.findByContent(formId, content, sdf.parse(startTime), endTime, new Sort(asc == 1 ? Sort.Direction.ASC : Sort.Direction.DESC, "createTime"));
        }
        List<FormProperty> formProperties = formPropertyRepository.findByForm(formId);
        List<String> fieldNames = new ArrayList<>();
        for (FormProperty property : formProperties) {
            fieldNames.add(property.getName());
        }
        fieldNames.add("提交人");
        fieldNames.add("修改人");
        fieldNames.add("提交时间");
        fieldNames.add("修改时间");
        fieldNames.add("浏览器");
        fieldNames.add("操作系统");
        fieldNames.add("IP");
        ExcelUtil.listToExcel(formValues, "Sheet1", fieldNames, formProperties, form.getTitle(), response);
    }

    @Override
    public void importExcel(MultipartFile file, String formId) throws Exception {
//        List<FormValue> results = ExcelUtil.excelToList(file.getInputStream(), "Sheet1", formPropertyRepository.findByForm(formId), null, formId);
        List<FormValue> results = ExcelUtil.excel2List(file.getInputStream(), "Sheet1", formPropertyRepository.findByForm(formId), null, formId, importPropLimited);
        formValueRepository.save(results);
    }

    @Override
    public FormDetailVO myFormData(String formId) {
        FormDetailVO formDetailVO = new FormDetailVO();
        formDetailVO.setForm(formRepository.findOne(formId));
        formDetailVO.setFormProperties(formPropertyRepository.findAllByForm(formId));
        return formDetailVO;
    }

    @Override
    public void outportOne(HttpServletResponse response, String id,String formId) throws ExcelException {
        Form form = formRepository.findOne(formId);
        List<FormValue> formValues = formValueRepository.findById(id);
        List<FormProperty> formProperties = formPropertyRepository.findByForm(formId);
        List<String> fieldNames = new ArrayList<>();
        for (FormProperty property : formProperties) {
            fieldNames.add(property.getName());
        }
        fieldNames.add("提交人");
        fieldNames.add("修改人");
        fieldNames.add("提交时间");
        fieldNames.add("修改时间");
        fieldNames.add("浏览器");
        fieldNames.add("操作系统");
        fieldNames.add("IP");
        ExcelUtil.listToExcel(formValues, "Sheet1", fieldNames, formProperties, form.getTitle(), response);
    }
}
