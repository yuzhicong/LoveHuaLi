package com.yzc.lovehuali;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.yzc.lovehuali.database.DbSoftwareNotice;
import com.yzc.lovehuali.tool.ACache;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import cn.bmob.push.PushConstants;

public class MyPushMessageReceiver extends BroadcastReceiver {

    private NotificationManager manager;
    private JSONObject data;
    ACache mcache;
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        if(intent.getAction().equals(PushConstants.ACTION_MESSAGE)){
            String pushString = intent.getStringExtra(PushConstants.EXTRA_PUSH_MESSAGE_STRING);
            Log.d("bmob", "客户端收到推送内容：" + pushString);
            mcache = ACache.get(context);
            try {
                JSONObject joType = new JSONObject(pushString);
                String type = joType.getString("type");
                data = joType.getJSONObject("data");
                switch(type){
                    case "news":
                        manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);//通知控制类
                        sendNotification(context,data.getString("tips"),data.getString("title"),data.getString("describe"),data.getString("publishUser"),data.getString("publishDate"),data.getString("content"));
                        break;
                    case "notice":
                        DbSoftwareNotice dbsn = new DbSoftwareNotice(context);
                        SQLiteDatabase dbwrite = dbsn.getWritableDatabase();
                        ContentValues cv = new ContentValues();
                        cv.put("title",data.getString("title"));
                        cv.put("publishDate",data.getString("publishDate"));
                        cv.put("content",data.getString("content"));
                        dbwrite.insert("software_notice", null, cv);
                        manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);//通知控制类
                        sendNotification(context, data.getString("tips"), data.getString("title"), data.getString("describe"), data.getString("publishUser"), data.getString("publishDate"), data.getString("content"));
                        break;
                    case "update":

                        break;
                    case "adPicture":
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                byte[] picdata = new byte[0];
                                try {
                                    picdata = getImage(data.getString("url"));
                                    int length = picdata.length;
                                    Bitmap bitMap = BitmapFactory.decodeByteArray(picdata, 0, length);
                                    mcache.put("splashPicture",bitMap,data.getInt("dayTime")*ACache.TIME_DAY);
                                    System.out.println("成功获取启动页图片");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();

                        break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 构造校园消息通知并发送到通知栏
     */
    private void sendNotification(Context context,String tips,String title,String describe ,String publishUser,String publishDate,String content){

        Intent intent = new Intent(context,NewsDetailsActivity.class);
        intent.putExtra("title",title);
        intent.putExtra("publishUser",publishUser);
        intent.putExtra("publishDate",publishDate);
        intent.putExtra("context", content);
        intent.putExtra("mode",1);

        PendingIntent pintent = PendingIntent.getActivities(context,0,new Intent[]{intent},0);

        Notification.Builder builder = new Notification.Builder(context);
        builder.setSmallIcon(R.drawable.app_icon);//状态栏上的小图标
        builder.setTicker(tips);//通知出现时在状态栏的文字提示，用户如果正在使用手机会看到。
        builder.setWhen(System.currentTimeMillis());//通知的时间
        builder.setContentTitle(title);//状态栏里Notification的标题
        builder.setContentText(describe);//状态栏里Notification的内容
        builder.setContentIntent(pintent);//设置点击后的意图
        builder.setDefaults(Notification.DEFAULT_SOUND);//设置通知铃声，另外有震动和指示灯的效果可以设置，但是需要权限

        Notification notification = builder.getNotification();//4.1以下使用，以上可以使用build();

        manager.notify(7788,notification);
    }

    public static byte[] getImage(String path) throws Exception {
        URL url = new URL(path);
        HttpURLConnection httpURLconnection =  (HttpURLConnection)url.openConnection();
        httpURLconnection.setRequestMethod("GET");
        httpURLconnection.setReadTimeout(6*1000);
        InputStream in = null;
        byte[] b = new byte[1024];
        int len = -1;
        if (httpURLconnection.getResponseCode() == 200) {
            in = httpURLconnection.getInputStream();
            byte[] result = readStream(in);
            in.close();
            return result;

        }
        return null;
    }

    public static byte[] readStream(InputStream in) throws Exception{
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = -1;
        while((len = in.read(buffer)) != -1) {
            outputStream.write(buffer, 0, len);
        }
        outputStream.close();
        in.close();
        return outputStream.toByteArray();
    }
}
