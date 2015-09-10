package com.yzc.lovehuali;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import com.yzc.lovehuali.tool.SystemBarTintManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/7/25.
 */
public class New_Student_Activity extends ActionBarActivity {

    private Toolbar mToolbar;

    private int[] image_adapterId = new int[]{R.drawable.img_001, R.drawable.img_002, R.drawable.img_003, R.drawable.img_004, R.drawable.img_005,R.drawable.img_006};
    protected static String[] title_adapterId = new String[]{"新生报道FAQ", "华立附近出行路线", "快递信息", "华立作息时间表", "蝴蝶上网教程","华立地图"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_student_activityforlayout);

        //设定状态栏的颜色，当版本大于4.4时起作用
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            //此处可以重新指定状态栏颜色
            tintManager.setStatusBarTintResource(R.color.colorPrimaryDark);
        }

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("新生指南");
        mToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        GridView gridView = (GridView) findViewById(R.id.newstudent_gridview);

        List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < image_adapterId.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("image", image_adapterId[i]);
            map.put("title", title_adapterId[i]);
            listItems.add(map);
        }
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, listItems, R.layout.new_student_adapter, new String[]{"title", "image"}, new int[]{R.id.title_adapter, R.id.image_adapter});
        gridView.setAdapter(simpleAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("gridView的测试------>>>>>>" + position);
                Intent intent = new Intent(getApplication(), New_Student_Second_Activity.class);
                intent.putExtra("temp", String.valueOf(position));
                startActivity(intent);
            }
        });


    }
}
