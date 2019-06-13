package com.example.wyy.sharecard;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.model.LatLng;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.githang.statusbar.StatusBarCompat;
import com.yyydjk.library.BannerLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;

import static android.R.attr.data;
import static android.app.Activity.RESULT_OK;
import static cn.bmob.v3.Bmob.getApplicationContext;

/**
 * Created by wyy on 17-7-5.
 */

public class FirstFragment extends Fragment implements LocationSource, AMapLocationListener {
    private  List<cardItem_listview> cardList=new ArrayList<>();
    private cardItem_adapter adapter;
    private ListView listView;
    private View view;
    private final BmobUser bmobUser = BmobUser.getCurrentUser();
    private MyUser userInfo;

    private int n;

    //声明AMapLocationClient类对象，定位发起端
    private AMapLocationClient mLocationClient = null;
    //声明mLocationOption对象，定位参数
    public AMapLocationClientOption mLocationOption = null;
    //声明mListener对象，定位监听器
    private LocationSource.OnLocationChangedListener mListener = null;
    //标识，用于判断是否只显示一次定位信息和用户重新定位
    private boolean isFirstLoc = true;

    private double Latitude;//纬度
    private double Longitude;//经度



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view=inflater.inflate(R.layout.fragment_first,container,false);
        StatusBarCompat.setStatusBarColor(getActivity(), Color.parseColor("#fedb14"));

        Log.e("msg", "准备初始化");


        if (bmobUser == null) {
            //未登录

        }
        else {
            userInfo = BmobUser.getCurrentUser(MyUser.class);
            Toast.makeText(getContext(), "已登录", Toast.LENGTH_SHORT).show();
        }



        //位置
        //开始定位
        location();


/*
        //btn_elec
        Button btn_elec=(Button) view.findViewById(R.id.index_btn_elec);
        btn_elec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),elec_list.class);
                intent.putExtra("Latitude", Latitude);
                intent.putExtra("Longitude", Longitude);
                startActivity(intent);
//                Toast.makeText(getApplicationContext(),"暂未开放此功能",Toast.LENGTH_SHORT).show();
            }
        });

        */

        //btn_vip
        Button btn_vip=(Button) view.findViewById(R.id.index_btn_vip);
        btn_vip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),List_Vip.class);
                intent.putExtra("Latitude", Latitude);
                intent.putExtra("Longitude", Longitude);
                startActivity(intent);
//                Toast.makeText(getApplicationContext(),"暂未开通此功能",Toast.LENGTH_SHORT).show();
            }
        });

        //btn_discount
        Button btn_discount=(Button) view.findViewById(R.id.index_btn_discount);
        btn_discount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),List_Discount.class);
                intent.putExtra("Latitude", Latitude);
                intent.putExtra("Longitude", Longitude);
                startActivity(intent);
            }
        });


        //轮播图
        BannerLayout bannerLayout = (BannerLayout) view.findViewById(R.id.banner);
        final List<String> urls = new ArrayList<>();
        urls.add("http://s1.dgtle.com/portal/201707/24/175235scrcdi4ted4icgr9.jpg?imageView2/2/w/930");
        urls.add("http://s1.dgtle.com/portal/201707/27/150454n0oa03bogyemgego.jpg?imageView2/2/w/930");
        urls.add("http://s1.dgtle.com/portal/201707/27/114304zdmilzskqus9quvd.png?imageView2/2/w/930");
        urls.add("http://s1.dgtle.com/portal/201705/02/100521fev4rikqokvrozoh.jpg?imageView2/2/w/930");
        //urls.add("http://p0.meituan.net/460.280/deal/5b2b4bc4ecca38f2c3c217dabe95014360360.jpg@460w_280h_1e_1c");
        //urls.add("http://p1.meituan.net/dpdeal/c103f03a9e2b24ae1b00f7dac3dc70c0963357.jpg@460w_280h_1e_1c");
        //urls.add("http://p0.meituan.net/hotel/6939cd10be90902c3438f2021755efd1237244.jpg@460w_280h_1e_1c");
        //urls.add("http://p0.meituan.net/dpdeal/719efe662b52d307633dbf6a95a3069066775.jpg@460w_280h_1e_1c");
        //urls.add("http://p1.meituan.net/zeus/b1efba736207c9656eba297f5f5851b184672.jpg@460w_280h_1e_1c");
        //urls.add("http://p0.meituan.net/dpdeal/d9f6c37d6c7a3b026f869af95e67636c49280.jpg@460w_280h_1e_1c");
        bannerLayout.setImageLoader(new GlideImageLoader());
        bannerLayout.setViewUrls(urls);


        //city_select监听
        TextView location = (TextView) view.findViewById(R.id.location);
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),CityActivity.class);
                startActivityForResult(intent,1);
            }
        });


        //search_edit 监听
        EditText search_edit = (EditText) view.findViewById(R.id.search_edit);
        search_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),SearchActivity.class);
                intent.putExtra("Latitude", Latitude);
                intent.putExtra("Longitude", Longitude);
                startActivity(intent);
            }
        });

        //发卡监听
        TextView shareCard=(TextView) view.findViewById(R.id.shareCard);
        shareCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),ShareCardActivity.class);
                startActivity(intent);
            }
        });


        //Listview------------------

//        adapter=new cardItem_adapter(getContext(),R.layout.may_like_item_layout,cardList);
//        listView=(ListView) view.findViewById(R.id.maylike_listView);
//        initCardList();
//        listView.setAdapter(adapter);
//        setListViewHeightBasedOnChildren(listView);
//        TextView textview=(TextView) view.findViewById(R.id.location);
//        textview.setFocusable(true);
//        textview.setFocusableInTouchMode(true);
//        textview.requestFocus();
//        adapter.notifyDataSetChanged();


        //轮播图添加监听事件------------------------------
        bannerLayout.setOnBannerItemClickListener(new BannerLayout.OnBannerItemClickListener() {
            @Override
            public void onItemClick(int position) {
                switch (String.valueOf(position)) {
                    case "0":
                        String data = "http://www.dgtle.com/article-18836-1.html";
                        Intent intent = new Intent(getActivity(),WebActivity.class);
                        intent.putExtra("extra_data",data);
                        startActivity(intent);
                        break;
                    case "1":
                        data = "http://www.dgtle.com/article-18882-1.html";
                        intent = new Intent(getActivity(),WebActivity.class);
                        intent.putExtra("extra_data",data);
                        startActivity(intent);
                        break;
                    case "2":
                        data = "http://www.dgtle.com/article-18878-1.html";
                        intent = new Intent(getActivity(),WebActivity.class);
                        intent.putExtra("extra_data",data);
                        startActivity(intent);
                        break;
                    case "3":
                        data = "http://www.dgtle.com/article-17940-1.html";
                        intent = new Intent(getActivity(),WebActivity.class);
                        intent.putExtra("extra_data",data);
                        startActivity(intent);
                        break;
                    default:
                        break;
                }
            }
        });
        return view;
    }

    //select_city by nfg
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    String returnedData = data.getStringExtra("data_return");
                    TextView location = (TextView) view.findViewById(R.id.location);
                    location.setText(returnedData);
                }
                break;
            default:
        }
    }

    //初始化List------------------------------------------
    private void  initCardList(){

        Log.e("msg", "初始化");
        cardList.clear();
        BmobQuery<card> query = new BmobQuery<card>();
        query.addWhereContains("name","推荐商品");
       // query.order("-sales");
        query.setLimit(5);
        query.findObjects(new FindListener<card>() {
            @Override
            public void done(List<card> list, BmobException e) {
                n = 0;
                if (e == null) {
                    Log.e("tag", "卡搜索成功");
                    for (final card c : list) {
                        //商品状态
                        if (c.getState() == 0)
                            continue;
                        if (bmobUser != null) {
                            if (c.getUser_id().equals(userInfo.getObjectId())) {
                                continue;
                            }
                        }
                        if (n++ > 5)
                            break;

                        BmobQuery<MyUser> query = new BmobQuery<MyUser>();
                        query.getObject(c.getUser_id(), new QueryListener<MyUser>() {

                            @Override
                            public void done(final MyUser myUser, BmobException e) {
                                if(e==null){
                                    Log.e("tag", "人搜索成功");
                                    BmobQuery <picture> query = new BmobQuery<>();
                                    query.addWhereEqualTo("card_id", c.getObjectId());
                                    //query.order("")
                                    query.findObjects(new FindListener<picture>() {
                                        @Override
                                        public void done(List<picture> list, BmobException e) {
                                            if (e == null) {
                                                Log.e("tag", "图搜索成功");
                                                for (picture p : list) {
                                                    if (p.getPic().getFilename().equals("tempCompress0.jpg" )) {
                                                        //距离
                                                        double latitude = c.getLatitude();
                                                        double longitude = c.getLongitude();
                                                        LatLng latLng1 = new LatLng(latitude ,longitude);
                                                        LatLng latLng2 = new LatLng(Latitude, Longitude);
                                                        double distance = AMapUtils.calculateLineDistance(latLng1,latLng2) * 0.001;  //km

                                                        cardItem_listview temp = new cardItem_listview(myUser.getHead_pic().getUrl().toString(), myUser.getName(), 11, distance, p.getPic().getUrl().toString(), c.getNow_price(), c.getName(), "110", c.getObjectId());
                                                        temp.setLatitude(latitude);
                                                        temp.setLongitude(longitude);
                                                        temp.setCard_inf(c.getInf());
                                                        temp.setOrg_price(c.getOrg_price());
                                                        temp.setCard_time(c.getTime());
                                                        temp.setUser_id(myUser.getObjectId());
                                                        temp.setCard_sort(c.getSort());
                                                        cardList.add(temp);
                                                        Log.e("tag", "搜索成功");

                                                        setListViewHeightBasedOnChildren(listView);
                                                        TextView textview=(TextView) view.findViewById(R.id.location);
                                                        textview.setFocusable(true);
                                                        textview.setFocusableInTouchMode(true);
                                                        textview.requestFocus();
                                                        adapter.notifyDataSetChanged();
                                                    }
                                                }
                                            }   else {
                                                Log.e("bmob","图失败："+e.getMessage()+","+e.getErrorCode());
                                                //Toast.makeText(getContext(), "网络连接失败，请重试", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });


                                }else{
                                    Log.e("bmob","人失败："+e.getMessage()+","+e.getErrorCode());
                                    //Toast.makeText(getContext(), "网络连接失败，请重试", Toast.LENGTH_SHORT).show();
                                }
                            }

                        });




                    }
                }
                else {
                    Log.e("bmob","卡失败："+e.getMessage()+","+e.getErrorCode());
                   //Toast.makeText(getContext(), "网络连接失败，请重试", Toast.LENGTH_SHORT).show();
                }
            }

//            @Override
//            public void done(List<Product> list, BmobException e) {
//
//            }
        });








//        for(int i=0;i<8;i++){
//            String url1="http://img1.imgtn.bdimg.com/it/u=3934882239,2009873357&fm=214&gp=0.jpg";
//            String url2="http://p1.meituan.net/zeus/b1efba736207c9656eba297f5f5851b184672.jpg@460w_280h_1e_1c";
//            cardItem_listview cardItem_listview=new cardItem_listview(url1,"仟仟",10,13.5,url2,6.7,"好利来30元代金券","13022222222","123456");
//            cardList.add(cardItem_listview);
//            setListViewHeightBasedOnChildren(listView);
//            TextView textView=(TextView) view.findViewById(R.id.location);
//            textView.setFocusable(true);
//            textView.setFocusableInTouchMode(true);
//            textView.requestFocus();
//            adapter.notifyDataSetChanged();
//        }
    }

    public void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }

    private void location() {
        //初始化定位
        mLocationClient = new AMapLocationClient(getContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(this);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为Hight_Accuracy高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(true);
        //设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }


    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息
                aMapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见官方定位类型表
                Latitude = aMapLocation.getLatitude();//获取纬度
                Longitude = aMapLocation.getLongitude();//获取经度
                aMapLocation.getAccuracy();//获取精度信息
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(aMapLocation.getTime());
                df.format(date);//定位时间
                aMapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                aMapLocation.getCountry();//国家信息
                aMapLocation.getProvince();//省信息
                aMapLocation.getCity();//城市信息

                TextView location=(TextView) view.findViewById(R.id.location);
                location.setText(aMapLocation.getCity());

                aMapLocation.getDistrict();//城区信息
                aMapLocation.getStreet();//街道信息
                aMapLocation.getStreetNum();//街道门牌号信息
                aMapLocation.getCityCode();//城市编码
                aMapLocation.getAdCode();//地区编码

                // 如果不设置标志位，此时再拖动地图时，它会不断将地图移动到当前的位置
                if (isFirstLoc) {
//                    //设置缩放级别
//                    aMap.moveCamera(CameraUpdateFactory.zoomTo(17));
//                    //将地图移动到定位点
//                    aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude())));
//                    //点击定位按钮 能够将地图的中心移动到定位点
//                    mListener.onLocationChanged(aMapLocation);
                    //添加图钉
                    //  aMap.addMarker(getMarkerOptions(amapLocation));
                    //获取定位信息
                    StringBuffer buffer = new StringBuffer();
                    buffer.append(aMapLocation.getCountry() + ""
                            + aMapLocation.getProvince() + ""
                            + aMapLocation.getCity() + ""
                            + aMapLocation.getProvince() + ""
                            + aMapLocation.getDistrict() + ""
                            + aMapLocation.getStreet() + ""
                            + aMapLocation.getStreetNum());
                  //  Toast.makeText(getContext(), buffer.toString(), Toast.LENGTH_LONG).show();
                    isFirstLoc = false;
                }
            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
        //        Toast.makeText(getContext(), "定位失败", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void activate(LocationSource.OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
    }

    @Override
    public void deactivate() {
        mListener = null;
    }

//    @Override
//    public void onDestroy(){
//        super.onDestroy();
//    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("Resume", "重启");
        //在activity执行onResume时执行，重新初始化
        adapter=new cardItem_adapter(getContext(),R.layout.may_like_item_layout,cardList);
        listView=(ListView) view.findViewById(R.id.maylike_listView);

        initCardList();
        listView.setAdapter(adapter);
        setListViewHeightBasedOnChildren(listView);
        TextView textview=(TextView) view.findViewById(R.id.location);
        textview.setFocusable(true);
        textview.setFocusableInTouchMode(true);
        textview.requestFocus();
        adapter.notifyDataSetChanged();
    }
}
