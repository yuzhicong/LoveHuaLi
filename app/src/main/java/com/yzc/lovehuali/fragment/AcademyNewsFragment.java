package com.yzc.lovehuali.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;


import com.yzc.lovehuali.NewsDetailsActivity;
import com.yzc.lovehuali.R;
import com.yzc.lovehuali.tool.ACache;
import com.yzc.lovehuali.tool.Getjson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AcademyNewsFragment extends Fragment {

	ListView newslist;
	private boolean isFirstRow;
	private boolean isLastRow;
	private View morenews;
	private TextView loadtv;
	private ProgressBar loadpb;
	int i = 1;
	String timemills = null;
    String NewData = null;
	private int count;
	List newslistitems = new ArrayList();
	Getjson newsjson = new Getjson();
	SimpleAdapter newsadapter;
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView =  inflater.inflate(R.layout.academy_newslist, container, false);
		newslist = (ListView) rootView.findViewById(R.id.newslv);
		morenews = getActivity().getLayoutInflater().inflate(R.layout.load, null);
		newslist.addFooterView(morenews);
		newsadapter = new SimpleAdapter(getActivity(), newslistitems, R.layout.academy_news_items, new String[]{"title","publishDate"}, new int[]{R.id.title,R.id.publishdata});
		newslist.setAdapter(newsadapter);
		timemills = System.currentTimeMillis()+" ";
		//String page = i + " ";
		String url = "http://61.160.137.196:18001/zsdxpt/findNewsByJsonp.action?" +
				"schoolCode=HLXY&page=" + i + "&rows=10&type=0&callback=callNews" +
				"&_=" + timemills;
		final ACache firstcache = ACache.get(getActivity().getApplicationContext());
		if(firstcache.getAsString("page_1") == null){
			newsjson.news(url);
		
		newsjson.setLoadDataComplete(new Getjson.isLoadDataListener() {
			@Override
			public void loadComplete(String newsjson) {
				// TODO Auto-generated method stub
				if(newsjson == "NwError"){
					Toast.makeText(getActivity(), "�����豸��������", Toast.LENGTH_LONG).show();
				}else{
					NewData= newsjson.substring(9, newsjson.lastIndexOf(")"));
                    firstcache.put("page_1", NewData,1*ACache.TIME_DAY);
                    DisposeNewsJ deal = new DisposeNewsJ();
                    deal.Dispose(NewData);
                    if(firstcache != null) {
                        System.out.println("-----------------------�������Ի��������-----------------------" + '\n' + firstcache.getAsString("page_1"));
                    }
				}
			}
		});
		}else{
			System.out.println("-------->>>>>>>�������Ի���<<<<<<--------");
			DisposeNewsJ deal = new DisposeNewsJ();
            deal.Dispose(firstcache.getAsString("page_1"));
		}
		newslist.setOnScrollListener(new NewsListListener());
		newslist.setOnItemClickListener(new ItemListener());
		return rootView;
	}
	
	class NewsListListener implements OnScrollListener{
		private int lastitem;
		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			// TODO Auto-generated method stub
			lastitem = firstVisibleItem + visibleItemCount - 1;  //��1����Ϊ������˸�addFooterView
			
		}

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			// TODO Auto-generated method stub
			if(lastitem == count  && scrollState == this.SCROLL_STATE_IDLE){
				morenews.setVisibility(view.VISIBLE);
				i = i + 1;
				System.out.println(i);
				final String page = "page_" + i;
				System.out.println(page);
				String url = "http://61.160.137.196:18001/zsdxpt/findNewsByJsonp.action?" +
						"schoolCode=HLXY&page=" + i + "&rows=10&type=0&callback=callNews" +
						"&_=" + timemills;
				final ACache othercache = ACache.get(getActivity().getApplicationContext());
				if(othercache.getAsString(page) == null){
					//Getjson newsjson = new Getjson();
					newsjson.news(url);
				newsjson.setLoadDataComplete(new Getjson.isLoadDataListener() {
					@Override
					public void loadComplete(String newsjson) {
						// TODO Auto-generated method stub
						if(newsjson == "NwError"){
							Toast.makeText(getActivity(), "�����豸��������", Toast.LENGTH_LONG).show();
						}else{
							newsjson = newsjson.substring(9, newsjson.lastIndexOf(")"));
							othercache.put(page, newsjson,1*ACache.TIME_DAY);
							DisposeNewsJ deal = new DisposeNewsJ();
		                    deal.Dispose(newsjson);
		                    if(othercache.getAsString(page) != null) {
		                        System.out.println("-----------------------�������Ի��������-----------------------" + '\n' + othercache.getAsString("page_1"));
		                    }
						}
					}
				});
                //morenews.setVisibility(View.GONE); 
				
				}else{
					System.out.println("-------->>>>>>>�������Ի���<<<<<<--------");
					DisposeNewsJ deal = new DisposeNewsJ();
		            deal.Dispose(othercache.getAsString(page));
				}
			}
		}
		
	}
	
	
	
	class ItemListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			int itemid = (int) newslist.getItemIdAtPosition(arg2);
			if(itemid == -1){
				System.out.println("������Ч�ĵ��!!!");
			}else{
				Intent newsinfo = new Intent();
				Map detailmap = (Map) newslistitems.get(itemid);
				String info = (String) detailmap.get("context");
				String newstitle = (String) detailmap.get("title");
				newsinfo.putExtra("context", info);
				newsinfo.putExtra("title", newstitle);
				newsinfo.setClass(getActivity(), NewsDetailsActivity.class);
				startActivity(newsinfo);
			}
		}
	}

    public class DisposeNewsJ{
        public void Dispose(String newsjson){
            try {
                JSONObject newsobject = new JSONObject(newsjson);
                JSONArray items = newsobject.getJSONArray("items");
                for(int i = 0;i < items.length();i++){
                    JSONObject eachnews = items.getJSONObject(i);
                    Map<String,Object> newsmap = new HashMap<String,Object>();
                    System.out.println("---------------------------");
                    String context = eachnews.getString("context");
                    String publishDate = eachnews.getString("publishDate");
                    publishDate = publishDate.substring(0, publishDate.lastIndexOf(" "));
                    String title = eachnews.getString("title");
                    newsmap.put("title", title);
                    newsmap.put("publishDate", publishDate);
                    newsmap.put("context",context);
                    System.out.println(context +'\n' + publishDate + '\n' + title + '\n');
                    newslistitems.add(newsmap);
                }
                newsadapter.notifyDataSetChanged();
                count = newslistitems.size();
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
