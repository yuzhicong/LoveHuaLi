package com.yzc.lovehuali;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.PopupWindowCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.yzc.lovehuali.adapter.MainViewPagerFragmentAdapter;
import com.yzc.lovehuali.adapter.UserToolListViewAdapter;
import com.yzc.lovehuali.bmob.StudentUser;
import com.yzc.lovehuali.tool.ACache;
import com.yzc.lovehuali.tool.SystemBarTintManager;
import com.yzc.lovehuali.widget.ChangeColorIconWithText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import cn.bmob.push.BmobPush;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobUser;


public class MainActivity extends ActionBarActivity {

    private Toolbar mToolbar;
    private ViewPager mainViewPager;
    private MainViewPagerFragmentAdapter viewPagerAdapter;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private List<ChangeColorIconWithText> mTabIndicators = new ArrayList<ChangeColorIconWithText>();
    private ChangeColorIconWithText one,two,three,four;
    private TextView tvUserName,tvUserEmail;
    private ImageView ivUserImage;
    private long mExitTime;//存储返回按钮按下时间
    private ListView lvUserTool;//用户个人功能菜单列表
    private int PagerNumber=0;//记录当前页面数
    private SharedPreferences sp;
    private int localWeek;//记录当前的周数

    private WeekChosePopwindow wpw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //设定状态栏的颜色，当版本大于4.4时起作用
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            //此处可以重新指定状态栏颜色
            tintManager.setStatusBarTintResource(R.color.material_blue);
        }


        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("Love华立");// 标题的文字需在setSupportActionBar之前，不然会无效
        mToolbar.setTitleTextColor(Color.WHITE);

        Calendar calendar = Calendar.getInstance();
        int weekofyear = calendar.get(Calendar.WEEK_OF_YEAR);
        sp = getSharedPreferences("mysp", Context.MODE_PRIVATE);
        localWeek = weekofyear - sp.getInt("betweenWeek", weekofyear - 1);
        if(localWeek<=0){
            localWeek+=52;
        }
        SharedPreferences.Editor edit = sp.edit();
        edit.putInt("localWeek",localWeek);
        edit.commit();
        wpw = new WeekChosePopwindow();
        wpw.initPopWindow();
        mToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(MainActivity.this,"你要改周数么？",Toast.LENGTH_LONG).show();
                if (PagerNumber == 0) {
                    wpw.showPop(mToolbar, 160, 0, 0);

                }
            }
        });
        setSupportActionBar(mToolbar);

        // 初始化BmobSDK
        Bmob.initialize(this, "ce44de9648c859db8001d4187e9d38b9");
        // 使用推送服务时的初始化操作
        BmobInstallation.getCurrentInstallation(this).save();
        // 启动推送服务
        BmobPush.startWork(this, "ce44de9648c859db8001d4187e9d38b9");

        //滑动切换功能区域
        mainViewPager = (ViewPager) findViewById(R.id.mainViewPager);
        viewPagerAdapter = new MainViewPagerFragmentAdapter(getSupportFragmentManager());
        myPagerChangeListener pagerChangeListener = new myPagerChangeListener();
        mainViewPager.setOnPageChangeListener(pagerChangeListener);
        mainViewPager.setOffscreenPageLimit(4);//设置保存的碎片限度，没有超过不会销毁碎片
        mainViewPager.setAdapter(viewPagerAdapter);
        ChangeActionBarTitle(mainViewPager.getCurrentItem());//刷新正确的ActionBar标题

        OnTabClickListener tabClickListener = new OnTabClickListener();//View控件的按下监听器
        //绑定导航控件
        one = (ChangeColorIconWithText) findViewById(R.id.indicator_one);
        mTabIndicators.add(one);
        two = (ChangeColorIconWithText) findViewById(R.id.indicator_two);
        mTabIndicators.add(two);
        three = (ChangeColorIconWithText) findViewById(R.id.indicator_three);
        mTabIndicators.add(three);
        four = (ChangeColorIconWithText) findViewById(R.id.indicator_four);
        mTabIndicators.add(four);
        one.setOnClickListener(tabClickListener);
        two.setOnClickListener(tabClickListener);
        three.setOnClickListener(tabClickListener);
        four.setOnClickListener(tabClickListener);

        ChangeSelectedTab(mainViewPager.getCurrentItem());


        //抽屉箭头效果按钮实现
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar,  R.string.drawer_open, R.string.drawer_close){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("我");
                mToolbar.setSubtitle("");
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                ChangeActionBarTitle(mainViewPager.getCurrentItem());//刷新正确的ActionBar标题
                ChangeSelectedTab(mainViewPager.getCurrentItem());
            }
        };
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        //设置侧滑菜单里面的用户信息
        tvUserName = (TextView) findViewById(R.id.tvUserName);
        tvUserEmail = (TextView) findViewById(R.id.tvUserEmail);
        ivUserImage = (ImageView) findViewById(R.id.ivUserImage);
        ivUserImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent();
                if(BmobUser.getCurrentUser(MainActivity.this,StudentUser.class)!=null){
                    intent.setClass(MainActivity.this,UserInformationActivity.class);
                    startActivity(intent);
                }else{
                    intent.setClass(MainActivity.this,LoginActivity.class);
                    startActivity(intent);
                }
            }
        });

        ImageView ivUserBackground = (ImageView) findViewById(R.id.ivUserBackground);
        ivUserBackground.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;//用户背景图片被触摸就截取触摸事件，防止点击导致背景下的控件触发
            }
        });


        //侧滑菜单里面用户个人功能列表
        lvUserTool = (ListView) findViewById(R.id.lvUserTool);
        lvUserTool.setAdapter(new UserToolListViewAdapter(MainActivity.this,R.layout.listview_user_tool_cell));
        lvUserTool.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i = new Intent();
                mDrawerLayout.closeDrawers();//关闭抽屉视图
                switch (position){
                    case 0:
                        if(BmobUser.getCurrentUser(MainActivity.this,StudentUser.class)!=null) {
                            i.setClass(MainActivity.this, UserInformationActivity.class);
                            startActivity(i);
                        }else{
                            i.setClass(MainActivity.this, LoginActivity.class);
                            startActivity(i);
                        }
                        break;
                    case 1:
                        i.setClass(MainActivity.this, QueryStudentScoreActivity.class);
                        startActivity(i);
                        break;
                    case 2:
                        i.setClass(MainActivity.this,SoftwareNoticeActivity.class);
                        startActivity(i);
                        break;
                    case 3:
                        i.setClass(MainActivity.this,AboutUsActivity.class);
                        startActivity(i);
                        break;
                    case 4:
                        i.setClass(MainActivity.this,SuggestUsActivity.class);
                        startActivity(i);
                        break;
                    case 5:

                        break;
                    case 6:
                        MainActivity.this.finish();
                        break;
                }
            }
        });//侧滑菜单功能响应项



    }

    @Override
    protected void onResume() {
        super.onResume();
        StudentUser user = BmobUser.getCurrentUser(MainActivity.this,StudentUser.class);
        if(user!=null) {
            tvUserName.setText(user.getUsername());
            tvUserEmail.setText(user.getEmail());
        }else{
            tvUserName.setText("点击头像登录");
            tvUserEmail.setText("登录后体验更多功能");
        }
    }


    class myPagerChangeListener implements ViewPager.OnPageChangeListener{

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            if(positionOffset>0){
                ChangeColorIconWithText left = mTabIndicators.get(position);
                ChangeColorIconWithText right = mTabIndicators.get(position+1);
                left.setIconAlpha(1-positionOffset);
                right.setIconAlpha(positionOffset);
            }

        }

        @Override
        public void onPageSelected(int position) {
            PagerNumber = position;
            ChangeActionBarTitle(position);//更新标题
            invalidateOptionsMenu();//更新菜单项
            ChangeSelectedTab(position);//更新导航选项
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    //根据当前页面数，设置ActionBar标题
    public void ChangeActionBarTitle(int position){
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        switch (position){
            case 0:
                Date date=new Date();
                SimpleDateFormat dateFm = new SimpleDateFormat("EEEE");
                //actionBar.setTitle(dateFm.format(date));
                //mToolbar.setSubtitle("第17周");
                mToolbar.setSubtitle(dateFm.format(date));
                if(sp.getInt("localWeek",1)!=localWeek){
                    actionBar.setTitle("第" + localWeek + "周(非本周) ▼");
                }else{
                    actionBar.setTitle("第" + localWeek + "周 ▼");}
                break;
            case 1:
                actionBar.setTitle("资讯");
                mToolbar.setSubtitle("");
                break;
            case 2:
                actionBar.setTitle("活动圈");
                mToolbar.setSubtitle("");
                break;
            case 3:
                actionBar.setTitle("工具");
                mToolbar.setSubtitle("");
                break;
            default:
                actionBar.setTitle("Love华立");
                mToolbar.setSubtitle("");
                break;
        }
    }


    //设置导航的选择状态
    public void ChangeSelectedTab(int position){
        //重置已选择的TabIndicator的颜色
        for(int i = 0;i< mTabIndicators.size();i++){
            mTabIndicators.get(i).setIconAlpha(0);
        }
        mTabIndicators.get(position).setIconAlpha(1);

    }

    //导航菜单按钮View的监听器
    public class OnTabClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.indicator_one:
                    mainViewPager.setCurrentItem(0,false);
                    break;
                case R.id.indicator_two:
                    mainViewPager.setCurrentItem(1,false);
                    break;
                case R.id.indicator_three:
                    mainViewPager.setCurrentItem(2,false);
                    break;
                case R.id.indicator_four:
                    mainViewPager.setCurrentItem(3,false);
                    break;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override//当菜单项被按下时调用，以便动态改变菜单项
    public boolean onPrepareOptionsMenu(Menu menu) {

        menu.clear();
        MenuInflater inflater = this.getMenuInflater();
        switch (PagerNumber) {
            case 0:
                inflater.inflate(R.menu.menu_schedule, menu);
                break;

            case 1:
                inflater.inflate(R.menu.menu_information, menu);
                break;
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_get_schedule) {
            Intent i= new Intent();
            i.setClass(MainActivity.this,getEduSystemSchedulerActivity.class);
            startActivity(i);
            return true;
        }
        if (id == R.id.action_school_notice) {
            Intent i= new Intent();
            i.setClass(MainActivity.this,SchoolNoticeActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);// 注意
            intent.addCategory(Intent.CATEGORY_HOME);
            this.startActivity(intent);

            /*if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Toast.makeText(this, "再按一次退出程序",Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();

            } else {
                finish();
            }*/
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    private class WeekChosePopwindow extends PopupWindow{
        private PopupWindow popupWindow;
        private ListView lvWeek;
        private Button btnSetLocalWeek;
        /**
         * 初始化popWindow
         * */
        private void initPopWindow() {
            View popView = View.inflate(MainActivity.this,R.layout.popupwindow_week_choose,null);
            popupWindow = new PopupWindow(popView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            popupWindow.setBackgroundDrawable(new ColorDrawable(Color.argb(255,250,250,250)));
            //设置popwindow出现和消失动画
            //popupWindow.setAnimationStyle(android.R.anim.fade_in);
            lvWeek = (ListView) popView.findViewById(R.id.lvWeek);
            btnSetLocalWeek = (Button) popView.findViewById(R.id.btnSetLocalWeek);

            ArrayList weekList = new ArrayList();
            for(int i=1;i<26;i++){
                HashMap weekitem = new HashMap();
                if(i==sp.getInt("localWeek",1)){
                weekitem.put("weekItem","第" + i + "周(本周)");
                }else{
                    weekitem.put("weekItem","第" + i + "周");
                }
                weekList.add(weekitem);
            }
            lvWeek.setAdapter(new SimpleAdapter(MainActivity.this,weekList,R.layout.popupwindow_weeklist_item,new String[]{"weekItem"},new int[]{R.id.tvWeekName}));
            lvWeek.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    localWeek = position + 1;
                    popupWindow.dismiss();

                    Intent i = new Intent();
                    i.setAction("MainActivity.ScheduleChange");
                    i.putExtra("weekChose",localWeek);
                    sendBroadcast(i);
                    System.out.println("课程更变广播已发送！");

                    if(sp.getInt("localWeek",1)!=localWeek){
                        getSupportActionBar().setTitle("第" + localWeek + "周(非本周) ▼");
                    }else{
                    getSupportActionBar().setTitle("第" + localWeek + "周 ▼");}
                }
            });
            lvWeek.setSelection(localWeek);
            btnSetLocalWeek.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Calendar calendar = Calendar.getInstance();
                    int weekofyear = calendar.get(Calendar.WEEK_OF_YEAR);
                    SharedPreferences.Editor edit = sp.edit();
                    edit.putInt("localWeek",localWeek);
                    edit.putInt("betweenWeek",Math.abs(weekofyear-localWeek));
                    edit.commit();
                    popupWindow.dismiss();
                    getSupportActionBar().setTitle("第" + localWeek + "周 ▼");
                    initPopWindow();
                }
            });
        }
        /**
         * 显示popWindow
         * */
        public void showPop(View parent, int x, int y,int postion) {
            //设置popwindow显示位置
            //popupWindow.showAtLocation(parent, 0, x, y);
            popupWindow.showAsDropDown(parent,x,y);
            //获取popwindow焦点
            popupWindow.setFocusable(true);
            //设置popwindow如果点击外面区域，便关闭。
            popupWindow.setOutsideTouchable(true);
            popupWindow.update();
            if (popupWindow.isShowing()) {
            }

        }
    }
}