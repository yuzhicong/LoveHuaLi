package com.yzc.lovehuali.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yzc.lovehuali.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Administrator on 2015/2/9 0009.
 */
public class StudentScoreExpandableListviewAdapter extends BaseExpandableListAdapter {

    private Context context;
    private  List<JSONObject> jsonObjectList;

    public StudentScoreExpandableListviewAdapter(Context context,List<JSONObject> jsonObjectList){
        this.context = context;
        this.jsonObjectList = jsonObjectList;

    }
    @Override
    public int getGroupCount() {
        return jsonObjectList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return null;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        TextView tvClassName = null;
        TextView tvFinalScore = null;

        LinearLayout ll =null;

        if(convertView !=null){
            ll = (LinearLayout)convertView;

        }else{
            ll = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.expandablelistview_main_student_score_cell, null);
        }//回收convertView机制

        JSONObject ClassInfo = jsonObjectList.get(groupPosition);

        tvClassName = (TextView)ll.findViewById(R.id.tvClassName);
        tvFinalScore = (TextView)ll.findViewById(R.id.tvFinalScore);

        try {
            tvClassName.setText(ClassInfo.getString("className"));
            tvFinalScore.setText(ClassInfo.getString("finalScore"));

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return ll;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        TextView tvClassName = null;
        TextView tvFinalScore = null;
        TextView tvClassCode = null;
        TextView tvDepartment = null;
        TextView tvGpa = null;
        TextView tvCredit = null;
        TextView tvProperty = null;

        LinearLayout ll =null;

        if(convertView !=null){
            ll = (LinearLayout)convertView;

        }else{
            ll = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.expandablelistview_detail_student_class, null);
        }//回收convertView机制

        JSONObject ClassInfo = jsonObjectList.get(groupPosition);

        tvClassName = (TextView)ll.findViewById(R.id.tvClassName);
        tvFinalScore = (TextView)ll.findViewById(R.id.tvFinalScore);
        tvCredit = (TextView) ll.findViewById(R.id.tvCredit);
        tvClassCode = (TextView) ll.findViewById(R.id.tvClassCode);
        tvDepartment = (TextView) ll.findViewById(R.id.tvDepartment);
        tvGpa = (TextView) ll.findViewById(R.id.tvGpa);
        tvProperty = (TextView) ll.findViewById(R.id.tvProperty);

        try {
            tvClassName.setText("课程名称：" + ClassInfo.getString("className"));
            tvFinalScore.setText("课程成绩：" + ClassInfo.getString("finalScore"));
            tvCredit.setText("课程学分：" + ClassInfo.getString("credit"));
            tvClassCode.setText("课程代码：" + ClassInfo.getString("classCode"));
            tvDepartment.setText("开设学部：" + ClassInfo.getString("department"));
            String gpa = ClassInfo.getString("gpa").substring(3,7);
            tvGpa.setText("课程绩点：" + gpa);
            tvProperty.setText("课程类别：" + ClassInfo.getString("property"));

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return ll;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
