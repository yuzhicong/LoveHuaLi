package com.yzc.lovehuali;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.widget.RemoteViews;

import com.yzc.lovehuali.fragment.ScheduleFragment;
import com.yzc.lovehuali.tool.ACache;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2015/8/21.
 */
public class Course_WidgetProvider extends AppWidgetProvider {

    private int[] textviewColorId = new int[]{R.color.color01, R.color.color02, R.color.color03, R.color.color04, R.color.color05, R.color.color06, R.color.color07, R.color.color08, R.color.color09, R.color.color10, R.color.color11, R.color.color12, R.color.color13, R.color.color14, R.color.color15, R.color.color16, R.color.color17, R.color.color18,R.color.color19,R.color.color20,R.color.color21,R.color.color22,R.color.color23,R.color.color24,R.color.color25,R.color.color26};

    private int[] textViewId = new int[]{R.id.week_widget1_1_2_textView, R.id.week_widget2_1_2_textView, R.id.week_widget3_1_2_textView, R.id.week_widget4_1_2_textView, R.id.week_widget5_1_2_textView, R.id.week6_1_2_textView, R.id.week7_1_2_textView,
            R.id.week_widget1_3_4_textView, R.id.week_widget2_3_4_textView, R.id.week_widget3_3_4_textView, R.id.week_widget4_3_4_textView, R.id.week_widget5_3_4_textView, R.id.week6_3_4_textView, R.id.week7_3_4_textView,
            R.id.week_widget1_5_6_textView, R.id.week_widget2_5_6_textView, R.id.week_widget3_5_6_textView, R.id.week_widget4_5_6_textView, R.id.week_widget5_5_6_textView, R.id.week6_5_6_textView, R.id.week7_5_6_textView,
            R.id.week_widget1_7_8_textView, R.id.week_widget2_7_8_textView, R.id.week_widget3_7_8_textView, R.id.week_widget4_7_8_textView, R.id.week_widget5_7_8_textView, R.id.week6_7_8_textView, R.id.week7_7_8_textView,};
//            R.id.week_widget1_9_10_textView, R.id.week_widget2_9_10_textView, R.id.week_widget3_9_10_textView, R.id.week_widget4_9_10_textView, R.id.week_widget5_9_10_textView, R.id.week6_9_10_textView, R.id.week7_9_10_textView,};

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

    private ACache aCache_wight;


    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
    }


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

        int temp_json = ScheduleFragment.locad_week;

        Show_course(views, context, temp_json);
        appWidgetManager.updateAppWidget(appWidgetIds, views);

    }

    @Override
    public void onEnabled(Context context) {

    }

    private void Show_course(RemoteViews views, Context context, int weekscourse) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("weeks", Activity.MODE_PRIVATE);
        if (sharedPreferences.getString("WEEKS_COURSE", "") != null) {
            views.setTextViewText(R.id.show_choose_weeks, "第" + sharedPreferences.getString("WEEKS_COURSE", "") + "周");
            weekscourse = Integer.parseInt(sharedPreferences.getString("WEEKS_COURSE", ""));
        } else {
            views.setTextViewText(R.id.show_choose_weeks, "第" + String.valueOf(weekscourse) + "周");
        }

        SharedPreferences sp = context.getSharedPreferences("mysp", Activity.MODE_PRIVATE);

        if (sp.getString("courseJson", "") != null) {
            int[] temp_int = new int[textViewId.length];
            for (int j = 0; j < textViewId.length; j++) {
                views.setTextViewText(textViewId[j], "");
                views.setInt(textViewId[j], "setBackgroundResource", R.color.widget_lucency);
                temp_int[j] = 0;
            }

            String json = sp.getString("courseJson", "");
            try {
                JSONArray jsonArray = new JSONArray(json);

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


                //显示课程
                for (int i = 0; i < courseNameString.length; i++) {
                    String[] courseSectionString_sz = courseSectionString[i].split(",");
                    //System.out.println("课程的节数选择" + (Integer.parseInt(courseSectionString_sz[1]) - 2) / 2);
                    int tempint = (Integer.parseInt(courseSectionString_sz[1]) - 2) / 2;
                    int allint = (tempint * 7) + Integer.parseInt(weekString[i]) - 1;

//                    views.setInt(textViewId[allint], "setTextColor", Color.parseColor("#666666"));
//                    views.setTextViewText(textViewId[allint], courseNameString[i] + classRoomString[i]);

                    System.out.println("显示课程的周数问题--->>" + i + "\t" + courseWeekString[i]);

                    if (courseWeekString[i].indexOf(",") != -1) {//处理含有","字符串
                        String[] courseWeekString_sz_1 = courseWeekString[i].split(",");
                        for (int k = 0; k < courseWeekString_sz_1.length; k++) {
                            if (courseWeekString_sz_1[k].indexOf("-") != -1) {
                                String[] courseWeekString_sz_1_1 = courseWeekString_sz_1[k].split("-");
                                if (weekscourse >= Integer.parseInt(courseWeekString_sz_1_1[0]) && weekscourse <= Integer.parseInt(courseWeekString_sz_1_1[1])) {

                                    views.setInt(textViewId[allint], "setBackgroundResource", textviewColorId[jsonint[i]]);
                                    views.setInt(textViewId[allint], "setTextColor", Color.parseColor("#FFFFFF"));
                                    views.setTextViewText(textViewId[allint], courseNameString[i] + classRoomString[i]);
                                    temp_int[allint] = 1;
                                } else {
                                    for (int t =0 ; t < textViewId.length; t ++) {
                                        if (temp_int[allint] == 0) {
                                            views.setInt(textViewId[allint], "setBackgroundResource", R.color.color_public);
                                            views.setInt(textViewId[allint], "setTextColor", Color.parseColor("#666666"));
                                            views.setTextViewText(textViewId[allint], courseNameString[i] + classRoomString[i]);
                                        }

                                    }

                                }

                            } else {
                                if (weekscourse == Integer.parseInt(courseWeekString_sz_1[k])) {
                                    views.setInt(textViewId[allint], "setBackgroundResource", textviewColorId[jsonint[i]]);
                                    views.setInt(textViewId[allint], "setTextColor", Color.parseColor("#FFFFFF"));
                                    views.setTextViewText(textViewId[allint], courseNameString[i] + classRoomString[i]);
                                    temp_int[allint] = 1;
                                } else {
//                                    views.setInt(textViewId[allint], "setBackgroundResource", R.color.color_public);
//                                    views.setInt(textViewId[allint], "setTextColor", Color.parseColor("#666666"));
                                    for (int t =0 ; t < textViewId.length; t ++) {
                                        if (temp_int[allint] == 0) {
                                            views.setInt(textViewId[allint], "setBackgroundResource", R.color.color_public);
                                            views.setInt(textViewId[allint], "setTextColor", Color.parseColor("#666666"));
                                            views.setTextViewText(textViewId[allint], courseNameString[i] + classRoomString[i]);
                                        }

                                    }
                                }

                            }
                        }

                    } else {
                        if (courseWeekString[i].indexOf("-") != -1) {//去掉含有","的字符串，处理含有"-"
                            String[] courseWeekString_sz_2 = courseWeekString[i].split("-");
                            if (weekscourse >= Integer.parseInt(courseWeekString_sz_2[0]) && weekscourse <= Integer.parseInt(courseWeekString_sz_2[1])) {
                                views.setInt(textViewId[allint], "setBackgroundResource", textviewColorId[jsonint[i]]);
                                views.setInt(textViewId[allint], "setTextColor", Color.parseColor("#FFFFFF"));
                                views.setTextViewText(textViewId[allint], courseNameString[i] + classRoomString[i]);
                                temp_int[allint] = 1;
                            } else {
//                                views.setInt(textViewId[allint], "setBackgroundResource", R.color.color_public);
//                                views.setInt(textViewId[allint], "setTextColor", Color.parseColor("#666666"));
                                for (int t =0 ; t < textViewId.length; t ++) {
                                    if (temp_int[allint] == 0) {
                                        views.setInt(textViewId[allint], "setBackgroundResource", R.color.color_public);
                                        views.setInt(textViewId[allint], "setTextColor", Color.parseColor("#666666"));
                                        views.setTextViewText(textViewId[allint], courseNameString[i] + classRoomString[i]);
                                    }

                                }
                            }

                        } else {//处理单个周数"8"
                            if (weekscourse == Integer.parseInt(courseWeekString[i])) {
                                views.setInt(textViewId[allint], "setBackgroundResource", textviewColorId[jsonint[i]]);
                                views.setInt(textViewId[allint], "setTextColor", Color.parseColor("#FFFFFF"));
                                views.setTextViewText(textViewId[allint], courseNameString[i] + classRoomString[i]);
                                temp_int[allint] = 1;
                            } else {
//                                views.setInt(textViewId[allint], "setBackgroundResource", R.color.color_public);
//                                views.setInt(textViewId[allint], "setTextColor", Color.parseColor("#666666"));
                                for (int t =0 ; t < textViewId.length; t ++) {
                                    if (temp_int[allint] == 0) {
                                        views.setInt(textViewId[allint], "setBackgroundResource", R.color.color_public);
                                        views.setInt(textViewId[allint], "setTextColor", Color.parseColor("#666666"));
                                        views.setTextViewText(textViewId[allint], courseNameString[i] + classRoomString[i]);
                                    }

                                }
                            }

                        }
                    }
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("SharedPreferences课程数据为空-------->>>");
        }

    }


}
