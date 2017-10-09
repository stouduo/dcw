package com.stouduo.dcw.service.impl;

import com.stouduo.dcw.domain.User;
import com.stouduo.dcw.repository.UserRepository;
import com.stouduo.dcw.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;

    @Override
    public User register(User user) {
        user = userRepository.findByUsername(user.getUsername());
        if (StringUtils.isEmpty(user.getId())) {
            user = userRepository.save(user);
        }
        return user;
    }

    @Override
    public User save(User user) {
        List<String> roles = new ArrayList<>();
        roles.add("ROLE_USER");
        return userRepository.save(user);
    }
}
