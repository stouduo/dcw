package com.stouduo.dcw.controller;

import com.stouduo.dcw.domain.Form;
import com.stouduo.dcw.service.FormService;
import com.stouduo.dcw.util.Log;
import com.stouduo.dcw.util.RestResult;
import com.stouduo.dcw.util.SecurityUtil;
import com.stouduo.dcw.vo.FormDetailVO;
import com.stouduo.dcw.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/form")
public class FormController {
    @Autowired
    private FormService formService;

    @GetMapping("/desktop")
    public String myforms() {
        new ModelAndView().addObject("forms", formService.getAllForms(SecurityUtil.getUsername()));
        return "desktop";
    }

    @GetMapping("/myform/{id}")
    public String formDetail(@PathVariable("id") String id) {
        new ModelAndView().addObject("formDetail", formService.formDetail(id));
        return "detail";
    }

    @PostMapping("/editDetail/{id}")
    @ResponseBody
    public RestResult<Form> editDetail(Form form) {
        formService.editDetail(form);
        return new RestResult<>().setCode(1).setMsg("编辑成功");
    }

    @PostMapping("/edit/{id}")
    public String toEdit(@PathVariable("id") String formId) {
        if (!"new".equals(formId)) {
            FormDetailVO formDetailVO = formService.getForm(formId);
            if (formDetailVO != null)
                new ModelAndView().addObject("form", formDetailVO);
        }
        return "GetForm";
    }

    @Log("修改表单")
    @PostMapping("/edit")
    public String edit(FormDetailVO formDetailVO) {
        formService.edit(formDetailVO);
        return "/index";
    }

    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") String formId) {
        FormDetailVO formDetailVO = formService.getForm(formId);
        if (formDetailVO == null) return "404";
        new ModelAndView().addObject("form", formDetailVO);
        return "view";
    }

    @GetMapping("/result/{id}")
    public String result(@PathVariable("id") String formId, int curPage, int pageSize) {
        ResultVO resultVO = formService.getResult(formId, curPage, pageSize);
        if (resultVO == null) return "404";
        new ModelAndView().addObject("form", resultVO);
        return "result";
    }
}
