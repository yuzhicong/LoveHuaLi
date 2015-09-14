package com.yzc.lovehuali;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.Gallery;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.dd.CircularProgressButton;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.yzc.lovehuali.bmob.StudentUser;
import com.yzc.lovehuali.tool.HttpUtil;
import com.yzc.lovehuali.tool.StringUtil;
import com.yzc.lovehuali.tool.SystemBarTintManager;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
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
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.UpdateListener;


public class BindingEduSystemActivity extends ActionBarActivity {

    private Toolbar mToolbar;

    private String idCodeImageUrl, key;
    private String massage;//存储登录信息
    private MaterialEditText etStudentId, etEduSystemPassword;
    private CircularProgressButton cpbtnBindingEduSystem;
    private SharedPreferences sp;

    StudentUser bmobuser;

    StudentUser mUser = new StudentUser();

    //复制部分
    private ProgressDialog m_Dialog = null;
    private CheckBox cbrp;

    private Handler handler = new Handler();
    DefaultHttpClient mHttpClient = new DefaultHttpClient();

    private static Boolean isExit = false;
    private static Boolean hasTask = false;
    Timer tExit = new Timer();

    private String studentEduNumber = "";
    private String pass = "";

    private String myUrl = "jwxt.hualixy.com";// 外网
    //private String myUrl = "10.10.2.18";// 内网

    private String xh = "";
    private String xm = "";

    private int result = 0;

    private String ksInfo = "";

    private String __VIEWSTATE_xscj_gc = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binding_edu_system);

        //设定状态栏的颜色，当版本大于4.4时起作用
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            //此处可以重新指定状态栏颜色
            tintManager.setStatusBarTintColor(Color.parseColor("#e21b24"));
        }

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("绑定教务系统");
        mToolbar.setBackgroundColor(Color.parseColor("#002196F3"));
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        sp = getSharedPreferences("mysp", Context.MODE_PRIVATE);
        bmobuser =  BmobUser.getCurrentUser(BindingEduSystemActivity.this,
                StudentUser.class);

        etStudentId = (MaterialEditText) findViewById(R.id.etStudentId);
        etEduSystemPassword = (MaterialEditText) findViewById(R.id.etEduSystemPassword);

        cpbtnBindingEduSystem = (CircularProgressButton) findViewById(R.id.cpbtnBindindEduSystem);

        cpbtnBindingEduSystem.setIndeterminateProgressMode(true);
        cpbtnBindingEduSystem.setOnClickListener(loginClick);

        etEduSystemPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                 /*判断是否是“Done”键*/
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    /*隐藏软键盘*/
                    InputMethodManager imm = (InputMethodManager) v
                            .getContext().getSystemService(
                                    Context.INPUT_METHOD_SERVICE);
                    if (imm.isActive()) {
                        imm.hideSoftInputFromWindow(
                                v.getApplicationWindowToken(), 0);
                    }

                    loginClick.onClick(v);

                    return true;
                }
                return false;
            }
        });

        // 请求超时
        mHttpClient.getParams().setParameter(
                CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);
        // 读取超时
        mHttpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
                10000);

        MyApp myApp = (MyApp) getApplication();
        myApp.setMyUrl(myUrl);
        //

    }


    TimerTask task = new TimerTask() {
        public void run() {
            isExit = false;
            hasTask = true;
        }
    };


    View.OnClickListener loginClick = new View.OnClickListener() {

        public void onClick(final View v) {
            cpbtnBindingEduSystem.setProgress(0);
            cpbtnBindingEduSystem.setProgress(50);
            new Thread(new Runnable() {

                public void run() {
                    MyApp myApp = (MyApp) getApplication();
                    if (myApp.getMyUrl() != null && myApp.getMyUrl() != "") {
                        myUrl = myApp.getMyUrl();
                    }
                    result = checkUser();// 获取登陆情况

                    // 更新界面
                    handler.post(new Runnable() {
                        public void run() {
                            if (result == 1) {

                                //Toast.makeText(v.getContext(),"用户登录成功！ 欢迎" + xm, Toast.LENGTH_SHORT).show();
                                getScoreJson.start();

                                // 更新界面
                                /*handler.post(new Runnable() {
                                    public void run() {
                                        Context context = v.getContext();
                                        Intent intent = new Intent(context, List_kb.class);
                                        Bundle b = new Bundle();
                                        b.putStringArray("ss", ss);
                                        b.putInt("i", i);
                                        intent.putExtras(b);
                                        context.startActivity(intent);
                                    }
                                });

                                m_Dialog.dismiss();*/


                            }
                            if (result == 0) {
                                Toast.makeText(v.getContext(), "用户验证失败！",
                                        Toast.LENGTH_SHORT).show();
                                cpbtnBindingEduSystem.setProgress(-1);
                            }
                            if (result == -1) {
                                Toast.makeText(v.getContext(), "请检查网络连接！",
                                        Toast.LENGTH_SHORT).show();
                                cpbtnBindingEduSystem.setProgress(-1);
                            }
                            if (result == -2) {
                                Toast.makeText(v.getContext(), "错误原因：系统正忙！",
                                        Toast.LENGTH_SHORT).show();
                                cpbtnBindingEduSystem.setProgress(-1);
                            }
                            if (result == -3) {
                                Toast.makeText(v.getContext(), "教务系统处于瘫痪状态，给点时间它恢复~",
                                        Toast.LENGTH_SHORT).show();
                                cpbtnBindingEduSystem.setProgress(-1);
                            }

                        }
                    });
                }
            }).start();

        }

    };

    Thread getScoreJson = new Thread(new Runnable() {
        @Override
        public void run() {
            String __VIEWSTATE = "";
            StringTokenizer tokenizer = null;
            String ddlXN = "";
            String ddlXQ = "";
            String[] ss = null;
            int i = 0;
            if (__VIEWSTATE_xscj_gc == "") {

                try {
                    ksInfo = HttpUtil
                            .getUrl("http://"
                                            + myUrl
                                            + "/xscj_gc.aspx?xh="
                                            + xh + "&xm=" + xm
                                            + "&gnmkdm=N121501",
                                    mHttpClient,
                                    "http://"
                                            + myUrl
                                            + "/xs_main.aspx?xh="
                                            + xh);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                if (ksInfo != "") {
                    tokenizer = new StringTokenizer(
                            ksInfo);
                    while (tokenizer.hasMoreTokens()) {
                        String valueToken = tokenizer
                                .nextToken();
                        // System.out.println(valueToken);
                        if (StringUtil.isValue(
                                valueToken, "value")
                                && valueToken.length() > 100) {
                            if (StringUtil.getValue(
                                    valueToken,
                                    "value", "\"", 7)
                                    .length() > 100) {
                                __VIEWSTATE = StringUtil
                                        .getValue(
                                                valueToken,
                                                "value",
                                                "\"", 7);// value
                                __VIEWSTATE_xscj_gc = __VIEWSTATE;
                            }
                        }
                    }
                }

            }
            if (__VIEWSTATE_xscj_gc != "") {
                __VIEWSTATE = __VIEWSTATE_xscj_gc;

            }
            System.out.println("数据1:"+ksInfo);
            if (ksInfo != "") {

                ksInfo = ksInfo.substring(ksInfo.indexOf("<p class=\"search_con\">"), ksInfo.length());
                ksInfo = ksInfo.substring(0, ksInfo.indexOf("<select name=\"ddlXN\" id=\"ddlXN\">"));
                System.out.println(ksInfo);
                Pattern p = Pattern.compile("<span id=\"Label\\w\">(.+?)</span>");
                Matcher m = p.matcher(ksInfo);

                int Switch = 0;
                while (m.find()) {
                    switch (Switch) {
                        case 0:
                            mUser.setStudentId(m.group(1).toString().substring(3));
                            break;
                        case 1:
                            mUser.setNickname(m.group(1).toString().substring(3));
                            break;
                        case 2:
                            mUser.setDepartment(m.group(1).toString().substring(3));
                            break;
                        case 4:
                            mUser.setMajor(m.group(1).toString());
                            break;
                        case 5:
                            mUser.setStuClass(m.group(1).toString().substring(4));
                            mUser.setGrade("20" + m.group(1).toString().substring(4, 6));
                            break;
                    }
                    Switch++;
                }
                mUser.setObjectId(bmobuser.getObjectId());
                mUser.setIsBindingEduSystem(true);
                mUser.update(BindingEduSystemActivity.this, new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        cpbtnBindingEduSystem.setProgress(100);
                        SharedPreferences.Editor edit = sp.edit();
                        edit.putString("studentId", etStudentId.getText().toString());
                        edit.putString("eduSystemPsw",etEduSystemPassword.getText().toString());
                        edit.commit();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                BindingEduSystemActivity.this.finish();
                            }
                        },1000);
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        cpbtnBindingEduSystem.setProgress(-1);
                    }
                });

            }
        }
    });

    private int checkUser() {

        // 此处先获取页面，读取到value值为post做准备
        String __VIEWSTATE = "";
        String temp = "";
        StringTokenizer tokenizer = null;

        try {
            temp = HttpUtil.getUrl("http://" + myUrl + "/default2.aspx",
                    mHttpClient, "");
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        if (temp == "") {
            return -1;
        }
        tokenizer = new StringTokenizer(temp);
        while (tokenizer.hasMoreTokens()) {
            String valueToken = tokenizer.nextToken();
            if (StringUtil.isValue(valueToken, "value")) {
                if (StringUtil.getValue(valueToken, "value", "\"", 7).length() == 48) {
                    __VIEWSTATE = StringUtil.getValue(valueToken, "value", "\"", 7);// value
                }
            }
        }

        studentEduNumber = etStudentId.getText().toString();
        pass = etEduSystemPassword.getText().toString();

        List<BasicNameValuePair> pairs = new ArrayList<BasicNameValuePair>();
        pairs.add(new BasicNameValuePair("__VIEWSTATE", __VIEWSTATE));
        pairs.add(new BasicNameValuePair("txtUserName", studentEduNumber));
        pairs.add(new BasicNameValuePair("TextBox2", pass));
        pairs.add(new BasicNameValuePair("RadioButtonList1", "%D1%A7%C9%FA"));
        pairs.add(new BasicNameValuePair("Button1", null));
        pairs.add(new BasicNameValuePair("lbLanguage", null));

        String info = "";
        try {
            info = HttpUtil.postUrl("http://" + myUrl + "/default2.aspx",
                    pairs, mHttpClient, "");
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (info != "") {
            MyApp myApp = (MyApp) getApplication();
            myApp.setmHttpClient(mHttpClient);// 将对象放入app中
            tokenizer = new StringTokenizer(info);

            //System.out.println(info);//输出教务系统返回数据

            while (tokenizer.hasMoreTokens()) {
                String valueToken = tokenizer.nextToken();
                if (StringUtil.isValue(valueToken, "密码错误！！")) {
                    return 0;//defer
                }
                if (StringUtil.isValue(valueToken, " <title>ERROR")) {
                    return -2;
                }
                if (StringUtil.isValue(valueToken, "The page you are looking for is temporarily unavailable.")) {
                    return -3;
                }
                if (StringUtil.isValue(valueToken, "id=\"xhxm")) {
                    xh = studentEduNumber;
                    //xh = StringUtil.getValue(valueToken, "id=\"xhxm", "<", 10);// value
                    xm = StringUtil.getValue(valueToken, "id=\"xhxm", "同", 6);
                    xm = xm.substring(4, xm.length());

                    //xm = StringUtil.getValue(tokenizer.nextToken().toString(), "","同", 0);// 从登陆后页面取得姓名
                }
            }
            return 1;
        } else {
            return -1;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_binding_edu_system, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            return true;
        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_chance_network) {
            if (myUrl.equals("jwxt.hualixy.com")) {
                myUrl = "10.10.2.18";
                Toast.makeText(BindingEduSystemActivity.this, "已切换网络为内网！", Toast.LENGTH_SHORT).show();
            } else if (myUrl.equals("10.10.2.18")) {
                myUrl = "jwxt.hualixy.com";
                Toast.makeText(BindingEduSystemActivity.this, "已切换网络为外网！", Toast.LENGTH_SHORT).show();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
