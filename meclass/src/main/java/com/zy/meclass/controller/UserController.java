package com.zy.meclass.controller;

import com.zy.meclass.entity.CommonResult;
import com.zy.meclass.entity.User;
import com.zy.meclass.service.UserService;
import com.zy.meclass.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@Slf4j
public class UserController {
    @Resource
    private UserService userService;

    //用户注册
    @PostMapping(value = "/user/register",produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResult create(@RequestBody User user){
        log.info("*****结果："+user);
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
    @RequestMapping(value = "/user/login", method = RequestMethod.GET)
    public CommonResult<User> login(@RequestBody User user){
        if (user.getUname() == null || user.getUname().length() <= 0 || user.getPwd() ==null || user.getPwd().length() <= 0 ){
            return new CommonResult(0,"用户或密码为空 ");
        }else {
            User u = userService.login(user);
            if(u != null){
                String token = JwtUtil.sign(u.getUname(),u.getPwd());
                if(token != null){
                    return new CommonResult(1,"登陆成功 ",u,u.getFlag(),token);
                }
                return new CommonResult(0,"认证失败");
            }else{
                return new CommonResult(0,"用户或密码错误");
            }
        }

    }

    //查询用户信息
    @RequestMapping(value = "/user/search", method = RequestMethod.GET)
    public CommonResult<User> getPaymentById(@PathVariable("id") Integer id)
    {
        User user = userService.getUserById(id);

        if(user != null)
        {
            return new CommonResult(1,"查询成功 ",user);
        }else{
            return new CommonResult(0,"没有对应记录,查询ID: "+id,null);
        }
    }
    //获取用户信息,需要Token验证的接口
    @RequestMapping(value = "/user/get", method = RequestMethod.GET)
    public String getPaymentById()
    {
        return "info";
    }

}
