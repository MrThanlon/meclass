package com.zy.meclass.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonResult<T> {
    private Integer code;
    private String msg;
    private Integer flag;
    private T result;
    private String token;

    public CommonResult(Integer code, String msg, T result, Integer flag,String token) {
        this.code = code;
        this.msg = msg;
        this.result = result;
        this.flag = flag;
        this.token = token;
    }
    public CommonResult(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    public CommonResult(Integer code, String msg, T result) {
        this.code = code;
        this.msg = msg;
        this.result = result;
    }
    public CommonResult(Integer code, String msg, T result, Integer flag) {
        this.code = code;
        this.msg = msg;
        this.result = result;
        this.flag = flag;
    }
}
