package com.yzc.lovehuali;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.yzc.lovehuali.adapter.UserInfoListviewAdapter;
import com.yzc.lovehuali.bmob.StudentUser;
import com.yzc.lovehuali.tool.SystemBarTintManager;

import cn.bmob.v3.BmobUser;


public class UserInformationActivity extends ActionBarActivity {

    private Toolbar mToolbar;
    private ListView ivUserInfo;//个人信息列表
    private TextView tvUserName,tvUserEmail;
    private Button btnBindingEduSystem,btnLogoutUser;
    private UserInfoListviewAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_information);

        //设定状态栏的颜色，当版本大于4.4时起作用
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            //此处可以重新指定状态栏颜色
            tintManager.setStatusBarTintResource(R.color.userInformationBackground);
        }

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("个人信息");
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setBackgroundColor(Color.parseColor("#002196F3"));
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvUserName  = (TextView) findViewById(R.id.tvUserName);
        tvUserEmail = (TextView) findViewById(R.id.tvUserEmail);

        StudentUser user = BmobUser.getCurrentUser(UserInformationActivity.this,StudentUser.class);
        tvUserName.setText(user.getUsername());
        tvUserEmail.setText(user.getEmail());

        ivUserInfo = (ListView) findViewById(R.id.lvUserInfo);
        adapter = new UserInfoListviewAdapter(UserInformationActivity.this,R.layout.listview_user_info_cell);
        ivUserInfo.setAdapter(adapter);

        btnBindingEduSystem = (Button) findViewById(R.id.btnBindingEduSystem);
        btnBindingEduSystem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setClass(UserInformationActivity.this,BindingEduSystemActivity.class);
                startActivity(i);
            }
        });
        btnLogoutUser = (Button) findViewById(R.id.btnLogoutUser);
        btnLogoutUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BmobUser.logOut(UserInformationActivity.this);
                Toast.makeText(UserInformationActivity.this,"账号已注销",Toast.LENGTH_SHORT).show();
                UserInformationActivity.this.finish();
            }
        });
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        adapter.notifyDataSetChanged();//当这个Activity重新启动的时候一般是绑定完教务系统后，所以刷新用户数据
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_information, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
