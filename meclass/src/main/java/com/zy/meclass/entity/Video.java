package com.zy.meclass.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Video {
    private Integer videoId;
    private String videoTitle;
    private String path;
    private Integer playCount;
    private Integer isFlash;

    public Video(String videoTitle, String path, int playCount, int isFlash) {
        this.videoTitle = videoTitle;
        this.path = path;
        this.playCount = playCount;
        this.isFlash = isFlash;
    }


}
