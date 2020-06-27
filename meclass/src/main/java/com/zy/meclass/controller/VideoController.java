package com.zy.meclass.controller;

import com.zy.meclass.entity.CommonResult;
import com.zy.meclass.entity.User;
import com.zy.meclass.entity.Video;
import com.zy.meclass.service.VideoService;
import com.zy.meclass.util.NonStaticResourceHttpRequestHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

@RestController
@Slf4j
public class VideoController {
    @Resource
    private VideoService videoService;

    @Resource
    private NonStaticResourceHttpRequestHandler nonStaticResourceHttpRequestHandler;


    //上传视频
    @PostMapping(value = "/video/add")
    public CommonResult uploadVideo(@RequestParam("file") MultipartFile file,@RequestParam("videoTitle")String videoTitle,Video video){
        try {
            //获取文件后缀
            String fileExt = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1)
                    .toLowerCase();
            //视频存放路径
            String path = "/Users/zhangye/Documents/video";
            //保存视频
            //Video fileSave = new Video(videoTitle,path,0);
            File fileSave = new File(path,videoTitle);
            //下载到本地
            file.transferTo(fileSave);
            Video newVideo = new Video(videoTitle,path,0);
            videoService.addVideo(newVideo);
            return new CommonResult(1,"上传视频成功 ");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new CommonResult(0,"上传视频失败 ");
    }

    //删除视频


    //查询所有视频

    //根据id查询视频
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public CommonResult getVideoByName(HttpServletRequest request, HttpServletResponse response, @RequestBody String videoTitle)
    {
        Video video = videoService.getVideoByTitle(videoTitle);

        response.reset();
        //获取从那个字节开始读取文件
        String rangeString = request.getHeader("Range");

        if(video != null)
        {
            return new CommonResult(1,"查询成功 ",video);
        }else{
            return new CommonResult(0,"查询失败 ");
        }
    }

    @RequestMapping(value = "/getVideo", method = RequestMethod.GET)
    public void getVideo(HttpServletRequest request,HttpServletResponse response,@RequestParam("videoTitle") String videoTitle)
    {
        //视频资源存储信息
        Video video = videoService.getVideoByTitle(videoTitle);
        response.reset();
        //获取从那个字节开始读取文件
        String rangeString = request.getHeader("Range");

        try {
            //获取响应的输出流
            OutputStream outputStream = response.getOutputStream();
            //File file = new File(videoSource.getFileAddress());
            File file = new File(video.getPath());
            if(file.exists()){
                RandomAccessFile targetFile = new RandomAccessFile(file, "r");
                long fileLength = targetFile.length();
                //播放
                if(rangeString != null){

                    long range = Long.valueOf(rangeString.substring(rangeString.indexOf("=") + 1, rangeString.indexOf("-")));
                    //设置内容类型
                    response.setHeader("Content-Type", "video/mp4");
                    //设置此次相应返回的数据长度
                    response.setHeader("Content-Length", String.valueOf(fileLength - range));
                    //设置此次相应返回的数据范围
                    response.setHeader("Content-Range", "bytes "+range+"-"+(fileLength-1)+"/"+fileLength);
                    //返回码需要为206，而不是200
                    response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
                    //设定文件读取开始位置（以字节为单位）
                    targetFile.seek(range);
                }else {//下载

                    //设置响应头，把文件名字设置好
                    //response.setHeader("Content-Disposition", "attachment; filename="+videoSource.getFileName() );
                    response.setHeader("Content-Disposition", "attachment; filename="+video.getVideoTitle());
                    //设置文件长度
                    response.setHeader("Content-Length", String.valueOf(fileLength));
                    //解决编码问题
                    response.setHeader("Content-Type","application/octet-stream");
                }


                byte[] cache = new byte[1024 * 300];
                int flag;
                while ((flag = targetFile.read(cache))!=-1){
                    outputStream.write(cache, 0, flag);
                }
            }else {
                //String message = "file:"+videoSource.getFileName()+" not exists";
                String message = "file:"+video.getVideoTitle()+" not exists";
                //解决编码问题
                response.setHeader("Content-Type","application/json");
                outputStream.write(message.getBytes(StandardCharsets.UTF_8));
            }

            outputStream.flush();
            outputStream.close();

        } catch (FileNotFoundException e) {

        } catch (IOException e) {

        }
    }


}
