package com.zy.meclass.controller;

import com.zy.meclass.entity.CommonResult;
import com.zy.meclass.entity.User;
import com.zy.meclass.entity.Video;
import com.zy.meclass.service.UserService;
import com.zy.meclass.service.VideoService;
import com.zy.meclass.util.JwtUtil;
import com.zy.meclass.util.NonStaticResourceHttpRequestHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;

@RestController
@Slf4j
public class VideoController {
    @Resource
    private VideoService videoService;

    @Value("${video.path}")
    private String videoPath;

    @Resource
    private UserService userService;



    //上传视频
    @PostMapping(value = "/video/add")
    public CommonResult uploadVideo(@RequestParam("file") MultipartFile file,
                                    @RequestParam("videoTitle")String videoTitle,
                                    @CookieValue("login_token_id") String cookievalue){
        String userNameByToken = JwtUtil.getUserNameByCookie(cookievalue);
        if (userNameByToken == null){
            return new CommonResult(1,"请登陆");
        }else{
            User userByName = userService.getUserByName(userNameByToken);
            if (userByName.getFlag() != 1){
                return new CommonResult(1,"很抱歉,您没有上传权限");
            }else {
                try {
                    //获取文件后缀
                    String fileExt = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1)
                            .toLowerCase();
                    System.out.println(fileExt);
                    //视频存放路径
                    String path = videoPath;
                    //获取项目根路径并转到static/videos
                    //String path = ClassUtils.getDefaultClassLoader().getResource("").getPath()+"static/videos/";
                    //保存视频
                    File fileSave = new File(path,videoTitle+".mp4");
                    //下载到本地
                    file.transferTo(fileSave);
                    Video videoByTitle = videoService.getVideoByTitle(videoTitle);
                    if (videoByTitle == null){
                        Video newVideo = new Video(videoTitle,path,0);
                        videoService.addVideo(newVideo);
                        return new CommonResult(0,"上传视频成功 ");
                    }else {
                        return new CommonResult(1,"视频名称已存在，请重新命名 ");
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return new CommonResult(1,"上传视频失败 ");
            }
        }

    }

    //删除视频
    @PostMapping("/video/deleteVideoById/{videoId}")
    public CommonResult deleteVideoById(@PathVariable("videoId") Integer videoId){
        String path = videoPath;
        Video videoById = videoService.getVideoById(videoId);
        if (videoById == null){
            return new CommonResult(1,"未找到要删除的视频");
        }else{
            String videoTitle = videoById.getVideoTitle();
            //保存视频
            File file = new File(path,videoTitle+".mp4");
            if (file.exists()) {//文件是否存在
                if (file.delete()) {//存在就删了，返回1
                    videoService.deleteVideo(videoId);
                    return new CommonResult(0,"删除成功");
                } else {
                    return new CommonResult(1,"删除失败");
                }
            } else {
                return new CommonResult(1,"文件不存在");
            }
        }

    }
    
    //查询所有视频名称
    @PostMapping("/video/findVideoAll")
    public CommonResult findVideoAll(Model model){
        List<Video> list = null;
        try{
            list = this.videoService.searchAllVideo();
            model.addAttribute("list",list);
        }catch (Exception e){
            e.printStackTrace();
            return new CommonResult(1,"查询失败");
        }

        return new CommonResult(0,"查询成功",list);
    }

    //根据视频标题查询视频
    @RequestMapping(value = "/video/get",method = RequestMethod.GET)
    public byte[] getVideoByName(@RequestParam("videoId") Integer videoId, HttpServletResponse response)
    {
            Video video = videoService.getVideoById(videoId);
        if (video == null){
                return null;
            }
            String filePath = video.getPath();
            String fileName = video.getVideoTitle();
            byte[] buffer = null;
            try {
                File file = new File(filePath, fileName+".mp4");
                FileInputStream fis = new FileInputStream(file);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                byte[] b = new byte[1024];
                int n;
                while ((n = fis.read(b)) != -1)
                {
                    bos.write(b, 0, n);
                }
                fis.close();
                bos.close();
                buffer = bos.toByteArray();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            response.setContentType("video/mp4");
            int playCount = video.getPlayCount() + 1;
            video.setPlayCount(playCount);
            videoService.updateVideo(video);
        return buffer;
        }


    }

