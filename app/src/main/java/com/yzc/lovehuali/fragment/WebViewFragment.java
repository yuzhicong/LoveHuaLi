package com.yzc.lovehuali.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.yzc.lovehuali.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class WebViewFragment extends Fragment {

    private WebView webView;
    private String Url;
    public WebViewFragment(String Url) {
        this.Url = Url;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_web_view, container, false);
        webView = (WebView) rootView.findViewById(R.id.webView);
        WebSettings settings = webView.getSettings();
        //适应屏幕
        settings.setUseWideViewPort(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setJavaScriptEnabled(true);
        settings.setLoadsImagesAutomatically(false);
        webView.setWebViewClient(new webViewClient());
        webView.loadUrl(Url);

        return rootView;

    }

    class webViewClient extends WebViewClient {

        //重写shouldOverrideUrlLoading方法，使点击链接后不使用其他的浏览器打开。

        @Override

        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            view.loadUrl(url);
            //如果不需要其他对点击链接事件的处理返回true，否则返回false

            return true;

        }

    }
}
