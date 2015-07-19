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
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.dd.CircularProgressButton;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.yzc.lovehuali.bmob.StudentUser;
import com.yzc.lovehuali.tool.ACache;
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


public class getEduSystemSchedulerActivity extends ActionBarActivity {

    private Toolbar mToolbar;
    private ImageSwitcher idCodeImage;
    private Bitmap idCodeBitmap;
    private String idCodeImageUrl,key;
    private String massage;//存储登录信息

    private String sessionId;//客户端缓存标识
    private CircularProgressButton cpbtnBindingEduSystem;
    final String getIdCodeUrl = new String("http://61.160.137.195:18001/zdinter/getInter.action?interCode=HLXY0001&para=&callback=HLXY0001");//获取验证码和密钥的网址，要考虑验证码获取失败的情况


    private ACache mCache;//全局缓存工具类对象
    //复制部分
    private MaterialEditText user;
    private MaterialEditText password;
    private CircularProgressButton loginBtn;
    private Button logoutBtn;
    private ProgressDialog m_Dialog = null;
    private CheckBox cbrp;
    private SharedPreferences sp;

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

    //
    private String[] ss = null;
    private int i = 0;
    private String ksInfo = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_edusystem_scheduler);

        //设定状态栏的颜色，当版本大于4.4时起作用
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            //此处可以重新指定状态栏颜色
            tintManager.setStatusBarTintResource(R.color.courseBackground);
        }

        mCache = ACache.get(this);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("获取课程表");
        mToolbar.setBackgroundColor(Color.parseColor("#002196F3"));
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //
        user = (MaterialEditText) findViewById(R.id.etStudentId);
        password = (MaterialEditText) findViewById(R.id.etEduSystemPassword);

        loginBtn = (CircularProgressButton) findViewById(R.id.cpbtnBindindEduSystem);
        loginBtn.setIndeterminateProgressMode(true);

        loginBtn.setOnClickListener(loginClick);


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
            loginBtn.setProgress(0);
            loginBtn.setProgress(50);

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
                                /*if (cbrp.isChecked()) {
                                    sp = getSharedPreferences(
                                            "UserInfo",
                                            Context.MODE_WORLD_WRITEABLE
                                                    | Context.MODE_WORLD_READABLE);

                                    sp.edit()
                                            .putString("account",
                                                    user.getText().toString())
                                            .commit();
                                    sp.edit()
                                            .putString(
                                                    "password",
                                                    password.getText()
                                                            .toString())
                                            .commit();
                                }*/
                                Toast.makeText(v.getContext(),
                                        "用户登录成功！ 欢迎" + xm, Toast.LENGTH_SHORT)
                                        .show();
                                /*Context context = v.getContext();
                                Intent intent = new Intent(context,
                                        LoginSuccess.class);
                                Bundle map = new Bundle();
                                map.putSerializable("xh", xh);
                                map.putSerializable("xm", xm);
                                intent.putExtra("session", map);
                                context.startActivity(intent);
                                Login.this.finish();*/

                                getCourseJson.start();

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
                                loginBtn.setProgress(-1);
                            }
                            if (result == -1) {
                                Toast.makeText(v.getContext(), "请检查网络连接！",
                                        Toast.LENGTH_SHORT).show();
                                loginBtn.setProgress(-1);
                            }
                            if (result == -2) {
                                Toast.makeText(v.getContext(), "错误原因：系统正忙！",
                                        Toast.LENGTH_SHORT).show();
                                loginBtn.setProgress(-1);
                            }

                        }
                    });

                    //m_Dialog.dismiss();
                }
            }).start();

        }

    };

    Thread getCourseJson = new Thread(new Runnable() {
        @Override
        public void run() {
            String kbInfo = "";

            try {
                kbInfo = HttpUtil.getUrl("http://" + myUrl
                        + "/xskbcx.aspx?xh=" + xh + "&xm=" + xm
                        + "&gnmkdm=N121603", mHttpClient, "http://"
                        + myUrl + "/xs_main.aspx?xh=" + xh);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            String temp = kbInfo.replaceAll("</td>", "</td>\n");// 转化换行
            Pattern p = Pattern.compile("(?<=>).*(?=</td>)");
            Matcher m = p.matcher(temp);
            ss = null;
            ss = new String[200];
            JSONArray courseArray = new JSONArray();//课程JsonArray
            i = 0;
            int w=0,fw=0;//w为计算表格位置，fw为辅助计算参数
            while (m.find() && (!m.group().toString().equals("编号"))) {
                if(m.group().toString().equals("&nbsp;")){
                    w++;
                    if(w%7==0){
                        w=w+fw;
                        fw=0;
                    }
                }

                if (!(m.group().toString().equals("&nbsp;"))
                        && !(m.group().toString().equals(""))
                        && !(m.group().toString().equals("早晨"))
                        && !(m.group().toString().equals("上午"))
                        && !(m.group().toString().equals("下午"))
                        && !(m.group().toString().equals("晚上"))
                        && !(m.group().toString().substring(0, 1)
                        .equals("星"))
                        && !(m.group().toString().equals("时间"))
                        && !(m.group().toString().substring(0, 1)
                        .equals("第"))) {
                    ss[i] = m.group().toString();
                    //System.out.println(ss[i]);
                            /*System.out.println(ss[i].substring(ss[i].lastIndexOf("<br>") + 4, ss[i].length()));
                            System.out.println(ss[i].substring(0,ss[i].indexOf("<br>")));
                            System.out.println(ss[i].substring(ss[i].indexOf("第") + 1, ss[i].indexOf("节")));
                            System.out.println(ss[i].substring(ss[i].indexOf("{") + 1, ss[i].lastIndexOf("}")));
                            System.out.println(ss[i].substring(ss[i].indexOf("周") + 1, ss[i].indexOf("周") + 2));*/
                    String week = ss[i].substring(ss[i].indexOf("周") + 1, ss[i].indexOf("周") + 2);
                    w++;
                    fw+=1;
                    int courseWeek=0;
                    courseWeek = w%7;
                    String courseSection = "";
                    switch (w/7){
                        case 0:
                        case 1:
                            courseSection="1,2";
                            break;
                        case 2:
                        case 3:
                            courseSection="3,4";
                            break;
                        case 4:
                        case 5:
                            courseSection="5,6";
                            break;
                        case 6:
                        case 7:
                            courseSection="7,8";
                            break;
                        case 8:
                        case 9:
                            courseSection="9,10";
                            break;
                    }
                    /*if(week.equals("一")){
                        courseWeek = 1;
                    }
                    if(week.equals("二")){
                        courseWeek = 2;
                    }
                    if(week.equals("三")){
                        courseWeek = 3;
                    }
                    if(week.equals("四")){
                        courseWeek = 4;
                    }
                    if(week.equals("五")){
                        courseWeek = 5;
                    }
                    if(week.equals("六")){
                        courseWeek = 6;
                    }
                    if(week.equals("日")){
                        courseWeek = 7;
                    }*/


                    try {

                        JSONObject classObject = new JSONObject();
                        classObject.put("classRoom", ss[i].substring(ss[i].lastIndexOf("<br>") + 4, ss[i].length()));
                        classObject.put("week", courseWeek);
                        classObject.put("courseName", ss[i].substring(0,ss[i].indexOf("<br>")));
                        //classObject.put("property", ss[i].substring(ss[i].indexOf("<br>", 1) + 3, ss[i].indexOf("<br>", 2) - 1));
                        //classObject.put("courseSection", ss[i].substring(ss[i].indexOf("第") + 1, ss[i].indexOf("节")));
                        classObject.put("courseSection", courseSection);
                        classObject.put("courseWeek", ss[i].substring(ss[i].indexOf("{") + 1, ss[i].lastIndexOf("}")));
                        Pattern pattern = Pattern.compile("<br>(.*?)<br>");
                        Matcher matcher = pattern.matcher(ss[i]);
                        int j=0;
                        while(matcher.find()){
                            if(j==0){
                                classObject.put("property",matcher.group(1).toString());}
                            if(j==1){
                                classObject.put("teacher",matcher.group(1).toString());}
                            j++;
                        }

                        System.out.println(classObject.toString());
                        courseArray.put(classObject);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    i++;
                }
            }

            mCache.put("courseJson",courseArray.toString());
            StudentUser newUser = new StudentUser();
            newUser.setStuCourse(courseArray.toString());//把课程数据存到用户对象中
            StudentUser user = BmobUser.getCurrentUser(getEduSystemSchedulerActivity.this,StudentUser.class);//获取用户对象
            newUser.update(getEduSystemSchedulerActivity.this,user.getObjectId(),new UpdateListener() {
                @Override
                public void onSuccess() {
                    Log.d("用户数据更新","课程表数据上传成功！");
                }

                @Override
                public void onFailure(int i, String s) {
                    Log.d("用户数据更新","课程表数据上传失败！");
                }
            });
            System.out.println(courseArray.toString());

            handler.post(new Runnable() {
                @Override
                public void run() {
                    loginBtn.setProgress(100);
                    final int activityFinishDelay = 2000;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getEduSystemSchedulerActivity.this.finish();
                        }
                    }, activityFinishDelay);
                }
            });

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

        studentEduNumber = user.getText().toString();
        pass = password.getText().toString();

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
                if (StringUtil.isValue(valueToken, "id=\"xhxm")) {
                    xh= studentEduNumber;
                    //xh = StringUtil.getValue(valueToken, "id=\"xhxm", "<", 10);// value
                    xm = StringUtil.getValue(valueToken, "id=\"xhxm", "同", 6);
                    xm = xm.substring(4,xm.length());

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
        getMenuInflater().inflate(R.menu.menu_get_edu_system_scheduler, menu);
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
            if(myUrl.equals("jwxt.hualixy.com")){
                myUrl="10.10.2.18";
                Toast.makeText(getEduSystemSchedulerActivity.this,"已切换网络为内网！",Toast.LENGTH_SHORT).show();
            }
            else if(myUrl.equals("10.10.2.18")){
                myUrl="jwxt.hualixy.com";
                Toast.makeText(getEduSystemSchedulerActivity.this,"已切换网络为外网！",Toast.LENGTH_SHORT).show();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
