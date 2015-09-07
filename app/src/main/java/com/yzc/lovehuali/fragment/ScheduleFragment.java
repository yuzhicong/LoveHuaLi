package com.yzc.lovehuali.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yzc.lovehuali.EditCourseActivity;
import com.yzc.lovehuali.R;
import com.yzc.lovehuali.tool.ACache;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2015/3/16.
 */
public class ScheduleFragment extends Fragment implements View.OnClickListener {


    private TextView[] textView;

    private ACache mCache;//缓存对象

    private int[] textViewId = {R.id.week1_1_2_textView, R.id.week2_1_2_textView, R.id.week3_1_2_textView, R.id.week4_1_2_textView, R.id.week5_1_2_textView, R.id.week6_1_2_textView, R.id.week7_1_2_textView,
            R.id.week1_3_4_textView, R.id.week2_3_4_textView, R.id.week3_3_4_textView, R.id.week4_3_4_textView, R.id.week5_3_4_textView, R.id.week6_3_4_textView, R.id.week7_3_4_textView,
            R.id.week1_5_6_textView, R.id.week2_5_6_textView, R.id.week3_5_6_textView, R.id.week4_5_6_textView, R.id.week5_5_6_textView, R.id.week6_5_6_textView, R.id.week7_5_6_textView,
            R.id.week1_7_8_textView, R.id.week2_7_8_textView, R.id.week3_7_8_textView, R.id.week4_7_8_textView, R.id.week5_7_8_textView, R.id.week6_7_8_textView, R.id.week7_7_8_textView,
            R.id.week1_9_10_textView, R.id.week2_9_10_textView, R.id.week3_9_10_textView, R.id.week4_9_10_textView, R.id.week5_9_10_textView, R.id.week6_9_10_textView, R.id.week7_9_10_textView};

    //private int[] textViewResoureId = new int[]{R.drawable.set_bar_01, R.drawable.set_bar_02, R.drawable.set_bar_03, R.drawable.set_bar_04, R.drawable.set_bar_05, R.drawable.set_bar_06, R.drawable.set_bar_07, R.drawable.set_bar_08, R.drawable.set_bar_09, R.drawable.set_bar_10, R.drawable.set_bar_11, R.drawable.set_bar_12, R.drawable.set_bar_13, R.drawable.set_bar_14, R.drawable.set_bar_15, R.drawable.set_bar_16, R.drawable.set_bar_17, R.drawable.set_bar_18};

    private int[] textViewColorId = new int[]{R.color.color01, R.color.color02, R.color.color03, R.color.color04, R.color.color05, R.color.color06, R.color.color07, R.color.color08, R.color.color09, R.color.color10, R.color.color11, R.color.color12, R.color.color13, R.color.color14, R.color.color15, R.color.color16, R.color.color17, R.color.color18};
    //用于获取课程的各种数据
    private String week;


    private String courseSection;
    private String courseWeek;
    private String courseName;
    private String classRoom;
    private String teacher;

    private String[] weekString;
    private String[] courseSectionString;
    private String[] courseNameString;
    private String[] classRoomString;
    private String[] teacherString;
    private String[] courseWeekString;
    private String[] nameString;
    private int[] jsonint;
    private View view;
    public static int locad_week = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_schedule, container, false);

        View courejsonLayout = rootView.findViewById(R.id.courejsonLayout);
        courejsonLayout.setVisibility(View.VISIBLE);

        //        绑定TextView并显示课程
        textView = new TextView[35];
        for (int i = 0; i < 35; i++) {
            textView[i] = (TextView) rootView.findViewById(textViewId[i]);
            textView[i].setOnClickListener(this);
        }

        mCache = ACache.get(getActivity());
        if (mCache.getAsString("courseJson") != null) {
            //System.out.println(mCache.getAsString("courseJson"));
            this.refresh(mCache.getAsString("courseJson"), locad_week);
        }


        IntentFilter intentFilter = new IntentFilter("MainActivity.ScheduleChange");
        getActivity().registerReceiver(broadcastReceiver, intentFilter);
        return rootView;
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
//            String string3 = intent.getAction();
            locad_week = intent.getIntExtra("weekChose", -1);

            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("weeks", Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("WEEKS_COURSE",String.valueOf(locad_week));
            editor.commit();

            refresh(mCache.getAsString("courseJson"), locad_week);
//            Toast.makeText(getActivity(), String.valueOf(locad_week), Toast.LENGTH_LONG).show();
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("课程碎片正在刷新");

        if (mCache.getAsString("courseJson") != null) {
            Log.d("MainActivity", "--------修复课程周数问题------>" + mCache.getAsString("courseJson").toString());
            System.out.println("--------修复课程周数问题------>" + mCache.getAsString("courseJson").toString());
            for (int i = 0; i < 35; i++) {
                textView[i].setText("");
//                textView[i].setBackgroundColor(android.R.color.white);
            }
            this.refresh(mCache.getAsString("courseJson"), locad_week);

        }
    }

    //jsonweeks的方法没有写出
    public void refresh(String courseJson, int jsonweeks) {
        //先清空原来的课程表格
        for (int j = 0; j < textViewId.length; j++) {
            textView[j].setText("");
//            textView[j].setBackgroundResource(R.color.cpb_white);
            textView[j].setBackgroundResource(R.drawable.textview_fragment_style);
        }

        try {
            JSONArray jsonArray = new JSONArray(courseJson);

            weekString = new String[jsonArray.length()];
            courseSectionString = new String[jsonArray.length()];
            courseNameString = new String[jsonArray.length()];
            jsonint = new int[jsonArray.length()];
            classRoomString = new String[jsonArray.length()];
            teacherString = new String[jsonArray.length()];
            courseWeekString = new String[jsonArray.length()];
            nameString = new String[jsonArray.length()];

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject itemsJsonObject = jsonArray.getJSONObject(i);
                week = itemsJsonObject.getString("week");
                courseSection = itemsJsonObject.getString("courseSection");
                courseWeek = itemsJsonObject.getString("courseWeek");   //课程的周数
                courseName = itemsJsonObject.getString("courseName");
                classRoom = itemsJsonObject.getString("classRoom");
                teacher = itemsJsonObject.getString("teacher");

                nameString[i] = courseName;


                if (courseName.length() > 20) {
                    courseName = courseName.substring(0, 16) + "...\n";
                } else {
                    courseName = courseName + "\n";
                }

                courseNameString[i] = courseName;               //课程名称
                weekString[i] = week;                           //星期
                courseSectionString[i] = courseSection;         //节数
                jsonint[i] = i;                                 //增加一个数字标志
                classRoomString[i] = classRoom;                 //教室
                teacherString[i] = teacher;                     //老师
                courseWeekString[i] = courseWeek;               //到几周
            }
            for (int i = 0; i < courseNameString.length; i++) {
                for (int j = i + 1; j < courseNameString.length; j++) {
                    if (courseNameString[i].equals(courseNameString[j])) {
                        jsonint[j] = jsonint[i];
                    }
                }
            }
            for (int i = 0; i < jsonint.length; i++) {
                Log.d("MainActivity", courseNameString[i]);
                Log.d("MainActivity", String.valueOf(jsonint[i]));
                Log.d("MainActivity", teacherString[i]);

            }

            //显示课程
            for (int i = 0; i < courseNameString.length; i++) {
//                String choosecourse = courseWeekString[i].substring(courseWeekString[i].indexOf("第") + 1, courseWeekString[i].indexOf("周"));   //筛选周数

                String[] courseSectionString_sz = courseSectionString[i].split(",");
                //System.out.println("课程的节数选择" + (Integer.parseInt(courseSectionString_sz[1]) - 2) / 2);
                int tempint = (Integer.parseInt(courseSectionString_sz[1]) - 2) / 2;
                int allint = (tempint * 7) + Integer.parseInt(weekString[i]) - 1;

                textView[allint].setTextColor(Color.parseColor("#666666"));
                textView[allint].setText(courseNameString[i] + classRoomString[i]);

                System.out.println("显示课程的周数问题--->>" + i + "\t" + courseWeekString[i]);

                if (courseWeekString[i].indexOf(",") != -1) {//处理含有","字符串
                    String[] courseWeekString_sz_1 = courseWeekString[i].split(",");
                    for (int k = 0; k < courseWeekString_sz_1.length; k++) {
                        if (courseWeekString_sz_1[k].indexOf("-") != -1) {
                            String[] courseWeekString_sz_1_1 = courseWeekString_sz_1[k].split("-");
                            if (jsonweeks >= Integer.parseInt(courseWeekString_sz_1_1[0]) && jsonweeks <= Integer.parseInt(courseWeekString_sz_1_1[1])) {

//                                textView[allint].setBackgroundColor(getResources().getColor(textViewColorId[jsonint[i]]));
                                GradientDrawable p = (GradientDrawable)textView[allint].getBackground();
                                p.setColor(getResources().getColor(textViewColorId[jsonint[i]]));
                                textView[allint].setTextColor(Color.parseColor("#FFFFFF"));
                            } else {
//                                textView[allint].setBackgroundResource(R.color.color_public);
                                GradientDrawable p = (GradientDrawable)textView[allint].getBackground();
                                p.setColor(getResources().getColor(R.color.color_public));
                                textView[allint].setTextColor(Color.parseColor("#666666"));
                            }

                        } else {
                            if (jsonweeks == Integer.parseInt(courseWeekString_sz_1[k])) {
//                                textView[allint].setBackgroundColor(getResources().getColor(textViewColorId[jsonint[i]]));
                                GradientDrawable p = (GradientDrawable)textView[allint].getBackground();
                                p.setColor(getResources().getColor(textViewColorId[jsonint[i]]));
                                textView[allint].setTextColor(Color.parseColor("#FFFFFF"));
                            } else {
//                                textView[allint].setBackgroundResource(R.color.color_public);
                                GradientDrawable p = (GradientDrawable)textView[allint].getBackground();
                                p.setColor(getResources().getColor(R.color.color_public));
                                textView[allint].setTextColor(Color.parseColor("#666666"));
                            }

                        }
                    }

                } else {
                    if (courseWeekString[i].indexOf("-") != -1) {//去掉含有","的字符串，处理含有"-"
                        String[] courseWeekString_sz_2 = courseWeekString[i].split("-");
                        if (jsonweeks >= Integer.parseInt(courseWeekString_sz_2[0]) && jsonweeks <= Integer.parseInt(courseWeekString_sz_2[1])) {
//                            textView[allint].setBackgroundColor(getResources().getColor(textViewColorId[jsonint[i]]));
                            GradientDrawable p = (GradientDrawable)textView[allint].getBackground();
                            p.setColor(getResources().getColor(textViewColorId[jsonint[i]]));
                            textView[allint].setTextColor(Color.parseColor("#FFFFFF"));
                        } else {
//                            textView[allint].setBackgroundResource(R.color.color_public);
                            GradientDrawable p = (GradientDrawable)textView[allint].getBackground();
                            p.setColor(getResources().getColor(R.color.color_public));
                            textView[allint].setTextColor(Color.parseColor("#666666"));
                        }

                    } else {//处理单个周数"8"
                        if (jsonweeks == Integer.parseInt(courseWeekString[i])) {
//                            textView[allint].setBackgroundColor(getResources().getColor(textViewColorId[jsonint[i]]));
                            GradientDrawable p = (GradientDrawable)textView[allint].getBackground();
                            p.setColor(getResources().getColor(textViewColorId[jsonint[i]]));
                            textView[allint].setTextColor(Color.parseColor("#FFFFFF"));
                        } else {
//                            textView[allint].setBackgroundResource(R.color.color_public);
                            GradientDrawable p = (GradientDrawable)textView[allint].getBackground();
                            p.setColor(getResources().getColor(R.color.color_public));
                            textView[allint].setTextColor(Color.parseColor("#666666"));
                        }

                    }
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        for (int i = 0; i < textViewId.length; i++) {
            if (v.getId() == textViewId[i]) {
                int choose = 0;
                String section = "";
                if (i < 7) {
                    choose = i + 1;
                    section = "1,2";
                } else if (i < 14) {
                    choose = i - 6;
                    section = "3,4";
                } else if (i < 21) {
                    choose = i - 13;
                    section = "5,6";
                } else if (i < 28) {
                    choose = i - 20;
                    section = "7,8";
                } else {
                    choose = i - 27;
                    section = "9,10";
                }

                String intentweek = String.valueOf(choose);              //传递星期
                String intentsection = section;                           //传递节数
                String intentteacher;                                   //传递老师
                String intentname;                                       //传递课程
                String intentroom;                                       //传递教室
                String intentcourseWeek;                                //传递周数


                if (weekString != null) {//2015.5.18
                    for (int k = 0; k < weekString.length; k++) {
                        if (String.valueOf(choose).equals(weekString[k]) && section.equals(courseSectionString[k])) {
                            System.out.println("json数据的courseName------->>>" + nameString[k]);
                            String getTextView = textView[i].getText().toString();
//                            System.out.println("获取TextView的内容------->>>" + getTextView);
                            String[] getTextView_coursename = getTextView.split("\n");
                            String getTextView_coursename2 = getTextView_coursename[0] + "\n";
                            if (getTextView_coursename2.equals(courseNameString[k])) {
                                System.out.println("二次json数据的courseName------->>>" + courseNameString[k] + "==" + getTextView_coursename2);
                                intentname = nameString[k];
                                intentroom = classRoomString[k];
                                intentteacher = teacherString[k];
                                intentcourseWeek = courseWeekString[k];
                                intentsection = courseSectionString[k];

                                intent.putExtra("intentname", intentname);
                                intent.putExtra("intentroom", intentroom);
                                intent.putExtra("intentteacher", intentteacher);
                                intent.putExtra("intentcourseWeek", intentcourseWeek);
                                intent.putExtra("intentsection", intentsection);
                                intent.putExtra("intentweek", intentweek);
                                break;
                            } else {
                                System.out.println("二次json数据的courseName------->>>" + courseNameString[k] + "==" + getTextView_coursename2);
                            }
                        } else {
                            intent.putExtra("intentname", "");
                            intent.putExtra("intentroom", "");
                            intent.putExtra("intentteacher", "");
                            intent.putExtra("intentcourseWeek", "");
                            intent.putExtra("intentsection", intentsection);
                            intent.putExtra("intentweek", intentweek);
                        }
                    }
                }
                intent.setClass(getActivity(), EditCourseActivity.class);
                startActivity(intent);


            }

        }

    }
}
