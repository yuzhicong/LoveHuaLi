package com.yzc.lovehuali;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.yzc.lovehuali.tool.SystemBarTintManager;

/**
 * 使用说明，本Activity为加载网页的作用。
 * 加载的网页地址 用Intent的 "url" 值来指定
 * 默认加载完成的标题是 网页的标题
 * 支持个性化定制，用Intent的 "style" 带#号前缀的颜色值来指定，否则为默认主题样式。
 */
public class LoadWebPageActivity extends ActionBarActivity {

    private Toolbar mToolbar;
    private WebView wvNews;
    private String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);

        Intent i = getIntent();
        String style = i.getStringExtra("style");

        //设定状态栏的颜色，当版本大于4.4时起作用
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            //此处可以重新指定状态栏颜色
            if(style!=null) {
                tintManager.setStatusBarTintColor(Color.parseColor(style));
            }else{
            tintManager.setStatusBarTintResource(R.color.colorPrimaryDark);}
        }

        url = i.getStringExtra("url");

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitleTextColor(Color.WHITE);
        String title = i.getStringExtra("title");
        if(title!=null){
            mToolbar.setTitle(title);
        }else{
        mToolbar.setTitle("正在加载...");}

        if(style!=null){
            mToolbar.setBackgroundColor(Color.parseColor(style));
        }

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

            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                getSupportActionBar().setTitle(title);
            }

        });

        //wvNews.loadDataWithBaseURL("about:blank", newsHtml.toString(), "text/html", "utf-8", null);
        wvNews.loadUrl(url);
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
