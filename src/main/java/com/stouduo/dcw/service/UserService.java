package com.stouduo.dcw.service;

import com.stouduo.dcw.domain.User;

public interface UserService {

    boolean save(User user);

    User userInfo();

    void bindInfo(User user) throws Exception;

    void editUser(User user, String oldPwd, String confirmPwd);
}
