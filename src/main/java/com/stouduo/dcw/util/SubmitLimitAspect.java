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
import org.springframework.web.servlet.ModelAndView;

@Component
@Aspect
public class SubmitLimitAspect {

    @Autowired
    private FormValueRepository formValueRepository;
    @Autowired
    private FormRepository formRepository;

    @Pointcut("execution(* com.stouduo.dcw.controller.FormValueController.submit(com.stouduo.dcw.domain.FormValue))")
    public void submit() {
    }

    @Around("submit()")
    public Object valid(ProceedingJoinPoint pjp) throws Throwable {
        Object[] args = pjp.getArgs();
        Model model = (Model) args[1];
        FormValue formValue = (FormValue) pjp.getArgs()[0];
        Form form = formRepository.findOne(formValue.getForm());
        long submitCount = formValueRepository.findSubmitCount(SecurityUtil.getUsername(), ControllerUtil.getIpAddress());
        if (form.getSubmitCountLimited() >= submitCount) {
            return pjp.proceed();
        } else {
            model.addAttribute("error", "对不起一个用户只能提交" + form.getSubmitCountLimited() + "次！");
            return "msg";
        }
    }

}
