package com.stouduo.dcw.service.impl;

import com.stouduo.dcw.repository.FormLogRepository;
import com.stouduo.dcw.service.FormLogService;
import com.stouduo.dcw.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class FormLogServiceImpl implements FormLogService {
    @Autowired
    private FormLogRepository formLogRepository;

    @Override
    public Page<Map<String, String>> myFormLogs(int curPage, int pageSize) {
        return formLogRepository.findPageByParams("select fl.*,f.title,f.id from formlog fl inner join form f on fl.form=f.id where fl.user=?", new PageRequest(curPage, pageSize, Sort.Direction.DESC, "opttime"), SecurityUtil.getUsername());
    }
}
