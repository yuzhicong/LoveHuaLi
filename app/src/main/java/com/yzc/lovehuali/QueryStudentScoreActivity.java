package com.yzc.lovehuali;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Gallery;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.dd.CircularProgressButton;
import com.rengwuxian.materialedittext.MaterialEditText;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class QueryStudentScoreActivity extends ActionBarActivity {

    private Toolbar mToolbar;
    private Spinner spSelectTerm;
    private SpinnerAdapter spTermAdapter;
    //验证码相关声明
    private ImageSwitcher idCodeImage;
    private Bitmap idCodeBitmap;
    private String idCodeImageUrl,key;
    final String getIdCodeUrl = new String("http://61.160.137.195:18001/zdinter/getInter.action?interCode=HLXY0001&para=&callback=HLXY0001");//获取验证码和密钥的网址，要考虑验证码获取失败的情况
    private MaterialEditText etStudentId,etEduSystemPassword;
    private CircularProgressButton cpbtnQueryStudentScore;
    private String massage;//存储登录信息

    private String scoreJsonArrayString;

    private int isLogin=0;//记录登陆状态


    //复制部分
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

    private String ksInfo = "";

    private String __VIEWSTATE_xscj_gc = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_student_score);

        //设定状态栏的颜色，当版本大于4.4时起作用
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            //此处可以重新指定状态栏颜色
            tintManager.setStatusBarTintResource(R.color.material_blue);
        }

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("成绩查询");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sp = getSharedPreferences("mysp", Context.MODE_PRIVATE);

        spSelectTerm = (Spinner)findViewById(R.id.spTerm);

        //用户信息控件绑定
        etStudentId = (MaterialEditText) findViewById(R.id.etStudentId);
        etEduSystemPassword = (MaterialEditText) findViewById(R.id.etEduSystemPassword);


        etStudentId.setText(sp.getString("studentId",""));
        etEduSystemPassword.setText(sp.getString("eduSystemPsw",""));

        etEduSystemPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                 /*判断是否是“Search”键*/
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
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

        cpbtnQueryStudentScore = (CircularProgressButton)findViewById(R.id.cpbtnQueryStudentScore);
        cpbtnQueryStudentScore.setIndeterminateProgressMode(true);
        cpbtnQueryStudentScore.setOnClickListener(loginClick);

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

    @Override
    protected void onResume() {
        super.onResume();
        Date date=new Date();
        SimpleDateFormat dateFmYear = new SimpleDateFormat("yyyy");
        SimpleDateFormat dateFmMonth = new SimpleDateFormat("MM");
        int year = Integer.parseInt(dateFmYear.format(date));
        int month = Integer.parseInt(dateFmMonth.format(date));
        System.out.println(year + ":" + month);
        if(month > 0 && month < 7) {
            String TermData[] = new String[]{(year-1) + "-" + (year) +" 第1学期",(year-2) + "-" + (year-1) + " 第2学期", (year-2) + "-" + (year-1) + " 第1学期",
                    (year-3) + "-" + (year-2) + " 第2学期", (year-3) + "-" + (year-2) + " 第1学期", (year-4) + "-" + (year-3) + " 第2学期", (year-4) + "-" + (year-3) + " 第1学期",
                    (year-5) + "-" + (year-4) + " 第2学期",(year-5) + "-" + (year-4) + " 第1学期"};//加强时要注意根据当前时间生成最多5年的数据，或着根据用户入学年数
            spTermAdapter = new ArrayAdapter<String>(QueryStudentScoreActivity.this, R.layout.spinner_item, TermData);
        }else if(month > 6){
            String TermData[] = new String[]{(year-1) + "-" + (year) +" 第2学期",(year-1) + "-" + (year) +" 第1学期",(year-2) + "-" + (year-1) + " 第2学期", (year-2) + "-" + (year-1) + " 第1学期",
                    (year-3) + "-" + (year-2) + " 第2学期", (year-3) + "-" + (year-2) + " 第1学期", (year-4) + "-" + (year-3) + " 第2学期", (year-4) + "-" + (year-3) + " 第1学期",
                    (year-5) + "-" + (year-4) + " 第2学期",(year-5) + "-" + (year-4) + " 第1学期"};//加强时要注意根据当前时间生成最多5年的数据，或着根据用户入学年数
            spTermAdapter = new ArrayAdapter<String>(QueryStudentScoreActivity.this, R.layout.spinner_item, TermData);
        }
        spSelectTerm.setAdapter(spTermAdapter);
    }

    TimerTask task = new TimerTask() {
        public void run() {
        isExit = false;
        hasTask = true;
        }
    };


View.OnClickListener loginClick = new View.OnClickListener() {

    public void onClick(final View v) {
        cpbtnQueryStudentScore.setProgress(0);
        cpbtnQueryStudentScore.setProgress(50);
        if (isLogin == 1) {
            System.out.println("已经登陆直接获取成绩中");
            new Thread(getScoreJson).start();

        } else {
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
                                isLogin = 1;//设置为已经登陆的状态
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
                                cpbtnQueryStudentScore.setProgress(-1);
                            }
                            if (result == -1) {
                                Toast.makeText(v.getContext(), "请检查网络连接！",
                                        Toast.LENGTH_SHORT).show();
                                cpbtnQueryStudentScore.setProgress(-1);
                            }
                            if (result == -2) {
                                Toast.makeText(v.getContext(), "错误原因：系统正忙！",
                                        Toast.LENGTH_SHORT).show();
                                cpbtnQueryStudentScore.setProgress(-1);
                            }
                            if (result == -3) {
                                Toast.makeText(v.getContext(), "教务系统处于瘫痪状态，给点时间它恢复~",
                                        Toast.LENGTH_SHORT).show();
                                cpbtnQueryStudentScore.setProgress(-1);
                            }

                        }
                    });

                }
            }).start();

        }
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
                                            + "&gnmkdm=N121605",
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

            ss = new String[500];
            i = 0;
            List<BasicNameValuePair> pairs = new ArrayList<BasicNameValuePair>();
            // System.out.println(__VIEWSTATE);
            ddlXN = spSelectTerm.getSelectedItem().toString().substring(0, 9);
            ddlXQ = spSelectTerm.getSelectedItem().toString().substring(11, 12);


            pairs.add(new BasicNameValuePair(
                    "__VIEWSTATE", __VIEWSTATE));
            pairs.add(new BasicNameValuePair("ddlXN",
                    ddlXN));
            pairs.add(new BasicNameValuePair("ddlXQ",
                    ddlXQ));
            pairs.add(new BasicNameValuePair("Button1",
                    "%B0%B4%D1%A7%C6%DA%B2%E9%D1%AF"));

            System.out.println(ksInfo);
            String info = "";
            try {
                info = HttpUtil.postUrl("http://"
                                + myUrl + "/xscj_gc.aspx?xh="
                                + xh + "&xm=" + xm
                                + "&gnmkdm=N121605", pairs,
                        mHttpClient, "http://" + myUrl
                                + "/xscj_gc.aspx?xh="
                                + xh + "&xm=" + xm
                                + "&gnmkdm=N121605");
                if(info==null){
                    System.out.println("第二次获取成绩：");
                    info = HttpUtil.postUrl("http://"
                                    + myUrl + "/xscj_gc.aspx?xh="
                                    + xh
                                    + "&gnmkdm=N121605", pairs,
                            mHttpClient, "http://" + myUrl
                                    + "/xscj_gc.aspx?xh="
                                    + xh
                                    + "&gnmkdm=N121605");
                }
                if(info==null){
                    System.out.println("第三次获取成绩：");
                    info = HttpUtil.postUrl("http://"
                                    + myUrl + "/xscj_gc.aspx?"
                                    + "&xm=" + xm
                                    + "&gnmkdm=N121605", pairs,
                            mHttpClient, "http://" + myUrl
                                    + "/xscj_gc.aspx?"
                                    + "&xm=" + xm
                                    + "&gnmkdm=N121605");
                }

            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


            //有些用户返回的成绩info为null可能是用户名字或是学号参数出错。

            if (info != null && info != "") {
                //String temp = info.replaceAll("</td>","</td>\n");// 转化换行
                //Pattern p = Pattern.compile("(?<=<td>).*(?=</td>)");
                Pattern p = Pattern.compile("(<td>).*(</td><td>&nbsp;</td><td>0</td><td></td>)");
                Matcher m = p.matcher(info);

                while (m.find()
                        && (m.group().toString().substring(4, 5)
                        .equals("2"))) {

                    ss[i] = m.group().toString();
                    //System.out.println(ss[i]);
                    i++;
                }
                JSONArray ScoreArray = new JSONArray();
                String scoreKey[] = new String[]{"year", "term", "classCode", "className", "property", "classBelong", "credit", "gpa", "finalScore", "tag1", "tag2", "tag3", "department"};
                for (int j = 0; j < i; j++) {

                    Pattern pattern = Pattern.compile("<td>(.*?)</td>");
                    Matcher matcher = pattern.matcher(ss[j]);
                    int k = 0;
                    JSONObject classInfo = new JSONObject();
                    while (matcher.find()) {
                        try {
                            String s = matcher.group().toString();
                            classInfo.put(scoreKey[k], s.substring(4, s.length() - 5));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        k++;
                        if (k == 13) {
                            break;
                        }

                    }
                    ScoreArray.put(classInfo);
                }
                System.out.println(ScoreArray.toString());
                scoreJsonArrayString = ScoreArray.toString();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(!scoreJsonArrayString.equals("[]")) {
                            cpbtnQueryStudentScore.setProgress(100);

                            final int activityFinishDelay = 1000;
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent i = new Intent();
                                    i.setClass(QueryStudentScoreActivity.this, StudentScoreReportActivity.class);
                                    i.putExtra("reportTitle", spSelectTerm.getSelectedItem().toString());
                                    i.putExtra("scoreJsonArrayString", scoreJsonArrayString);
                                    startActivity(i);
                                }
                            }, activityFinishDelay);
                        }else{
                            Toast.makeText(getApplicationContext(),"该学期成绩暂无",Toast.LENGTH_SHORT).show();
                            cpbtnQueryStudentScore.setProgress(0);
                        }

                    }
                });

            }else{
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Snackbar.make(cpbtnQueryStudentScore,"学霸的成绩，小i我竟然看不到，天机不可泄露啊！快向反馈客服吧,让他们给我技能点吧！",Snackbar.LENGTH_LONG).show();
                        cpbtnQueryStudentScore.setProgress(0);
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

        studentEduNumber =etStudentId.getText().toString();
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
                if(StringUtil.isValue(valueToken,"The page you are looking for is temporarily unavailable.")){
                    return -3;
                }
                if (StringUtil.isValue(valueToken, "id=\"xhxm")) {
                    xh= studentEduNumber;
                    //xh = StringUtil.getValue(valueToken, "id=\"xhxm", "<", 10);// value
                    xm = StringUtil.getValue(valueToken, "id=\"xhxm", "同", 6);
                    xm = xm.substring(4, xm.length());
                    System.out.println("姓名："+ xm);
                    //xm = StringUtil.getValue(tokenizer.nextToken().toString(), "","同", 0);// 从登陆后页面取得姓名
                }
            }
            return 1;
        } else {
            return -1;
        }

    }


    @Override
    protected void onRestart() {
        cpbtnQueryStudentScore.setProgress(0);
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences.Editor edit = sp.edit();
        edit.putString("studentId",etStudentId.getText().toString());
        edit.putString("eduSystemPsw",etEduSystemPassword.getText().toString());
        edit.commit();
    }

}
