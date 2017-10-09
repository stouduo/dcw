package com.stouduo.dcw.service;

import com.stouduo.dcw.domain.User;

public interface UserService {
    User register(User user);

    User save(User user);
}
