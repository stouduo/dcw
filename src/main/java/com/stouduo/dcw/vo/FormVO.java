package com.stouduo.dcw.vo;

import java.util.List;
import java.util.Map;

/**
 * Created by ChenRui on 2017/10/15.
 */
public class FormVO {
    private int totalPages;
    private List<Map<String, Object>> formList;

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public List<Map<String, Object>> getFormList() {
        return formList;
    }

    public void setFormList(List<Map<String, Object>> formList) {
        this.formList = formList;
    }
}
