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
            tintManager.setStatusBarTintResource(R.color.colorPrimaryDark);
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
