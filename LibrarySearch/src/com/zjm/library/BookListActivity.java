package com.zjm.library;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;


public class BookListActivity extends ActionBarActivity{

	private Toolbar mToolbar;
	private ListView booklist;
	private Handler LibraryH;
	private View morebooks;
	SimpleAdapter booklistadapter;
	List booklistitem = new ArrayList();
	String url;
	String line;
	int count;
	int page = 1;
	int OldPageNum = 1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.booklist);

		//设定状态栏的颜色，当版本大于4.4时起作用
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			SystemBarTintManager tintManager = new SystemBarTintManager(this);
			tintManager.setStatusBarTintEnabled(true);
			//此处可以重新指定状态栏颜色
			tintManager.setStatusBarTintColor(Color.parseColor("#2196F3"));
		}

		mToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
		mToolbar.setTitle("图书列表");
		mToolbar.setTitleTextColor(Color.WHITE);
		setSupportActionBar(mToolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		Intent DataRec = getIntent();
		booklist = (ListView) findViewById(R.id.booklist);
		morebooks = BookListActivity.this.getLayoutInflater().inflate(R.layout.load, null);
		booklist.addFooterView(morebooks);
		booklistadapter = new SimpleAdapter(BookListActivity.this, booklistitem, R.layout.bookitem, new String[]{"bookName","bookMsg"}, new int[]{R.id.bookname,R.id.bookmsg});
		booklist.setAdapter(booklistadapter);
		url = DataRec.getStringExtra("url");
		NetWork getbook = new NetWork(url);
		getbook.start();
		LibraryH = new NwHandler();
		booklist.setOnItemClickListener(new BookitemListener());
		booklist.setOnScrollListener(new BooklistListener());
	}

    class BookitemListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			int itemid = (int) booklist.getItemIdAtPosition(position);
			if(itemid == -1){
				System.out.println("这是无效点击!!!!!!");
			}else{
				Intent bookdetail = new Intent();
				HashMap selbook = (HashMap) booklistitem.get(itemid);
				String bookhref = (String) selbook.get("href");
				bookhref = bookhref.substring(1,bookhref.length());
				String itemurl = "http://61.144.79.226:8007/" + bookhref;
				bookdetail.putExtra("itemurl", itemurl);
				bookdetail.setClass(BookListActivity.this, BookDetailActivity.class);
				startActivity(bookdetail);
			}
		}
	}
	
	class BooklistListener implements OnScrollListener{
		private int lastitem;
		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			// TODO Auto-generated method stub
			if(lastitem == count  && scrollState == this.SCROLL_STATE_IDLE){
				morebooks.setVisibility(view.VISIBLE);
				String pageKVO ="page=" + (page - 1);
				String pageKVN = "page=" + page;
				System.out.println(pageKVO + "----" + pageKVN);
				url = url.replace(pageKVO, pageKVN);
				System.out.println(url);
				ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo info = connMgr.getActiveNetworkInfo();
				if(info == null){
					Toast.makeText(BookListActivity.this, "网络出现问题,请检查网络连接!", Toast.LENGTH_LONG).show();
				}else{
					boolean isAlive = info.isAvailable();
					if(isAlive){
						if(OldPageNum == page){
							System.out.println("还没刷新出来!!!");
							System.out.println(OldPageNum);
						}else{
							NetWork getbook = new NetWork(url);
							getbook.start();
							OldPageNum++;
							System.out.println(OldPageNum);
						}
					}else{
						Toast.makeText(BookListActivity.this, "信号不是很好,请稍后再试!", Toast.LENGTH_LONG).show();
					}
				}
			}
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			// TODO Auto-generated method stub
			lastitem = firstVisibleItem + visibleItemCount- 1;
		}
	}

	class NwHandler extends Handler{
		@Override
		public void handleMessage(Message msg) {
			line = (String) msg.obj;
			System.out.println("============================\n"+line);
			Dealjson dealjson = new Dealjson(line);
		}
	}
	
	class NetWork extends Thread{
		String url;
		public NetWork(String url){
			this.url = url;
			System.out.println(this.url);
		}
		@Override
		public void run() {
			HttpClient NwClient = new DefaultHttpClient();
			HttpGet NwGet = new HttpGet(url);
			try {
				HttpResponse NwRespond = NwClient.execute(NwGet);
				if(NwRespond.getStatusLine().getStatusCode() == 200){
					HttpEntity NwEntity = NwRespond.getEntity();
					BufferedReader NwReader = new BufferedReader(new InputStreamReader(NwEntity.getContent()));
					String jsonline;
					StringBuilder newsbuilder = new StringBuilder();
					while((jsonline = NwReader.readLine()) != null)
					{
						newsbuilder.append(jsonline);
					}
					jsonline = newsbuilder.toString();
					//System.out.println(jsonline);
					Message msg = LibraryH.obtainMessage();
					msg.obj = jsonline;
					LibraryH.sendMessage(msg);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	class Dealjson{
		String line;
		public Dealjson(String line){
			try {
				JSONObject jsondata = new JSONObject(line);
				int code = jsondata.getInt("code");
				String message = jsondata.getString("message");
				System.out.println(">>>>>>>>>>>>>>>>>>>>>>>><<<<<<<<<<<<<<<<<<<<<<<<<<<");
				System.out.println(code + ":" + message);
				if(code == 200){
					JSONArray bookitem = jsondata.getJSONArray("data");
					for(int i = 0;i < bookitem.length();i++){
						JSONObject eachbook = bookitem.getJSONObject(i);
						HashMap bookmap = new HashMap();
						String bookname = eachbook.getString("bookName");
						//System.out.println(bookname);
						bookmap.put("bookName", bookname);
						String bookmsg = eachbook.getString("bookMsg");
						bookmsg = bookmsg.replaceAll("\t", "");
						bookmsg = bookmsg.replaceAll("&nbsp", "");
						bookmsg = bookmsg.substring(2,bookmsg.length()-1);
						//System.out.println(bookmsg);
						bookmap.put("bookMsg", bookmsg);
						String href = eachbook.getString("href");
						//System.out.println(href);
						bookmap.put("href", href);
						booklistitem.add(bookmap);
					}
					booklistadapter.notifyDataSetChanged();
					count = booklistitem.size();
					page = page +1;
				}
				if(code == 400){
                    finish();
					Toast.makeText(BookListActivity.this, "你输入的关键字有误,请重新输入!", Toast.LENGTH_LONG).show();
				}
				/*if(code == 404){
					
				}*/
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
