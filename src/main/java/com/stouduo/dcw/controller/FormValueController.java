package com.stouduo.dcw.controller;

import com.stouduo.dcw.domain.FormValue;
import com.stouduo.dcw.service.FormValueService;
import com.stouduo.dcw.util.ExcelException;
import com.stouduo.dcw.util.RestResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;

@Controller("/formValue")
public class FormValueController {
    @Autowired
    FormValueService formValueService;
    @Value("${file.suffix:.xls(x)?}")
    private String fileSuffix;
    @Value("${file.suffix.error:请上传.xls或.xlsx的文件}")
    private String suffixErrorMsg;

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

        formValueService.save(formValue);
        return "submitMsg";
    }

    @GetMapping("/formDatas")
    @ResponseBody
    public RestResult<Page<FormValue>> formDatas(String content, String formId, int asc, int pageSize, int curPage) {
        return new RestResult<>().setData(formValueService.formDatas(formId, content, asc, new PageRequest(curPage, pageSize)));
    }

    @GetMapping("/outport")
    @ResponseBody
    public RestResult<Page<FormValue>> outport(String content, String formId, int asc, int pageSize, int curPage) {
        try {
            formValueService.outport(formId, content, asc, pageSize, curPage);
            return new RestResult<>().setCode(1).setMsg("导出成功");
        } catch (ExcelException e) {
            return new RestResult<>().setCode(0).setMsg(e.getMessage());
        }
    }

    @PostMapping("/import")
    @ResponseBody
    public RestResult<Page<FormValue>> importExcel(@RequestParam("excel") MultipartFile file, String formId) {
        try {
            String suffix = file.getOriginalFilename().split("\\.")[1];
            if (!suffix.matches(fileSuffix))
                return new RestResult<>().setCode(0).setMsg(suffixErrorMsg);
            String filePath = "/tempFiles/" + new SimpleDateFormat("yyMMddHHmmss") + "_" + file.getOriginalFilename();
            file.transferTo(ResourceUtils.getFile("classpath:" + filePath));
            formValueService.importExcel(filePath, formId);
        } catch (Exception e) {
            return new RestResult<>().setCode(0).setMsg(e.getMessage());
        }
        return new RestResult<>().setCode(1).setMsg("导入成功");
    }

}
