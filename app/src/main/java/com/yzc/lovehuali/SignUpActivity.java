package com.yzc.lovehuali;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.dd.CircularProgressButton;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.yzc.lovehuali.bmob.StudentUser;
import com.yzc.lovehuali.tool.SystemBarTintManager;

import cn.bmob.v3.listener.SaveListener;


public class SignUpActivity extends ActionBarActivity {

    private Toolbar mToolbar;
    private CircularProgressButton cpbtnSignUp;
    private StudentUser user;//定义用户
    private MaterialEditText etUserName,etPassword,etEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //设定状态栏的颜色，当版本大于4.4时起作用
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            //此处可以重新指定状态栏颜色
            tintManager.setStatusBarTintResource(R.color.material_blue);
        }

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("用户注册");// 标题的文字需在setSupportActionBar之前，不然会无效
        mToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etUserName = (MaterialEditText) findViewById(R.id.etUserName);
        etPassword = (MaterialEditText) findViewById(R.id.etPassword);
        etEmail = (MaterialEditText) findViewById(R.id.etEmail);

        cpbtnSignUp = (CircularProgressButton) findViewById(R.id.cpbtnSignUp);
        cpbtnSignUp.setIndeterminateProgressMode(true);
        cpbtnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cpbtnSignUp.setProgress(0);
                cpbtnSignUp.setProgress(50);//注册按钮开始显示进度

                user = new StudentUser();
                user.setUsername(etUserName.getText().toString());
                user.setPassword(etPassword.getText().toString());
                user.setEmail(etEmail.getText().toString());
                user.signUp(SignUpActivity.this,new SaveListener() {
                    @Override
                    public void onSuccess() {
                        cpbtnSignUp.setProgress(100);
                        //Toast.makeText(SignUpActivity.this, "注册成功！", Toast.LENGTH_SHORT).show();
                        final int activityFinishDelay = 2000;
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                SignUpActivity.this.finish();
                            }
                        }, activityFinishDelay);

                    }

                    @Override
                    public void onFailure(int i, String s) {

                        //Toast.makeText(SignUpActivity.this, "错误码："+ i +"注册失败：" + s, Toast.LENGTH_SHORT).show();
                        switch (i){
                            case 202:
                                cpbtnSignUp.setErrorText("注册失败，用户名已存在");
                                break;
                            case 203:
                                cpbtnSignUp.setErrorText("注册失败，邮箱已使用");
                                break;
                            case 305:
                                cpbtnSignUp.setErrorText("注册失败，用户名或密码为空");
                                break;
                            case 9016:
                                cpbtnSignUp.setErrorText("无网络连接，请检查您的手机网络");
                                break;
                            case 9010:
                                cpbtnSignUp.setErrorText("网络连接超时");
                                break;
                            default:
                                cpbtnSignUp.setErrorText("注册失败");
                                break;
                        }
                        cpbtnSignUp.setProgress(-1);
                    }
                });
            }
        });
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_sign_up, menu);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
