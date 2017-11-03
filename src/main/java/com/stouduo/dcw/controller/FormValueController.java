package com.stouduo.dcw.controller;

import com.stouduo.dcw.domain.FormValue;
import com.stouduo.dcw.service.FormValueService;
import com.stouduo.dcw.util.ExcelException;
import com.stouduo.dcw.util.RestResult;
import com.stouduo.dcw.vo.FormValueRestVO;
import freemarker.template.utility.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;
import org.thymeleaf.util.DateUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

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
    public RestResult<FormValue> delFormValue(@RequestParam("ids") String formValueIds) {
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
        try {
            return formValueService.formDatas(formId, content, startTime, StringUtils.isEmpty(endTime) ? new Date() : sdf.parse(endTime), asc, pageSize, curPage - 1);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/myFormData/{id}")
    public String myFormData(@PathVariable("id") String formId, Model model, String today) {
        if (!StringUtils.isEmpty(today))
            model.addAttribute("today", 1);
        model.addAttribute("formDetail", formValueService.myFormData(formId));
        return "pages/formValue";
    }

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @GetMapping("/outport")
    public void outport(@RequestParam(defaultValue = "") String content, String formId, @RequestParam(defaultValue = "1970-01-01 00:00:00") String startTime, String endTime, @RequestParam(defaultValue = "0") int asc, @RequestParam(defaultValue = "0") int pageSize, @RequestParam(defaultValue = "0") int curPage) {
        try {
            formValueService.outport(formId, content, startTime, StringUtils.isEmpty(endTime) ? new Date() : sdf.parse(endTime), asc, pageSize, curPage - 1);
        } catch (Exception e) {
            e.printStackTrace();
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

    @PostMapping("/uploadImg")
    @ResponseBody
    public RestResult<Map<String, String>> uploadImg(MultipartFile file, HttpServletRequest request) {
        Map<String, String> img = new HashMap<>();
        String filename, filepath = "/uploadfiles/" + sdf.format(new Date()).substring(0, 10) + "/";
        String baseDir = request.getServletContext().getRealPath("/");
        File uploadDir = new File(baseDir + filepath);
        if (!uploadDir.exists()) uploadDir.mkdirs();
        try {
            filename = file.getOriginalFilename().split("\\.")[0];
            filepath += file.getOriginalFilename();
            file.transferTo(new File(baseDir + filepath));
            img.put("imgName", filename);
            img.put("imgPath", filepath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new RestResult<>().setData(img);
    }

    @GetMapping("/delImg")
    @ResponseBody
    public RestResult<Map<String, String>> delImg(String imgPath, HttpServletRequest request) {
        File file = new File(request.getServletContext().getRealPath("/") + imgPath);
        if (file.exists()) file.delete();
        return restSuccess("删除成功");
    }
}
