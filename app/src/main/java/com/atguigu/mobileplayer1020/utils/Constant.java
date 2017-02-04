package com.atguigu.mobileplayer1020.utils;

/**
 * 作者：尚硅谷-杨光福 on 2017/1/10 16:20
 * 微信：yangguangfu520
 * QQ号：541433511
 * 作用：联网的路径
 */
public class Constant {

//    public static final String BASE_URL = "http://192.168.11.218:8080/web_home";
    public static final String BASE_URL = "http://182.92.5.3/mobileplayer";
    /**
     * 网络视频路径
     */
//    public static final String NET_URL = "http://api.m.mtime.cn/PageSubArea/TrailerList.api";
    public static final String NET_URL = BASE_URL+"/json/video.json";



    /**
     * 网络音乐地址
     */
//    public static final String NET_AUDIO = "http://s.budejie.com/topic/list/jingxuan/1/budejie-android-6.2.8/0-20.json?market=baidu&udid=863425026599592&appname=baisibudejie&os=4.2.2&client=android&visiting=&mac=98%3A6c%3Af5%3A4b%3A72%3A6d&ver=6.2.8";
    public static final String NET_AUDIO = BASE_URL+"/json/audio.json";



    /**
     * 搜索地址
     */
    public static final String NET_SEARCH_URL = "http://hot.news.cntv.cn/index.php?controller=list&action=searchList&sort=date&n=20&wd=";


}