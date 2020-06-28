package com.zy.meclass.service;

import com.zy.meclass.entity.User;
import org.apache.ibatis.annotations.Param;

public interface UserService {
    int create(User user) ;

    User getUserById(@Param("id") Integer id);

    User login(User user);

    String getPassword(@Param("uname") String uname);
}
