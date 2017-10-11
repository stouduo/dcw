package com.stouduo.dcw.service;

import org.springframework.data.domain.Page;

import java.util.Map;

public interface FormLogService {
    Page<Map<String, String>> myFormLogs(int curPage, int pageSize);
}
