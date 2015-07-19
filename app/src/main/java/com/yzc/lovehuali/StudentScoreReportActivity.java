package com.yzc.lovehuali;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.yzc.lovehuali.adapter.StudentScoreAnimatedListViewAdapter;
import com.yzc.lovehuali.adapter.StudentScoreExpandableListviewAdapter;
import com.yzc.lovehuali.tool.SystemBarTintManager;
import com.yzc.lovehuali.widget.AnimatedExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class StudentScoreReportActivity extends ActionBarActivity {

    private Toolbar mToolbar;
    private AnimatedExpandableListView elvStudentScore;
    private int total;//统计科目总数
    private List<JSONObject> scoreList;
    private StudentScoreAnimatedListViewAdapter adapter;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_score_report);

        //设定状态栏的颜色，当版本大于4.4时起作用
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            //此处可以重新指定状态栏颜色
            tintManager.setStatusBarTintResource(R.color.colorPrimaryDark);
        }

        intent = getIntent();

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(intent.getStringExtra("reportTitle")+ "  成绩单");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        scoreList = new ArrayList<JSONObject>();

        elvStudentScore = (AnimatedExpandableListView) findViewById(R.id.elvStudentScore);
        elvStudentScore.setAnimationCacheEnabled(true);
        adapter = new StudentScoreAnimatedListViewAdapter(StudentScoreReportActivity.this,scoreList);
        elvStudentScore.setAdapter(adapter);

        elvStudentScore.setOnGroupClickListener(new OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                // We call collapseGroupWithAnimation(int) and
                // expandGroupWithAnimation(int) to animate group
                // expansion/collapse.
                if (elvStudentScore.isGroupExpanded(groupPosition)) {
                    elvStudentScore.collapseGroupWithAnimation(groupPosition);
                } else {
                    elvStudentScore.expandGroupWithAnimation(groupPosition);
                }
                return true;
            }

        });

        JSONArray ScoreArray = null;
        try {
            ScoreArray = new JSONArray(intent.getStringExtra("scoreJsonArrayString"));
            for (int i= 0; i< ScoreArray.length(); i++){
                JSONObject ClassInfo = ScoreArray.getJSONObject(i);
                System.out.println("--------------------------------------");//分割线
                System.out.println("className(科目名称)=" + ClassInfo.getString("className"));
                System.out.println("finalScore(最后成绩)=" + ClassInfo.getString("finalScore"));
                System.out.println("gpa(绩点)=" + ClassInfo.getString("gpa"));
                scoreList.add(ClassInfo);
            }
            Toast.makeText(getApplicationContext(),"共查询到："+ ScoreArray.length() + "科成绩" ,Toast.LENGTH_SHORT).show();
            adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
