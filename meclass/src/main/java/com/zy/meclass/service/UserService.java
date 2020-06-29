package com.zy.meclass.service;

import com.zy.meclass.entity.User;
import org.apache.ibatis.annotations.Param;

public interface UserService {
    public int create(User user) ;

    public User getUserById(@Param("id") Integer id);

    public User login(User user);

    public String getPassword(@Param("uname") String uname);
}
