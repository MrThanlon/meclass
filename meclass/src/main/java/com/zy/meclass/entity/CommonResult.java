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

    public CommonResult(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
