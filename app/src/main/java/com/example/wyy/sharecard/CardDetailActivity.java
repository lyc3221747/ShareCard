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

public class CardDetailActivity extends CheckPermissionsActivity implements AMap.OnMyLocationChangeListener{
    private MapView mMapView = null;
    private AMap aMap;
    private boolean isfirst = true;

    private cardItem_listview card;

//    private String card_id;
//    private Double Latitude;//纬度
//    private Double Longitude;//经度

    private LocalReceiver localReceiver;
    private LocalBroadcastManager localBroadcastManager;
    private IntentFilter intentFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_detail);
        StatusBarCompat.setStatusBarColor(this, Color.parseColor("#FFFFFF"));
        //接受支付成功的广播
        localBroadcastManager = LocalBroadcastManager.getInstance(this);        //广播获取实例
        intentFilter = new IntentFilter();
        intentFilter.addAction("支付成功");
        localReceiver = new LocalReceiver();
        localBroadcastManager.registerReceiver(localReceiver, intentFilter);    //注册本地广播监听器

        //接收商品列表类
        Intent intent = getIntent();
        card = (cardItem_listview) intent.getSerializableExtra("cardItem_listview");
        Log.e("tag", card.getCard_id());

        init();


        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.map);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView.onCreate(savedInstanceState);// 此方法须覆写，虚拟机需要在很多情况下保存地图绘制的当前状态。
        //初始化地图控制器对象

        if (aMap == null) {
            aMap = mMapView.getMap();
        }

        LatLng latLng = new LatLng(card.getLatitude(),card.getLongitude());
        final Marker marker = aMap.addMarker(new MarkerOptions().position(latLng).title(card.getCard_name()).snippet(card.getCard_inf()));
        //设置标志物是否可见
        marker.setVisible(true);
        //设置标志物是否可以拖拽
        marker.setDraggable(false);

        MyLocationStyle myLocationStyle;

        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE) ;//定位一次，且将视角移动到地图中心点。
        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        aMap.getUiSettings().setMyLocationButtonEnabled(true);//设置默认定位按钮是否显示，非必需设置。
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        //设置SDK 自带定位消息监听
        aMap.setOnMyLocationChangeListener(this);

        //约卡
        Button confirm=(Button) findViewById(R.id.confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final BmobUser bmobUser = BmobUser.getCurrentUser();
                if (bmobUser != null) {
                    //已登录

                    MyUser userInfo = BmobUser.getCurrentUser(MyUser.class);
                    if (userInfo.getObjectId().equals(card.getUser_id())) {
                        Toast.makeText(getApplicationContext(),"不能购买自己的卡券",Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Intent intent1=new Intent(getApplicationContext(),ConfirmOrderActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("cardItem_listview", card);
                    intent1.putExtras(bundle);

//                    Intent intent1=new Intent(CardDetailActivity.this,ConfirmOrderActivity.class);
//                    intent1.putExtra("card_id", card.getCard_id());
                    startActivity(intent1);

                } else {
                    //未登录,转到登录
                    Toast.makeText(getApplicationContext(),"请先登录",Toast.LENGTH_SHORT).show();
                    Intent intent2 = new Intent(getApplication(), LoginActivity.class);
                    startActivity(intent2);
                }



            }
        });



    }

    private void init() {
        final TextView card_name = (TextView) findViewById(R.id.card_name);
        final TextView org_price = (TextView) findViewById(R.id.org_price);
        final TextView now_price = (TextView) findViewById(R.id.now_price);
        final TextView owner_name = (TextView) findViewById(R.id.owner_name);
        final TextView card_info = (TextView) findViewById(R.id.card_info);

        //19.3.17
//        final TextView sales = (TextView) findViewById(R.id.sales);
        final TextView time = (TextView) findViewById(R.id.time);

        card_name.setText(card.getCard_name());
        org_price.setText(Double.toString(card.getOrg_price()));
        now_price.setText(Double.toString(card.getPrice()));
        owner_name.setText(card.getUser_name());
        card_info.setText(card.getCard_inf());
        //sales
        time.setText(card.getCard_time() + "发布");



        final ImageView card_pic = (ImageView) findViewById(R.id.card_pic);
        final ImageView owner_pic = (ImageView) findViewById(R.id.owner_pic);
        final ImageView picture1 = (ImageView) findViewById(R.id.picture1);
        final ImageView picture2 = (ImageView) findViewById(R.id.picture2);
        final ImageView picture3 = (ImageView) findViewById(R.id.picture3);

//        BmobQuery<card> query = new BmobQuery<card>();
//        query.getObject(card_id, new QueryListener<card>() {
//
//            @Override
//            public void done(final card c, BmobException e) {
//                if (e == null) {
//                    Log.e("tag", "卡搜索成功");
//
//                    BmobQuery<MyUser> query = new BmobQuery<MyUser>();
//                    query.getObject(c.getUser_id(), new QueryListener<MyUser>() {
//
//                        @Override
//                        public void done(final MyUser myUser, BmobException e) {
//                            if (e == null) {
//                                Log.e("tag", "人搜索成功");
//
//                                BmobQuery <picture> query = new BmobQuery<>();
//                                query.addWhereEqualTo("card_id", card_id);
//                                //query.order("")
//                                query.findObjects(new FindListener<picture>() {
//                                    @Override
//                                    public void done(List<picture> list, BmobException e) {
//                                        if (e == null) {
//
//                                            Log.e("tag", "图搜索成功");
//                                            for (picture p : list) {
//
//                                                Latitude = c.getLatitude();//纬度
//                                                Longitude = c.getLongitude();//经度
//                                                card_name.setText(c.getName());
//                                                org_price.setText(Double.toString(c.getOrg_price()));
//                                                now_price.setText(Double.toString(c.getNow_price()));
//                                                owner_name.setText(myUser.getName());
//                                                card_info.setText(c.getInf());
//                                                //sales
//                                                time.setText(c.getTime() + "发布");
//
//                                                Glide.with(getApplicationContext()).load(myUser.getHead_pic().getUrl().toString()).transform(new GlideCircleTransform(getApplicationContext())).into(owner_pic);
//                                                if (p.getPic().getFilename().equals("tempCompress0.jpg")) {
//                                                    Glide.with(getApplicationContext()).load(p.getPic().getUrl().toString()).transform(new GlideRoundTransform(getApplicationContext())).into(card_pic);
//                                                    Glide.with(getApplicationContext()).load(p.getPic().getUrl().toString()).transform(new GlideRoundTransform(getApplicationContext())).into(picture1);
//                                                    picture1.setVisibility(View.VISIBLE);
//                                                }
//
//                                                if (p.getPic().getFilename().equals("tempCompress1.jpg")) {
//                                                    Glide.with(getApplicationContext()).load(p.getPic().getUrl().toString()).transform(new GlideRoundTransform(getApplicationContext())).into(picture2);
//                                                    picture2.setVisibility(View.VISIBLE);
//                                                }
//
//                                                if (p.getPic().getFilename().equals("tempCompress2.jpg")) {
//                                                    Glide.with(getApplicationContext()).load(p.getPic().getUrl().toString()).transform(new GlideRoundTransform(getApplicationContext())).into(picture3);
//                                                    picture3.setVisibility(View.VISIBLE);
//                                                }
//
//                                                LatLng latLng = new LatLng(Latitude,Longitude);
//                                                final Marker marker = aMap.addMarker(new MarkerOptions().position(latLng).title(c.getName()).snippet(c.getInf()));
//                                                //设置标志物是否可见
//                                                marker.setVisible(true);
//                                                //设置标志物是否可以拖拽
//                                                marker.setDraggable(false);
//
//                                            }
//                                        }else {
//                                            Log.e("tag", "图搜索失败");
//                                        }
//                                    }
//                                });
//                            } else {
//                                Log.e("tag", "人搜索失败");
//                            }
//                        }
//                    });
//                }
//                else {
//                    Log.e("tag", "卡搜索失败");
//                }
//            }
//        });


        BmobQuery <picture> query = new BmobQuery<>();
        query.addWhereEqualTo("card_id", card.getCard_id());
        //query.order("")
        query.findObjects(new FindListener<picture>() {
            @Override
            public void done(List<picture> list, BmobException e) {
                if (e == null) {

                    Log.e("tag", "图搜索成功");
                    for (picture p : list) {


                        Glide.with(getApplicationContext()).load(card.getHead_url()).transform(new GlideCircleTransform(getApplicationContext())).into(owner_pic);
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



                    }
                }else {
                    Log.e("tag", "图搜索失败");
                }
            }
        });



    }

//    @Override
//    protected void onRestart() {
//
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();

        localBroadcastManager.unregisterReceiver(localReceiver);
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }


    @Override
    public void onMyLocationChange(Location location) {
        // 定位回调监听
        if(location != null) {
            Log.e("amap", "onMyLocationChange 定位成功， lat: " + location.getLatitude() + " lon: " + location.getLongitude());

            Bundle bundle = location.getExtras();


            //设置缩放级别
            aMap.moveCamera(CameraUpdateFactory.zoomTo(10));


            if (isfirst) {







                String str = location.toString();
                String[] arr = str.split("#");
                String cityCode = arr[3];
                arr = cityCode.split("=");
                cityCode = arr[1].trim();
                Log.e("城市名称", cityCode);
//                Toast.makeText(getApplicationContext(), cityCode, Toast.LENGTH_LONG).show();
//                //距离
//                LatLng latLng1 = new LatLng(25,119.397972);
//                LatLng latLng2 = new LatLng(location.getLatitude(), location.getLongitude());
//                float distance = AMapUtils.calculateLineDistance(latLng1,latLng2);
//                Toast.makeText(getApplicationContext(), "距离："+distance+"m", Toast.LENGTH_LONG).show();
//                Toast.makeText(getApplicationContext(), location.toString(), Toast.LENGTH_LONG).show();
                isfirst = false;
            }
            if(bundle != null) {
                int errorCode = bundle.getInt(MyLocationStyle.ERROR_CODE);
                String errorInfo = bundle.getString(MyLocationStyle.ERROR_INFO);
                // 定位类型，可能为GPS WIFI等，具体可以参考官网的定位SDK介绍
                int locationType = bundle.getInt(MyLocationStyle.LOCATION_TYPE);
                /*
                errorCode
                errorInfo
                locationType
                */
                Log.e("amap", "定位信息， code: " + errorCode + " errorInfo: " + errorInfo + " locationType: " + locationType );
            } else {
                Log.e("amap", "定位信息， bundle is null ");

            }

        } else {
            Log.e("amap", "定位失败");
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
