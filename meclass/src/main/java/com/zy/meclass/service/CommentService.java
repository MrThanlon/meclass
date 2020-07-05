package com.zy.meclass.service;

import com.zy.meclass.entity.Comment;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CommentService {
    //查询评论
    public List<Comment> searchAllComment(@Param("videoId") Integer videoId);
    //修改评论
    public Comment updateComment(@Param("commentId") Integer commentId);
    //添加评论
    public int addComment(Comment comment);
}
