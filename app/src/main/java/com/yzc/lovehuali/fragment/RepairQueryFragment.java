package com.yzc.lovehuali.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.yzc.lovehuali.R;
import com.yzc.lovehuali.tool.LogUtil;

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
 * 更新记录
 * Created by 镜界 on 2015/8/28 0028.
 * 8/29:功能性测试成功
 * 8/29:优化输入为空的检查提示
 * 8/31:初步实现,预输入用户输入的手机号
 * 9/7:点击子item可查看详细
 * 9/7:优化报修文本太长导致文本重叠的情况
 * 9/8:优化edtitext：只允许数字，光标自动到文本最右，软键盘支持发送，发送后关闭软键盘
 * 9/8:优化详细碎片：切换动画close，添加返回按钮
 * 9/8:解决输入无效手机号的情况
 * 9/9:删除查询中报修描述的<br>标签-正则非贪婪模式
 * -----------------
 * 优化网页解析速度
 * 遇到学校渣网络的错误界面
 */
public class RepairQueryFragment extends Fragment{
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
        View view = inflater.inflate(R.layout.repair_query, container, false);
        LogUtil.e("Query", "3-onCreateView");
        setInstance(view); //1实例化控件
        putPhone();//5填充用户输入过的手机号
        //6软键盘回车监听事件
        etPhone.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND)
                    sendPhone();
                return true;
            }
        });
        //10按钮点击事件，检查用户情况
        btnWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendPhone();//这里进行网络访问的查询
            }
        });
        //50每个报修单的详情点击事件
        lvWeb.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                RepairQueryDetairFragment repairQuerydetailFragment = new RepairQueryDetairFragment(itemInfoArrayList.get(position));
                RepairQueryDetairFragment repairQuerydetailFragment = RepairQueryDetairFragment.getInstance(itemInfoArrayList.get(position));
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.query_layout, repairQuerydetailFragment);
                transaction.addToBackStack(null);
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                transaction.commit();
            }
        });
        return view;
    }

    public void setInstance(View view) {
        etPhone = (EditText) view.findViewById(R.id.et_phone);
        pbWeb = (ProgressBar) view.findViewById(R.id.pb_web);
        lvWeb = (ListView) view.findViewById(R.id.lv_web);
        cardView = (CardView) view.findViewById(R.id.cardview);
        btnWeb = (Button) view.findViewById(R.id.btn_web);
    }

    public void savePhone(String str) {
        //存储用户输入的手机号下次启动可以填充
        editor = sharedPreferences.edit();
        editor.putString("user_phone", str);
        editor.putBoolean("user", true);
        editor.commit();
        LogUtil.e("savePhone", "保存用户手机号" + str);
    }

    public void putPhone() {
        //5如果有，填充用户输入过的手机号
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        if (true) {
            String spPhone = sharedPreferences.getString("user_phone", "");
            etPhone.setText(spPhone);
            etPhone.setSelection(spPhone.length());
            LogUtil.e("putPhone", "填充用户手机号" + spPhone);
        }
    }

    public void sendPhone() {
        LogUtil.e("开始时间", "" + System.nanoTime());

        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etPhone.getWindowToken(), 0);

        String strOriginPhone = etPhone.getText().toString();
        if (TextUtils.isEmpty(strOriginPhone)) {
            etPhone.setError("说好的联系方式呢?");
            LogUtil.e("MainActivity", "4输入为空");
        } else {
            pbWeb.setVisibility(View.VISIBLE);
            String netOrigin = "http://hq.hualixy.com/RepairModule/Repair/Show.aspx?RepairID=" + strOriginPhone;
            LogUtil.e("MainActivity", "4查询的号码是" + strOriginPhone);
            itemInfoArrayList = new ArrayList<>();
            new PhoneNumAsyncTask().execute(netOrigin);
            savePhone(strOriginPhone); //存储用户手机号下次用
        }
    }

    class PhoneNumAsyncTask extends AsyncTask<String, Void, Void> {
        int arrayListSize;
        List arrayList = new ArrayList<>();//记录报修记录的数目
        String strB = "http://hq.hualixy.com/RepairModule/Repair/";
        @Override
        protected Void doInBackground(String... params) {
            LogUtil.e("doInBackground", "进入异步");
            StringBuilder sbResult = new StringBuilder("");
            try {
                URLConnection urlConn = new URL(params[0]).openConnection();
                urlConn.setConnectTimeout(5000);//连接主机超时5s
                urlConn.setReadTimeout(10000);  //从主机读取数据超时10s
                BufferedReader br = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
                String strRegex = "Show.aspx\\?id=\\w{5}";
                String strLine;
                Pattern p = Pattern.compile(strRegex);
                Matcher m;
                while ((strLine = br.readLine()) != null) {
                    m = p.matcher(strLine);
                    if (m.find()) {
                        //如果找到类似Show.aspx?id=23333的字符串就放进arrayList里面
                        arrayList.add(m.group());
                        LogUtil.e("doInBackground", "找到一条报修记录:" + m.group());
                    }
                }
                if (arrayList.size() > 0) {
                    arrayListSize = arrayList.size();
                    for (int i = 0; i < arrayListSize; i++) {
                        this.catchItem(strB + arrayList.get(i));
                    }
                    LogUtil.e("doInBackground", "itemInfoArrayList总数:" + itemInfoArrayList.size());
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

        public void catchItem(String str) {
            LogUtil.e("doInBackground", "catchItem");
            String strRegexAll = "ctl00_ContentPlaceHolder1_(\\w*)\">(.*?)(<br>|</span>)";
            String strLine2;
            HashMap<String, String> eachItemMap = new HashMap<>();
            try {
                URLConnection urlConn2 = new URL(str).openConnection();
                BufferedReader br2 = new BufferedReader(new InputStreamReader(urlConn2.getInputStream()));
                Pattern p2All = Pattern.compile(strRegexAll);
                Matcher m2All = null;
                while ((strLine2 = br2.readLine()) != null) {
                    m2All = p2All.matcher(strLine2);
                    if (m2All.find()) {
                        LogUtil.e("m2All", "group[1]=" + m2All.group(1) + "  group[2]=" + m2All.group(2));
                        eachItemMap.put(m2All.group(1), m2All.group(2));
                    }
                }

                LogUtil.e("eachItemMap",""+eachItemMap.toString());
                itemInfoArrayList.add(eachItemMap);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            LogUtil.e("onPostExecute", "进入onPostExecute");
            if (arrayList.size() == 0) {
                LogUtil.e("onPostExecute", "没有报修记录");
                Toast.makeText(getActivity(), "没有相关报修记录", Toast.LENGTH_SHORT).show();
                lvWeb.setVisibility(View.INVISIBLE);
            } else {
                lvWeb.setAdapter(adapter);
                lvWeb.setVisibility(View.VISIBLE);
                LogUtil.e("结束时间", "" + System.nanoTime());
//                LogUtil.e("itemInfoArrayList",""+itemInfoArrayList);
            }
            pbWeb.setVisibility(View.GONE);
        }
    }

}