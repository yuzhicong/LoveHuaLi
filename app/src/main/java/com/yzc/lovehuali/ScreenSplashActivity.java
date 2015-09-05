package com.yzc.lovehuali;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.widget.ImageView;

import com.yzc.lovehuali.bmob.StudentUser;
import com.yzc.lovehuali.tool.ACache;

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
        ImageView ivSplash = (ImageView) findViewById(R.id.ivSplash);

        /** set time to splash out **/
        int nWelcomeScreenDisplay = 300;

        ACache mcache = ACache.get(this);
        if(mcache.getAsBitmap("splashPicture")!=null){
            ivSplash.setImageBitmap(mcache.getAsBitmap("splashPicture"));
            nWelcomeScreenDisplay = 1500;
        }

        //Bmob.initialize(this, "ce44de9648c859db8001d4187e9d38b9");//BmobSDK初始化
        /*// 使用推送服务时的初始化操作
        BmobInstallation.getCurrentInstallation(this).save();
        // 启动推送服务
        BmobPush.startWork(this, "ce44de9648c859db8001d4187e9d38b9");
        System.out.println("推送服务启动成功！");*/



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /*if(BmobUser.getCurrentUser(getApplicationContext(), StudentUser.class)==null) {
                    Intent mainIntent = new Intent(ScreenSplashActivity.this, LoginActivity.class);
                    startActivity(mainIntent);
                    ScreenSplashActivity.this.finish();
                }
                else {
                    Intent mainIntent = new Intent(ScreenSplashActivity.this, MainActivity.class);
                    startActivity(mainIntent);
                    ScreenSplashActivity.this.finish();
                }//暂时改成不用账号可以直接使用*/
                Intent mainIntent = new Intent(ScreenSplashActivity.this, MainActivity.class);
                startActivity(mainIntent);
                ScreenSplashActivity.this.finish();
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);

            }
        }, nWelcomeScreenDisplay);
    }
}
