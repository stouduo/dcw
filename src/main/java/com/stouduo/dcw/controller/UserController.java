package com.stouduo.dcw.controller;

import com.stouduo.dcw.domain.User;
import com.stouduo.dcw.service.MailRecordService;
import com.stouduo.dcw.service.UserService;
import com.stouduo.dcw.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private MailRecordService mailRecordService;
    @Autowired
    protected AuthenticationManager authenticationManager;

    @PostMapping("/getEmail")
    public String getEmail(HttpSession session, @RequestParam(name = "email") String email) {
        try {
            mailRecordService.sendEmail(email);
            session.setAttribute("email", email);
        } catch (Exception e) {
            e.printStackTrace();
            return "/error";
        }
        return "/verifyEmail";
    }

    @GetMapping("/verify")
    public String verify(@RequestParam(name = "token") String token) {
        if (mailRecordService.verify(token)) {
            new ModelAndView().addObject("error", "验证失败");
            return "/verifyError";
        } else {
            return "/signup";
        }
    }

    @PostMapping("/signup")
    public String signup(HttpSession session, String username, String password, HttpServletRequest request) {
        String email = (String) session.getAttribute("email");
        session.removeAttribute("email");
        User user = new User();
        user.setEmail(email);
        user.setPassword(MD5Util.encode(password));
        user.setUsername(username);
        if (!userService.save(user)) {
            new ModelAndView().addObject("usernameError", "用户已存在");
            return "/signup";
        }
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
        token.setDetails(new WebAuthenticationDetails(request));
        Authentication authenticatedUser = authenticationManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(authenticatedUser);
        request.getSession().setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
        return "/index";
    }

    @GetMapping("/userInfo")
    public String userInfo() {
        new ModelAndView().addObject("user", userService.userInfo());
        return "/userInfo";
    }

    @PostMapping("/bindInfo")
    public String bindInfo(User user, HttpSession session) {
        try {
            if (StringUtils.isEmpty(user.getEmail())) {
                session.setAttribute("email", user.getEmail());
            }
            userService.bindInfo(user);
        } catch (Exception e) {
            return "error";
        }
        return "/userInfo";
    }

    @GetMapping("/active")
    public String active(String token, HttpSession session) {
        if (mailRecordService.verify(token)) {
            new ModelAndView().addObject("error", "激活失败");
            return "/verifyError";
        } else {
            User user = userService.userInfo();
            user.setEmail((String) session.getAttribute("email"));
            session.removeAttribute("email");
            userService.save(user);
            return "/userInfo";
        }
    }

    @PostMapping("/editUser")
    public String editUser(User user, String oldPwd, String confirmPwd) {
        userService.editUser(user, oldPwd, confirmPwd);
        return "/userInfo";
    }


}
