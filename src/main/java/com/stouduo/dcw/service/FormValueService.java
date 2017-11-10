package com.stouduo.dcw.service;

import com.stouduo.dcw.domain.FormValue;
import com.stouduo.dcw.util.ExcelException;
import com.stouduo.dcw.vo.FormDetailVO;
import com.stouduo.dcw.vo.FormValueRestVO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.util.Date;

public interface FormValueService {
    void save(FormValue formValue);

    void delete(String formValueId);


    void outport(HttpServletResponse response, String formId, String content, String startTime, Date endTime, int asc, int pageSize, int curPage) throws Exception;

    FormValueRestVO formDatas(String formId, String content, String startTime, Date endTime, int asc, int pageSize, int curPage) throws Exception;

    void importExcel(MultipartFile file, String formId) throws Exception;

    FormDetailVO myFormData(String formId);
}
