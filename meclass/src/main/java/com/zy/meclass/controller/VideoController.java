package com.zy.meclass.controller;

import com.zy.meclass.entity.CommonResult;
import com.zy.meclass.entity.Video;
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



    //上传视频
    @PostMapping(value = "/video/add")
    public CommonResult uploadVideo(@RequestParam("file") MultipartFile file,@RequestParam("videoTitle")String videoTitle){
        try {
            //获取文件后缀
            String fileExt = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1)
                    .toLowerCase();
            System.out.println(fileExt);
            //视频存放路径
            String path = videoPath;
            //获取项目根路径并转到static/videos
            //String path = ClassUtils.getDefaultClassLoader().getResource("").getPath()+"static/videos/";
            //String path = " /Users/zhangye/IdeaProjects/meclass/src/main/resources/static/videos";
            //保存视频
            //Video fileSave = new Video(videoTitle,path,0);
            //String videoName = file.substring(0, pngFilename.lastIndexOf(".")) + ".jpg";
            File fileSave = new File(path,videoTitle+".mp4");
            //下载到本地
            file.transferTo(fileSave);

            //fileSave.renameTo(new File(videoTitle+".mp4"));//改名
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

    //删除视频

    
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
    public byte[] getVideoByName(@RequestParam("videoTitle") String videoTitle, HttpServletResponse response)
    {
            Video video = videoService.getVideoByTitle(videoTitle);
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
            return buffer;
        }

    //未实现
    @RequestMapping(value = "/getVideosss", method = RequestMethod.GET)
    public void getVideosss(HttpServletRequest request,HttpServletResponse response,@RequestParam("videoTitle") String videoTitle)
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
