package com.stouduo.dcw.service.impl;

import com.stouduo.dcw.domain.Const;
import com.stouduo.dcw.domain.User;
import com.stouduo.dcw.repository.MailRecordRepository;
import com.stouduo.dcw.repository.UserRepository;
import com.stouduo.dcw.service.MailRecordService;
import com.stouduo.dcw.service.UserService;
import com.stouduo.dcw.util.MD5Util;
import com.stouduo.dcw.util.MailUtil;
import com.stouduo.dcw.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    MailRecordService mailRecordService;


    @Override
    @Transactional
    public boolean save(User user) {
        if (userRepository.findByUsername(user.getUsername()) != null)
            return false;
        if (StringUtils.isEmpty(user.getRoles()))
            user.setRoles(Const.ROLE_USER);
        userRepository.save(user);
        return true;
    }

    @Override
    public User userInfo() {
        return userRepository.findByUsername(SecurityUtil.getUsername());
    }

    @Override
    public void bindInfo(User user) throws Exception {
        Map<String, Object> model = new HashMap<>();
        model.put("username", SecurityUtil.getUsername());
        model.put("token", MD5Util.getToken(user.getEmail()));
        model.put("subject", "您的邮箱有变更，请及时确认");
        if (!StringUtils.isEmpty(user.getEmail())) {
            mailRecordService.sendEmail(user.getEmail(), model, "/activeEmail.ftl");
        }
    }

    @Override
    public void editUser(User user, String oldPwd, String confirmPwd) {
        User temp = userRepository.findByUsername(SecurityUtil.getUsername());
        if (!StringUtils.isEmpty(temp.getUsername())) {
            temp.setUsername(user.getUsername());
        }
        if (!StringUtils.isEmpty(oldPwd)) {
            if (temp.getPassword().equals(MD5Util.encode(oldPwd)) && user.getPassword().equals(confirmPwd)) {
                temp.setPassword(MD5Util.encode(temp.getPassword()));
            }
        }
        userRepository.save(temp);
    }
}
