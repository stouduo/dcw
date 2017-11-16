package com.stouduo.dcw.service.impl;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.stouduo.dcw.events.SMSEvent;
import com.stouduo.dcw.events.SMSEventObj;
import com.stouduo.dcw.service.SMSService;
import com.stouduo.dcw.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * Created by ChenRui on 2017/10/14.
 */
@Service
public class SMSServiceImpl implements SMSService {
    //产品名称:云通信短信API产品,开发者无需替换
    static final String product = "Dysmsapi";
    //产品域名,开发者无需替换
    static final String domain = "dysmsapi.aliyuncs.com";

    @Value("${aliyun.sms.akid}")
    private String accessKeyId;
    @Value("${aliyun.sms.aks}")
    private String accessKeySecret;
    @Value("${aliyun.sms.signname}")
    private String smsSignName;
    @Value("${aliyun.sms.tpl}")
    private String smsTpl;

    @Autowired
    private ApplicationContext context;

    @Override
    public Object[] sendSms(String tel) throws Exception {
        int code = -1;
        if (!StringUtils.isEmpty(tel)) {
            code = CommonUtil.getSMSCode();
            SMSEventObj obj = new SMSEventObj();
            obj.setCode(code);
            obj.setTel(tel);
            context.publishEvent(new SMSEvent(obj));
        }
        return new Object[]{code, new Date()};
    }

    @Override
    public int sendSMS(String tel) throws Exception {
        return (int) sendSms(tel)[0];
    }

    @Override
    public void send(int code, String tel) throws Exception {
//可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");

        //初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);

        //组装请求对象-具体描述见控制台-文档部分内容
        SendSmsRequest request = new SendSmsRequest();
        //必填:待发送手机号
        request.setPhoneNumbers(tel);
        //必填:短信签名-可在短信控制台中找到
        request.setSignName(smsSignName);
        //必填:短信模板-可在短信控制台中找到
        request.setTemplateCode(smsTpl);
        //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
        request.setTemplateParam("{\"code\":\"" + code + "\"}");

        //选填-上行短信扩展码(无特殊需求用户请忽略此字段)
        //request.setSmsUpExtendCode("90997");

        //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
//            request.setOutId("yourOutId");

        //hint 此处可能会抛出异常，注意catch
        SendSmsResponse response = acsClient.getAcsResponse(request);
    }
}
