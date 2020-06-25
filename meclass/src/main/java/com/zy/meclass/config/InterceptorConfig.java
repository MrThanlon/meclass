package com.zy.meclass.config;

import com.zy.meclass.util.JwtInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class InterceptorConfig implements WebMvcConfigurer {

    //设置拦截路径
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authenticationInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/user/login");
    }
    //将拦截器注入到context
    //@Bean
    public JwtInterceptor authenticationInterceptor(){
        return new JwtInterceptor();
    }

    //跨域支持


    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowCredentials(true)
                .allowedMethods("GET","POST","DELETE","PUT","PATCH","OPTIONS","HEAD")
                .maxAge(3600 * 24);
    }
}
