package com.atguigu.mobileplayer1020;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;

public class WelcomeActivity extends AppCompatActivity {

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        //延迟两秒进入主页面
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //两秒后执行
                startMainActivity();
            }
        }, 2000);
    }
//    private boolean isStartMain = false;

    /**
     * 进入主页面
     */
    private void startMainActivity() {
//        if(!isStartMain){
//            isStartMain = true;
            //1.进入主页面
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
            //2.关闭当前页面
            finish();
//        }


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //按下和离开
        startMainActivity();
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //移除所有消息
        handler.removeCallbacksAndMessages(null);
    }
}
