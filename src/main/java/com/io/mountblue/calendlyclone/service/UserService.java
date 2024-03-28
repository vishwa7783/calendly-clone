package com.io.mountblue.calendlyclone.service;

import com.io.mountblue.calendlyclone.entity.User;

public interface UserService {

    User findUserByEmail(String useremail);

    void save(User theUser);
}
