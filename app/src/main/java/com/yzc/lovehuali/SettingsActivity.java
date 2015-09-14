package com.yzc.lovehuali;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.yzc.lovehuali.tool.ACache;
import com.yzc.lovehuali.tool.SystemBarTintManager;
import com.yzc.lovehuali.tool.ToastUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class SettingsActivity extends ActionBarActivity {

    private Toolbar mToolbar;
    private Button btnCleanCache,btnCheckUpdate,btnShareApp;
    private ACache mCache;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //设定状态栏的颜色，当版本大于4.4时起作用
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            //此处可以重新指定状态栏颜色
            tintManager.setStatusBarTintResource(R.color.material_blue);
        }

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("软件设置");
        mToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnCleanCache = (Button) findViewById(R.id.btnCleanCache);
        btnCheckUpdate = (Button) findViewById(R.id.btnCheckUpdate);
        btnShareApp = (Button) findViewById(R.id.btnShareApp);

        mCache = ACache.get(SettingsActivity.this);

        btnCleanCache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                builder.setMessage("确定清理软件缓存吗？");
                builder.setTitle("提示");
                builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        mCache.clear();
                        Toast.makeText(SettingsActivity.this, "清理完成!", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.create().show();
            }
        });

        btnCheckUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showMessage(SettingsActivity.this, "软件已是最新版本！");
            }
        });

        btnShareApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initShareIntent("com.tencent.mm");
            }
        });



    }
    private void initShareIntent(String type) {
        boolean found = false;
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        //share.setType("image/jpeg");
        // gets the list of intents that can be loaded.
        List<ResolveInfo> resInfo = getPackageManager().queryIntentActivities(share, 0);
        if (!resInfo.isEmpty()){
            for (ResolveInfo info : resInfo) {
                if (info.activityInfo.packageName.toLowerCase().contains(type) ||
                        info.activityInfo.name.toLowerCase().contains(type) ) {
                    share.putExtra(Intent.EXTRA_SUBJECT,  "应用分享");
                    share.putExtra(Intent.EXTRA_TEXT,     "Hold住你的大学！\n华立学院专属校园App\n点击下载：\nhttp://1.lovehuali.sinaapp.com/index.html");
                    //share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file)); // Optional, just if you wanna share an image.
                    share.setPackage(info.activityInfo.packageName);
                    found = true;
                    break;
                }
            }
            if (!found)
                return;
            startActivity(Intent.createChooser(share, "选择分享到..."));
        }
    }

    public void inputstreamtofile(InputStream ins,File file){
        OutputStream os = null;
        try {
            os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
