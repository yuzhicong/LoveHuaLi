package com.yzc.lovehuali;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.yzc.lovehuali.tool.HtmlDataOptimizeTool;
import com.yzc.lovehuali.tool.SystemBarTintManager;

import org.jsoup.nodes.Document;


public class NewsDetailsActivity extends ActionBarActivity {

    private Toolbar mToolbar;
    private WebView wvNews;
    private String Context;
    private String NewBar = "<p style=\"text-align: center;\">\n" +
            "    <span style=\"font-size: 20px;\"><strong><br/></strong></span>\n" +
            "</p>\n" +
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
            "<p>\n" +
            "    <span style=\"color: rgb(127, 127, 127); font-size: 14px;\"></span><br/>\n" +
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
        mToolbar.setTitle("新闻内容");
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
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
