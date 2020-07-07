package com.zy.meclass.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    private Integer commentId;
    private String username;
    private Integer videoId;
    private Integer replyId;
    private String commentMsg;
    private String createTime;
}
