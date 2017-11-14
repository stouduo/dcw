package com.stouduo.dcw.util;

import com.stouduo.dcw.domain.Const;
import com.stouduo.dcw.domain.Form;
import com.stouduo.dcw.repository.FormRepository;
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
public class ViewPrivilegeAspect {

    @Autowired
    private FormRepository formRepository;

    @Pointcut("execution(public String com.stouduo.dcw.controller.FormController.view*(..))")
    public void viewPrivilege() {
    }

    @Around("viewPrivilege()")
    public Object valid(ProceedingJoinPoint pjp) throws Throwable {
        String methodName = pjp.getSignature().getName();
        Object[] args = pjp.getArgs();
        Form form = formRepository.findOne((String) args[0]);
        Model model = (Model) args[1];
        if (form.getAuthor().equals(SecurityUtil.getUsername()))
            return pjp.proceed();
        if (methodName.equals("viewForm")) {
            if (!form.getCollectFlag()) {
                model.addAttribute("error", "对不起，保单已停止收集数据！");
                return "msg";
            }
            if (form.getSubmitPrivilege().equals(Const.PEOPLE_OF_ALL)) {
                return pjp.proceed();
            } else {
                model.addAttribute("error", "对不起，该表单不公开！");
                return "msg";
            }
        } else {
            if (form.getResultShow().equals(Const.PEOPLE_OF_ALL)) {
                return pjp.proceed();
            } else {
                model.addAttribute("error", "对不起，该结果不公开！");
                return "msg";
            }
        }
    }
}
