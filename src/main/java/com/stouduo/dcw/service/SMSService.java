package com.stouduo.dcw.service;

import com.aliyuncs.exceptions.ClientException;

/**
 * Created by ChenRui on 2017/10/14.
 */
public interface SMSService {
    Object[] sendSms(String tel) throws ClientException, Exception;

    int sendSMS(String tel) throws Exception;
}
