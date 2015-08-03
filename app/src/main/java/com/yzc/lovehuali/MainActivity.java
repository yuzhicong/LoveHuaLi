package com.yzc.lovehuali;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.yzc.lovehuali.adapter.MainViewPagerFragmentAdapter;
import com.yzc.lovehuali.adapter.UserToolListViewAdapter;
import com.yzc.lovehuali.bmob.StudentUser;
import com.yzc.lovehuali.tool.SystemBarTintManager;
import com.yzc.lovehuali.widget.ChangeColorIconWithText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    private long mExitTime;//存储返回按钮按下时间
    private ListView lvUserTool;//用户个人功能菜单列表
    private int PagerNumber=0;//记录当前页面数
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //设定状态栏的颜色，当版本大于4.4时起作用
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            //此处可以重新指定状态栏颜色
            tintManager.setStatusBarTintResource(R.color.colorPrimaryDark);
        }


        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("Love华立");// 标题的文字需在setSupportActionBar之前，不然会无效
        mToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mToolbar);


        // 使用推送服务时的初始化操作
        BmobInstallation.getCurrentInstallation(this).save();
        // 启动推送服务
        BmobPush.startWork(this, "ce44de9648c859db8001d4187e9d38b9");
        final Intent serviceIntent = new Intent(this,MyPushMessageReceiver.class);
        startService(serviceIntent);

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
        StudentUser user = BmobUser.getCurrentUser(MainActivity.this,StudentUser.class);
        tvUserName.setText(user.getUsername());
        tvUserEmail.setText(user.getEmail());

        //侧滑菜单里面用户个人功能列表
        lvUserTool = (ListView) findViewById(R.id.lvUserTool);
        lvUserTool.setAdapter(new UserToolListViewAdapter(MainActivity.this,R.layout.listview_user_tool_cell));
        lvUserTool.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i = new Intent();
                mDrawerLayout.closeDrawers();//关闭抽屉视图
                final int LogOffDelay = 500;
                switch (position){
                    case 0:
                        new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent i = new Intent();
                            i.setClass(MainActivity.this,UserInformationActivity.class);
                            startActivity(i);
                        }
                    }, LogOffDelay);

                        break;
                    case 1:
                        i.setClass(MainActivity.this,QueryStudentScoreActivity.class);
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

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                BmobUser.logOut(MainActivity.this);
                                Intent i = new Intent();
                                i.setClass(MainActivity.this,LoginActivity.class);
                                startActivity(i);
                                finish();
                            }
                        }, LogOffDelay);
                        break;

                }
            }
        });//侧滑菜单功能响应项



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
                actionBar.setTitle("第17周");
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
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();

            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    //防止经理的课程表点击闪退
    public void LayoutOnclick(View view){
        switch (view.getId()){
            default:
                break;
        }
    }
}
