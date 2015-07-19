package com.yzc.lovehuali;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Gallery;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.dd.CircularProgressButton;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.yzc.lovehuali.bmob.StudentUser;

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

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.UpdateListener;


public class BindingEduSystemActivity extends ActionBarActivity {

    private Toolbar mToolbar;
    private ImageSwitcher idCodeImage;
    private Bitmap idCodeBitmap;
    private String idCodeImageUrl,key;
    private String massage;//存储登录信息
    private MaterialEditText etStudentId,etEduSystemPassword,etEduSystemIdCode;
    private CircularProgressButton cpbtnBindingEduSystem;
    final String getIdCodeUrl = new String("http://61.160.137.195:18001/zdinter/getInter.action?interCode=HLXY0001&para=&callback=HLXY0001");//获取验证码和密钥的网址，要考虑验证码获取失败的情况
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binding_edu_system);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("绑定教务系统");
        mToolbar.setBackgroundColor(Color.parseColor("#002196F3"));
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        etStudentId = (MaterialEditText) findViewById(R.id.etStudentId);
        etEduSystemPassword = (MaterialEditText) findViewById(R.id.etEduSystemPassword);
        etEduSystemIdCode = (MaterialEditText) findViewById(R.id.etEduSystemIdCode);

        idCodeImage = (ImageSwitcher) findViewById(R.id.IdCodeImage);
        idCodeImage.setFactory(new ViewSwitcher.ViewFactory() {

            @Override
            public View makeView() {
                ImageView imageView = new ImageView(BindingEduSystemActivity.this);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setLayoutParams(new ImageSwitcher.LayoutParams( Gallery.LayoutParams.MATCH_PARENT , Gallery.LayoutParams.MATCH_PARENT) );
                return imageView;
            }
        });
        getIdCodeImage(getIdCodeUrl);
        idCodeImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                getIdCodeImage(getIdCodeUrl);

            }
        });

        cpbtnBindingEduSystem = (CircularProgressButton)findViewById(R.id.cpbtnBindindEduSystem);
        cpbtnBindingEduSystem.setIndeterminateProgressMode(true);
        cpbtnBindingEduSystem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cpbtnBindingEduSystem.setProgress(0);
                cpbtnBindingEduSystem.setProgress(50);
                BindindEduSystem(null);
            }
        });

    }

    //获取验证码方法
    public void getIdCodeImage(String Url){
        new AsyncTask<String, Void, Void>() {

            @Override
            protected Void doInBackground(String... params) {

                try {
                    URL url = new URL(params[0]);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoOutput(true);
                    connection.setRequestMethod("POST");

                    System.out.println(connection.getResponseCode());//检查服务器返回状态码，200为正常

                    StringBuilder jasonStringBuilder = new StringBuilder();

                    if(connection.getResponseCode() == 200){
                        InputStream is = connection.getInputStream();
                        InputStreamReader isr = new InputStreamReader(is,"utf-8");
                        isr.skip(9);//跳过了前面九个字符,去掉json数据前面的不规范头部
                        BufferedReader br = new BufferedReader(isr);
                        String str;

                        while((str = br.readLine()) != null){

                            System.out.println(str);//输出读取的每一行数据，调试信息

                            jasonStringBuilder.append(str);
                        }
                        br.close();//关闭BufferedReader。
                        isr.close();//关闭isr。

                        JSONObject answerJSONObject = new JSONObject(jasonStringBuilder.toString());
                        JSONObject resultJSONObject = answerJSONObject.getJSONObject("result");
                        idCodeImageUrl = resultJSONObject.getString("img");
                        key = resultJSONObject.getString("key");

                        System.out.println("验证码地址：" + idCodeImageUrl);//调试信息
                        System.out.println("密钥：" + key);//调试信息


                        URL idCodeUrl  = new URL(idCodeImageUrl);
                        InputStream idCodeis = idCodeUrl.openConnection().getInputStream();
                        BufferedInputStream idCodebis = new BufferedInputStream(idCodeis);
                        idCodeBitmap = BitmapFactory.decodeStream(idCodebis);
                        idCodebis.close();
                        idCodeis.close();



                    }
                    else{
                        Toast.makeText(BindingEduSystemActivity.this, "获取验证码失败！", Toast.LENGTH_SHORT).show();
                    }


                }
                catch (MalformedURLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                catch (ProtocolException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return null;
            }
            protected void onPostExecute(Void result) {
                Drawable dw = new BitmapDrawable(idCodeBitmap);
                idCodeImage.setImageDrawable(dw);

            };

        }.execute(Url);

    }


    public void  BindindEduSystem(Void i){
        new AsyncTask<Void, Void, Void>(){

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    URL url = new URL("http://61.160.137.195:18001/zdinter/getInter.action?");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoOutput(true);
                    connection.setRequestMethod("POST");

                    OutputStreamWriter osw = new OutputStreamWriter(connection.getOutputStream());
                    BufferedWriter bw = new BufferedWriter(osw);
                    bw.write("interCode=HLXY0002&para=" + "loginName=" + etStudentId.getText().toString() +
                            ";loginPwd=" + etEduSystemPassword.getText().toString() + ";key=" +
                            key + ";imgCode=" + etEduSystemIdCode.getText().toString() + "&callback=HLXY0002");
                    bw.flush();

                    System.out.println(connection.getResponseCode());//检查服务器返回状态码，200为正常

                    StringBuilder jasonStringBuilder = new StringBuilder();

                    if(connection.getResponseCode() == 200){

                        InputStream is = connection.getInputStream();
                        InputStreamReader isr = new InputStreamReader(is,"utf-8");
                        isr.skip(9);//跳过了前面九个字符,去掉json数据前面的不规范头部
                        BufferedReader br = new BufferedReader(isr);
                        String str;

                        while((str = br.readLine()) != null){

                            System.out.println(str);//输出读取的每一行数据，调试信息

                            jasonStringBuilder.append(str);
                        }
                        br.close();//关闭BufferedReader。
                        isr.close();//关闭isr。

                        JSONObject answerJSONObject = new JSONObject(jasonStringBuilder.toString());
                        JSONObject resultJSONObject = answerJSONObject.getJSONObject("detail");

                        final StudentUser bmobuser = BmobUser.getCurrentUser(BindingEduSystemActivity.this, StudentUser.class);
                        StudentUser mUser = new StudentUser();

                        massage = answerJSONObject.getString("msg");
                        //将返回的用户数据存储到用户对应的账户中
                        mUser.setStuClass(resultJSONObject.getString("stuclass"));
                        mUser.setStudentId(resultJSONObject.getString("studentId"));
                        mUser.setEduSystemPw(etEduSystemPassword.getText().toString());
                        mUser.setDepartment(resultJSONObject.getString("department"));
                        mUser.setGrade(resultJSONObject.getString("grade"));
                        mUser.setMajor(resultJSONObject.getString("major"));
                        mUser.setObjectId(bmobuser.getObjectId());
                        mUser.update(BindingEduSystemActivity.this, new UpdateListener() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onFailure(int i, String s) {

                            }
                        });



                    }
                    else{
                        cpbtnBindingEduSystem.setProgress(-1);
                    }


                }
                catch (MalformedURLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                catch (ProtocolException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return null;
            }
            protected void onPostExecute(Void result) {

                if(massage == null)
                {
                    cpbtnBindingEduSystem.setProgress(-1);
                    getIdCodeImage(getIdCodeUrl);
                }
                else{
                    cpbtnBindingEduSystem.setProgress(100);
                    final int LogOffDelay = 1500;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            /*Toast.makeText(getApplicationContext(),BmobUser.getCurrentUser(getApplicationContext(),StudentUser.class).getMajor(),Toast.LENGTH_LONG).show();
                            Toast.makeText(getApplicationContext(),"请退出登录，并重新登录更新数据",Toast.LENGTH_LONG).show();
                            Toast.makeText(getApplicationContext(),"若因为修改教务系统密码导致软件不正常使用，请在个人信息下重新进行绑定即可^_^!",Toast.LENGTH_LONG).show();*/
                            Intent i = new Intent(BindingEduSystemActivity.this,LoginActivity.class);
                            startActivity(i);
                            finish();
                        }
                    }, LogOffDelay);


                }
            }
        }.execute(i);

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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
