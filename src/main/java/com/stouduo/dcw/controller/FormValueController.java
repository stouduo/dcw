package com.stouduo.dcw.controller;

import com.stouduo.dcw.domain.FormValue;
import com.stouduo.dcw.service.FormValueService;
import com.stouduo.dcw.util.ExcelException;
import com.stouduo.dcw.util.RestResult;
import com.stouduo.dcw.vo.FormValueRestVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@RequestMapping("/formValue")
public class FormValueController extends BaseController {
    @Autowired
    FormValueService formValueService;
    @Value("${file.suffix:xls(x)?}")
    private String fileSuffix;
    @Value("${file.suffix.error:请上传.xls或.xlsx的文件}")
    private String suffixErrorMsg;

    @GetMapping("/del")
    @ResponseBody
    public RestResult<FormValue> delFormValue(String formValueIds) {
        formValueService.delete(formValueIds);
        return restSuccess("删除成功");
    }

    @PostMapping("/save")
    @ResponseBody
    public RestResult<FormValue> saveFormValue(FormValue formValue) {
        formValueService.save(formValue);
        return restSuccess("编辑成功");
    }

    @PostMapping("/submit")
    public String submit(FormValue formValue, Model model) {

        formValueService.save(formValue);
        return success("感谢您的提交！", model);
    }

    @GetMapping("/formDatas")
    @ResponseBody
    public FormValueRestVO formDatas(@RequestParam(defaultValue = "") String content, String formId, @RequestParam(defaultValue = "1970-01-01 00:00:00") String startTime, String endTime, @RequestParam(defaultValue = "0") int asc, @RequestParam(defaultValue = "15") int pageSize, @RequestParam(defaultValue = "1") int curPage) {
        return formValueService.formDatas(formId, content, startTime, StringUtils.isEmpty(endTime) ? new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) : endTime, asc, pageSize, curPage);
    }

    @GetMapping("/myFormData/{id}")
    public String myFormData(@PathVariable("id") String formId, Model model) {
        model.addAttribute("formDetail", formValueService.myFormData(formId));
        return "pages/formEditor";
    }

    @GetMapping("/outport")
    @ResponseBody
    public RestResult<Page<FormValue>> outport(@RequestParam(defaultValue = "") String content, String formId, @RequestParam(defaultValue = "1970-01-01 00:00:00") String startTime, String endTime, @RequestParam(defaultValue = "0") int asc, @RequestParam(defaultValue = "0") int pageSize, @RequestParam(defaultValue = "0") int curPage) {
        try {
            formValueService.outport(formId, content, startTime, StringUtils.isEmpty(endTime) ? new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) : endTime, asc, pageSize, curPage);
            return restSuccess("导出成功");
        } catch (ExcelException e) {
            return restError("导出失败");
        }
    }

    @PostMapping("/import")
    @ResponseBody
    public RestResult<Page<FormValue>> importExcel(@RequestParam("excel") MultipartFile file, String formId) {
        try {
            String suffix = file.getOriginalFilename().split("\\.")[1];
            if (!suffix.matches(fileSuffix))
                return restError(suffixErrorMsg);
            formValueService.importExcel(file, formId);
        } catch (Exception e) {
            return restError("导入失败");
        }
        return restSuccess("导入成功");
    }

}
