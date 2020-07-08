package com.zy.meclass.service.impl;

import com.zy.meclass.dao.UserDao;
import com.zy.meclass.entity.User;
import com.zy.meclass.service.UserService;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserDao userDao;

    public int create(User user){
        return userDao.create(user);
    }
    public User getUserByName(@Param("uname") String username){
        return userDao.getUserByName(username);
    }

    public User login(User user){
        return userDao.login(user);
    }

    public String getPassword(@Param("uname") String uname){
        return userDao.getPassword(uname);
    }

    public User getUserById(@Param("iduser") Integer iduser){
        return userDao.getUserById(iduser);
    }



}
