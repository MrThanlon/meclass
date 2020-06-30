package com.zy.meclass.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonResult implements Serializable {
    private Integer code;
    private String msg;
    private Object data;
    //private String token;
    private Byte buff;

    public CommonResult(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    public CommonResult(Integer code, String msg,Byte buff) {
        this.code = code;
        this.msg = msg;
        this.buff = buff;
    }
    public CommonResult(Integer code, String msg,Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
}
