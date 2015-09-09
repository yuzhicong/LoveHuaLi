package com.yzc.lovehuali;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.yzc.lovehuali.tool.HtmlDataOptimizeTool;
import com.yzc.lovehuali.tool.SystemBarTintManager;

import org.jsoup.nodes.Document;

/**
 * 新闻详情Activity模式说明,默认mode=0，为院站新闻的点击事件
 * mode=1为推送Notification的点击事件进入的事件。
 * mode=2为系统公告的点击进入事件。
 */
public class NewsDetailsActivity extends ActionBarActivity {

    private int mode =0;

    private Toolbar mToolbar;
    private WebView wvNews;
    private String Context;
    private String NewBar = "<p style=\"text-align: center;\">\n" +
            "<p style=\"text-align: center;\">\n" +
            "    <span style=\"font-size: 20px;\"><strong>Title</strong></span>\n" +
            "</p>\n" +
            "<p style=\"text-align: center;\">\n" +
            "    <span style=\"color: rgb(127, 127, 127); font-size: 14px;\">Publisher &nbsp;Date</span>\n" +
            "</p>\n" +
            "<p>\n" +
            "    <span style=\"color: rgb(127, 127, 127); font-size: 14px;\"></span>\n" +
            "</p>\n" +
            "<hr/>\n" +
            "</p>";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);

        Intent i = getIntent();

        //设定状态栏的颜色，当版本大于4.4时起作用
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            //此处可以重新指定状态栏颜色
            tintManager.setStatusBarTintResource(R.color.colorPrimaryDark);
        }

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mode = i.getIntExtra("mode", 0);
        System.out.println("mode: " + mode);
        if(mode==1){
            mToolbar.setTitle("公告");
            NotificationManager manager = (NotificationManager) this.getSystemService(android.content.Context.NOTIFICATION_SERVICE);//通知控制类
            manager.cancel(7788);
        }
        //mToolbar.setTitle("新闻内容");



        Context = i.getStringExtra("context");

        if(i.getCharSequenceExtra("publishUser")!=null) {
            //mToolbar.setSubtitle(i.getCharSequenceExtra("publishUser") + "    " + i.getCharSequenceExtra("publishDate"));
            NewBar = NewBar.replace("Title", i.getCharSequenceExtra("title"));
            NewBar = NewBar.replace("Publisher", i.getCharSequenceExtra("publishUser"));
            NewBar = NewBar.replace("Date",i.getCharSequenceExtra("publishDate"));
        }else{
            NewBar = NewBar.replace("Title", i.getCharSequenceExtra("title"));
            NewBar = NewBar.replace("Publisher &nbsp;Date",i.getCharSequenceExtra("publishDate"));
        }
        mToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final ProgressBar pbBar = (ProgressBar)findViewById(R.id.pbWebView);

        wvNews = (WebView) findViewById(R.id.webViewNews);
        WebSettings settings = wvNews.getSettings();
        //适应屏幕
        settings.setUseWideViewPort(true);
//        settings.setSupportZoom(true);
      settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
//      settings.setLoadWithOverviewMode(true);
//      settings.setBuiltInZoomControls(true);
        settings.setJavaScriptEnabled(true);

        Document newsHtml = new HtmlDataOptimizeTool().HtmlDataOptimizeTool(NewBar+Context);

        wvNews.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    pbBar.setVisibility(View.INVISIBLE);
                    LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) pbBar.getLayoutParams(); // 取控件pbBar当前的布局参数
                    linearParams.height = 0;// 当控件的高强制设成0象素
                    pbBar.setLayoutParams(linearParams);
                } else {
                    if (View.INVISIBLE == pbBar.getVisibility()) {
                        pbBar.setVisibility(View.VISIBLE);
                    }
                    pbBar.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }

        });

        wvNews.loadDataWithBaseURL("about:blank", newsHtml.toString(), "text/html", "utf-8", null);
        //wvNews.loadDataWithBaseURL("about:blank", i.getStringExtra("context"), "text/html", "utf-8", null);//读取intent传过来的本地网页数据


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            switch (mode){
                case 0:
                    finish();
                    break;
                case 1:
                    Intent intent = new Intent(NewsDetailsActivity.this,MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                    break;
                case 2:
                    finish();
                    break;
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            switch (mode){
                case 0:
                    finish();
                    break;
                case 1:
                    Intent intent = new Intent(NewsDetailsActivity.this,MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                    break;
                case 2:
                    finish();
                    break;
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
