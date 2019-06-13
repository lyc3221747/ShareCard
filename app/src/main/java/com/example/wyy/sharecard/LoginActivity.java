package com.example.wyy.sharecard;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.os.StrictMode;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.RequestManager;
import com.githang.statusbar.StatusBarCompat;
import com.tencent.connect.UserInfo;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

import static cn.bmob.v3.Bmob.getApplicationContext;

public class LoginActivity extends CheckPermissionsActivity {


    private String tel;     //手机号
    private String pass;    //验证码

    private Tencent mTencent;
    private IUiListener listener;
    private IUiListener infoUilistener;
    private MyUser userinfo;
    private UserInfo mUserinfo;
    private File file1;
    private MyUser userInfo;
    private CountDownTimer countDownTimer;


    private LocalReceiver localReceiver;
    private LocalBroadcastManager localBroadcastManager;
    private IntentFilter intentFilter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        StatusBarCompat.setStatusBarColor(this, Color.parseColor("#FFFFFF"));
        //接受QQ登录成功的广播
        localBroadcastManager = LocalBroadcastManager.getInstance(this);    //广播获取实例
        intentFilter = new IntentFilter();
        intentFilter.addAction("QQ登录成功");
        localReceiver = new LocalReceiver();
        localBroadcastManager.registerReceiver(localReceiver, intentFilter);    //注册本地广播监听器

        //获取qq信息并上传
        infoUilistener = new IUiListener() {

            @Override
            public void onError(UiError arg0) {
                // TODO Auto-generated method stub
                Log.e("error", "," + arg0.errorCode + arg0.errorMessage );
            }

            /**
             * 返回用户信息样例
             *
             * {"is_yellow_year_vip":"0","ret":0,
             * "figureurl_qq_1":"http:\/\/q.qlogo.cn\/qqapp\/1104732758\/015A22DED93BD15E0E6B0DDB3E59DE2D\/40",
             * "figureurl_qq_2":"http:\/\/q.qlogo.cn\/qqapp\/1104732758\/015A22DED93BD15E0E6B0DDB3E59DE2D\/100",
             * "nickname":"攀爬←蜗牛","yellow_vip_level":"0","is_lost":0,"msg":"",
             * "city":"黄冈","
             * figureurl_1":"http:\/\/qzapp.qlogo.cn\/qzapp\/1104732758\/015A22DED93BD15E0E6B0DDB3E59DE2D\/50",
             * "vip":"0","level":"0",
             * "figureurl_2":"http:\/\/qzapp.qlogo.cn\/qzapp\/1104732758\/015A22DED93BD15E0E6B0DDB3E59DE2D\/100",
             * "province":"湖北",
             * "is_yellow_vip":"0","gender":"男",
             * "figureurl":"http:\/\/qzapp.qlogo.cn\/qzapp\/1104732758\/015A22DED93BD15E0E6B0DDB3E59DE2D\/30"}
             */
            @Override
            public void onComplete(Object arg0) {
                // TODO Auto-generated method stub
                if(arg0 == null){
                    return;
                }
                try {
                    JSONObject jo = (JSONObject) arg0;
                    int ret = jo.getInt("ret");
                    System.out.println("json=" + String.valueOf(jo));
                    final String nickName = jo.getString("nickname");
                    String image=jo.getString("figureurl_2");
                    StrictMode.setThreadPolicy(new
                            StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
                    StrictMode.setVmPolicy(
                            new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());
                    Bitmap bitmap=getbitmap(image);
                    saveBitmapFile(bitmap);
                    if(file1!=null)
                    {
                        System.out.println("文件取得"+file1.getAbsolutePath()+" "+file1.getName());
                    }
                    System.out.println(nickName);
                    final BmobFile bmobFile=new BmobFile(file1);
                    final MyUser sendfor=new MyUser();
                    sendfor.setHead_pic(bmobFile);
                    sendfor.getHead_pic().uploadblock(new UploadFileListener() {
                        @Override
                        public void done(BmobException e) {
                            if(e==null){
                                //bmobFile.getFileUrl()--返回的上传文件的完整地址
                                System.out.println("上传文件成功:");
                                sendfor.setName(nickName);
                                sendfor.setPassword(userinfo.getUsername().substring(0,6));
                                sendfor.setPass(userinfo.getUsername().substring(0,6));
                                sendfor.update(userinfo.getObjectId(), new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        if(e==null){
                                            System.out.println("更新用户信息成功:" + e.getMessage());
                                            //toast("更新用户信息成功");
                                        }else{

                                            System.out.println("更新用户信息失败:" + e.getMessage());
                                            //toast("更新用户信息失败:" + e.getMessage());
                                        }
                                    }
                                });
                                Toast.makeText(getApplication(), "你好，" + nickName,
                                        Toast.LENGTH_LONG).show();
                            }else{
                                System.out.println("上传文件失败：" + e.getMessage());
                            }

                        }

                        @Override
                        public void onProgress(Integer value) {
                            super.onProgress(value);
                        }
                    });
                    sendfor.update(userinfo.getObjectId(),new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if(e==null){
                                System.out.println("更新用户信息成功:" + e.getMessage());
                                // toast("更新用户信息成功");
                            }else{
                                System.out.println("更新用户信息失败:" + e.getMessage());
                                // toast("更新用户信息失败:" + e.getMessage());
                            }
                        }
                    });



                } catch (Exception e) {
                    // TODO: handle exception
                }


            }

            @Override
            public void onCancel() {
                // TODO Auto-generated method stub

            }
        };


        final Button yanzheng = (Button) findViewById(R.id.yanzheng);
        yanzheng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText tele = (EditText) findViewById(R.id.tele);
                tel = tele.getText().toString();
                Log.e("smile", "点击发送验证码");
                Log.e("smile", "tel"+tel);
                BmobSMS.requestSMSCode(tel,"share", new QueryListener<Integer>() {

                    @Override
                    public void done(Integer smsId,BmobException ex) {
                        if(ex==null){//验证码发送成功
                            Log.e("smile", "短信id："+smsId);//用于查询本次短信发送

                            // 发送成功进入倒计时
                            countDownTimer = new CountDownTimerUtils(yanzheng, 60000, 1000);
                            countDownTimer.start();



                        } else {
                            Log.e("smile", ex.getErrorCode() + ex.getMessage() + tel);
                            if (ex.getErrorCode() == 9018) {
                                Toast.makeText(getApplicationContext(), "请输入手机号码", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (ex.getErrorCode() == 301) {
                                Toast.makeText(getApplicationContext(), "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                    }
                });

            }
        });

        Button login = (Button) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //仅登录
                EditText password = (EditText) findViewById(R.id.password);
                pass = password.getText().toString();

                BmobUser.signOrLoginByMobilePhone(tel, pass, new LogInListener<MyUser>() {

                    @Override
                    public void done(MyUser user, BmobException e) {
                        if(user!=null){
                            Log.e("smile","用户登陆成功");
                            Toast.makeText(getApplicationContext(), "手机登录成功", Toast.LENGTH_SHORT).show();

                            if (user.getName() == null) {
                                user.setName(tel);
                                user.update(new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                             if (e == null) {

                                             }
                                    }
                                });
                            }

                            finish();
//                            Intent intent = new Intent(getApplication(), HomeActivity.class);
//                            startActivity(intent);
                        }
                    }
                });

                //登录并设置

                //"http://img0.imgtn.bdimg.com/it/u=1095909580,3513610062&fm=23&gp=0.jpg"

//                EditText password = (EditText) findViewById(R.id.password);
//                pass = password.getText().toString();
//                MyUser user = new MyUser();
//                user.setMobilePhoneNumber(tel);//设置手机号码（必填）
//                user.setUsername(tel);                  //设置用户名，如果没有传用户名，则默认为手机号码
//                //user.setPassword(xxx);                  //设置用户密码
//                //user.setAge(18);                        //设置额外信息：此处为年龄
//                user.signOrLogin("验证码", new SaveListener<MyUser>() {
//
//                    @Override
//                    public void done(MyUser user,BmobException e) {
//                        if(e==null){
//                            //toast("注册或登录成功");
//                            //Log.i("smile", ""+user.getUsername()+"-"+user.getAge()+"-"+user.getObjectId());
//                        }else{
//                            if (e.getErrorCode() == 207) {
//                                Toast.makeText(getApplicationContext(), "验证码错误，请重新输入", Toast.LENGTH_SHORT).show();
//                            }
//                            Log.e("失败:", e.getErrorCode() + e.getMessage());
//                        }
//
//                    }
//
//                });
            }
        });

        ImageView weixin = (ImageView) findViewById(R.id.weixin);
        weixin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "此功能暂未开通", Toast.LENGTH_SHORT).show();
            }
        });

        ImageView qq = (ImageView) findViewById(R.id.qq);
        qq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                do_QQ_login();
            }
        });

        ImageView weibo = (ImageView) findViewById(R.id.weibo);
        weibo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "此功能暂未开通", Toast.LENGTH_SHORT).show();
            }
        });

    }


    //QQ登录验证
    private void do_QQ_login() {
        if (mTencent==null) {
            mTencent = Tencent.createInstance(Constants.QQ_APP_ID, getApplicationContext());
        }

        listener = new IUiListener() {
            @Override
            public void onCancel() {
                toast("取消qq授权");
            }

            @Override
            public void onError(UiError uiError) {
                toast("QQ授权出错：" + uiError.errorCode + "--" + uiError.errorDetail);
            }

            @Override
            public void onComplete(Object o) {
                toast("qq授权成功");
                if (o != null) {
                    JSONObject jsonObject = (JSONObject) o;
                    try {
                        String token = jsonObject.getString(com.tencent.connect.common.Constants.PARAM_ACCESS_TOKEN);
                        String expires = jsonObject.getString(com.tencent.connect.common.Constants.PARAM_EXPIRES_IN);
                        String openId = jsonObject.getString(com.tencent.connect.common.Constants.PARAM_OPEN_ID);
                        System.out.println(token);
                        mTencent.setOpenId(openId);
                        mTencent.setAccessToken(token,expires);

                        BmobUser.BmobThirdUserAuth authInfo = new BmobUser.BmobThirdUserAuth(BmobUser.BmobThirdUserAuth.SNS_TYPE_QQ,token, expires,openId);
                        // BmobQuery<BmobUser> query = new BmobQuery<BmobUser>();
                        // System.out.println("{\"qq\":{\"access_token\":\""+token+"\",\"expires_in\":"+expires+",\"openid\":\""+openId+"\"}}");
                        // query.addWhereEqualTo("authData", "{\'qq\':{\'access_token\':\'"+token+"\',\'expires_in\':"+expires+",\'openid\':\'"+openId+"\'}}");



                        //登录
                        BmobUser.loginWithAuthData(authInfo, new LogInListener<JSONObject>() {


                            @Override
                            public void done(JSONObject jsonObject, BmobException e)
                            {
                                userinfo = BmobUser.getCurrentUser(MyUser.class);
                                System.out.println(userinfo.getUsername());
                                if (userinfo.getUsername().length()>11&&userinfo.getName()==null)
                                {
                                    getinfomation();




//                                    Intent intent=new Intent(getApplication(),HomeActivity.class);
//                                    startActivity(intent);
                                    Toast.makeText(getApplicationContext(), "QQ登录成功", Toast.LENGTH_SHORT).show();
                                    Intent intent1 = new Intent("QQ登录成功");
                                    localBroadcastManager.sendBroadcast(intent1);   //发送本地广播
                                    finish();
                                }
                                else{
                                    Toast.makeText(getApplicationContext(), "QQ登录成功", Toast.LENGTH_SHORT).show();
                                    Intent intent1 = new Intent("QQ登录成功");
                                    localBroadcastManager.sendBroadcast(intent1);   //发送本地广播
                                    finish();
                                }
                            }
                        });

                    } catch (JSONException e) {
                    }

                }
            }

        };
        mTencent.login(this, "all", listener);


    }


    private void toast(String msg) {
        Toast.makeText(getApplication(),msg,Toast.LENGTH_SHORT).show();
    }
    void  getinfomation()
    {
        System.out.println("开始获取用户信息");
        mUserinfo = new UserInfo(getApplication(), mTencent.getQQToken());
        mUserinfo.getUserInfo(infoUilistener);
    }

    public static Bitmap getbitmap(String imageUri) {
        Log.v("Util", "getbitmap:" + imageUri);
        // 显示网络上的图片
        Bitmap bitmap = null;
        try {
            URL myFileUrl = new URL(imageUri);
            HttpURLConnection conn = (HttpURLConnection) myFileUrl
                    .openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();

            Log.v("Util", "image download finished." + imageUri);
        } catch (IOException e) {
            e.printStackTrace();
            Log.v("Util", "getbitmap bmp fail---");
            return null;
        }
        return bitmap;
    }
    public void saveBitmapFile(Bitmap bitmap){
        System.out.println(getApplication().getExternalCacheDir());
        File file=new File(getApplication().getExternalCacheDir()+"/temp.jpg");
        System.out.println(getApplication().getExternalCacheDir()+"/temp.jpg");
        // /将要保存图片的路径
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
            file1=new File(file.getAbsolutePath());
        } catch (IOException e) {
            System.out.println(e.getMessage());
            if (e==null)
            {
                System.out.println("图片保存成功"+e.getMessage());
            }
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Tencent.onActivityResultData(requestCode,resultCode,data,listener);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == com.tencent.connect.common.Constants.REQUEST_LOGIN) {
            if (resultCode == com.tencent.connect.common.Constants.ACTIVITY_OK) {
                Tencent.handleResultData(data, listener);
            }
        }
    }

    private class LocalReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Toast.makeText(getApplication(), "接到广播", Toast.LENGTH_SHORT).show();
            finish();
        }
    }



}
