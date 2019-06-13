package com.example.wyy.sharecard;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.githang.statusbar.StatusBarCompat;
import com.tencent.connect.UserInfo;
import com.tencent.connect.dataprovider.*;
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
import java.util.ArrayList;
import java.util.List;

import c.b.BP;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

import static cn.bmob.v3.Bmob.getApplicationContext;

/**
 * Created by wyy on 17-7-8.
 */

public class FourthFragment extends Fragment {
    private RequestManager glideRequest;
    private Tencent mTencent;
    private IUiListener listener;
    private IUiListener infoUilistener;
    private View view;
    private MyUser userinfo;
    private UserInfo mUserinfo;
    private File file1;




    private BmobUser bmobUser = BmobUser.getCurrentUser();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_fourth, container, false);
        StatusBarCompat.setStatusBarColor(getActivity(), Color.parseColor("#FFFFFF"));
        //initFragment();

        //
        LinearLayout btn_mine_inf =(LinearLayout) view.findViewById(R.id.btn_mine_inf);
        btn_mine_inf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bmobUser == null) {
                    //未登录,跳转至登录界面
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    startActivity(intent);
                }
                else {
                    //已登录
                }


            }
        });

        //我发布的
        LinearLayout publish_page=(LinearLayout) view.findViewById(R.id.publish_page);
        publish_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),PublishActivity.class);
                startActivity(intent);
            }
        });

        //我约到的
        LinearLayout out_page=(LinearLayout) view.findViewById(R.id.out_page);
        out_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),OutActivity.class);
                startActivity(intent);
            }
        });





        /*个人主页

        LinearLayout btn_blog=(LinearLayout) view.findViewById(R.id.btn_blog);
        btn_blog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),BlogActivity.class);
                startActivity(intent);
            }
        });

        */
        /*安全

        LinearLayout btn_security=(LinearLayout) view.findViewById(R.id.btn_security);
        btn_security.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),SecurityActivity.class);
                startActivity(intent);
            }
        });

        */
        /*设置

        LinearLayout setting = (LinearLayout) view.findViewById(R.id.setting);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),SettingActivity.class);
                startActivity(intent);
            }
        });

        */

        return view;
    }



    private void initFragment(){

        //int publish_num;



        bmobUser = BmobUser.getCurrentUser();
        ImageView user_head_pic=(ImageView) view.findViewById(R.id.user_head_pic);
        TextView user_name = (TextView) view.findViewById(R.id.user_name);

        final TextView publish_num = (TextView) view.findViewById(R.id.publish_num);    //已发布
        final TextView out_num = (TextView) view.findViewById(R.id.out_num);    //约出
        final TextView in_num = (TextView) view.findViewById(R.id.in_num);     //约到

        //查询是否登录
        if (bmobUser == null) {
            user_name.setText("请登录");
            glideRequest = Glide.with(getContext());
            glideRequest.load("http://img0.imgtn.bdimg.com/it/u=1095909580,3513610062&fm=23&gp=0.jpg").transform(new GlideCircleTransform(getContext())).into(user_head_pic);
        }
        else {
            MyUser userInfo;
            userInfo = BmobUser.getCurrentUser(MyUser.class);
            user_name.setText(userInfo.getName());
            glideRequest = Glide.with(getContext());
            if (BmobUser.getCurrentUser(MyUser.class).getHead_pic() == null) {
                glideRequest.load("http://img0.imgtn.bdimg.com/it/u=1095909580,3513610062&fm=23&gp=0.jpg").transform(new GlideCircleTransform(getContext())).into(user_head_pic);
            }
            else {
                glideRequest.load(userInfo.getHead_pic().getUrl()).transform(new GlideCircleTransform(getContext())).into(user_head_pic);
            }
        }

        //查询已发布卡券数量
        BmobQuery<card> query0 = new BmobQuery<card>();
        query0.addWhereEqualTo("user_id", bmobUser.getObjectId());
        query0.count(card.class, new CountListener() {
            @Override
            public void done(Integer count, BmobException e) {
                if(e==null){
                    Log.e("tag", "用户为：" + bmobUser.getObjectId() + "已发布个数为："+count);

                    publish_num.setText(Integer.toString(count));

                }else{
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });

        //查询已约出卡券数量
        BmobQuery<card> eq1 = new BmobQuery<card>();
        eq1.addWhereEqualTo("user_id", bmobUser.getObjectId());
        BmobQuery<card> eq2 = new BmobQuery<card>();
        eq2.addWhereEqualTo("state", 0);
        //组装and
        List<BmobQuery<card>> andQuerys = new ArrayList<BmobQuery<card>>();
        andQuerys.add(eq1);
        andQuerys.add(eq2);
        BmobQuery<card> query1 = new BmobQuery<card>();
        query1.and(andQuerys);
        query1.count(card.class, new CountListener() {
            @Override
            public void done(Integer count, BmobException e) {
                if(e==null){
                    Log.e("tag", "用户为：" + bmobUser.getObjectId() + "已约出个数为："+count);
                    out_num.setText(Integer.toString(count));
                }else{
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });

        //查询已约到的卡券数量
        BmobQuery<Order> query2 = new BmobQuery<Order>();
        query2.addWhereEqualTo("user_id", bmobUser.getObjectId());
        query2.count(Order.class, new CountListener() {
            @Override
            public void done(Integer count, BmobException e) {
                if(e==null){
                    Log.e("tag", "用户为：" + bmobUser.getObjectId() + "已约到个数为："+count);
                    in_num.setText(Integer.toString(count));
                }else{
                    Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });



    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Tencent.onActivityResultData(requestCode,resultCode,data,listener);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("Resume", "个人中心");
        //在activity执行onResume时执行，重新初始化
        initFragment();

//        TextView publish_num = (TextView) view.findViewById(R.id.publish_num);
//        Log.e("tag", "用户为：" + bmobUser.getObjectId() + "个数为："+count0);
//        publish_num.setText(Integer.toString(count0));
//        publish_num.setFocusable(true);
//        publish_num.setFocusableInTouchMode(true);
//        publish_num.requestFocus();
    }

}
