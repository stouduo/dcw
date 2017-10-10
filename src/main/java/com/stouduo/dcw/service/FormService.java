package com.stouduo.dcw.service;

import com.stouduo.dcw.domain.Form;
import com.stouduo.dcw.vo.FormDetailVO;

import java.util.List;

public interface FormService {
    List<Form> getAllForms(String username);

    FormDetailVO formDetail(String formId);


    void editDetail(Form form);

    FormDetailVO getForm(String formId);

    void edit(FormDetailVO formDetailVO);

}
