package com.zy.meclass.dao;

import com.zy.meclass.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserDao {
    //添加用户
    public int create(User user);

    //登陆
    public User login(User user);

    //查询用户
    public User getUserByName(@Param("uname") String username);

    //获取密码
    public String getPassword(@Param("uname") String uname);

    //根据id查询用户
    public User getUserById(@Param("iduser") Integer iduser);

}
