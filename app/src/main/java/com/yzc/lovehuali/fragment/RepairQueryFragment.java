package com.yzc.lovehuali.fragment;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.yzc.lovehuali.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 镜界 on 2015/8/28 0028.
 * 8/29:功能性测试成功
 * 8/29:优化输入为空的检查提示
 * 8/31:初步实现,预输入用户输入的手机号
 * 8/31:改善布局配色和样式
 * -----------------
 * 优化网页解析速度
 * 点击子item可查看详细
 *      a:OnItemClickListener接口，根据position重新加载详细信息到新的碎片中布局
 *      b:数据库
 * 学校渣网络的友好错误界面，问题是没法模拟
 *
 *
 */
public class RepairQueryFragment extends Fragment {

    EditText etPhone;
    Button btnWeb;
    ProgressBar pbWeb;
    ListView lvWeb;
    SimpleAdapter adapter;
    ArrayList<HashMap<String, String>> itemInfoArrayList;
    CardView cardView;
    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.repair_query,container,false);
        Log.e("Query", "3-onCreateView");

        //1实例化控件
        Log.e("MainActivity", "2实例化控件");
        etPhone = (EditText)view.findViewById(R.id.et_phone);
        pbWeb = (ProgressBar)view.findViewById(R.id.pb_web);
        lvWeb = (ListView)view.findViewById(R.id.lv_web);
        cardView = (CardView)view.findViewById(R.id.cardview);

        //5如果有，填充用户输入过的手机号
        putPhone();

        //10按钮点击事件，检查用户情况
        btnWeb = (Button)view.findViewById(R.id.btn_web);
        btnWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("开始时间", ""+System.nanoTime());
                Log.e("MainActivity", "3进入点击事件");
                String strOriginPhone=etPhone.getText().toString();
                if(TextUtils.isEmpty(strOriginPhone)){
                    etPhone.setError("说好的联系方式呢?");
                    Log.e("MainActivity", "4输入为空");
                }else{
                    pbWeb.setVisibility(View.VISIBLE);
                    String netOrigin = "http://hq.hualixy.com/RepairModule/Repair/Show.aspx?RepairID=" + strOriginPhone;
                    Log.e("MainActivity", "4查询的号码是" + strOriginPhone);
                    Toast.makeText(getActivity(),"正在查询",Toast.LENGTH_SHORT).show();
                    itemInfoArrayList = new ArrayList<>();
                    new PhoneNumAsyncTask().execute(netOrigin);
                    //存储用户输入的手机号下次启动可以填充
                    savePhone(strOriginPhone);
                }
            }
        });


        //50每个报修单的详情点击事件
        lvWeb.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:

                        break;
                    case 1:

                        break;
                    case 2:

                        break;
                    case 3:

                        break;
                    case 4:

                        break;
                    case 5:

                        break;
                    case 6:

                        break;

                }
            }
        });

        return view;
    }

    public void savePhone(String str){
        //存储用户输入的手机号下次启动可以填充
        editor = sharedPreferences.edit();
        editor.putString("user_phone",str);
        editor.putBoolean("user", true);
        editor.commit();
        Log.e("savePhone", "保存用户手机号" + str);
    }

    public void putPhone(){
        //5如果有，填充用户输入过的手机号
        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getActivity());
        if(true){
            String spPhone = sharedPreferences.getString("user_phone","");

            etPhone.setText(spPhone);

            Log.e("putPhone", "填充用户手机号" + spPhone);
        }
    }

    class PhoneNumAsyncTask extends AsyncTask<String,Void,Void>{

        int arrayListSize;
        List arrayList = new ArrayList<>();//记录报修记录的数目
        String strB = "http://hq.hualixy.com/RepairModule/Repair/";

        @Override
        protected Void doInBackground(String... params) {
            Log.e("doInBackground","进入异步");
            StringBuilder sbResult = new StringBuilder("");
            try {
                URLConnection urlConn = new URL(params[0]).openConnection();
                BufferedReader br = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
                String strRegex = "Show.aspx\\?id=\\w{5}";
                String strLine ;
                Pattern p = Pattern.compile(strRegex);
                Matcher m ;
                while ((strLine = br.readLine()) != null) {
                    m = p.matcher(strLine);
                    if (m.find()) {
                        //如果找到类似Show.aspx?id=23333的字符串就放进arrayList里面
                        arrayList.add(m.group());
                        Log.e("doInBackground", "找到一条报修记录:"+m.group());
                    }
                }
                if (arrayList.size() > 0) {
                    arrayListSize = arrayList.size();
                    for (int i = 0; i < arrayListSize; i++) {
                        this.catchItem(strB + arrayList.get(i));
                    }
                    Log.e("doInBackground", "总共有几条记录:"+arrayList.size());
                    //实例化适配器
                    Log.e("doInBackground", "itemInfoArrayList总数:" +itemInfoArrayList.size());
                    adapter = new SimpleAdapter(getActivity(),
                            itemInfoArrayList,
                            R.layout.repair_item,
                            new String[]{"lblRepairContentID", "lblReportTime", "lblCompleteDesribe"},
                            new int[]{R.id.tv_repair_content, R.id.tv_report_time, R.id.tv_complete_describe}
                    );
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        //


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.e("onPostExecute", "进入onPostExecute");
            if (arrayList.size() == 0) {
                Log.e("onPostExecute", "没有报修记录");
                Toast.makeText(getActivity(), "没有相关报修记录", Toast.LENGTH_SHORT);
            } else {
                lvWeb.setAdapter(adapter);
                pbWeb.setVisibility(View.GONE);
                Log.e("结束时间", ""+System.nanoTime());

            }
        }

        //
        public void catchItem(String str) {
            Log.e("doInBackground", "catchItem");
            String strRegexAll = "ctl00_ContentPlaceHolder1_(\\w*)\">(.*)(<br>|</span>)";
            String strLine2;
            HashMap<String, String> eachItemMap = new HashMap<>();
            try {
                URLConnection urlConn2 = new URL(str).openConnection();
                BufferedReader br2 = new BufferedReader(new InputStreamReader(urlConn2.getInputStream()));
                Pattern p2All = Pattern.compile(strRegexAll);
                Matcher m2All = null;
                while ((strLine2 = br2.readLine()) != null) {
                    m2All=p2All.matcher(strLine2);
                    if(m2All.find()){
                        Log.e("m2All","group[1]="+m2All.group(1)+"  group[2]="+m2All.group(2));
                        eachItemMap.put(m2All.group(1), m2All.group(2));
                    }
                }
                itemInfoArrayList.add(eachItemMap);
//                Log.e("doInBackground", "itemInfoArrayList总数:" +itemInfoArrayList.size());
//                adapter = new SimpleAdapter(getActivity(),
//                        itemInfoArrayList,
//                        R.layout.repair_item,
//                        new String[]{"lblRepairContentID", "lblReportTime", "lblCompleteDesribe"},
//                        new int[]{R.id.tv_repair_content, R.id.tv_report_time, R.id.tv_complete_describe}
//                );
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.e("Query","4-onActivityCreated");
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e("Query", "9-onDestroyView");
    }
    @Override
    public void onDetach() {
        super.onDetach();
        Log.e("Query", "11-onDetach");
    }

}
