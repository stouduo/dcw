package com.stouduo.dcw.controller;

import com.stouduo.dcw.dto.IFormLogDTO;
import com.stouduo.dcw.service.FormLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/formlog")
public class FormLogController {
    @Autowired
    private FormLogService formLogService;

    @GetMapping("/myFormLogs")
    public Page<IFormLogDTO> myFormLogs(int curPage, int pageSize) {
        return formLogService.myFormLogs(curPage, pageSize);
    }
}
