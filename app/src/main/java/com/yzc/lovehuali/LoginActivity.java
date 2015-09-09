package com.yzc.lovehuali;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.dd.CircularProgressButton;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.yzc.lovehuali.bmob.StudentUser;
import com.yzc.lovehuali.tool.ACache;
import com.yzc.lovehuali.tool.SystemBarTintManager;

import android.widget.CheckBox;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;


public class LoginActivity extends ActionBarActivity {

    private Toolbar mToolbar;
    private CircularProgressButton cpbtnLogin;
    private StudentUser newUser;//定义用户
    private MaterialEditText etUserName,etPassword;
    private TextView tvForgotPw,tvSignUp;
    private SharedPreferences sp;
    private CheckBox cbSavePw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //设定状态栏的颜色，当版本大于4.4时起作用
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            //此处可以重新指定状态栏颜色
            tintManager.setStatusBarTintResource(R.color.colorPrimaryDark);
        }

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("用户登录");// 标题的文字需在setSupportActionBar之前，不然会无效
        mToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //mToolbar.setNavigationIcon(R.drawable.icon_arrowleft);

        Bmob.initialize(this,"ce44de9648c859db8001d4187e9d38b9");//BmobSDK初始化

        sp = getSharedPreferences("mysp", Context.MODE_PRIVATE);//得到一个SharedPreferences,前面是定义的名称，后面是分享模式

        etUserName = (MaterialEditText) findViewById(R.id.etUserName);
        etPassword = (MaterialEditText) findViewById(R.id.etPassword);

        cbSavePw = (CheckBox) findViewById(R.id.cbSavePw);
        cbSavePw.setChecked(sp.getBoolean("isSavePassword",true));
        cbSavePw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor e = sp.edit();//当CheckBox的选中状态被改变时，用sp.edit()得到编辑sp的对象。
                e.putBoolean("isSavePassword",isChecked);//通过putBoolean方法把对应的键值对进行编辑，注意还没有保存
                e.commit();//通过Editor 对象的commit()方法提交保存即可。
            }
        });




        etUserName.setText(sp.getString("UserName",""));
        if(cbSavePw.isChecked()) {
            etPassword.setText(sp.getString("Password", ""));
        }

        etPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                 /*判断是否是“Done”键*/
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    /*隐藏软键盘*/
                    InputMethodManager imm = (InputMethodManager) v
                            .getContext().getSystemService(
                                    Context.INPUT_METHOD_SERVICE);
                    if (imm.isActive()) {
                        imm.hideSoftInputFromWindow(
                                v.getApplicationWindowToken(), 0);
                    }

                    new onLoginListener().onClick(v);

                    return true;
                }
                return false;
            }
        });

        cpbtnLogin = (CircularProgressButton) findViewById(R.id.cpbtnLogin);
        cpbtnLogin.setIndeterminateProgressMode(true);
        cpbtnLogin.setOnClickListener(new onLoginListener());

        tvSignUp = (TextView) findViewById(R.id.tvSignUp);
        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setClass(LoginActivity.this,SignUpActivity.class);
                startActivity(i);
            }
        });

        tvForgotPw = (TextView) findViewById(R.id.tvForgotPw);


    }

    class onLoginListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            cpbtnLogin.setProgress(0);
            cpbtnLogin.setProgress(50);
            newUser = new StudentUser();
            newUser.setUsername(etUserName.getText().toString());
            newUser.setPassword(etPassword.getText().toString());
            newUser.login(LoginActivity.this,new SaveListener() {
                @Override
                public void onSuccess() {
                    cpbtnLogin.setProgress(100);
                    //Toast.makeText(LoginActivity.this, "登录成功！", Toast.LENGTH_SHORT).show();
                        /*StudentUser user = BmobUser.getCurrentUser(getApplicationContext(),StudentUser.class);
                        ACache mCache = ACache.get(getApplicationContext());
                        if(!user.getStuCourse().isEmpty()) {
                            System.out.println("存储用户课程数据");
                            mCache.put("courseJson", user.getStuCourse().toString());
                        }else{
                            mCache.remove("courseJson");
                            System.out.println("删除用户课程数据");
                        }*/
                    final int activityFinishDelay = 1000;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //Intent i = new Intent(LoginActivity.this,MainActivity.class);
                            //startActivity(i);
                            LoginActivity.this.finish();
                        }
                    }, activityFinishDelay);

                }

                @Override
                public void onFailure(int i, String s) {

                    //Toast.makeText(LoginActivity.this, "错误码：" + i + "登录失败：" + s, Toast.LENGTH_SHORT).show();
                    switch (i){
                        case 101:
                            cpbtnLogin.setErrorText("登录失败，用户名或密码错误");
                            break;
                        case 305:
                            cpbtnLogin.setErrorText("登录失败，用户名或密码为空");
                            break;
                        case 9016:
                            cpbtnLogin.setErrorText("无网络连接，请检查您的手机网络");
                            break;
                        case 9010:
                            cpbtnLogin.setErrorText("网络连接超时");
                            break;
                        default:
                            cpbtnLogin.setErrorText("登录失败");
                            break;
                    }
                    cpbtnLogin.setProgress(-1);
                }
            });
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        System.out.println(id);

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_sign_up) {
            Intent i = new Intent();
            i.setClass(LoginActivity.this,SignUpActivity.class);
            startActivity(i);
            return true;
        }
        if(id == android.R.id.home){
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences.Editor e = sp.edit();
        e.putString("UserName",etUserName.getText().toString());
        e.commit();
        if(cbSavePw.isChecked())
        {
            e.putString("Password",etPassword.getText().toString());
            e.commit();
        }
    }
}
