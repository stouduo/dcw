package com.stouduo.dcw.util;

public class RestResult<T> {
    private int code;
    private String msg;
    private T data;


    public String getMsg() {
        return msg;
    }

    public RestResult setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public T getData() {
        return data;
    }

    public RestResult setData(T data) {
        this.data = data;
        return this;
    }

    public int getCode() {
        return code;
    }

    public RestResult setCode(int code) {
        this.code = code;
        return this;
    }
}
