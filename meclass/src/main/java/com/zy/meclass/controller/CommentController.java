package com.zy.meclass.controller;

import com.zy.meclass.entity.Comment;
import com.zy.meclass.entity.CommonResult;
import com.zy.meclass.entity.User;
import com.zy.meclass.entity.Video;
import com.zy.meclass.service.CommentService;
import com.zy.meclass.service.TimeService;
import com.zy.meclass.service.VideoService;
import com.zy.meclass.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@RestController
@Slf4j
public class CommentController {
    @Resource
    private CommentService commentService;
    @Resource
    private TimeService timeService;
    @Resource
    private VideoService videoService;

    //添加评论
    @PostMapping(value = "/comment/add")
    public CommonResult create(@RequestBody Comment comment,
                               @CookieValue("login_token_id") String cookievalue) {
        String userNameByToken = JwtUtil.getUserNameByCookie(cookievalue);
        if (userNameByToken == null) {
            return new CommonResult(1, "请登陆");
        } else {
            String commentMsg = comment.getCommentMsg();
            Integer replyId = comment.getReplyId();
            Integer videoId = comment.getVideoId();
            Comment newcomment = new Comment();
            String createTime = timeService.getTime();
            newcomment.setCommentMsg(commentMsg);
            newcomment.setVideoId(videoId);
            newcomment.setCreateTime(createTime);
            newcomment.setReplyId(replyId);
            newcomment.setUsername(userNameByToken);
            commentService.addComment(newcomment);
            return new CommonResult(0, "评论成功");
        }
    }

    //查询所有评论
    @PostMapping("/comment/get")
    public CommonResult findCommentAll(Model model,@RequestBody Video video) {
        Integer videoId = video.getVideoId();
        List<Comment> list = null;
        try {
            list = this.commentService.searchAllComment(videoId);
            model.addAttribute("list", list);
        } catch (Exception e) {
            e.printStackTrace();
            return new CommonResult(1, "查询失败");
        }
        Video videoById = videoService.getVideoById(videoId);
        if (videoById == null){
            return new CommonResult(1, "查询失败");
        }
        return new CommonResult(0,"查询成功",list);
    }
}
