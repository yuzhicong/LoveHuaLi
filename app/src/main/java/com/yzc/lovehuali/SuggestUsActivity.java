package com.yzc.lovehuali;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.yzc.lovehuali.email.MailSenderInfo;
import com.yzc.lovehuali.email.SimpleMailSender;
import com.yzc.lovehuali.tool.NetUtils;
import com.yzc.lovehuali.tool.SystemBarTintManager;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;


public class SuggestUsActivity extends ActionBarActivity {

    private MaterialEditText etUserName,etQQNumber,etPhoneCall;
    private EditText etContent;
    private Button btnSendFeedBack;
    private Toolbar mToolbar;
    private HttpClient client;
    private String value;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggest_us);

        //设定状态栏的颜色，当版本大于4.4时起作用
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            //此处可以重新指定状态栏颜色
            tintManager.setStatusBarTintResource(R.color.colorPrimaryDark);
        }

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("用户反馈");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etUserName = (MaterialEditText) findViewById(R.id.etUserName);
        etQQNumber = (MaterialEditText) findViewById(R.id.etQQNumber);
        etPhoneCall = (MaterialEditText) findViewById(R.id.etPhoneCall);
        etContent = (EditText) findViewById(R.id.etContent);
        btnSendFeedBack = (Button) findViewById(R.id.btnSendFeedBack);
        client = new DefaultHttpClient();
        btnSendFeedBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!etContent.getText().toString().isEmpty()){
                Toast.makeText(SuggestUsActivity.this,"正在发送中……",Toast.LENGTH_SHORT).show();
                sendFeedBack("http://1.lovehuali.sinaapp.com/sendFeedBack.php?name=" + etUserName.getText() + "&qq=" + etQQNumber.getText() + "&phoneNum=" + etPhoneCall.getText() + "&description=" + etContent.getText());
                btnSendFeedBack.setClickable(false);}
                else{
                    Toast.makeText(SuggestUsActivity.this,"亲！您还没有输入内容呢...",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /*//向指定的邮箱发送信息
    public void sendEmailtoUs(View view) {
        if (!etContent.getText().toString().isEmpty()) {
            if(NetUtils.isConnected(getApplicationContext())) {
                new Thread() {
                    @Override
                    public void run() {
                        //这个类主要是设置邮件
                        MailSenderInfo mailInfo = new MailSenderInfo();
                        mailInfo.setMailServerHost("smtp.163.com");
                        mailInfo.setMailServerPort("25");
                        mailInfo.setValidate(true);
                        mailInfo.setUserName("yzcdyxhi@163.com");
                        mailInfo.setPassword("yzcdwyyx.");//您的邮箱密码
                        mailInfo.setFromAddress("yzcdyxhi@163.com");
                        mailInfo.setToAddress("yzcdyxhi@163.com");
                        mailInfo.setSubject("用户建议");

                        //读取用户输入的信息
                        String EmailContent = new String();
                        if(!etUserName.getText().toString().isEmpty()){
                            EmailContent+="用户昵称：" + etUserName.getText().toString() + "\n";
                        }
                        if(!etQQNumber.getText().toString().isEmpty()){
                            EmailContent+="用户QQ：" + etQQNumber.getText().toString() + "\n";
                        }
                        if(!etPhoneCall.getText().toString().isEmpty()){
                            EmailContent+="联系号码：" + etPhoneCall.getText().toString() + "\n";
                        }
                        EmailContent += "建议内容:" + etContent.getText().toString();

                        mailInfo.setContent(EmailContent);
                        //这个类主要来发送邮件
                        SimpleMailSender sms = new SimpleMailSender();
                        //sms.sendTextMail(mailInfo);//发送文体格式
                        //sms.sendHtmlMail(mailInfo);//发送html格式
                        if(sms.sendTextMail(mailInfo)){
                            Message msg = new Message();
                            msg.obj = "true";
                            mHandler.sendMessage(msg);
                        }else{
                            Message msg = new Message();
                            msg.obj = "false";
                            mHandler.sendMessage(msg);
                        }

                    }
                }.start();
            }
            else {
                Toast.makeText(getApplicationContext(),"网络不给力，无法发送T-T!",Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(getApplicationContext(),"请输入建议再发送哟！",Toast.LENGTH_SHORT).show();
        }
    }

    private Handler mHandler = new Handler(){

        public void handleMessage(Message msg) {
            if(msg.obj.toString() == "true"){
                Toast.makeText(getApplicationContext(),"发送成功，感谢您的支持！",Toast.LENGTH_SHORT).show();
                finish();
            }
            if(msg.obj.toString() == "false"){
                Toast.makeText(getApplicationContext(),"发送失败T-T!",Toast.LENGTH_SHORT).show();
            }
        };
    };*/

    public void sendFeedBack(String url){
        new AsyncTask<String, Void, Void>() {

            @Override
            protected Void doInBackground(String... params) {
                String urlString = params[0];
                HttpGet get = new HttpGet(urlString);



                try {
                    HttpResponse response = client.execute(get);

                    value = EntityUtils.toString(response.getEntity());
                    System.out.println(value);

                } catch (ClientProtocolException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if(value.contains("200")){
                    Toast.makeText(SuggestUsActivity.this,"反馈成功，谢谢您的支持！",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(SuggestUsActivity.this,"反馈失败，请稍后重试T-T",Toast.LENGTH_SHORT).show();
                }
            }
        }.execute(url);
    }
}
