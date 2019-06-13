package com.example.wyy.sharecard;

        import android.app.Activity;
        import android.content.BroadcastReceiver;
        import android.content.Context;
        import android.content.Intent;
        import android.content.IntentFilter;
        import android.graphics.Color;
        import android.location.Location;
        import android.support.v4.content.LocalBroadcastManager;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.widget.Button;
        import android.widget.ImageView;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.amap.api.maps.AMap;
        import com.amap.api.maps.AMapUtils;
        import com.amap.api.maps.CameraUpdateFactory;
        import com.amap.api.maps.MapView;
        import com.amap.api.maps.model.LatLng;
        import com.amap.api.maps.model.Marker;
        import com.amap.api.maps.model.MarkerOptions;
        import com.amap.api.maps.model.MyLocationStyle;
        import com.bumptech.glide.Glide;
        import com.githang.statusbar.StatusBarCompat;

        import java.util.List;

        import cn.bmob.v3.BmobQuery;
        import cn.bmob.v3.BmobUser;
        import cn.bmob.v3.exception.BmobException;
        import cn.bmob.v3.listener.FindListener;
        import cn.bmob.v3.listener.QueryListener;
        import cn.bmob.v3.listener.UpdateListener;

public class PublishDetailActivity extends CheckPermissionsActivity{
    private MapView mMapView = null;
    private AMap aMap;
    private boolean isfirst = true;

    private cardItem_listview card;

    private String card_id;
    private Double Latitude;//纬度
    private Double Longitude;//经度

    private LocalReceiver localReceiver;
    private LocalBroadcastManager localBroadcastManager;
    private IntentFilter intentFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_detail);
        StatusBarCompat.setStatusBarColor(this, Color.parseColor("#FFFFFF"));
        //接受支付成功的广播
        localBroadcastManager = LocalBroadcastManager.getInstance(this);        //广播获取实例
        intentFilter = new IntentFilter();
        intentFilter.addAction("支付成功");
        localReceiver = new LocalReceiver();
        localBroadcastManager.registerReceiver(localReceiver, intentFilter);    //注册本地广播监听器

        //接收商品id列表类
        Bundle bundle = this.getIntent().getExtras();
        card_id = bundle.getString("card_id");
//        Log.e("tag", card.getCard_id());

        init();




        //下架
        Button delete=(Button) findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                card card = new card();
                card.setObjectId(card_id);
                card.delete(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if(e==null){
                            Toast.makeText(getApplicationContext(), "下架成功", Toast.LENGTH_SHORT).show();
                            finish();
                        }else{
//                            toast("删除失败：" + e.getMessage());
                        }
                    }
                });
            }
        });



    }

    private void init() {
        final TextView card_name = (TextView) findViewById(R.id.card_name);
        final TextView org_price = (TextView) findViewById(R.id.org_price);
        final TextView now_price = (TextView) findViewById(R.id.now_price);
        final TextView owner_name = (TextView) findViewById(R.id.owner_name);
        final TextView card_info = (TextView) findViewById(R.id.card_info);


        final TextView time = (TextView) findViewById(R.id.time);




        final ImageView card_pic = (ImageView) findViewById(R.id.card_pic);
        final ImageView owner_pic = (ImageView) findViewById(R.id.owner_pic);
        final ImageView picture1 = (ImageView) findViewById(R.id.picture1);
        final ImageView picture2 = (ImageView) findViewById(R.id.picture2);
        final ImageView picture3 = (ImageView) findViewById(R.id.picture3);

        BmobQuery<card> query = new BmobQuery<card>();
        query.getObject(card_id, new QueryListener<card>() {

            @Override
            public void done(final card c, BmobException e) {
                if (e == null) {
                    Log.e("tag", "卡搜索成功");

                    BmobQuery<MyUser> query = new BmobQuery<MyUser>();
                    query.getObject(c.getUser_id(), new QueryListener<MyUser>() {

                        @Override
                        public void done(final MyUser myUser, BmobException e) {
                            if (e == null) {
                                Log.e("tag", "人搜索成功");

                                BmobQuery <picture> query = new BmobQuery<>();
                                query.addWhereEqualTo("card_id", card_id);
                                //query.order("")
                                query.findObjects(new FindListener<picture>() {
                                    @Override
                                    public void done(List<picture> list, BmobException e) {
                                        if (e == null) {

                                            Log.e("tag", "图搜索成功");
                                            for (picture p : list) {

                                                Latitude = c.getLatitude();//纬度
                                                Longitude = c.getLongitude();//经度
                                                card_name.setText(c.getName());
                                                org_price.setText(Double.toString(c.getOrg_price()));
                                                now_price.setText(Double.toString(c.getNow_price()));
                                                owner_name.setText(myUser.getName());
                                                card_info.setText(c.getInf());
                                                //sales
                                                time.setText(c.getTime() + "发布");

                                                Glide.with(getApplicationContext()).load(myUser.getHead_pic().getUrl().toString()).transform(new GlideCircleTransform(getApplicationContext())).into(owner_pic);
                                                if (p.getPic().getFilename().equals("tempCompress0.jpg")) {
                                                    Glide.with(getApplicationContext()).load(p.getPic().getUrl().toString()).transform(new GlideRoundTransform(getApplicationContext())).into(card_pic);
                                                    Glide.with(getApplicationContext()).load(p.getPic().getUrl().toString()).transform(new GlideRoundTransform(getApplicationContext())).into(picture1);
                                                    picture1.setVisibility(View.VISIBLE);
                                                }

                                                if (p.getPic().getFilename().equals("tempCompress1.jpg")) {
                                                    Glide.with(getApplicationContext()).load(p.getPic().getUrl().toString()).transform(new GlideRoundTransform(getApplicationContext())).into(picture2);
                                                    picture2.setVisibility(View.VISIBLE);
                                                }

                                                if (p.getPic().getFilename().equals("tempCompress2.jpg")) {
                                                    Glide.with(getApplicationContext()).load(p.getPic().getUrl().toString()).transform(new GlideRoundTransform(getApplicationContext())).into(picture3);
                                                    picture3.setVisibility(View.VISIBLE);
                                                }

                                                //若已出售，则隐藏下架图标
                                                if (c.getState() == 0) {
                                                    Button delete=(Button) findViewById(R.id.delete);
                                                    delete.setVisibility(View.INVISIBLE);
                                                }


                                            }
                                        }else {
                                            Log.e("tag", "图搜索失败");
                                        }
                                    }
                                });
                            } else {
                                Log.e("tag", "人搜索失败");
                            }
                        }
                    });
                }
                else {
                    Log.e("tag", "卡搜索失败");
                }
            }
        });






    }



    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
    @Override
    protected void onResume() {
        super.onResume();

    }
    @Override
    protected void onPause() {
        super.onPause();

    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }




    private class LocalReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Toast.makeText(getApplication(), "接到广播", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
