package com.stouduo.dcw.controller;

import com.stouduo.dcw.domain.FormValue;
import com.stouduo.dcw.service.FormValueService;
import com.stouduo.dcw.util.ControllerUtil;
import com.stouduo.dcw.util.RestResult;
import com.stouduo.dcw.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller("/formValue")
public class FormValueController {
    @Autowired
    FormValueService formValueService;

    @GetMapping("/del/{id}")
    @ResponseBody
    public RestResult<FormValue> delFormValue(@PathVariable("id") String formValueId) {
        formValueService.delete(formValueId);
        return new RestResult<FormValue>().setCode(1).setMsg("删除成功");
    }

    @PostMapping("/save")
    @ResponseBody
    public RestResult<FormValue> saveFormValue(FormValue formValue) {
        formValueService.save(formValue);
        return new RestResult<FormValue>().setCode(1).setMsg("编辑成功");
    }

    @PostMapping("/submit")
    public String submit(FormValue formValue, HttpServletRequest request) {
        String[] clientMsg = ControllerUtil.getUserAgent(request);
        formValue.setBrowser(clientMsg[0]);
        formValue.setOs(clientMsg[1]);
        formValue.setSubmitIP(ControllerUtil.getIpAddress(request));
        formValueService.save(formValue);
        return "submitMsg";
    }

    @GetMapping("/formDatas")
    public RestResult<Page<FormValue>> formDatas(String content, String formId, int asc, int pageSize, int curPage) {
        return new RestResult<>().setData(formValueService.formDatas(formId, content, asc, new PageRequest(curPage, pageSize)));
    }


}
