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
import javax.servlet.http.HttpServletRequest;
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
        String username = user.getUname();
        if ( userService.getUserByName(username) != null ){
            return new CommonResult(1,"该用户名已注册，注册失败");
        }else {
            user.setPwd(pwd);
            int result = userService.create(user);
            log.info("*****插入结果："+result);

            if(result > 0)
            {
                return new CommonResult(0,"注册成功");
            }else{
                return new CommonResult(1,"注册失败");
            }
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
            System.out.println(u);
            if(u != null){
                String token = JwtUtil.sign(u.getUname(),u.getPwd());
                if(token != null){
                    //Cookie cookie = new Cookie(u.getUname(),u.getPwd());
                    Cookie tokenCookie = new Cookie("login_token_id", token);
                    tokenCookie.setPath("/");
                    response.addCookie(tokenCookie);
                    User userPwd = new User(u.getIduser(), u.getUname(), u.getFlag());
                    return new CommonResult(0,"登陆成功 ",userPwd);
                }
                return new CommonResult(1,"认证失败");
            }else{
                return new CommonResult(1,"用户或密码错误");
            }
        }

    }

    //根据id查询用户信息
    @PostMapping(value = "/user/getUserById/{iduser}")
    public CommonResult getUserById(@PathVariable("iduser") Integer iduser)
    {
        User user = userService.getUserById(iduser);
        if(user != null)
        {
            User userPwd = new User(user.getIduser(), user.getUname(), user.getFlag());
            return new CommonResult(0,"查询成功 ",userPwd);
        }else{
            return new CommonResult(1,"查询失败 ");
        }
    }
    //根据id查询用户信息
    @PostMapping(value = "/user/getUserByName/{uname}")
    public CommonResult getUserByName(@PathVariable("uname") String uname)
    {
        User user = userService.getUserByName(uname);
        if(user != null)
        {
            User userPwd = new User(user.getIduser(), user.getUname(), user.getFlag());
            return new CommonResult(0,"查询成功 ",userPwd);
        }else{
            return new CommonResult(1,"查询失败 ");
        }
    }

    //获取用户信息,需要Token验证的接口
    @PostMapping(value = "/user/get")
    public CommonResult getUser(@CookieValue("login_token_id") String cookievalue)
    {
        //String token = request.getHeader("token")
        //cookies =  request.getCookies();
        //System.out.println(cookies);
        //String userNameByToken = JwtUtil.getUserNameByToken(request);
        String userNameByToken = JwtUtil.getUserNameByCookie(cookievalue);
        if (userNameByToken == null){
            return new CommonResult(1,"查询失败 ");
        }else{
            User userByName = userService.getUserByName(userNameByToken);
            User userPwd = new User(userByName.getIduser(), userByName.getUname(), userByName.getFlag());
            return new CommonResult(0,"查询成功 ",userPwd);
        }

    }

}
