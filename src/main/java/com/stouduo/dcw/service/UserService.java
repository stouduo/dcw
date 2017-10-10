package com.stouduo.dcw.service;

import com.stouduo.dcw.domain.User;

public interface UserService {
    User register(User user);

    boolean save(User user);
}
