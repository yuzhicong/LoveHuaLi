package com.yzc.lovehuali;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;

import com.yzc.lovehuali.bmob.StudentUser;

import cn.bmob.v3.BmobUser;


public class ScreenSplashActivity extends ActionBarActivity {

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_screen_splash);



        /** set time to splash out **/
        final int nWelcomeScreenDisplay = 1500;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(BmobUser.getCurrentUser(getApplicationContext(), StudentUser.class)==null) {
                    Intent mainIntent = new Intent(ScreenSplashActivity.this, LoginActivity.class);
                    startActivity(mainIntent);
                    ScreenSplashActivity.this.finish();
                }
                else {
                    Intent mainIntent = new Intent(ScreenSplashActivity.this, MainActivity.class);
                    startActivity(mainIntent);
                    ScreenSplashActivity.this.finish();
                }
            }
        }, nWelcomeScreenDisplay);
    }
}
