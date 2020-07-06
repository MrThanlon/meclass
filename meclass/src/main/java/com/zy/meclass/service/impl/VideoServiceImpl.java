package com.zy.meclass.service.impl;


import com.zy.meclass.dao.VideoDao;
import com.zy.meclass.entity.Video;
import com.zy.meclass.service.VideoService;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class VideoServiceImpl implements VideoService {
    @Resource
    private VideoDao videoDao;

    //添加视频
    public int addVideo(Video video){
        return videoDao.addVideo(video);
    }
    //删除视频
    public int deleteVideo(@Param("videoId") Integer videoId){
        return videoDao.deleteVideo(videoId);
    }
    //查询所有视频
    public List<Video> searchAllVideo(){
        return videoDao.searchAllVideo();
    }
    //根据名字查询视频
    public   Video getVideoByTitle(@Param("videoTitle") String videoTitle){
        return videoDao.getVideoByTitle(videoTitle);
    }
    //根据id查询视频
    public Video getVideoById(@Param("videoId") Integer videoId){
        return videoDao.getVideoById(videoId);
    }

    //修改playCount
    public  int updateVideo(Video video){ return videoDao.updateVideo(video);
    }
}
