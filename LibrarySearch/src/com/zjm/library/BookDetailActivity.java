package com.zjm.library;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class BookDetailActivity extends ActionBarActivity{

	private Toolbar mToolbar;
	private BookCollectionDBHelper helper;
	static String bookhref;
	static Intent bookdetail;
	String FromActivity;
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

		bookdetail = getIntent();

		bookhref = bookdetail.getStringExtra("bookhref");
		FromActivity = bookdetail.getStringExtra("FromActivity");
		String url = "http://61.144.79.226:8007/" + bookhref;
		WebView bookdetailmsg = (WebView) findViewById(R.id.bookdetail);
		bookdetailmsg.getSettings().setJavaScriptEnabled(true);
		bookdetailmsg.setWebChromeClient(new WebChromeClient());
		bookdetailmsg.setWebViewClient(new WebViewClient());
		bookdetailmsg.loadUrl(url);
		helper = new BookCollectionDBHelper(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater noticemenu = getMenuInflater();
		if(FromActivity.equals("BookCollectionActivity")){
			System.out.println(">>>>>>>>>>>>>>>>>>>>>>来自BookCollectionActivity");
		}else if(FromActivity.equals("BookListActivity")) {
			System.out.println(">>>>>>>>>>>>>>>>>>>>>>来自BookListActivity");
			noticemenu.inflate(R.menu.main, menu);
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		//System.out.println(bookname);
		//System.out.println(bookmsg);
		//System.out.println(bookhref);
		String bookname = bookdetail.getStringExtra("bookname");
		String  bookmsg = bookdetail.getStringExtra("bookmsg");
		String R1 = "\\n(.*?)\\n(.*?)\\n";
		String R2 = "[0-9]*、";
		Pattern pattern = Pattern.compile(R1);
		Matcher matcher = pattern.matcher(bookmsg);
		StringBuffer sbr = new StringBuffer();
		while (matcher.find()) {
			matcher.appendReplacement(sbr, "\n");
		}
		matcher.appendTail(sbr);
		bookmsg = sbr.toString();
		pattern = Pattern.compile(R2);
		matcher = pattern.matcher(bookname);
		StringBuffer sbr1 = new StringBuffer();
		while (matcher.find()) {
			matcher.appendReplacement(sbr1, "");
		}
		matcher.appendTail(sbr1);
		bookname = sbr1.toString();
		dialog(bookname,bookmsg,bookhref);
		return super.onOptionsItemSelected(item);
	}

	public void dialog(String bookName,String bookMsg,String href){
		AlertDialog.Builder builder = new Builder(BookDetailActivity.this);
		builder.setMessage("确定加入收藏吗？");
		builder.setTitle("提示");
		final String bookname = bookName;
		final String bookmsg = bookMsg;
		final String bookhref = href;
		builder.setNegativeButton("确定", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				//System.out.println(">>>>"+bookname+"<<<<");
				//System.out.println(bookmsg);
				//System.out.println(bookhref);
				if (helper.Check(bookname)) {
					Toast.makeText(BookDetailActivity.this, "收藏列表已经存放此书!", Toast.LENGTH_LONG).show();
				} else {
					helper.insert(bookname, bookmsg, bookhref);
					Toast.makeText(BookDetailActivity.this, "已成功加入收藏", Toast.LENGTH_LONG).show();
				}
			}
		});
		builder.setPositiveButton("取消", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				//Toast.makeText(BookDetail.this, "取消", Toast.LENGTH_LONG).show();
			}
		});
		builder.create().show();
	}

}
