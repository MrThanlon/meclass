package com.zy.meclass.dao;

import com.zy.meclass.entity.Comment;
import com.zy.meclass.entity.Video;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CommentDao {
    //查询评论
    public List<Comment> searchAllComment(@Param("videoId") Integer videoId);
    //修改评论
    public Comment updateComment(@Param("commentId") Integer commentId);
    //添加评论
    public int addComment(Comment comment);

}
