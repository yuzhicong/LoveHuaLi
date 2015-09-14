package com.yzc.lovehuali;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageSwitcher;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.Collator;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
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
    private Spinner spTerm,spType;
    private SpinnerAdapter spTermAdapter,spTypeAdapter;

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
    private String __VIEWSTATE_xskb_gc = "";

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


        sp = getSharedPreferences("mysp",Context.MODE_PRIVATE);

        user = (MaterialEditText) findViewById(R.id.etStudentId);
        password = (MaterialEditText) findViewById(R.id.etEduSystemPassword);
        spTerm = (Spinner) findViewById(R.id.spTerm);
        spType = (Spinner) findViewById(R.id.spType);
        String strType[]={"学生个人课表","专业推荐课表"};
        spTypeAdapter = new ArrayAdapter<String>(getEduSystemSchedulerActivity.this,R.layout.spinner_item,strType);
        spType.setAdapter(spTypeAdapter);

        user.setText(sp.getString("studentId", ""));
        password.setText(sp.getString("eduSystemPsw", ""));

        password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
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

    @Override
    protected void onResume() {
        super.onResume();
        Date date=new Date();
        SimpleDateFormat dateFmYear = new SimpleDateFormat("yyyy");
        SimpleDateFormat dateFmMonth = new SimpleDateFormat("MM");
        int year = Integer.parseInt(dateFmYear.format(date));
        int month = Integer.parseInt(dateFmMonth.format(date));
        System.out.println(year + ":" + month);
        if(month >=7) {
            String TermData[] = new String[]{(year) + "-" + (year+1) +" 第1学期",(year-1) + "-" + (year) +" 第2学期",(year-1) + "-" + (year) +" 第1学期",(year-2) + "-" + (year-1) + " 第2学期", (year-2) + "-" + (year-1) + " 第1学期",
                    (year-3) + "-" + (year-2) + " 第2学期", (year-3) + "-" + (year-2) + " 第1学期", (year-4) + "-" + (year-3) + " 第2学期", (year-4) + "-" + (year-3) + " 第1学期",
                    (year-5) + "-" + (year-4) + " 第2学期",(year-5) + "-" + (year-4) + " 第1学期"};//加强时要注意根据当前时间生成最多5年的数据，或着根据用户入学年数
            spTermAdapter = new ArrayAdapter<String>(getEduSystemSchedulerActivity.this, R.layout.spinner_item, TermData);
        }else if(month >= 1){
            String TermData[] = new String[]{(year-1) + "-" + (year) +" 第2学期",(year-1) + "-" + (year) +" 第1学期",(year-2) + "-" + (year-1) + " 第2学期", (year-2) + "-" + (year-1) + " 第1学期",
                    (year-3) + "-" + (year-2) + " 第2学期", (year-3) + "-" + (year-2) + " 第1学期", (year-4) + "-" + (year-3) + " 第2学期", (year-4) + "-" + (year-3) + " 第1学期",
                    (year-5) + "-" + (year-4) + " 第2学期",(year-5) + "-" + (year-4) + " 第1学期"};//加强时要注意根据当前时间生成最多5年的数据，或着根据用户入学年数
            spTermAdapter = new ArrayAdapter<String>(getEduSystemSchedulerActivity.this, R.layout.spinner_item, TermData);
        }
        spTerm.setAdapter(spTermAdapter);
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
                    if(result!=1) {
                        result = checkUser();// 获取登陆情况
                    }
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
                            if (result == -3) {
                                Toast.makeText(v.getContext(), "教务系统处于瘫痪状态，给点时间它恢复~",
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

    private Thread getCourseJson = new Thread(new Runnable() {
        @Override
        public void run() {

            String gnmkdm = "";

            String __VIEWSTATE = "";
            StringTokenizer tokenizer = null;
            String ddlXN = "";
            String ddlXQ = "";
            String[] ss = null;

            String xn = "";
            String xq = "";
            String nj = "";
            String xy = "";
            String zy = "";
            String kb = "";
            String strClass = "";

            if (spType.getSelectedItem().toString().equals("学生个人课表")) {
                gnmkdm = "N121603";
                try {
                    ksInfo = HttpUtil
                            .getUrl("http://" + myUrl
                                            + "/xskbcx.aspx?xh=" + xh + "&xm=" + xm
                                            + "&gnmkdm=" + gnmkdm,
                                    mHttpClient,
                                    "http://"
                                            + myUrl
                                            + "/xs_main.aspx?xh="
                                            + xh);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else {
                gnmkdm = "N121601";
                try {
                    ksInfo = HttpUtil
                            .getUrl("http://" + myUrl
                                            + "/tjkbcx.aspx?xh=" + xh + "&xm=" + xm
                                            + "&gnmkdm=" + gnmkdm,
                                    mHttpClient,
                                    "http://"
                                            + myUrl
                                            + "/xs_main.aspx?xh="
                                            + xh);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            int i = 0;
            if (__VIEWSTATE_xskb_gc == "") {


                //System.out.println("网页内容" + ksInfo);
                if (ksInfo != "") {
                    Pattern pat = Pattern.compile("<option selected=\"selected\" value=\"(.*?)\">(.*?)</option>");
                    Matcher mat = pat.matcher(ksInfo);
                    int keycount = 0;
                    while (mat.find()) {
                        System.out.println(mat.group(1));
                        switch (keycount) {
                            case 0:
                                xn = mat.group(1);
                                break;
                            case 1:
                                xq = mat.group(1);
                                break;
                            case 2:
                                nj = mat.group(1);
                                break;
                            case 3:
                                xy = mat.group(1);
                                break;
                            case 4:
                                zy = mat.group(1);
                                break;
                            case 5:
                                strClass = mat.group(2);
                                break;
                            default:
                                break;
                        }
                        keycount++;
                    }

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
                                __VIEWSTATE_xskb_gc = __VIEWSTATE;
                            }
                        }
                    }
                }

            }
            if (__VIEWSTATE_xskb_gc != "") {
                __VIEWSTATE = __VIEWSTATE_xskb_gc;

            }

            String kbInfo = "";
            List<BasicNameValuePair> pairs = new ArrayList<BasicNameValuePair>();
            // System.out.println(__VIEWSTATE);
            ddlXN = spTerm.getSelectedItem().toString().substring(0, 9);
            ddlXQ = spTerm.getSelectedItem().toString().substring(11, 12);
            System.out.println(ddlXN + ":" + ddlXQ + "\n" + xn + ":" + xq);

            if (!(xn.equals(ddlXN) & xq.equals(ddlXQ))) {
                if (gnmkdm.equals("N121603")) {
                    if (!xn.equals(ddlXN) & xq.equals(ddlXQ)) {
                        pairs.add(new BasicNameValuePair("__EVENTTARGET", "xnd"));
                    } else if (xn.equals(ddlXN) & !xq.equals(ddlXQ)) {
                        pairs.add(new BasicNameValuePair("__EVENTTARGET", "xqd"));
                    } else if (!xn.equals(ddlXN) & !xq.equals(ddlXQ)) {
                        pairs.add(new BasicNameValuePair("__EVENTTARGET", "xnd"));
                        pairs.add(new BasicNameValuePair("__VIEWSTATE", __VIEWSTATE));
                        pairs.add(new BasicNameValuePair("xnd",
                                ddlXN));
                        pairs.add(new BasicNameValuePair("xqd",
                                ddlXQ));
                        try {
                            kbInfo = HttpUtil.postUrl("http://" + myUrl
                                    + "/xskbcx.aspx?xh=" + xh + "&xm=" + xm
                                    + "&gnmkdm=" + gnmkdm, pairs, mHttpClient, "http://"
                                    + myUrl + "/xs_main.aspx?xh=" + xh);
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        pairs = new ArrayList<>();
                        pairs.add(new BasicNameValuePair("__EVENTTARGET", "xqd"));
                    }
                    pairs.add(new BasicNameValuePair("__VIEWSTATE", __VIEWSTATE));
                    pairs.add(new BasicNameValuePair("xnd",
                            ddlXN));
                    pairs.add(new BasicNameValuePair("xqd",
                            ddlXQ));

                    try {
                        kbInfo = HttpUtil.postUrl("http://" + myUrl
                                + "/xskbcx.aspx?xh=" + xh + "&xm=" + xm
                                + "&gnmkdm=" + gnmkdm, pairs, mHttpClient, "http://"
                                + myUrl + "/xs_main.aspx?xh=" + xh);
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                } else {
                    try {
                        if (!xn.equals(ddlXN) && xq.equals(ddlXQ)) {
                            pairs.add(new BasicNameValuePair("__EVENTTARGET", "xn"));
                        } else if (xn.equals(ddlXN) && !xq.equals(ddlXQ)) {
                            pairs.add(new BasicNameValuePair("__EVENTTARGET", "xq"));
                        } else if (!xn.equals(ddlXN) & !xq.equals(ddlXQ)) {
                            pairs.add(new BasicNameValuePair("__EVENTTARGET", "xn"));
                            pairs.add(new BasicNameValuePair("__VIEWSTATE", __VIEWSTATE));
                            pairs.add(new BasicNameValuePair("xn", ddlXN));
                            pairs.add(new BasicNameValuePair("xq", ddlXQ));
                            pairs.add(new BasicNameValuePair("nj", nj));
                            pairs.add(new BasicNameValuePair("xy", xy));
                            pairs.add(new BasicNameValuePair("zy", zy));

                            kbInfo = HttpUtil.postUrl("http://" + myUrl
                                    + "/tjkbcx.aspx?xh=" + xh + "&xm=" + xm
                                    + "&gnmkdm=" + gnmkdm, pairs, mHttpClient, "http://"
                                    + myUrl + "/xs_main.aspx?xh=" + xh);
                            pairs = new ArrayList<>();
                            pairs.add(new BasicNameValuePair("__EVENTTARGET", "xq"));

                        }
                        pairs.add(new BasicNameValuePair("__VIEWSTATE", __VIEWSTATE));
                        pairs.add(new BasicNameValuePair("xn", ddlXN));
                        pairs.add(new BasicNameValuePair("xq", ddlXQ));
                        pairs.add(new BasicNameValuePair("nj", nj));
                        pairs.add(new BasicNameValuePair("xy", xy));
                        pairs.add(new BasicNameValuePair("zy", zy));

                        kbInfo = HttpUtil.postUrl("http://" + myUrl
                                + "/tjkbcx.aspx?xh=" + xh + "&xm=" + xm
                                + "&gnmkdm=" + gnmkdm, pairs, mHttpClient, "http://"
                                + myUrl + "/xs_main.aspx?xh=" + xh);

                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

               //System.out.println("获取的网页内容：" + kbInfo);
            } else {
                kbInfo = ksInfo;
                //System.out.println("重新输出第一次获取的网页内容：" + kbInfo);
            }
            /*String kzInfo=new String();
            if(gnmkdm.equals("N121601")){
            kzInfo = kbInfo.substring(kbInfo.indexOf("<td colspan=\"2\" rowspan=\"1\" width=\"2%\">"), kbInfo.indexOf("</table>"));
            }*/
            System.out.println(kbInfo + (kbInfo.contains("<option selected=\"selected\" value=\""+ ddlXN +"\">")&kbInfo.contains("<option selected=\"selected\" value=\""+ ddlXQ +"\">")) + "length:" + kbInfo.length());
            if(kbInfo.contains("<option selected=\"selected\" value=\""+ ddlXN +"\">")&kbInfo.contains("<option selected=\"selected\" value=\""+ ddlXQ +"\">")&kbInfo.length()>10800) {
                kbInfo = kbInfo.substring(kbInfo.indexOf("<td colspan=\"2\" rowspan=\"1\" width=\"2%\">"), kbInfo.indexOf("</table>"));
                String temp = kbInfo.replaceAll("</td>", "</td>\n");// 转化换行
                Pattern p = Pattern.compile("(?<=>).*(?=</td>)");
                Matcher m = p.matcher(temp);
                ss = null;
                ss = new String[500];
                JSONArray courseArray = new JSONArray();//课程JsonArray
                ArrayList<JSONObject> courseArrayList = new ArrayList<JSONObject>();
                i = 0;
                int w = 0, fw = 0;//w为计算表格位置，fw为辅助计算参数
                while (m.find() && (!m.group().toString().equals("编号")) && (!m.group().toString().equals("课程名称"))) {
                    if (m.group().toString().equals("&nbsp;")) {
                        w++;
                        if (w % 7 == 0) {
                            w = w + fw;
                            fw = 0;
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
                        try {
                            ss[i] = new String(m.group().toString().getBytes(),"UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        /*System.out.println(ss[i]);
                        System.out.println(ss[i].substring(ss[i].lastIndexOf("<br>") + 4, ss[i].length()));
                        System.out.println(ss[i].substring(0, ss[i].indexOf("<br>")));
                        System.out.println(ss[i].substring(ss[i].indexOf("第") + 1, ss[i].indexOf("节")));
                        System.out.println(ss[i].substring(ss[i].indexOf("{") + 1, ss[i].lastIndexOf("}")));
                        System.out.println(ss[i].substring(ss[i].indexOf("周") + 1, ss[i].indexOf("周") + 2));*/
                        //String week = ss[i].substring(ss[i].indexOf("周") + 1, ss[i].indexOf("周") + 2);
                        w++;
                        fw += 1;
                        int courseWeek = 0;
                        courseWeek = w % 7;
                        String courseSection = "";
                        switch (w / 7) {
                            case 0:
                            case 1:
                                courseSection = "1,2";
                                break;
                            case 2:
                            case 3:
                                courseSection = "3,4";
                                break;
                            case 4:
                            case 5:
                                courseSection = "5,6";
                                break;
                            case 6:
                            case 7:
                                courseSection = "7,8";
                                break;
                            case 8:
                            case 9:
                                courseSection = "9,10";
                                break;
                        }
                        /*if (week.equals("一")) {
                            courseWeek = 1;
                        }
                        if (week.equals("二")) {
                            courseWeek = 2;
                        }
                        if (week.equals("三")) {
                            courseWeek = 3;
                        }
                        if (week.equals("四")) {
                            courseWeek = 4;
                        }
                        if (week.equals("五")) {
                            courseWeek = 5;
                        }
                        if (week.equals("六")) {
                            courseWeek = 6;
                        }
                        if (week.equals("日")) {
                            courseWeek = 7;
                        }*/


                        try {

                            /*if(ss[i].contains("red")){
                                String sstemp1 = ss[i].substring(0,ss[i].indexOf("<font"));
                                String sstemp2 = ss[i].substring(ss[i].indexOf("font>")+5,ss[i].length());
                                ss[i] = sstemp1.toString()+sstemp2.toString();
                            }*/
                            int oldi = i;
                            if(ss[i].contains("<br><br>")){
                                if(gnmkdm.equals("N121603")) {
                                    String[] group = ss[i].split("(<br><br>)");
                                    for(int l=0;l<group.length;l++){
                                        System.out.println(group[l]);
                                        if(!(group[l].contains("调")||group[l].contains("停")||group[l].contains("换"))){
                                            ss[i] = group[l];
                                            i++;
                                        }
                                    }
                                }else{
                                    String[] group = ss[i].split("(<br><br><br>)");
                                    for(int l=0;l<group.length;l++){
                                        System.out.println(group[l]);
                                        if(!(group[l].contains("调")||group[l].contains("停")||group[l].contains("换"))){
                                            ss[i] = group[l];
                                            i++;
                                        }
                                    }
                                }
                                }
                            if(oldi<i){i--;}
                            for(int g=oldi;g<=i;g++) {
                                JSONObject classObject = new JSONObject();
                                if (gnmkdm.equals("N121603")) {
                                    classObject.put("classRoom", ss[g].substring(ss[g].lastIndexOf("<br>") + 4, ss[g].length()));
                                } else {
                                    if((ss[g].length()-ss[g].lastIndexOf("<br>"))<4){
                                        classObject.put("classRoom", ss[g].substring(ss[g].lastIndexOf("<br>") + 4, ss[g].length()));
                                    }else{
                                    String tempClassRoom = ss[g].substring(0, ss[g].lastIndexOf("<br>"));
                                    tempClassRoom = tempClassRoom.substring(tempClassRoom.lastIndexOf("<br>") + 4, tempClassRoom.length());
                                    classObject.put("classRoom", tempClassRoom);}

                                }
                                classObject.put("week", courseWeek);
                                classObject.put("courseName", ss[g].substring(0, ss[g].indexOf("<br>")));
                                //classObject.put("property", ss[i].substring(ss[i].indexOf("<br>", 1) + 3, ss[i].indexOf("<br>", 2) - 1));
                                //classObject.put("courseSection", ss[i].substring(ss[i].indexOf("第") + 1, ss[i].indexOf("节")));
                                classObject.put("courseSection", courseSection);
                                Pattern pattern = Pattern.compile("<br>(.*?)<br>");
                                Matcher matcher = pattern.matcher(ss[g]);
                                int j = 0;
                                while (matcher.find()) {
                                    if (j == 0) {
                                        classObject.put("property", matcher.group(1).toString());
                                    }
                                    if (j == 1) {
                                        classObject.put("teacher", matcher.group(1).toString());
                                    }
                                    j++;
                                }
                                if (gnmkdm.equals("N121603")) {//根据不同的课表类型，由于格式不同，上课的第几周到第几周的解析不同
                                    String tempCourseWeek = ss[g].substring(ss[g].indexOf("{") + 1, ss[g].lastIndexOf("}"));
                                    tempCourseWeek = tempCourseWeek.substring(tempCourseWeek.indexOf("第")+1,tempCourseWeek.indexOf("周"));
                                    String[] weekAB = tempCourseWeek.split("-");
                                    if(weekAB[0].equals(weekAB[1])){
                                        tempCourseWeek = tempCourseWeek.substring(0,tempCourseWeek.indexOf("-"));
                                    }
                                    classObject.put("courseWeek", tempCourseWeek);
                                } else {
                                    String tempCourseWeek = ss[g].substring(0, ss[g].indexOf("("));
                                    tempCourseWeek = tempCourseWeek.substring(tempCourseWeek.lastIndexOf(">") + 1, tempCourseWeek.length());
                                    String[] weekAB = tempCourseWeek.split("-");
                                    if(weekAB[0].equals(weekAB[1])){
                                        tempCourseWeek = tempCourseWeek.substring(0,tempCourseWeek.indexOf("-"));
                                    }
                                    classObject.put("courseWeek", tempCourseWeek);
                                }

                                System.out.println(classObject.toString());
                                courseArrayList.add(classObject);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        i++;
                    }
                }
                int size=courseArrayList.size();
                for(int q=0;q<size-1;q++){
                    try {
                        JSONObject joNow = courseArrayList.get(q);
                        ArrayList<String> hbcourseWeek = new ArrayList<String>();
                        hbcourseWeek.add(joNow.getString("courseWeek"));
                        int hb =q+1;
                        if(hb>=courseArrayList.size()){
                            size = courseArrayList.size();
                            break;}
                        JSONObject joNext = courseArrayList.get(hb);
                        while(joNext.getInt("week")==joNow.getInt("week")&&joNext.getString("courseSection").equals(joNow.getString("courseSection"))){
                            //Log.d("hbCourseData","出现一样格子课程");
                            if(joNext.getString("courseName").equals(joNow.getString("courseName"))&&joNext.getString("classRoom").equals(joNow.getString("classRoom")))
                            {
                                hbcourseWeek.add(joNext.getString("courseWeek"));
                                courseArrayList.remove(hb);
                                joNext = courseArrayList.get(hb);

                            }else {
                                if (hb == courseArrayList.size() - 1) {
                                    break;
                                }
                                joNext = courseArrayList.get(++hb);
                            }
                        }

                        //Collections.sort(hbcourseWeek,Collator.getInstance(Locale.ENGLISH));
                        String tempCourseWeek = hbcourseWeek.get(0);
                        for(int q1=1;q1<hbcourseWeek.size();q1++){
                            tempCourseWeek = tempCourseWeek + "," + hbcourseWeek.get(q1);
                        }
                        courseArrayList.get(q).remove("courseWeek");
                        courseArrayList.get(q).put("courseWeek",tempCourseWeek);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    size = courseArrayList.size();
                }
                for(int q2=0;q2<courseArrayList.size();q2++){
                    courseArray.put(courseArrayList.get(q2));
                }

                System.out.print("获取到的课程数据:" + courseArray.toString());
                /*StudentUser newUser = new StudentUser();
                newUser.setStuCourse(courseArray.toString());//把课程数据存到用户对象中
                StudentUser user = BmobUser.getCurrentUser(getEduSystemSchedulerActivity.this, StudentUser.class);//获取用户对象
                newUser.update(getEduSystemSchedulerActivity.this, user.getObjectId(), new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        Log.d("用户数据更新", "课程表数据上传成功！");
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        Log.d("用户数据更新", "课程表数据上传失败！");
                    }
                });用户课程表数据上传到服务器的功能*/
                System.out.println(courseArray.toString());
                mCache.put("courseJson", courseArray.toString());
                SharedPreferences sp = getSharedPreferences("mysp", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("courseJson",courseArray.toString());
                editor.commit();

                handler.post(new Runnable() {
                        @Override
                        public void run() {
                        loginBtn.setProgress(100);
                            Snackbar.make(loginBtn,"日后选课或教务系统调课，均会导致课表不正确，欢迎再次导入哦！", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        final int activityFinishDelay = 3000;
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                getEduSystemSchedulerActivity.this.finish();
                            }
                        }, activityFinishDelay);
                    }
                });
            }else{
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        loginBtn.setProgress(-1);
                        //Toast.makeText(getApplication(),"此学期该类型课表暂无！",Toast.LENGTH_SHORT).show();
                        Snackbar.make(loginBtn,"此学期本类型课表暂无，请尝试更换课表类型", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        final int activityFinishDelay = 3000;
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                getEduSystemSchedulerActivity.this.finish();
                            }
                        }, activityFinishDelay);
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
                if(StringUtil.isValue(valueToken,"The page you are looking for is temporarily unavailable.")){
                    return -3;
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
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences.Editor edit = sp.edit();
        edit.putString("studentId", user.getText().toString());
        edit.putString("eduSystemPsw", password.getText().toString());
        edit.commit();
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
