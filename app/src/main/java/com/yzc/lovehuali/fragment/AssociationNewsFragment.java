package com.yzc.lovehuali.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.baoyz.widget.PullRefreshLayout;
import com.yzc.lovehuali.NewsDetailsActivity;
import com.yzc.lovehuali.R;
import com.yzc.lovehuali.adapter.IntegrateNewsListviewAdapter;
import com.yzc.lovehuali.tool.ACache;
import com.yzc.lovehuali.tool.NetUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/2/1 0001.
 */
public class AssociationNewsFragment extends Fragment {

    private ListView lvIntegrateNews;
    private List<JSONObject> NewsList;
    private int page=1;
    private IntegrateNewsListviewAdapter adapter;
    private View listFootView;
    private PullRefreshLayout prlIntegrateNews;
    private ACache mCache;//全局缓存工具类对象
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_integrate_news, container, false);

        mCache = ACache.get(getActivity().getApplicationContext());

        final String URL = new String("http://61.160.137.196:18001/zsdxpt/findNewsByJsonp.action");
        NewsList = new ArrayList<JSONObject>();

        lvIntegrateNews = (ListView) rootView.findViewById(R.id.lvIntegrateNews);

        listFootView = getActivity().getLayoutInflater().inflate(R.layout.listview_foot_loading, null);
        lvIntegrateNews.addFooterView(listFootView);

        adapter = new IntegrateNewsListviewAdapter(getActivity(),R.layout.listview_integrate_news_cell,NewsList);
        lvIntegrateNews.setAdapter(adapter);

        lvIntegrateNews.setOnScrollListener(new AbsListView.OnScrollListener() {
            private int lastItem;
            private int count;
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                count = lvIntegrateNews.getCount()-1;

                if(lastItem == count  && scrollState == this.SCROLL_STATE_IDLE){
                    listFootView.setVisibility(view.VISIBLE);

                        getNewsList(URL);}


            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                lastItem = firstVisibleItem + visibleItemCount - 1;  //减1是因为加了FooterView
            }
        });

        lvIntegrateNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == lvIntegrateNews.getCount()-1){
                    return;
                }

                JSONObject NewsObject = NewsList.get(position);
                Intent i = new Intent(getActivity(), NewsDetailsActivity.class);
                try {
                    i.putExtra("title",NewsObject.getString("title"));
                    i.putExtra("publishUser",NewsObject.getString("publishUser"));
                    i.putExtra("publishDate",NewsObject.getString("publishDate").substring(0,10));
                    i.putExtra("context",NewsObject.getString("context"));

                    startActivity(i);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        //上滑刷新列表控件
        prlIntegrateNews = (PullRefreshLayout) rootView.findViewById(R.id.prlIntegrateNews);
        prlIntegrateNews.setRefreshStyle(PullRefreshLayout.STYLE_MATERIAL);//设置刷新风格为material
        //监听刷新并操作
        prlIntegrateNews.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(NetUtils.isConnected(getActivity())) {
                    page = 1;//设置页面为第一页，以获取最新的综合新闻
                    NewsList.clear();//清空原来的所有新闻数据，注意优化的时候要检查网络状况
                    int i = 1;
                    while (mCache.getAsJSONArray("integrateNewsFragment" + "&page=" + i) != null) {
                        mCache.remove("integrateNewsFragment" + "&page=" + i);
                        i++;
                    }//清除所有现有的页面缓存
                    getNewsList(URL);//获取新闻
                }else{
                    Toast.makeText(getActivity(),"网络不给力呀！请检查网络连接",Toast.LENGTH_SHORT).show();
                    prlIntegrateNews.setRefreshing(false);//结束刷新
                }
            }
        });


        getNewsList(URL);


        return rootView;
    }

    public void getNewsList(String Url){
        new AsyncTask<String, Void, Void>(){

            @Override
            protected Void doInBackground(String... params) {

                JSONArray NewsArray = mCache.getAsJSONArray("integrateNewsFragment" + "&page=" + page);
                //获取本地缓存，如果缓存不为空就直接读取缓存的新闻数据
                if(NewsArray != null) {
                     for (int i = 0; i < NewsArray.length(); i++) {
                            JSONObject NewsObject = null;
                            try {
                                NewsObject = NewsArray.getJSONObject(i);
                                NewsList.add(NewsObject);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        return null;
                    }

                try {
                    URL url = new URL(params[0]);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoOutput(true);
                    connection.setRequestMethod("POST");

                    OutputStreamWriter osw = new OutputStreamWriter(connection.getOutputStream());
                    BufferedWriter bw = new BufferedWriter(osw);
                    bw.write("callback=callNews&schoolCode=HLXY&page=" + page + "&rows=10&clubId=&type=1&_=" + System.currentTimeMillis());
                    bw.flush();

                    System.out.println(connection.getResponseCode());//检查服务器返回状态码，200为正常

                    StringBuilder jasonStringBuilder = new StringBuilder();

                    if (connection.getResponseCode() == 200) {
                        InputStream is = connection.getInputStream();
                        InputStreamReader isr = new InputStreamReader(is, "utf-8");
                        isr.skip(9);//跳过了前面九个字符,去掉json数据前面的不规范头部
                        BufferedReader br = new BufferedReader(isr);
                        String str;

                        while ((str = br.readLine()) != null) {
                            System.out.println(str);//输出读取的每一行数据
                            jasonStringBuilder.append(str);
                        }
                        br.close();//关闭BufferedReader。
                        isr.close();//关闭isr。

                        JSONObject answerJSONObject = new JSONObject(jasonStringBuilder.toString());

                        String massage = answerJSONObject.getString("msg");//请求返回的新闻jason数据是否成功，成功返回success

                        NewsArray = answerJSONObject.getJSONArray("items");

                        mCache.put("integrateNewsFragment" + "&page=" + page, NewsArray, ACache.TIME_DAY);//将获取到的新闻JsonArray缓存到本地

                        //调试功能，输出NewsArry信息。
                        for (int i = 0; i < NewsArray.length(); i++) {
                            JSONObject NewsObject = NewsArray.getJSONObject(i);
                            System.out.println("--------------------------------------");//分割线
                            System.out.println("title(标题)=" + NewsObject.getString("title"));
                            System.out.println("publishUser(发布用户)=" + NewsObject.getString("publishUser"));
                            System.out.println("Date(发布日期)=" + NewsObject.getString("publishDate"));
                            NewsList.add(NewsObject);
                        }


                    } else {
                        Toast.makeText(getActivity(), "网络不给力呀！", Toast.LENGTH_SHORT).show();
                    }


                } catch (MalformedURLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void result) {

                if(page == 1){
                    prlIntegrateNews.setRefreshing(false);//当获取的页数为第一页，也就是最新一页的时候，完成下拉刷新的操作
                }

                adapter.notifyDataSetChanged();
                page++;
                super.onPostExecute(result);
            }



        }.execute(Url);


    }
}
