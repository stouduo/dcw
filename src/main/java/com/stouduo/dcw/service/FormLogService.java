package com.stouduo.dcw.service;

import com.stouduo.dcw.dto.IFormLogDTO;
import org.springframework.data.domain.Page;

public interface FormLogService {
    Page<IFormLogDTO> myFormLogs(int curPage, int pageSize);
}
