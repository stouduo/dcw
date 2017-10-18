package com.stouduo.dcw.controller;

import com.stouduo.dcw.domain.User;
import com.stouduo.dcw.service.MailRecordService;
import com.stouduo.dcw.service.SMSService;
import com.stouduo.dcw.service.UserService;
import com.stouduo.dcw.util.RestResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Date;
import java.util.GregorianCalendar;

@Controller
@RequestMapping("/user")
public class UserController extends BaseController {
    @Autowired
    private UserService userService;
    @Autowired
    private MailRecordService mailRecordService;
    @Autowired
    protected AuthenticationManager authenticationManager;
    @Autowired
    private SMSService smsService;
    @Autowired
    private UserDetailsService userDetailsService;

    @PostMapping("/signup/info")
    public String getEmail(HttpSession session, String tel, String email, Model model) {
        try {
            if (StringUtils.isEmpty(email) && StringUtils.isEmpty(tel))
                return error("发送失败！", model);
            if (!StringUtils.isEmpty(email)) {
                mailRecordService.sendEmail(email);
                session.setAttribute("email", email);
            }
            if (!StringUtils.isEmpty(tel)) {
                Object[] codeInfo = smsService.sendSms(tel);
                int code = (int) codeInfo[0];
                if (code == -1) return error("发送失败！", model);
                session.setAttribute("code", codeInfo);
                session.setAttribute("tel", tel);
            }
            return "verifyEmail";
        } catch (Exception e) {
            e.printStackTrace();
            return error("发送失败！", model);
        }
    }

    @GetMapping("/signup/validInfo")
    @ResponseBody
    public RestResult<User> validInfo(String tel, String email) {
        return userService.validInfo(StringUtils.isEmpty(tel) ? email : tel) != null ? restError("电话号码或邮箱已被占用") : restSuccess("");
    }

    @GetMapping("/signup/reSend")
    @ResponseBody
    public RestResult<User> reSend(HttpSession session, String tel, String email) {
        try {
            if (!StringUtils.isEmpty(email)) {
                mailRecordService.sendEmail(email);
            }
            if (!StringUtils.isEmpty(tel)) {
                Object[] codeInfo = smsService.sendSms(tel);
                int code = (int) codeInfo[0];
                if (code == -1) return restError("发送失败！");
                session.setAttribute("code", codeInfo);
            }
        } catch (Exception e) {
            return restError("发送失败！");
        }
        return restSuccess("发送成功");
    }

    @GetMapping("/signup/verify")
    public String verify(Model model, String token, String code, HttpSession session) {
        if (!StringUtils.isEmpty(code)) {
            Object[] codeInfo = (Object[]) session.getAttribute("code");
            if (codeInfo != null && codeInfo[0].equals(code)) {
                GregorianCalendar gc = new GregorianCalendar();
                gc.setTime(new Date());
                gc.add(GregorianCalendar.MINUTE, -5);
                if (gc.getTime().before((Date) codeInfo[1]))
                    return "redirect:/tosignup";
            }
        }
        if (!StringUtils.isEmpty(token) && mailRecordService.verify(token)) {
            return "redirect:/tosignup";
        }
        return error("验证失败", model);
    }


    @PostMapping("/signup")
    public String signup(HttpSession session, @Valid User user, BindingResult bindingResult, HttpServletRequest request, Model model) {
        if (bindingResult.hasErrors()) return "/signup";
        String email = (String) session.getAttribute("email");
        String tel = (String) session.getAttribute("tel");
        if (StringUtils.isEmpty(email) && StringUtils.isEmpty(tel)) {
            return error("您还没有验证您的邮箱或手机号", model);
        }
//        else {
//            session.removeAttribute("email");
//            session.removeAttribute("tel");
//        }
        user.setEmail(email);
        user.setTel(tel);
        String accessUsername = StringUtils.isEmpty(email) ? tel : email;
        if (!userService.save(user)) {
            model.addAttribute("error", true);
            return "/signup";
        }
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(accessUsername, user.getPassword());
        token.setDetails(userDetailsService.loadUserByUsername(accessUsername));
        Authentication authenticatedUser = authenticationManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(authenticatedUser);
        request.getSession().setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
        return "redirect:/form/desktop";
    }

    @GetMapping("/userInfo")
    public ModelAndView userInfo() {
        return new ModelAndView("pages/userInfo").addObject("user", userService.userInfo());
    }


    @PostMapping("/loginSuccess")
    public String toDesktop() {
        return "redirect:/form/desktop";
    }

    @PostMapping("/bindInfo")
    public String bindInfo(User user, HttpSession session, Model model) {
        try {
            session.setAttribute("code", userService.bindInfo(user));
            if (!StringUtils.isEmpty(user.getEmail())) {
                session.setAttribute("email", user.getEmail());
            }
            if (!StringUtils.isEmpty(user.getTel())) {
                session.setAttribute("tel", user.getTel());
            }
        } catch (Exception e) {
            return error("发送失败！", model);
        }
        return "/user/userInfo";
    }

    @GetMapping("/active")
    public RestResult<User> active(String code, HttpSession session) {
        if (!code.equals(session.getAttribute("code"))) {
            return restError("激活失败");
        } else {
            String email = (String) session.getAttribute("email");
            String tel = (String) session.getAttribute("tel");
            User user = userService.userInfo();
            if (!StringUtils.isEmpty(email)) {
                user.setEmail(email);
                session.removeAttribute("email");
            }
            if (!StringUtils.isEmpty(tel)) {
                user.setTel(tel);
                session.removeAttribute("tel");
            }
            userService.save(user);
            return restSuccess("修改成功");
        }
    }


    @PostMapping("/editUser")
    public String editUser(User user, String oldPwd, String confirmPwd) {
        userService.editUser(user, oldPwd, confirmPwd);
        return "redirect:/user/userInfo";
    }


}
