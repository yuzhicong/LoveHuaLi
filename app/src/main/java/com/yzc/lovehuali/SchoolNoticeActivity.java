package com.yzc.lovehuali;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
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

import com.yzc.lovehuali.tool.ACache;
import com.yzc.lovehuali.tool.Getjson;
import com.yzc.lovehuali.tool.SystemBarTintManager;


public class SchoolNoticeActivity extends ActionBarActivity{

	List noticelistitems = new ArrayList();
	Getjson noticejson = new Getjson();
	SimpleAdapter noticeadapter;
	ListView noticelist;
	private View morenews;
	private int i = 1;
	private int count;
	String timemills = null;

    private Toolbar mToolbar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_school_notice);

        //设定状态栏的颜色，当版本大于4.4时起作用
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            //此处可以重新指定状态栏颜色
            tintManager.setStatusBarTintResource(R.color.colorPrimaryDark);
        }


        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("校园通知");// 标题的文字需在setSupportActionBar之前，不然会无效
        mToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		noticelist = (ListView) findViewById(R.id.noticeslv);
		morenews = SchoolNoticeActivity.this.getLayoutInflater().inflate(R.layout.listview_foot_loading, null);
		noticelist.addFooterView(morenews);
		noticeadapter = new SimpleAdapter(SchoolNoticeActivity.this, noticelistitems, R.layout.noticeitem, new String[]{"title","publishPer","publishTime"}, new int[]{R.id.noticetitle,R.id.publishper,R.id.publishtime});
		noticelist.setAdapter(noticeadapter);
		timemills = System.currentTimeMillis()+" ";
		String url = "http://202.102.101.78:18001/frametest/findNotifyByJsonp.action?" +
				"callback=callNotify&schoolCode=HLXY&page=" + i + "&rows=7&type=0" +
				"&_=" + timemills;
		final ACache Noticefcache = ACache.get(SchoolNoticeActivity.this.getApplicationContext());
		if(Noticefcache.getAsString("Noticepage_1") == null){
		noticejson.news(url);
		noticejson.setLoadDataComplete(new Getjson.isLoadDataListener() {
			@Override
			public void loadComplete(String noticejson) {
				// TODO Auto-generated method stub
				if(noticejson == "NwError"){
					Toast.makeText(SchoolNoticeActivity.this, "请检查设备网络连接", Toast.LENGTH_LONG).show();
				}else{
					noticejson= noticejson.substring(11, noticejson.lastIndexOf(")"));
					Noticefcache.put("Noticepage_1", noticejson,1*ACache.TIME_DAY);
					DisposeNoticeJ deal = new DisposeNoticeJ();
					deal.Dispose(noticejson);
					if(Noticefcache.getAsString("Noticepage_1") != null) {
                        System.out.println("-----------------------这是来自缓存的数据-----------------------" + '\n' + Noticefcache.getAsString("Noticepage_1"));
                    }
				}
			}
		});
		}else{
			System.out.println("-------->>>>>>>数据来自缓存<<<<<<--------");
			DisposeNoticeJ deal = new DisposeNoticeJ();
            deal.Dispose(Noticefcache.getAsString("Noticepage_1"));
		}
		noticelist.setOnItemClickListener(new ItemListener());
		noticelist.setOnScrollListener(new NoticeListListener());
	}
	
	class NoticeListListener implements OnScrollListener{
		private int lastitem;
		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			// TODO Auto-generated method stub
			lastitem = firstVisibleItem + visibleItemCount - 1;  //减1是因为上面加了个addFooterView
		}

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			// TODO Auto-generated method stub
			if(lastitem == count  && scrollState == this.SCROLL_STATE_IDLE){
				morenews.setVisibility(view.VISIBLE);
				i = i + 1;
				final String page = "Noticepage_" + i;
				System.out.println(page);
				String url = "http://202.102.101.78:18001/frametest/findNotifyByJsonp.action?" +
						"callback=callNotify&schoolCode=HLXY&page=" + i + "&rows=5&type=0" +
						"&_=" + timemills;
				final ACache Noticeocache = ACache.get(SchoolNoticeActivity.this.getApplicationContext());
				if(Noticeocache.getAsString(page) == null){
					//Getjson newsjson = new Getjson();
					noticejson.news(url);
				noticejson.setLoadDataComplete(new Getjson.isLoadDataListener() {
					@Override
					public void loadComplete(String noticejson) {
						// TODO Auto-generated method stub
						if(noticejson == "NwError"){
							Toast.makeText(SchoolNoticeActivity.this, "请检查设备网络连接", Toast.LENGTH_LONG).show();
						}else{
							noticejson = noticejson.substring(11, noticejson.lastIndexOf(")"));
							Noticeocache.put(page, noticejson,1*ACache.TIME_DAY);
							DisposeNoticeJ deal = new DisposeNoticeJ();
		                    deal.Dispose(noticejson);
		                    if(Noticeocache.getAsString(page) != null) {
		                        System.out.println("-----------------------这是来自缓存的数据-----------------------" + '\n' + Noticeocache.getAsString(page));
		                    }
						}
					}
				});
                //morenews.setVisibility(View.GONE); 
				
				}else{
					System.out.println("-------->>>>>>>数据来自缓存<<<<<<--------");
					DisposeNoticeJ deal = new DisposeNoticeJ();
		            deal.Dispose(Noticeocache.getAsString(page));
				}
			}
		}
		
	}
	
	class ItemListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			int itemid = (int) noticelist.getItemIdAtPosition(arg2);
			if(itemid == -1){
				System.out.println("这是无效的点击!!!");
			}else{
			Intent noticeinfo = new Intent();
			Map detailmap = (Map) noticelistitems.get(itemid);
			String info = (String) detailmap.get("context");
			String noticetitle = (String) detailmap.get("title");
			noticeinfo.putExtra("context", info);
			noticeinfo.putExtra("title", noticetitle);
			noticeinfo.putExtra("publishDate",(String) detailmap.get("publishTime"));
			noticeinfo.setClass(SchoolNoticeActivity.this, NewsDetailsActivity.class);
			startActivity(noticeinfo);
			}
		}
	}
	
	public class DisposeNoticeJ{
		public void Dispose(String noticejson){
			try {
				JSONObject noticeobject = new JSONObject(noticejson);
				JSONArray items = noticeobject.getJSONArray("items");
				for(int i = 0;i < items.length();i++){
					JSONObject eachnotice = items.getJSONObject(i);
					Map<String,String> noticesmap = new HashMap<String,String>();
					System.out.println("---------------------------");
					String context = eachnotice.getString("context");
					String publishTime = eachnotice.getString("publishTime");
					publishTime = publishTime.substring(0, publishTime.lastIndexOf(" "));
					String publishPer = eachnotice.getString("publishPer");
					String title = eachnotice.getString("title");
					noticesmap.put("title", title);
					noticesmap.put("publishTime", publishTime);
					noticesmap.put("context",context);
					noticesmap.put("publishPer", publishPer);
					System.out.println(context +'\n' + publishTime + '\n' + title + '\n' + publishPer + '\n');
					noticelistitems.add(noticesmap);
					//System.out.println(noticelistitems.size());
				}
				noticeadapter.notifyDataSetChanged();
				count = noticelistitems.size();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}
