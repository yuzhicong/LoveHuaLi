package com.yzc.lovehuali;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

/**
 * Created by Administrator on 2015/8/21.
 */
public class Course_WidgetProvider extends AppWidgetProvider {

    private int[] textviewColorId = new int[]{R.color.wcolor01, R.color.wcolor02, R.color.wcolor03, R.color.wcolor04, R.color.wcolor05, R.color.wcolor06, R.color.wcolor07, R.color.wcolor08, R.color.wcolor09, R.color.wcolor10, R.color.wcolor11, R.color.wcolor12, R.color.wcolor13, R.color.wcolor14, R.color.wcolor15, R.color.wcolor16, R.color.wcolor17};

    private int[] textViewId = new int[]{R.id.week_widget1_1_2_textView, R.id.week_widget2_1_2_textView, R.id.week_widget3_1_2_textView, R.id.week_widget4_1_2_textView, R.id.week_widget5_1_2_textView,
            R.id.week_widget1_3_4_textView, R.id.week_widget2_3_4_textView, R.id.week_widget3_3_4_textView, R.id.week_widget4_3_4_textView, R.id.week_widget5_3_4_textView,
            R.id.week_widget1_5_6_textView, R.id.week_widget2_5_6_textView, R.id.week_widget3_5_6_textView, R.id.week_widget4_5_6_textView, R.id.week_widget5_5_6_textView,
            R.id.week_widget1_7_8_textView, R.id.week_widget2_7_8_textView, R.id.week_widget3_7_8_textView, R.id.week_widget4_7_8_textView, R.id.week_widget5_7_8_textView};
    private String classRoom;
    private String week;
    private String courseName;
    private String courseSection;
    private String courseWeek;
    private String teacher;

    private String[] classRoomString;
    private String[] weekString;
    private String[] courseNameString;
    private String[] courseSectionString;
    private String[] courseWeekString;
    private String[] teacherString;

    private int[] jsonint;

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


//        String[] courseSectionString_sz = courseSectionString[i].split(",");
//        System.out.println("课程的节数选择_小插件===>>>" + (Integer.parseInt(courseSectionString_sz[1]) - 2) / 2 + "\t" + courseNameString[i]);
//        int tempint = (Integer.parseInt(courseSectionString_sz[1]) - 2) / 2;
//        int allint = tempint * 5 + Integer.parseInt(weekString[i]) - 1;
//        System.out.println("桌面小插件课程的Id号======>>>" + "\t" + allint);
//        views.setTextViewText(textViewId[allint], courseNameString[i] + classRoomString[i]);
//        int testweek = 18;//选择周数的小测试
//        if (testweek >= Integer.parseInt(weekString_widget[0]) && testweek <= Integer.parseInt(weekString_widget[1])){
//            views.setInt(textViewId[allint],"setBackgroundResource",textviewColorId[jsonint[i]]);
//        }else {
//            views.setInt(textViewId[allint],"setBackgroundResource",R.color.wcolor_public);
//            views.setInt(textViewId[allint],"setTextColor", Color.parseColor("#666666"));
//        }
//        
        appWidgetManager.updateAppWidget(appWidgetIds, views);

    }

    @Override
    public void onEnabled(Context context) {


    }
}
