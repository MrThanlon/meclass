package com.zy.meclass.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Integer iduser;
    private String uname;
    private String pwd;
    private Integer flag;

    public User(Integer iduser, String uname, Integer flag) {
        this.iduser = iduser;
        this.uname = uname;
        this.flag = flag;
    }
}
