package com.zjm.library;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class BookDetailActivity extends ActionBarActivity{

	private Toolbar mToolbar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bookdetail);

		//设定状态栏的颜色，当版本大于4.4时起作用
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			SystemBarTintManager tintManager = new SystemBarTintManager(this);
			tintManager.setStatusBarTintEnabled(true);
			//此处可以重新指定状态栏颜色
			tintManager.setStatusBarTintColor(Color.parseColor("#2196F3"));
		}

		mToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
		mToolbar.setTitle("图书详情");
		mToolbar.setTitleTextColor(Color.WHITE);
		setSupportActionBar(mToolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		Intent bookdetail = getIntent();
		String url = bookdetail.getStringExtra("itemurl");
		WebView bookdetailmsg = (WebView) findViewById(R.id.bookdetail);
		bookdetailmsg.getSettings().setJavaScriptEnabled(true);
		bookdetailmsg.setWebChromeClient(new WebChromeClient());
		bookdetailmsg.setWebViewClient(new WebViewClient());
		bookdetailmsg.loadUrl(url);
	}
}
