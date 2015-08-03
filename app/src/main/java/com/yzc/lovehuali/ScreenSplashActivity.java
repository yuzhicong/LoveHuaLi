package com.yzc.lovehuali;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;

import com.yzc.lovehuali.bmob.StudentUser;

import cn.bmob.push.BmobPush;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobUser;


public class ScreenSplashActivity extends ActionBarActivity {

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_screen_splash);
        Bmob.initialize(this, "ce44de9648c859db8001d4187e9d38b9");//BmobSDK初始化
        /*// 使用推送服务时的初始化操作
        BmobInstallation.getCurrentInstallation(this).save();
        // 启动推送服务
        BmobPush.startWork(this, "ce44de9648c859db8001d4187e9d38b9");
        System.out.println("推送服务启动成功！");*/


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
