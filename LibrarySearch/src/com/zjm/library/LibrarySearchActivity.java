package com.zjm.library;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;

public class LibrarySearchActivity extends ActionBarActivity {

	private com.rengwuxian.materialedittext.MaterialEditText inputText;
	private Button Search;
	private Spinner typeS;
	private SpinnerAdapter spadaType;
	//private View searchload;
	String url = null;
	String inputString = null;
	String typ = null;
    static Boolean FirstTime = true;

	private Toolbar mToolbar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.librarysearch);

		//设定状态栏的颜色，当版本大于4.4时起作用
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			SystemBarTintManager tintManager = new SystemBarTintManager(this);
			tintManager.setStatusBarTintEnabled(true);
			//此处可以重新指定状态栏颜色
			tintManager.setStatusBarTintColor(Color.parseColor("#2196F3"));
		}

		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		mToolbar.setTitle("图书馆");
		mToolbar.setTitleTextColor(Color.WHITE);
		setSupportActionBar(mToolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		Search = (Button) findViewById(R.id.search);
		inputText = (MaterialEditText) findViewById(R.id.etKeyWord);
		typeS = (Spinner) findViewById(R.id.type);

		String typearray[] = {"任意字段","题名","作者","ISBN"};
		spadaType = new ArrayAdapter<String>(LibrarySearchActivity.this, R.layout.spinner_item, typearray);
		typeS.setAdapter(spadaType);

		Search.setOnClickListener(new buttonlistener());
		//searchload = LibrarySearch.this.getLayoutInflater().inflate(R.layout.searchload, null);
		typeS.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				typ = parent.getItemAtPosition(position).toString();	
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
	}
	
	class buttonlistener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo info = connMgr.getActiveNetworkInfo();
			inputString = inputText.getText().toString();
			inputString = inputString.replace(" ", "+");
			if(inputString.equals("")){
				Toast.makeText(LibrarySearchActivity.this, "= =输入是空的,搜毛线啊!", Toast.LENGTH_LONG).show();
			}
			//http://1.lovehuali.sinaapp.com/getbook.php
			else{
				switch(typ){
				case"任意字段":
					url = "http://1.lovehuali.sinaapp.com/getbook.php?kw=" + inputString + "&searchtype=anywords&page=1&xc=3";
					break;
				case"题名":
					url = "http://1.lovehuali.sinaapp.com/getbook.php?kw=" + inputString + "&searchtype=title&page=1&xc=3";
					break;
				case"作者":
					url = "http://1.lovehuali.sinaapp.com/getbook.php?kw=" + inputString + "&searchtype=author&page=1&xc=3";
					break;
				case"ISBN":
					url = "http://1.lovehuali.sinaapp.com/getbook.php?kw=" + inputString + "&xc=3&searchtype=isbn_f";
					break;
				default:
					Toast.makeText(LibrarySearchActivity.this, "出现错误", Toast.LENGTH_LONG).show();
					break;
				}
				System.out.println(url);
				Intent Backdata = new Intent();
				Backdata.putExtra("url", url);
				Backdata.setClass(LibrarySearchActivity.this, BookListActivity.class);
				if(info == null){
					Toast.makeText(LibrarySearchActivity.this, "网络出现问题,请检查网络连接!", Toast.LENGTH_LONG).show();
				}else{
					boolean isAlive = info.isAvailable();
					System.out.println(isAlive);
					if(isAlive){
					    startActivity(Backdata);
					}else{
						Toast.makeText(LibrarySearchActivity.this, "信号不是很好,请稍后再试!", Toast.LENGTH_LONG).show();
					}
				}
			}
		}	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		MenuInflater noticemenu = getMenuInflater();
		noticemenu.inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		Intent collectionlist = new Intent(LibrarySearchActivity.this,BookCollectionActivity.class);
		startActivity(collectionlist);
		return super.onOptionsItemSelected(item);
	}

}
