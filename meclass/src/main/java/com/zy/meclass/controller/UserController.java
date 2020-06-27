package com.zy.meclass.controller;

import com.zy.meclass.entity.CommonResult;
import com.zy.meclass.entity.User;
import com.zy.meclass.service.UserService;
import com.zy.meclass.util.JwtUtil;
import com.zy.meclass.util.MD5Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@RestController
@Slf4j
public class UserController {
    @Resource
    private UserService userService;

    //用户注册
    @PostMapping(value = "/user/register",produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResult create(@RequestBody User user){
        log.info("*****结果："+user);
        String pwd=	MD5Util.string2MD5(user.getPwd());
        user.setPwd(pwd);
        int result = userService.create(user);
        log.info("*****插入结果："+result);

        if(result > 0)
        {
            return new CommonResult(1,"注册成功");
        }else{
            return new CommonResult(0,"注册失败");
        }

    }

    //用户登陆
    @PostMapping(value = "/user/login")
    public CommonResult login(@RequestBody User user, HttpServletResponse response){
        if (user.getUname() == null || user.getUname().length() <= 0 || user.getPwd() ==null || user.getPwd().length() <= 0 ){
            return new CommonResult(0,"用户或密码为空 ");
        }else {
            //String pwd=MD5Util.convertMD5(MD5Util.convertMD5(user.getPwd()));
            String pwd=	MD5Util.string2MD5(user.getPwd());
            System.out.println(pwd);
            user.setPwd(pwd);
            User u = userService.login(user);
            if(u != null){
                String token = JwtUtil.sign(u.getUname(),u.getPwd());
                if(token != null){
                    //Cookie cookie = new Cookie(u.getUname(),u.getPwd());
                    Cookie tokenCookie = new Cookie("login_token_id", token);
                    response.addCookie(tokenCookie);
                    User userPwd = new User(u.getIduser(), u.getUname(), u.getFlag());
                    return new CommonResult(1,"登陆成功 ",userPwd);
                }
                return new CommonResult(0,"认证失败");
            }else{
                return new CommonResult(0,"用户或密码错误");
            }
        }

    }

    //查询用户信息(未实现)
    @PostMapping(value = "/user/search")
    public CommonResult getPaymentById(@PathVariable("id") Integer id)
    {
        User user = userService.getUserById(id);

        if(user != null)
        {
            return new CommonResult(1,"查询成功 ",user);
        }else{
            return new CommonResult(0,"查询失败 ");
        }
    }

    //获取用户信息,需要Token验证的接口
    @PostMapping(value = "/user/get")
    public String getPaymentById()
    {
        return "info";
    }

}
