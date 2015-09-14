package com.yzc.lovehuali;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.TextView;

import com.yzc.lovehuali.tool.SystemBarTintManager;

public class YellowPagerActivity extends ActionBarActivity {

    private Toolbar mToolbar;
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_yellow_pager);

        //设定状态栏的颜色，当版本大于4.4时起作用
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            //此处可以重新指定状态栏颜色
            tintManager.setStatusBarTintResource(R.color.material_blue);
        }

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("校园黄页");
        mToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        final ExpandableListAdapter adapter = new BaseExpandableListAdapter() {
            //一级显示
            private String[] generalsTypes = new String[] {
//                    "订餐",
//                    "投宿",
                    "出行电话",
//                    "娱乐",
//                    "快递",
                    "学院电话" };
            //二级显示
            private String[][] generals = new String[][] {
//                    { "华立园餐厅（男）:15812498846","华立园餐厅（女）:13826415224","华立园餐厅（男）:638846","华立园餐厅（女）:685224"},
                    /*{       "学府公寓:13316222118",
                            "朵拉公寓:13539765918",
//                            "青青公寓:18899733261",
                            "金一假日:02082733688" },*/
                    {       "增城二汽:02082755205",
                            "市汽车站:02082755205",
                            "天河客运站:02037085070",
                            "海珠客运站:02084013301",
                            "广园客运站:02086379888",
                            "增城光明车站:02082626722",
                            "省汽车客运站:02086661297",},
                   /* {       "玩乐ing:15915761457","朗源农庄:13500227178","乐活公寓:15014160323"
//                            "初溪:18899733261",
                    },*/

//                    {       "申通:18899733261","圆通:18899733261","韵达:18899733261",
//                            "中通:18899733261","EMS:18899733261","顺丰:18899733261"},
                    {
                            "网络中心电话：02082906461",
                            "招生办:02082900921",
                            "学生处:02082901320",
                            "财务处:02082904180",
                            "水电中心:02082908726",
                            "秩序维护队:02082906515",
                            "医务所：02082904036",
                            "学院总务：02082905162",
                            "心理咨询中心：02082905646",
                            "学生处生活指导科：02082901320",
                            "保卫处：02082912445",
                            "维修报修投诉：02082908726",
                            "机电学部:02082903511",
                            "管理学部:02082907103",
                            "城建学部:02082907196",
                            "经济学部:02082907206",
                            "会计学部:02082915967",
                            "外语外贸学部:02082907173",
                            "传媒与艺术设计学部:02082903307",
                    }

            };

            //重写ExpandableListAdapter中的各个方法
            @Override
            public int getGroupCount() {
                // TODO Auto-generated method stub
                return generalsTypes.length;
            }

            @Override
            public Object getGroup(int groupPosition) {
                // TODO Auto-generated method stub
                return generalsTypes[groupPosition];
            }

            @Override
            public long getGroupId(int groupPosition) {
                // TODO Auto-generated method stub
                return groupPosition;
            }

            @Override
            public int getChildrenCount(int groupPosition) {
                // TODO Auto-generated method stub
                return generals[groupPosition].length;
            }

            @Override
            public Object getChild(int groupPosition, int childPosition) {
                // TODO Auto-generated method stub
                return generals[groupPosition][childPosition];
            }

            @Override
            public long getChildId(int groupPosition, int childPosition) {
                // TODO Auto-generated method stub
                return childPosition;
            }

            @Override
            public boolean hasStableIds() {
                // TODO Auto-generated method stub
                return true;
            }

            @Override
            public View getGroupView(int groupPosition, boolean isExpanded,
                                     View convertView, ViewGroup parent) {
                // TODO Auto-generated method stub
                View view = convertView;
                if (view == null) {
                    // 通过getSystemService方法实例化一个视图的填充器
                    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    view = inflater.inflate(R.layout.first_list, null);
                }
                TextView title = (TextView) view.findViewById(R.id.content_001);
                title.setText(getGroup(groupPosition).toString());

                return view;
            }


            @Override
            public View getChildView(int groupPosition, int childPosition,
                                     boolean isLastChild, View convertView, ViewGroup parent) {
                // TODO Auto-generated method stub

                View view = convertView;
                if (view == null) {
                    //填充视图
                    LayoutInflater inflater = (LayoutInflater) getSystemService
                            (Context.LAYOUT_INFLATER_SERVICE);
                    view = inflater.inflate(R.layout.second_list, null);
                }
                final TextView title = (TextView) view.findViewById
                        (R.id.child_text);
                title.setText(getChild(groupPosition , childPosition).toString());
                return view;
            }


            @Override
            public boolean isChildSelectable(int groupPosition,
                                             int childPosition) {
                // TODO Auto-generated method stub
                return true;
            }

        };

        ExpandableListView expandableListView = (ExpandableListView) findViewById(R.id.list);
        expandableListView.setAdapter(adapter);

        //将所有项设置成默认展开

        int groupCount = expandableListView.getCount();

        for (int i=0; i<groupCount; i++) {

            expandableListView.expandGroup(i);

        }

        //设置item点击的监听器
        expandableListView.setOnChildClickListener(new OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                //取电话号码，取字符的后11位
                String str=(String)adapter.getChild(groupPosition, childPosition);
                String s = null;
                if (str.length()>11){
                    s=str.substring(str.length()-11,str.length());
                }
                else{
                    s=str;
                }

                Intent intent = new Intent(Intent.ACTION_DIAL,
                        Uri.parse("tel:" + s));
                startActivity(intent);
                return false;
            }
        });
    }
}