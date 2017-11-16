package com.stouduo.dcw.util;


import com.stouduo.dcw.domain.Form;
import com.stouduo.dcw.domain.FormValue;
import com.stouduo.dcw.repository.FormRepository;
import com.stouduo.dcw.repository.FormValueRepository;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

@Component
@Aspect
public class SubmitLimitAspect {

    @Autowired
    private FormValueRepository formValueRepository;
    @Autowired
    private FormRepository formRepository;

    @Pointcut("execution(public com.stouduo.dcw.util.RestResult com.stouduo.dcw.controller.FormValueController.submit(com.stouduo.dcw.domain.FormValue))")
    public void submit() {
    }

    @Around("submit()")
    public Object valid(ProceedingJoinPoint pjp) throws Throwable {
        Object[] args = pjp.getArgs();
        FormValue formValue = (FormValue) pjp.getArgs()[0];
        Form form = formRepository.findOne(formValue.getForm());
        String username = SecurityUtil.getUsername();
        long submitCount = username.equals("anonymousUser")?formValueRepository.findSubmitCount(ControllerUtil.getIpAddress()):formValueRepository.findSubmitCount(username, ControllerUtil.getIpAddress());
        if (form.getCollectFlag() && form.getSubmitCountLimited() >= submitCount) {
            return pjp.proceed();
        } else {
            return new RestResult<>().setCode(0).setMsg(form.getCollectFlag() ? "对不起！一个用户只能提交" + form.getSubmitCountLimited() + "次！" : "对不起！表单已停止收集数据");
        }
    }

}
