package com.stouduo.dcw.vo;

import java.util.List;
import java.util.Map;

/**
 * Created by ChenRui on 2017/10/16.
 */
public class FormValueRestVO {
    private long count;
    private String msg;
    private int code;
    private List<Map<String, String>> data;

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<Map<String, String>> getData() {
        return data;
    }

    public void setData(List<Map<String, String>> data) {
        this.data = data;
    }
}
