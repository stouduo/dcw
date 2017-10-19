package com.stouduo.dcw.service.impl;

import com.stouduo.dcw.domain.Const;
import com.stouduo.dcw.domain.User;
import com.stouduo.dcw.repository.UserRepository;
import com.stouduo.dcw.service.MailRecordService;
import com.stouduo.dcw.service.SMSService;
import com.stouduo.dcw.service.UserService;
import com.stouduo.dcw.util.CommonUtil;
import com.stouduo.dcw.util.MD5Util;
import com.stouduo.dcw.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    MailRecordService mailRecordService;
    @Autowired
    SMSService smsService;


    @Override
    @Transactional
    public boolean save(User user) {
        if (userRepository.findByUsername(user.getUsername()) != null)
            return false;
        if (StringUtils.isEmpty(user.getRoles()))
            user.setRoles(Const.ROLE_USER);
        user.setPassword(MD5Util.encode(user.getPassword()));
        user.setNickname(user.getUsername());
        userRepository.save(user);
        return true;
    }

    @Override
    public User userInfo() {
        User user = userRepository.findByUsername(SecurityUtil.getUsername());
        String tel = user.getTel();
        if (!StringUtils.isEmpty(tel))
            user.setTel(tel.substring(0, 3) + "****" + tel.substring(7, 11));
        return user;
    }

    @Override
    public int bindInfo(User user) throws Exception {
        int code = CommonUtil.getSMSCode();
        if (!StringUtils.isEmpty(user.getEmail())) {
            Map<String, Object> model = new HashMap<>();
            model.put("username", SecurityUtil.getUsername());
            model.put("code", code);
            model.put("subject", "您的邮箱有变更，请及时确认");
            mailRecordService.sendEmail(user.getEmail(), model, "activeEmail");
        } else {
            code = smsService.sendSMS(user.getTel());
        }
        return code;
    }

    @Override
    public void editUser(User user, String oldPwd, String confirmPwd) {
        User temp = userRepository.findByUsername(SecurityUtil.getUsername());
        if (!StringUtils.isEmpty(user.getNickname())) {
            temp.setNickname(user.getNickname());
        }
        if (!StringUtils.isEmpty(oldPwd)) {
            if (temp.getPassword().equals(MD5Util.encode(oldPwd)) && user.getPassword().equals(confirmPwd)) {
                temp.setPassword(MD5Util.encode(temp.getPassword()));
            }
        }
        userRepository.save(temp);
    }

    @Override
    public User validInfo(String accessUsername) {
        return userRepository.findByTelOrEmail(accessUsername);
    }
}
