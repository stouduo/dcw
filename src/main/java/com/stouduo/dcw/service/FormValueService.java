package com.stouduo.dcw.service;

import com.stouduo.dcw.domain.FormValue;
import com.stouduo.dcw.util.ExcelException;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface FormValueService {
    void save(FormValue formValue);

    void delete(String formValueId);


    Page<FormValue> formDatas(String formId, String content, int asc, int pageSize, int curPage);

    void outport(String formId, String content, int asc, int pageSize, int curPage) throws ExcelException;

    void importExcel(MultipartFile file, String formId) throws Exception;
}
