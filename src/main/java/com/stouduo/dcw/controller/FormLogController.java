package com.stouduo.dcw.controller;

import com.stouduo.dcw.service.FormLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController("/formlog")
public class FormLogController {
    @Autowired
    private FormLogService formLogService;

    @GetMapping("/myFormLogs")
    public Page<Map<String, String>> myFormLogs(int curPage, int pageSize) {
        return formLogService.myFormLogs(curPage, pageSize);
    }
}
