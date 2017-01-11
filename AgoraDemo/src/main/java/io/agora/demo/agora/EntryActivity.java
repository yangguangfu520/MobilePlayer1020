package io.agora.demo.agora;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

/**
 * Launcher screen of app
 */
public class EntryActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);

        enterAppWithDelay(2000);
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig){

        super.onConfigurationChanged(newConfig);
    }

    // move to login screen in delayInMillis
    private void enterAppWithDelay(long delayInMillis){

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent=new Intent(EntryActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        },delayInMillis);
    }
}
