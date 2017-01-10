package com.atguigu.mobileplayer1020.bean;

import java.io.Serializable;

/**
 * 作者：尚硅谷-杨光福 on 2017/1/7 11:39
 * 微信：yangguangfu520
 * QQ号：541433511
 * 作用： 代码一个视频或者音频
 */
public class MediaItem implements Serializable{

    String name ;
    long duration ;
    long size ;
    String data ;
    String artist ;
    /**
     * 图片路径
     */
    String imageUrl;
    /**
     * 描述
     */
    String desc;

    String heightUrl;

    public String getHeightUrl() {
        return heightUrl;
    }

    public void setHeightUrl(String heightUrl) {
        this.heightUrl = heightUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    @Override
    public String toString() {
        return "MediaItem{" +
                "name='" + name + '\'' +
                ", duration=" + duration +
                ", size=" + size +
                ", data='" + data + '\'' +
                ", artist='" + artist + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", desc='" + desc + '\'' +
                '}';
    }
}
