package com.yzc.lovehuali;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.yzc.lovehuali.tool.ACache;
import com.yzc.lovehuali.tool.SystemBarTintManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class EditCourseActivity extends ActionBarActivity {

    private Toolbar mToolbar;

    private ACache aCache;
//注释
    private int temp = 0;
    private int temp_save = 0;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_course);

        //设定状态栏的颜色，当版本大于4.4时起作用
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            //此处可以重新指定状态栏颜色
            tintManager.setStatusBarTintResource(R.color.colorPrimaryDark);
        }

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("查看课程");
        mToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        final MaterialEditText course_name = (MaterialEditText) findViewById(R.id.course_edit_nameId);
        final MaterialEditText course_room = (MaterialEditText) findViewById(R.id.course_edit_classId);
        final MaterialEditText course_teachar = (MaterialEditText) findViewById(R.id.course_edit_teacher);
        final MaterialEditText course_weeks = (MaterialEditText) findViewById(R.id.course_edit_weekId);
        final MaterialEditText course_section = (MaterialEditText) findViewById(R.id.course_edit_sectionId);


//        final Button save_course = (Button) findViewById(R.id.save_course);


        Intent intent = getIntent();

        final String intentname = intent.getStringExtra("intentname");
        final String intentroom = intent.getStringExtra("intentroom");
        final String intentteacher = intent.getStringExtra("intentteacher");
        final String intentcourseWeek = intent.getStringExtra("intentcourseWeek");
        final String intentsection = intent.getStringExtra("intentsection");
        final String intentweek = intent.getStringExtra("intentweek");


//        if (!intentname.equals("")) {
//            course_name.setFocusable(false);
//            course_room.setFocusable(false);
//            course_teachar.setFocusable(false);
//            course_weeks.setFocusable(false);
//
//        } else {
//            mToolbar.setTitle("添加课程");
//            temp_save = 1;
//        }
//2015.5.18
        if (intentname != null) {
            course_name.setFocusable(false);
            course_room.setFocusable(false);
            course_teachar.setFocusable(false);
            course_weeks.setFocusable(false);
            course_section.setFocusable(false);
        } else {
            mToolbar.setTitle("添加课程");
            temp_save = 1;
        }


        course_name.setText(intentname);
        course_name.setSelection(course_name.getText().length());
        course_room.setText(intentroom);
        course_teachar.setText(intentteacher);
        course_weeks.setText(intentcourseWeek);
        course_section.setText(intentsection);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (temp_save == 0) {
            getMenuInflater().inflate(R.menu.menu_edit_course, menu);
        } else {
            getMenuInflater().inflate(R.menu.menu_edit_course_forsave, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_empty:
                AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                alertDialog.setTitle("删除？");
                alertDialog.setMessage("确定删除该节课程吗？？");

                alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "   是的      ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = getIntent();
                        String weekss = intent.getStringExtra("intentweek");

                        MaterialEditText course_section = (MaterialEditText) findViewById(R.id.course_edit_sectionId);
                        MaterialEditText course_name = (MaterialEditText) findViewById(R.id.course_edit_nameId);
                        String getsection = course_section.getText().toString();
                        String getname = course_name.getText().toString();


                        if (getname.equals("")) {
                            Toast.makeText(getApplication(), "请输入有效的课程信息！", Toast.LENGTH_LONG).show();
                        } else {

                            String newjson = "";
                            String[] jsonString;
                            aCache = ACache.get(getApplication());
                            String courseJson = aCache.getAsString("courseJson");

                            try {
                                JSONArray jsonArray = new JSONArray(courseJson);
                                jsonString = new String[jsonArray.length()];

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String jsonweek = jsonObject.getString("week");
                                    String jsoncourseSection = jsonObject.getString("courseSection");
                                    String jsoncourseWeek = jsonObject.getString("courseWeek");
                                    String jsoncourseName = jsonObject.getString("courseName");
                                    String jsonclassRoom = jsonObject.getString("classRoom");
                                    String jsonteacher = jsonObject.getString("teacher");

                                    if (weekss.equals(jsonweek) && getsection.equals(jsoncourseSection)) {
                                        continue;

                                    } else {

                                        jsonString[i] = "{\"courseWeek\":\"" + jsoncourseWeek + "\",\"classRoom\":\"" + jsonclassRoom + "\",\"teacher\":\"" + jsonteacher + "\",\"courseSection\":\"" + jsoncourseSection + "\",\"courseName\":\"" + jsoncourseName + "\",\"week\":" + jsonweek + "}";
                                    }

                                }


                                for (int i = 0; i < jsonString.length; i++) {
                                    System.out.println("======================++" + jsonString[i]);
                                    if (jsonString[i] == null) {
                                        continue;
                                    } else {
                                        newjson = newjson + jsonString[i] + ",";
                                    }
                                }
                                if (!newjson.equals("")) {
                                    newjson = newjson.substring(0, newjson.length() - 1);
                                    newjson = "[" + newjson + "]";

                                    aCache.put("courseJson", newjson);
                                    finish();
                                } else {
                                    aCache.put("courseJson", "");
                                    finish();
                                }
                                System.out.println("======================++" + newjson);


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                });
                alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "      不是   ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alertDialog.show();
                break;
            case R.id.action_edit:

                mToolbar.setTitle("编辑课程");
                if (temp == 0) {
                    MaterialEditText course_name_edit = (MaterialEditText) findViewById(R.id.course_edit_nameId);
                    MaterialEditText course_room_edit = (MaterialEditText) findViewById(R.id.course_edit_classId);
                    MaterialEditText course_teachar_edit = (MaterialEditText) findViewById(R.id.course_edit_teacher);
                    MaterialEditText course_weeks_edit = (MaterialEditText) findViewById(R.id.course_edit_weekId);
                    MaterialEditText course_section_edit = (MaterialEditText) findViewById(R.id.course_edit_sectionId);
                    course_name_edit.setFocusableInTouchMode(true);
                    course_room_edit.setFocusableInTouchMode(true);
                    course_teachar_edit.setFocusableInTouchMode(true);
                    course_weeks_edit.setFocusableInTouchMode(true);
//                  course_section_edit.setFocusableInTouchMode(true);

                    item.setTitle("保存");
                    temp = 1;

                } else {
                    final MaterialEditText course_name = (MaterialEditText) findViewById(R.id.course_edit_nameId);
                    final MaterialEditText course_room = (MaterialEditText) findViewById(R.id.course_edit_classId);
                    final MaterialEditText course_teachar = (MaterialEditText) findViewById(R.id.course_edit_teacher);
                    final MaterialEditText course_weeks = (MaterialEditText) findViewById(R.id.course_edit_weekId);
                    final MaterialEditText course_section = (MaterialEditText) findViewById(R.id.course_edit_sectionId);

                    Intent intent = getIntent();

                    final String intentweek = intent.getStringExtra("intentweek");

//                获取点击TextView的内容
                    String getname = course_name.getText().toString();
                    String getroom = course_room.getText().toString();
                    String getteacher = course_teachar.getText().toString();
                    String getweeks = course_weeks.getText().toString();
                    String getsection = course_section.getText().toString();
                    String week = intentweek;


                    if (!getname.equals("")) {

                        int TEST = 0;
                        String newjson = "";
                        String[] jsonString;
                        aCache = ACache.get(getApplication());
                        String courseJson = aCache.getAsString("courseJson");

                        try {
                            JSONArray jsonArray = new JSONArray(courseJson);
                            jsonString = new String[jsonArray.length()];

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String jsonweek = jsonObject.getString("week");
                                String jsoncourseSection = jsonObject.getString("courseSection");
                                String jsoncourseWeek = jsonObject.getString("courseWeek");
                                String jsoncourseName = jsonObject.getString("courseName");
                                String jsonclassRoom = jsonObject.getString("classRoom");
                                String jsonteacher = jsonObject.getString("teacher");

                                if (week.equals(jsonweek) && getsection.equals(jsoncourseSection)) {
                                    TEST = 1;
                                    jsoncourseWeek = getweeks;
                                    jsoncourseName = getname;
                                    jsonclassRoom = getroom;
                                    jsonteacher = getteacher;
                                }
                                jsonString[i] = "{\"courseWeek\":\"" + jsoncourseWeek + "\",\"classRoom\":\"" + jsonclassRoom + "\",\"teacher\":\"" + jsonteacher + "\",\"courseSection\":\"" + jsoncourseSection + "\",\"courseName\":\"" + jsoncourseName + "\",\"week\":" + jsonweek + "}";

                            }

                            if (TEST == 1) {
                                for (int i = 0; i < jsonString.length; i++) {
                                    newjson = newjson + jsonString[i] + ",";
                                    if (i == jsonString.length - 1) {
                                        newjson = newjson.substring(0, newjson.length() - 1);
                                        newjson = "[" + newjson + "]";
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (TEST == 0) {
                            String newjsonone = "{\"courseWeek\":\"" + getweeks + "\",\"classRoom\":\"" + getroom + "\",\"teacher\":\"" +
                                    getteacher + "\",\"courseSection\":\"" + getsection + "\",\"courseName\":\"" + getname + "\",\"week\":" + week + "}";
                            newjson = courseJson.substring(0, courseJson.length() - 1) + "," + newjsonone + "]";

                        }
                        Log.d("MainActivity", " ---------------------->>>" + newjson);
                        aCache.put("courseJson", newjson);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "请输入完整的课程信息！", Toast.LENGTH_SHORT).show();
                    }

                }


                break;
            case R.id.action_forsave:

                final MaterialEditText course_name = (MaterialEditText) findViewById(R.id.course_edit_nameId);
                final MaterialEditText course_room = (MaterialEditText) findViewById(R.id.course_edit_classId);
                final MaterialEditText course_teachar = (MaterialEditText) findViewById(R.id.course_edit_teacher);
                final MaterialEditText course_weeks = (MaterialEditText) findViewById(R.id.course_edit_weekId);
                final MaterialEditText course_section = (MaterialEditText) findViewById(R.id.course_edit_sectionId);

                Intent intent = getIntent();

                final String intentweek = intent.getStringExtra("intentweek");

//                获取点击TextView的内容
                String getname = course_name.getText().toString();
                String getroom = course_room.getText().toString();
                String getteacher = course_teachar.getText().toString();
                String getweeks = course_weeks.getText().toString();
                String getsection = course_section.getText().toString();
                String week = intentweek;


                if (!getname.equals("")) {

                    int TEST = 0;
                    String newjson = "";
                    String[] jsonString;
                    aCache = ACache.get(getApplication());
                    String courseJson = aCache.getAsString("courseJson");

                    if (courseJson != null) {//2015.5.18

                        try {
                            JSONArray jsonArray = new JSONArray(courseJson);
                            jsonString = new String[jsonArray.length()];

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String jsonweek = jsonObject.getString("week");
                                String jsoncourseSection = jsonObject.getString("courseSection");
                                String jsoncourseWeek = jsonObject.getString("courseWeek");
                                String jsoncourseName = jsonObject.getString("courseName");
                                String jsonclassRoom = jsonObject.getString("classRoom");
                                String jsonteacher = jsonObject.getString("teacher");

                                if (week.equals(jsonweek) && getsection.equals(jsoncourseSection)) {
                                    TEST = 1;
                                    jsoncourseWeek = getweeks;
                                    jsoncourseName = getname;
                                    jsonclassRoom = getroom;
                                    jsonteacher = getteacher;
                                }
                                jsonString[i] = "{\"courseWeek\":\"" + jsoncourseWeek + "\",\"classRoom\":\"" + jsonclassRoom + "\",\"teacher\":\"" + jsonteacher + "\",\"courseSection\":\"" + jsoncourseSection + "\",\"courseName\":\"" + jsoncourseName + "\",\"week\":" + jsonweek + "}";

                            }

                            if (TEST == 1) {
                                for (int i = 0; i < jsonString.length; i++) {
                                    newjson = newjson + jsonString[i] + ",";
                                    if (i == jsonString.length - 1) {
                                        newjson = newjson.substring(0, newjson.length() - 1);
                                        newjson = "[" + newjson + "]";
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        if (TEST == 0) {
                            String newjsonone = "{\"courseWeek\":\"" + getweeks + "\",\"classRoom\":\"" + getroom + "\",\"teacher\":\"" +
                                    getteacher + "\",\"courseSection\":\"" + getsection + "\",\"courseName\":\"" + getname + "\",\"week\":" + week + "}";

                            if (courseJson.length() == 0){
                                newjson = "[" + newjsonone + "]";
                            }else {
                                newjson = courseJson.substring(0, courseJson.length() - 1) + "," + newjsonone + "]";
                            }
                        }
                    }
                    Log.d("MainActivity", " ---------------------->>>" + newjson);
                    aCache.put("courseJson", newjson);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "请输入完整的课程信息！", Toast.LENGTH_SHORT).show();
                }


                break;

            default:
                break;


        }

        return super.onOptionsItemSelected(item);
    }
}
