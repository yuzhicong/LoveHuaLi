package com.yzc.lovehuali.fragment;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.yzc.lovehuali.R;
import com.yzc.lovehuali.adapter.CollegeActivityRecyclerviewAdapter;
import com.yzc.lovehuali.tool.ACache;

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
 * A simple {@link Fragment} subclass.
 */
public class CollegeActivityFragment extends Fragment {


    public CollegeActivityFragment() {
        // Required empty public constructor
    }


    private View rootView;
    private RecyclerView rvCollegeActivity;
    private LinearLayoutManager mLayoutManager;
    private CollegeActivityRecyclerviewAdapter mAdapter;
    private List<JSONObject> collegeActivityList;
    private ACache mCache;//全局缓存工具类对象
    private int page=1;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_college_activity, container, false);

        mCache = ACache.get(getActivity().getApplicationContext());

        final String URL = new String("http://1.lovehuali.sinaapp.com/getActivityFromDatabase.php");

        collegeActivityList = new ArrayList<JSONObject>();

        rvCollegeActivity = (RecyclerView)rootView.findViewById(R.id.rvCollegeActivity);
        //创建默认的线性LayoutManager
        mLayoutManager = new LinearLayoutManager(getActivity());
        rvCollegeActivity.setLayoutManager(mLayoutManager);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        //rvCollegeActivity.setHasFixedSize(true);
        //创建并设置Adapter
        mAdapter = new CollegeActivityRecyclerviewAdapter(collegeActivityList);
        rvCollegeActivity.setAdapter(mAdapter);
        rvCollegeActivity.setOnScrollListener(new RecyclerView.OnScrollListener() {

            private int lastItem;
            private int count;
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                count = mLayoutManager.getItemCount();
                lastItem = mLayoutManager.findLastVisibleItemPosition();
                if(lastItem == count && newState == recyclerView.SCROLL_STATE_IDLE){
                    System.out.println("page:"+ page);
                    getCollegeActivityList(URL);
                }
            }
        });

        getCollegeActivityList(URL);
        return rootView;
    }

    public void getCollegeActivityList(String Url){
        new AsyncTask<String, Void, Void>(){

            @Override
            protected Void doInBackground(String... params) {

                JSONArray collegeActivityArray = mCache.getAsJSONArray("CollegeActivityFragment" + "&page=" + page);

                //获取本地缓存，如果缓存不为空就直接读取缓存的新闻数据
                if(collegeActivityArray != null) {
                    for (int i = 0; i < collegeActivityArray.length(); i++) {
                        JSONObject collegeActivityObject = null;
                        try {
                            collegeActivityObject = collegeActivityArray.getJSONObject(i);
                            collegeActivityList.add(collegeActivityObject);
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
                    bw.write("page=" + page );
                    bw.flush();

                    System.out.println(connection.getResponseCode());//检查服务器返回状态码，200为正常

                    StringBuilder jasonStringBuilder = new StringBuilder();

                    if (connection.getResponseCode() == 200) {
                        InputStream is = connection.getInputStream();
                        InputStreamReader isr = new InputStreamReader(is, "utf-8");
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

                        collegeActivityArray = answerJSONObject.getJSONArray("data");

                        mCache.put("CollegeActivityFragment" + "&page=" + page, collegeActivityArray, ACache.TIME_DAY);//将获取到的新闻JsonArray缓存到本地

                        //调试功能，输出NewsArry信息。
                        for (int i = 0; i < collegeActivityArray.length(); i++) {
                            JSONObject NewsObject = collegeActivityArray.getJSONObject(i);
                            System.out.println("--------------------------------------");//分割线
                            System.out.println("title(标题)=" + NewsObject.getString("title"));
                            System.out.println("publishUser(发布用户)=" + NewsObject.getString("publishUser"));
                            System.out.println("Date(发布日期)=" + NewsObject.getString("publishDate"));
                            collegeActivityList.add(NewsObject);
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

                mAdapter.notifyDataSetChanged();
                page++;
                super.onPostExecute(result);
            }



        }.execute(Url);


    }


}
