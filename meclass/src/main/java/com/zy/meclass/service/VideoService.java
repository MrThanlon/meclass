package com.zy.meclass.service;

import com.zy.meclass.entity.Video;
import org.apache.ibatis.annotations.Param;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.util.List;

public interface VideoService {
    //添加视频
    int addVideo(Video video);
    //删除视频
    int deleteVideo(@Param("videoId") Integer videoId);
    //查询所有视频
    List<Video> searchAllVideo();
    //根据名字查询视频
    Video getVideoByTitle(@Param("videoTitle") String videoTitle);
    //根据id查询视频
    Video getVideoById(@Param("videoId") Integer videoId);

    //修改playCount
    int updateVideo(Video video);
}
