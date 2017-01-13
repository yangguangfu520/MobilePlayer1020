package com.atguigu.mobileplayer1020.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

import com.atguigu.mobileplayer1020.bean.LyricBean;

import java.util.ArrayList;

/**
 * 作者：尚硅谷-杨光福 on 2017/1/13 14:20
 * 微信：yangguangfu520
 * QQ号：541433511
 * 作用：自定义显示歌词的控件
 */
public class LyricShowView extends TextView {
    private int width;
    private int height;
    private ArrayList<LyricBean> lyricBeen;
    private Paint paint;
    private Paint nopaint;
    /**
     * 歌词的索引
     */
    private int index = 0;
    private float textHeight = 20;

    public LyricShowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();

    }

    private void initView() {
        //创建画笔
        paint = new Paint();
        paint.setTextSize(20);
        paint.setColor(Color.GREEN);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setAntiAlias(true);

        nopaint = new Paint();
        nopaint.setTextSize(20);
        nopaint.setColor(Color.WHITE);
        nopaint.setTextAlign(Paint.Align.CENTER);
        nopaint.setAntiAlias(true);


        lyricBeen = new ArrayList<>();
        //添加歌词列表
        LyricBean lyricBean = new LyricBean();
        for (int i = 0; i < 1000; i++) {
            lyricBean.setContent("aaaaaaaaaaa" + i);
            lyricBean.setSleepTime(i + 1000);
            lyricBean.setTimePoint(i * 1000);
            //添加到集合中
            lyricBeen.add(lyricBean);
            //重新创建
            lyricBean = new LyricBean();

        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
    }


    /**
     * 绘制歌词
     */

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (lyricBeen != null && lyricBeen.size() > 0) {
            //绘制歌词
            //当前句-绿色
            String content = lyricBeen.get(index).getContent();
            canvas.drawText(content, width / 2, height / 2, paint);
            //绘制前面部分

            float tempY = height / 2;
            for (int i = index - 1; i >= 0; i--) {

                tempY = tempY - textHeight;
                if (tempY < 0) {
                    break;
                }

                String preContent = lyricBeen.get(i).getContent();
                canvas.drawText(preContent, width / 2, tempY, nopaint);
            }


            //绘制后面部分

            tempY = height / 2;
            for (int i = index + 1; i < lyricBeen.size(); i++) {
                tempY = tempY + textHeight;
                if (tempY > height) {
                    break;
                }
                String nextContent = lyricBeen.get(i).getContent();
                canvas.drawText(nextContent, width / 2, tempY, nopaint);
            }

        } else {


            //没有歌词
            canvas.drawText("没有找到歌词...", width / 2, height / 2, paint);
        }


    }
}
