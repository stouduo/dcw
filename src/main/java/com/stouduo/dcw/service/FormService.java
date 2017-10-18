package com.stouduo.dcw.service;

import com.stouduo.dcw.domain.Form;
import com.stouduo.dcw.vo.DesktopVO;
import com.stouduo.dcw.vo.FormDetailVO;
import com.stouduo.dcw.vo.FormVO;
import com.stouduo.dcw.vo.ResultVO;

public interface FormService {

    DesktopVO getAllForms(String username, int curPage, int pageSize);

    FormDetailVO formDetail(String formId);


    void editDetail(Form form);

    FormDetailVO getForm(String formId);

    void edit(FormDetailVO formDetailVO);

    ResultVO getResult(String formId, int curPage, int pageSize) throws Exception;

    void editForm(Form form) throws IllegalAccessException, Exception;

    FormVO myForms(String username, int curPage, int pageSize);

    void delForm(String id, String type);
}
