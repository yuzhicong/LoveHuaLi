package com.yzc.lovehuali;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.yzc.lovehuali.database.DbSoftwareNotice;
import com.yzc.lovehuali.tool.SystemBarTintManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SoftwareNoticeActivity extends ActionBarActivity {

    private ListView lvSoftwareNotice;
    private SimpleCursorAdapter adapter;
    private DbSoftwareNotice dbsn;
    private SQLiteDatabase dbRead,dbWrite;
    private Cursor c;
    private Toolbar mToolbar;
    private LinearLayout llSoftwareNotice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_software_notice);

        //设定状态栏的颜色，当版本大于4.4时起作用
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            //此处可以重新指定状态栏颜色
            tintManager.setStatusBarTintResource(R.color.material_blue);
        }

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("软件公告");
        mToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        llSoftwareNotice = (LinearLayout) findViewById(R.id.llSoftwareNotice);

        lvSoftwareNotice = (ListView) findViewById(R.id.lvSoftwareNotice);

        dbsn = new DbSoftwareNotice(this);
        dbRead = dbsn.getReadableDatabase();
        dbWrite = dbsn.getWritableDatabase();

        adapter = new SimpleCursorAdapter(this,R.layout.academy_news_items,c,new String[]{"title","publishDate","content"}, new int[]{R.id.title,R.id.publishdata});
        c = dbRead.query("software_notice", null, null, null, null, null, null);

        if(c.getCount()==0){
            llSoftwareNotice.setBackgroundResource(R.drawable.no_notice);
        }

        adapter.changeCursor(c);
        lvSoftwareNotice.setAdapter(adapter);
        lvSoftwareNotice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                c.moveToPosition(position);
                Intent intent = new Intent(SoftwareNoticeActivity.this,NewsDetailsActivity.class);
                intent.putExtra("title",c.getString(c.getColumnIndex("title")));
                intent.putExtra("publishDate",c.getString(c.getColumnIndex("publishDate")));
                intent.putExtra("context",c.getString(c.getColumnIndex("content")));
                intent.putExtra("mode",2);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_software_notice, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_clean) {
            AlertDialog.Builder builder = new AlertDialog.Builder(SoftwareNoticeActivity.this);
            builder.setMessage("确定清空软件公告吗？");
            builder.setTitle("提示");
            builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dbWrite.execSQL("DELETE FROM software_notice");
                    c = dbRead.query("software_notice", null, null, null, null, null, null);
                    adapter.changeCursor(c);
                    llSoftwareNotice.setBackgroundResource(R.drawable.no_notice);
                    Toast.makeText(SoftwareNoticeActivity.this, "清空完成!", Toast.LENGTH_SHORT).show();
                }
            });
            builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            builder.create().show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
