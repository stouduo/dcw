package com.stouduo.dcw.service;

import com.stouduo.dcw.domain.FormValue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface FormValueService {
    void save(FormValue formValue);

    void delete(String formValueId);


    Page<FormValue> formDatas(String formId, String content, int asc, PageRequest pageRequest);
}
