package com.stouduo.dcw.controller;

import com.stouduo.dcw.util.RestResult;
import org.springframework.ui.Model;

public class BaseController {

    protected String success(String msg, Model model) {
        return sendMsg("success", msg, model);
    }

    protected String error(String msg, Model model) {
        return sendMsg("error", msg, model);
    }

    protected String sendMsg(String name, String msg, Model model) {
        model.addAttribute(name, msg);
        return "msg";
    }

    protected RestResult restSuccess(String msg) {
        return sendRestMsg(1, msg);
    }

    protected RestResult restError(String msg) {
        return sendRestMsg(0, msg);
    }

    protected RestResult sendRestMsg(int code, String msg) {
        return new RestResult().setCode(code).setMsg(msg);
    }
}
