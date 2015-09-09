package com.yzc.lovehuali;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import com.yzc.lovehuali.fragment.AcademyNewsFragment;
import com.yzc.lovehuali.fragment.AssociationNewsFragment;
import com.yzc.lovehuali.tool.SystemBarTintManager;


public class FragmentContainerActivity extends ActionBarActivity{

	private Toolbar mToolbar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragmentcontainer);

		//设定状态栏的颜色，当版本大于4.4时起作用
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			SystemBarTintManager tintManager = new SystemBarTintManager(this);
			tintManager.setStatusBarTintEnabled(true);
			//此处可以重新指定状态栏颜色
			tintManager.setStatusBarTintResource(R.color.colorPrimaryDark);
		}
		Intent i = getIntent();
		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		mToolbar.setTitle(i.getStringExtra("barTitle"));// 标题的文字需在setSupportActionBar之前，不然会无效
		mToolbar.setTitleTextColor(Color.WHITE);
		setSupportActionBar(mToolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		if(findViewById(R.id.fragmentcontainer) != null)
		{
			if (savedInstanceState != null)
			{
				return;
			}
			switch (i.getStringExtra("barTitle")){
				case "社团新闻":
					AssociationNewsFragment assnews = new AssociationNewsFragment();
					assnews.setArguments(getIntent().getExtras());
					getSupportFragmentManager().beginTransaction()
							.add(R.id.fragmentcontainer, assnews).commit();
					break;
				case "学院新闻":
					AcademyNewsFragment acanews = new AcademyNewsFragment();
					acanews.setArguments(getIntent().getExtras());
					getSupportFragmentManager().beginTransaction()
							.add(R.id.fragmentcontainer, acanews).commit();
					break;
			}

		}
	}

}
