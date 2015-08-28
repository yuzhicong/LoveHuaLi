package com.yzc.lovehuali.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.yzc.lovehuali.FragmentContainerActivity;
import com.yzc.lovehuali.NewsDetailsActivity;
import com.yzc.lovehuali.R;
import com.yzc.lovehuali.SchoolNoticeActivity;
import com.yzc.lovehuali.tool.ACache;
import com.yzc.lovehuali.widget.autoscrollviewpager.AutoScrollViewPager;

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
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import me.relex.circleindicator.CircleIndicator;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsListFragment extends Fragment {

    private ListView lvNewsList;
    private AutoScrollViewPager newsPager;
    private ACache mCache;

    private List<JSONObject> NewsList;
    private List<JSONObject> tempList;
    private NewsPagerAdapter pagerAdapter;
    private CircleIndicator pagerIndicator;

    public NewsListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_news_list, container, false);

        String academyNewsUrl = "http://61.160.137.196:18001/zsdxpt/findNewsByJsonp.action?" +
                "schoolCode=HLXY&page=1&rows=5&type=0&callback=callNews" +
                "&_=" + System.currentTimeMillis()+" ";
        mCache = ACache.get(getActivity());
        NewsList = new ArrayList<JSONObject>();
        tempList = new ArrayList<JSONObject>();
        getAcademyNewList(academyNewsUrl);

        newsPager = (AutoScrollViewPager) rootView.findViewById(R.id.newsPager);
        newsPager.setOffscreenPageLimit(5);

        pagerAdapter = new NewsPagerAdapter(getFragmentManager(),NewsList);
        newsPager.setAdapter(pagerAdapter);

        pagerIndicator = (CircleIndicator) rootView.findViewById(R.id.pagerIndicator);


        lvNewsList = (ListView) rootView.findViewById(R.id.lvNewsList);
        NewsListAdapter adapter = new NewsListAdapter(getActivity(),R.layout.listview_tool_cell);
        lvNewsList.setAdapter(adapter);

        lvNewsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                switch (position){
                    case 0:
                        intent.setClass(getActivity(), SchoolNoticeActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        intent.setClass(getActivity(), FragmentContainerActivity.class);
                        intent.putExtra("barTitle", "社团新闻");
                        startActivity(intent);
                        break;
                    case 2:
                        intent.setClass(getActivity(),FragmentContainerActivity.class);
                        intent.putExtra("barTitle","学院新闻");
                        startActivity(intent);
                        break;
                }
            }
        });

        return rootView;
    }
    class NewsListAdapter extends ArrayAdapter {
        private Context context;
        private int listViewCellId;
        private String NewsListName[] = {"校园通知","社团新闻","学院新闻"};
        private String NewsListDescription[] = {"放假，校园活动信息在这","社团协会的发展和活动情况","学院取得的成就展示"};
        private int NewsListIcon[] = {R.drawable.ic_new_notice,R.drawable.ic_new_association,R.drawable.ic_new_academy};
        public NewsListAdapter(Context context, int resource) {
            super(context, resource);
            this.context = context;
            this.listViewCellId = resource;
        }

        @Override
        public int getCount() {
            return NewsListName.length;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView icon = null;
            TextView name = null;
            TextView description = null;

            convertView = LayoutInflater.from(context).inflate(listViewCellId,null);

            icon = (ImageView) convertView.findViewById(R.id.ivToolItem);
            name = (TextView) convertView.findViewById(R.id.tvToolItemName);
            description = (TextView) convertView.findViewById(R.id.tvToolItemDescription);

            icon.setImageResource(NewsListIcon[position]);
            name.setText(NewsListName[position]);
            description.setText(NewsListDescription[position]);


            return convertView;
        }
    }

    class NewsPagerAdapter extends FragmentPagerAdapter {
        private List<JSONObject> jsonList;
        public NewsPagerAdapter(FragmentManager fm,List<JSONObject> list) {
            super(fm);
            this.jsonList = list;
        }

        @Override
        public Fragment getItem(int position) {
            JSONObject F = jsonList.get(position);
            try {
                final String title = F.getString("title");
                final String publishDate = F.getString("publishDate");
                final String context = F.getString("context");
                JSONArray S = F.getJSONArray("images");
                JSONObject Ss = S.getJSONObject(0);
                String picUri = Ss.getString("subUrl");

                Intent newsinfo = new Intent();
                newsinfo.putExtra("context", context);
                newsinfo.putExtra("title", title);
                newsinfo.putExtra("publishDate", publishDate);

                ImageAndTitleViewFragment iatf = new ImageAndTitleViewFragment(picUri,title,newsinfo);

                return iatf;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public int getCount() {
            return jsonList.size();
        }
    }

    public void getAcademyNewList(String Url){
        new AsyncTask<String, Void, Void>(){

            @Override
            protected Void doInBackground(String... params) {

                JSONArray academyNewsArray = null;
                academyNewsArray = mCache.getAsJSONArray("ShowNews");

                //获取本地缓存，如果缓存不为空就直接读取缓存的新闻数据
                if(academyNewsArray != null) {
                    for (int i = 0; i < academyNewsArray.length(); i++) {
                        JSONObject NewsObject = null;
                        try {
                            NewsObject = academyNewsArray.getJSONObject(i);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        tempList.add(NewsObject);
                    }
                    return null;
                }

                try {
                    URL url = new URL(params[0]);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                    System.out.println(connection.getResponseCode());//检查服务器返回状态码，200为正常

                    StringBuilder jasonStringBuilder = new StringBuilder();

                    if (connection.getResponseCode() == 200) {
                        InputStream is = connection.getInputStream();
                        InputStreamReader isr = new InputStreamReader(is, "utf-8");
                        isr.skip(9);
                        BufferedReader br = new BufferedReader(isr);
                        String str;

                        while ((str = br.readLine()) != null) {
                            System.out.println(str);//输出读取的每一行数据
                            jasonStringBuilder.append(str);
                        }
                        br.close();//关闭BufferedReader。
                        isr.close();//关闭isr。

                        JSONObject answerJSONObject = new JSONObject(jasonStringBuilder.toString());

                        //String massage = answerJSONObject.getString("msg");//请求返回的新闻jason数据是否成功，成功返回success

                        academyNewsArray = answerJSONObject.getJSONArray("items");

                        mCache.put("ShowNews", academyNewsArray, ACache.TIME_DAY);//将获取到的新闻JsonArray缓存到本地

                        //调试功能，输出NewsArry信息。
                        for (int i = 0; i < academyNewsArray.length(); i++) {
                            JSONObject NewsObject = academyNewsArray.getJSONObject(i);
                            System.out.println("--------------------------------------");//分割线
                            System.out.println("title(标题)=" + NewsObject.getString("title"));
                            System.out.println("Date(发布日期)=" + NewsObject.getString("publishDate"));
                            tempList.add(NewsObject);
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
                super.onPostExecute(result);
                for(int i=0;i<tempList.size();i++){
                    NewsList.add(tempList.get(i));
                }
                tempList.clear();
                pagerAdapter.notifyDataSetChanged();

                pagerIndicator.setViewPager(newsPager);

                newsPager.startAutoScroll(4500);
                newsPager.setInterval(4500);
                newsPager.setScrollDurationFactor(5);

            }



        }.execute(Url);


    }

}
