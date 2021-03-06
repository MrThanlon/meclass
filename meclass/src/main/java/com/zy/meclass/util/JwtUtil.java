package com.zy.meclass.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

public class JwtUtil {
    //Token过期时间30分钟
    public static final long EXPIRE_TIME = 30*60*1000;

    //检验token是否正确
    public static boolean verify(String token, String username, String secret){
        try {
            //设置加密算法
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withClaim("username",username)
                    .build();
            //效验TOKEN
            DecodedJWT jwt = verifier.verify(token);
            return true;
        }catch (Exception exception){
            return false;
        }
    }

    //生成签名，30min后过期
    public static String sign(String username, String secret){
        Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        Algorithm algorithm = Algorithm.HMAC256(secret);
        //附带username信息
        return JWT.create()
                .withClaim("username",username)
                .withExpiresAt(date)
                .sign(algorithm);
    }

    //获得用户名
    public static String getUserNameByToken(HttpServletRequest request){
        //String token = request.getHeader("token");
        //String cookie = request.getHeader("Cookie");
        String token = request.getHeader("login_token_id");
        if (token == null){
            return null;
        }
        DecodedJWT jwt = JWT.decode(token);
        return jwt.getClaim("username").asString();
    }
    //根据cookie获得用户名
    public static String getUserNameByCookie(String cookievalue){
        String token = cookievalue;
        if (token == null){
            return null;
        }
        DecodedJWT jwt = JWT.decode(token);
        return jwt.getClaim("username").asString();
    }
}
