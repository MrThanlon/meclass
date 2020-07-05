package com.zy.meclass.service.impl;

import com.zy.meclass.dao.CommentDao;
import com.zy.meclass.entity.Comment;
import com.zy.meclass.service.CommentService;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
    @Resource
    private CommentDao commentDao;
    //查询评论
    public List<Comment> searchAllComment(@Param("videoId") Integer videoId){
        return commentDao.searchAllComment(videoId);
    }
    //修改评论
    public Comment updateComment(@Param("commentId") Integer commentId){
        return commentDao.updateComment(commentId);
    }
    //添加评论
    public int addComment(Comment comment){
        return commentDao.addComment(comment);
    }
}
