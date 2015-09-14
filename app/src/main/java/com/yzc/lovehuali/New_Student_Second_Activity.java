package com.yzc.lovehuali;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yzc.lovehuali.tool.SystemBarTintManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/7/25.
 */
public class New_Student_Second_Activity extends ActionBarActivity {

    private Toolbar mToolbar;


    private String[] title_ex = DATA_newstudents.title_ex;

    private String[] content = DATA_newstudents.content;
    private String student_content = DATA_newstudents.student_content;
    private ExpandableListView expandableListView = null;
    private List<String> parent = null;
    private Map<String, List<String>> map = null;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_student_second_activityforlayout);


        intent = getIntent();
        String s = intent.getStringExtra("temp");

        //设定状态栏的颜色，当版本大于4.4时起作用
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            //此处可以重新指定状态栏颜色
            tintManager.setStatusBarTintResource(R.color.material_blue);
        }

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(New_Student_Activity.title_adapterId[Integer.parseInt(s)]);
        mToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        switch (s) {
            case "0":
                LinearLayout linearLayout0 = (LinearLayout) findViewById(R.id.el_list_id_forlayout);
                linearLayout0.setVisibility(View.VISIBLE);
                expandableListView = (ExpandableListView) findViewById(R.id.el_list);
                expandableListView.setGroupIndicator(null);
                initData();
                expandableListView.setAdapter(new MyAdapter());
                break;
            case "1":
                LinearLayout linearLayout1 = (LinearLayout) findViewById(R.id.newstudent_traffic_forlayout);
                linearLayout1.setVisibility(View.VISIBLE);
                TextView newstudent_traffic7_fortextview = (TextView) findViewById(R.id.newstudent_traffic7_fortextview);
                newstudent_traffic7_fortextview.setText(" 7路公车: --->>");
                newstudent_traffic7_fortextview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(New_Student_Second_Activity.this, "始发时间6:30，末班时间21:30，15-20分钟一班", Toast.LENGTH_LONG).show();
                    }
                });
                TextView newstudent_traffic1_fortextview = (TextView) findViewById(R.id.newstudent_traffic1_fortextview);
                newstudent_traffic1_fortextview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(New_Student_Second_Activity.this, "始发时间6:30，末班时间21:30，15-20分钟一班", Toast.LENGTH_LONG).show();
                    }
                });
                TextView newstudent_traffic110_fortextview = (TextView) findViewById(R.id.newstudent_traffic110_fortextview);
                newstudent_traffic110_fortextview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(New_Student_Second_Activity.this, "始发时间6:15，末班时间20:00，15-20分钟一班", Toast.LENGTH_LONG).show();
                    }
                });
                break;
            case "2":
                LinearLayout linearLayout2 = (LinearLayout) findViewById(R.id.newstudent_express_forlayout);
                linearLayout2.setVisibility(View.VISIBLE);
                break;

            case "3":
                LinearLayout linearLayout3 = (LinearLayout) findViewById(R.id.student_rest_id_forlayout);
                linearLayout3.setVisibility(View.VISIBLE);
                TextView title_studentrest_TextView = (TextView) findViewById(R.id.title_student_rest);
                title_studentrest_TextView.setText("华立作息时间表大全");
                break;

            case "4":
                LinearLayout linearLayout4 = (LinearLayout) findViewById(R.id.newstudent_supplicant_forlayout);
                linearLayout4.setVisibility(View.VISIBLE);
                WebView supplicant_WebView = (WebView) findViewById(R.id.webview_supplicant);
                supplicant_WebView.getSettings().setJavaScriptEnabled(true);
                //设置缩放效果无效
//                supplicant_WebView.getSettings().setSupportZoom(true);
//                supplicant_WebView.getSettings().setBuiltInZoomControls(true);
//                supplicant_WebView.getSettings().setUseWideViewPort(true);
                supplicant_WebView.loadUrl("http://mp.weixin.qq.com/s?__biz=MzAwMDY4MTE4Mg==&mid=215243222&idx=1&sn=8826698eb802fb2d8cfc3e1000df8ccb&scene=0#rd");

                break;
            case "5":
                Intent intent = new Intent(getApplication(),StaticMap.class);
                startActivity(intent);
                finish();
                break;
            default:
                break;
        }

//        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
//            @Override
//            public void onGroupExpand(int groupPosition) {
//                temp_groupPostion = groupPosition;
//                for (int i = 0; i < parent.size(); i++) {
//                    if (groupPosition != i) {
//                        expandableListView.collapseGroup(i);
//
//                    }
//                }
//                System.out.println("设置焦点问题------>>>>" + groupPosition);
//            }
//        });

//        expandableListView.requestFocus(temp_groupPostion);
    }

    // 初始化数据
    public void initData() {
        parent = new ArrayList<String>();
        parent.add(title_ex[0]);
        parent.add(title_ex[1]);
        parent.add(title_ex[2]);
        parent.add(title_ex[3]);
        parent.add(title_ex[4]);
        parent.add(title_ex[5]);
        parent.add(title_ex[6]);
        parent.add(title_ex[7]);
        parent.add(title_ex[8]);
        parent.add(title_ex[9]);

        map = new HashMap<String, List<String>>();

        List<String> list1 = new ArrayList<String>();
        list1.add(content[0]);
        map.put(title_ex[0], list1);

        List<String> list2 = new ArrayList<String>();
        list2.add(content[1]);
        map.put(title_ex[1], list2);

        List<String> list3 = new ArrayList<String>();
        list3.add(content[2]);
        map.put(title_ex[2], list3);

        List<String> list4 = new ArrayList<String>();
        list4.add(content[3]);
        map.put(title_ex[3], list4);

        List<String> list5 = new ArrayList<String>();
        list5.add(content[4]);
        map.put(title_ex[4], list5);

        List<String> list6 = new ArrayList<String>();
        list6.add(content[5]);
        map.put(title_ex[5], list6);

        List<String> list7 = new ArrayList<String>();
        list7.add(content[6]);
        map.put(title_ex[6], list7);

        List<String> list8 = new ArrayList<String>();
        list8.add(content[7]);
        map.put(title_ex[7], list8);

        List<String> list9 = new ArrayList<String>();
        list9.add(content[8]);
        map.put(title_ex[8], list9);

        List<String> list10 = new ArrayList<String>();
        list10.add(content[9]);
        map.put(title_ex[9], list10);
    }

    class MyAdapter extends BaseExpandableListAdapter {

        @Override
        public int getGroupCount() {
            return parent.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            String key = parent.get(groupPosition);
            int size = map.get(key).size();
            return size;
        }

        @Override
        public Object getGroup(int groupPosition) {
            return parent.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            String key = parent.get(groupPosition);
            return (map.get(key).get(childPosition));
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) New_Student_Second_Activity.this
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.ex_parent, null);
            }
            TextView tv = (TextView) convertView
                    .findViewById(R.id.parent_textview);
            tv.setText(New_Student_Second_Activity.this.parent.get(groupPosition));
            return tv;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            String key = New_Student_Second_Activity.this.parent.get(groupPosition);
            String info = map.get(key).get(childPosition);
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) New_Student_Second_Activity.this
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.ex_childs, null);
            }
            TextView tv = (TextView) convertView
                    .findViewById(R.id.second_textview);

            tv.setText(info);

            return tv;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }


}

