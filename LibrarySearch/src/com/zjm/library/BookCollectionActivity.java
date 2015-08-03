package com.zjm.library;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;


/**
 * Created by Zero on 2015/7/23.
 */
public class BookCollectionActivity extends ActionBarActivity{
    private Toolbar mToolbar;
    private BookCollectionDBHelper helper;
    private Cursor cursor;
    private ListView bookcollectionlist;
    List bookcollistitem = new ArrayList();
    SimpleAdapter bookcollistadapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.booklist);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            //此处可以重新指定状态栏颜色
            tintManager.setStatusBarTintColor(Color.parseColor("#2196F3"));
        }

        mToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("图书列表");
        mToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        bookcollectionlist = (ListView) findViewById(R.id.booklist);
        bookcollistadapter = new SimpleAdapter(BookCollectionActivity.this, bookcollistitem, R.layout.bookitem, new String[]{"bookName","bookMsg"}, new int[]{R.id.bookname,R.id.bookmsg});
        bookcollectionlist.setAdapter(bookcollistadapter);
        helper = new BookCollectionDBHelper(this);
        GetBookFromDB();
        bookcollectionlist.setOnItemClickListener(new BookColitemListener());
        bookcollectionlist.setOnItemLongClickListener(new BookDelListener());
    }

    class BookColitemListener implements OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            // TODO Auto-generated method stub
            int itemid = (int) bookcollectionlist.getItemIdAtPosition(position);
            HashMap bookcolitem = (HashMap) bookcollistitem.get(itemid);
            String url = (String) bookcolitem.get("href");
            Intent bookdetail = new Intent();
            String bookname = (String) bookcolitem.get("bookName");
            String bookmsg = (String) bookcolitem.get("bookMsg");
            bookdetail.putExtra("FromActivity","BookCollectionActivity");
            bookdetail.putExtra("bookname", bookname);
            bookdetail.putExtra("bookmsg", bookmsg);
            bookdetail.putExtra("bookhref", url);
            bookdetail.setClass(BookCollectionActivity.this, BookDetailActivity.class);
            startActivity(bookdetail);
        }
    }

    class BookDelListener implements OnItemLongClickListener{
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view,
                                       int position, long id) {
            // TODO Auto-generated method stub
            int itemid = (int) bookcollectionlist.getItemIdAtPosition(position);
            HashMap bookcolitem = (HashMap) bookcollistitem.get(itemid);
            String bookname = (String) bookcolitem.get("bookName");
            dialog(bookname);
            return true;
        }
    }

    public void dialog(String bookName){
        AlertDialog.Builder builder = new Builder(BookCollectionActivity.this);
        builder.setMessage("确定删除收藏吗？");
        builder.setTitle("提示");
        final String bookname = bookName;
        builder.setNegativeButton("确定", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                helper.Delete(bookname);
                GetBookFromDB();
                Toast.makeText(BookCollectionActivity.this, "删除成功!", Toast.LENGTH_LONG).show();
            }
        });
        builder.setPositiveButton("取消", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                //Toast.makeText(BookCollectionActivity.this, "取消", Toast.LENGTH_LONG).show();
            }
        });
        builder.create().show();
    }

    public void GetBookFromDB(){
        cursor = helper.RetAllCollectBook();
        bookcollistitem.clear();
        if(cursor.moveToFirst()){
            while (!cursor.isAfterLast()){
                String bookname = cursor.getString(cursor.getColumnIndex("BookName"));
                String bookmsg = cursor.getString(cursor.getColumnIndex("BookMsg"));
                String href = cursor.getString(cursor.getColumnIndex("href"));
                HashMap bookcolmap = new HashMap();
                bookcolmap.put("bookName", bookname);
                bookcolmap.put("bookMsg", bookmsg);
                bookcolmap.put("href", href);
                //System.out.println(bookname);
                //System.out.println(bookmsg);
                //System.out.println(href);
                bookcollistitem.add(bookcolmap);
                cursor.moveToNext();
            }
        }
        bookcollistadapter.notifyDataSetChanged();
    }
}
