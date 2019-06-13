package com.example.wyy.sharecard;


import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.LocationSource;
import com.githang.statusbar.StatusBarCompat;

import net.bither.util.NativeUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import c.b.BP;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobBatch;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BatchResult;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadBatchListener;
import cn.bmob.v3.listener.UploadFileListener;

import static com.tencent.open.utils.Global.getContext;


public class ShareCardActivity extends CheckPermissionsActivity implements CameraCore.CameraResult, LocationSource, AMapLocationListener {
    private MyUser userInfo;

    private final card p2 = new card();

    //声明AMapLocationClient类对象，定位发起端
    private AMapLocationClient mLocationClient = null;
    //声明mLocationOption对象，定位参数
    public AMapLocationClientOption mLocationOption = null;
    //声明mListener对象，定位监听器
    private LocationSource.OnLocationChangedListener mListener = null;
    //标识，用于判断是否只显示一次定位信息和用户重新定位
    private boolean isFirstLoc = true;

    private String Choice;  //美食窝,娱乐窝,大学窝,美发窝,美容窝
    private int ChoiceChecked=0;
    private TextView cardHouse;

    private Integer sort;   //分类
    private String time;    //发卡时间
    private String address;  //地址
    private Double Latitude = 0.0;   //纬度
    private Double Longitude = 0.0;  //经度

    private String card_id;     //用来存卡的id


    private boolean[] ImageViewState={false,false,false};
    private int imagetotal=0;

    private List<BmobObject> pic = new ArrayList<BmobObject>();
    private String[] path = new String[3];  //暂时存储
    //private String[] paths = new String[3];  //最终存储

    private CheckBox checkBox1;
    private CheckBox checkBox2;
    private CheckBox checkBox3;

    private ImageView picture;

    private CameraProxy cameraProxy;
    /** SD卡根目录 */
    private final String externalStorageDirectory = Environment.getExternalStorageDirectory().getPath()+"/picture/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_card);
        StatusBarCompat.setStatusBarColor(this, Color.parseColor("#FFFFFF"));
        final BmobUser bmobUser = BmobUser.getCurrentUser();
        if (bmobUser != null) {
            //已登录
            Toast.makeText(getApplicationContext(),"已登录",Toast.LENGTH_SHORT).show();
            userInfo = BmobUser.getCurrentUser(MyUser.class);
        } else {
            //未登录,转到登录
            Toast.makeText(getApplicationContext(),"请先登录",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplication(), LoginActivity.class);
            startActivity(intent);
        }

        //位置
        //开始定位
        location();

        checkBox1=(CheckBox) findViewById(R.id.checkbox_elec);
        checkBox2=(CheckBox) findViewById(R.id.checkbox_vip);
        checkBox3=(CheckBox) findViewById(R.id.checkbox_discount);

        cardHouse=(TextView) findViewById(R.id.cardHouse);




        //关闭页面
        TextView btn_close=(TextView) findViewById(R.id.btn_close);
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //选择卡窝
        LinearLayout chooseCardHouse=(LinearLayout) findViewById(R.id.choose_house);
        chooseCardHouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShareCardActivity.this,ChooseCardHouseActivity.class);
                startActivityForResult(intent,3);
            }
        });

        //确定发布
        TextView btn_submit=(TextView) findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText card_name_edit = (EditText) findViewById(R.id.card_name);
                if(card_name_edit.getText().toString().isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"卡券名称为空，请重新输入",Toast.LENGTH_SHORT).show();
                    return;
                }
                String card_name = card_name_edit.getText().toString();

                EditText card_info_edit = (EditText) findViewById(R.id.card_info);
                if(card_info_edit.getText().toString().isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"卡券描述为空，请重新输入",Toast.LENGTH_SHORT).show();
                    return;
                }
                String card_info = card_info_edit.getText().toString();

                EditText org_price_edit = (EditText) findViewById(R.id.org_price);
                if(org_price_edit.getText().toString().isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"原价为空，请重新输入",Toast.LENGTH_SHORT).show();
                    return;
                }
                Double org_price=Double.valueOf(org_price_edit.getText().toString());

                EditText now_price_edit = (EditText) findViewById(R.id.now_price);
                if(now_price_edit.getText().toString().isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"出价为空，请重新输入",Toast.LENGTH_SHORT).show();
                    return;
                }
                Double now_price=Double.valueOf(now_price_edit.getText().toString());





                if(checkBox1.isChecked()) {
                    Toast.makeText(getApplicationContext(), "选择电子卡", Toast.LENGTH_SHORT).show();
                    sort = 1;
                }
                else if(checkBox2.isChecked()) {
                    Toast.makeText(getApplicationContext(), "选择会员卡", Toast.LENGTH_SHORT).show();
                    sort = 2;
                }else if(checkBox3.isChecked()) {
                    Toast.makeText(getApplicationContext(), "选择优惠券", Toast.LENGTH_SHORT).show();
                    sort = 3;
                }else {
                    Toast.makeText(getApplicationContext(), "请选择卡窝", Toast.LENGTH_SHORT).show();
                    return;
                }

                //存入对象中准备上传至数据库
                p2.setUser_id(userInfo.getObjectId());
                p2.setCard_house(cardHouse.getText().toString());
                p2.setInf(card_info);
                p2.setName(card_name);
                p2.setNow_price(now_price);
                p2.setOrg_price(org_price);
                p2.setSort(sort);
                p2.setAddress(address);
                p2.setTime(time);
                p2.setLatitude(Latitude);
                p2.setLongitude(Longitude);
                p2.setState(1);

                //存入数据库
                p2.save(new SaveListener<String>() {

                    @Override
                    public void done(String objectId, BmobException e) {
//                        Toast.makeText(getApplicationContext(),"1",Toast.LENGTH_SHORT).show();
//                        Toast.makeText(getApplicationContext(),"2",Toast.LENGTH_SHORT).show();
                        if(e==null){
                            card_id = objectId;
                            finish();
//                            Intent intent = new Intent(AddProductActivity.this, HomeActivity.class);
//                            startActivity(intent);
                        }else{
                            Toast.makeText(getApplicationContext(),"创建数据失败：" + e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                int n = 0;
                String[] paths = new String[imagetotal];  //最终存储
                for (int i = 0; i < 3; i++) {
                    if (ImageViewState[i]) {
                        paths[n] = path[i];
                        Log.e("地址", paths[n]);
                        n++;

                    }
                }

                BmobFile.uploadBatch(paths, new UploadBatchListener() {

                    @Override
                    public void onSuccess(List<BmobFile> files,List<String> urls) {
                        //1、files-上传完成后的BmobFile集合，是为了方便大家对其上传后的数据进行操作，例如你可以将该文件保存到表中
                        //2、urls-上传文件的完整url地址
                        if(urls.size() == imagetotal){//如果数量相等，则代表文件全部上传完成
                            //do something
                            for (BmobFile bmobFile : files) {
                                picture p = new picture();
                                p.setPic(bmobFile);
                                p.setCard_id(card_id);
                                pic.add(p);
                            }

                            new BmobBatch().insertBatch(pic).doBatch(new QueryListListener<BatchResult>() {
                                @Override
                                public void done(List<BatchResult> o, BmobException e) {
                                    if(e==null){
                                        for(int i=0;i<o.size();i++){
                                            BatchResult result = o.get(i);
                                            BmobException ex =result.getError();
                                            if(ex==null){
                                                Log.e("AmapError", "location Error, ErrCode:" );
                                                Log.e("第"+i+"个数据批量添加成功：", result.getCreatedAt() + ","+result.getObjectId() + ","+result.getUpdatedAt());
                                            }else{
                                                Log.e("第"+i+"个数据批量添加失败：", ex.getMessage()+","+ex.getErrorCode());
                                            }
                                        }
                                    }else{
                                        Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                                    }
                                }
                            });


                        }


                    }

                    @Override
                    public void onError(int statuscode, String errormsg) {
                        Log.e("错误", "错误码"+statuscode +",错误描述："+errormsg);
                    }

                    @Override
                    public void onProgress(int curIndex, int curPercent, int total,int totalPercent) {
                        //1、curIndex--表示当前第几个文件正在上传
                        //2、curPercent--表示当前上传文件的进度值（百分比）
                        //3、total--表示总的上传文件数
                        //4、totalPercent--表示总的上传进度（百分比）
                    }
                });



            }
        });

        //添加图片
        FrameLayout frameLayout=(FrameLayout) findViewById(R.id.addIamge);
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imagetotal==3){
                    Toast.makeText(getApplicationContext(),"最多添加三张图片",Toast.LENGTH_SHORT).show();
                    return;
                }
                //判断是否有SD卡读写权限,没有则申请
                if (ContextCompat.checkSelfPermission(ShareCardActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ShareCardActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                }
                //打开相册
                else {

                    //压缩后保存临时文件目录
                    File tempFile = new File(externalStorageDirectory);
                    if(!tempFile.exists()){
                        tempFile.mkdirs();
                    }
                    cameraProxy = new CameraProxy(ShareCardActivity.this,ShareCardActivity.this);
                    cameraProxy.getPhoto2Album();
//                   openAlbum();
                }
            }
        });

    }

    //选择卡窝返回值
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        switch (requestCode){
            case 3:
                if(resultCode==RESULT_OK){
                    Choice=data.getStringExtra("Choice");
                    cardHouse.setText(Choice);
                    ChoiceChecked=1;
                }
                if(resultCode==RESULT_CANCELED);

                break;
            case 1002:
                cameraProxy.onResult(requestCode, resultCode, data);
                break;
        }
    }


    //删除图片
    public void close(View view){
        switch (view.getId()){
            case R.id.close1:
                FrameLayout frameLayout1=(FrameLayout) findViewById(R.id.part1);
                frameLayout1.setVisibility(View.GONE);

                ImageViewState[0]=false;
                imagetotal--;
                break;
            case R.id.close2:
                FrameLayout frameLayout2=(FrameLayout) findViewById(R.id.part2);
                frameLayout2.setVisibility(View.GONE);
                ImageViewState[1]=false;
                imagetotal--;
                break;
            case R.id.close3:
                FrameLayout frameLayout3=(FrameLayout) findViewById(R.id.part3);
                frameLayout3.setVisibility(View.GONE);
                ImageViewState[2]=false;
                imagetotal--;
                break;
        }
    }

    //checkbox事件
    public void  checked(View view){
        switch (view.getId()){
            case R.id.checkbox_elec:
                checkBox2.setChecked(false);
                checkBox3.setChecked(false);
                break;
            case R.id.checkbox_vip:
                checkBox1.setChecked(false);
                checkBox3.setChecked(false);
                break;
            case R.id.checkbox_discount:
                checkBox1.setChecked(false);
                checkBox2.setChecked(false);
                break;
        }
    }

    //拍照选图片成功回调
    @Override
    public void onSuccess(final String filePath) {
        final File file = new File(filePath);
        if (file.exists()) {
            new Thread(){
                public void run() {
                    final int code=requestImageView();

                    final File file = new File(externalStorageDirectory+"/tempCompress" + code + ".jpg");
                    NativeUtil.compressBitmap(filePath,file.getPath());
                    ShareCardActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            FrameLayout frameLayout;
                            if (code==0){
                                frameLayout=(FrameLayout) findViewById(R.id.part1);
                                frameLayout.setVisibility(View.VISIBLE);
                                picture=(ImageView) findViewById(R.id.picture1);
                            }
                            else if (code==1){
                                frameLayout=(FrameLayout) findViewById(R.id.part2);
                                frameLayout.setVisibility(View.VISIBLE);
                                picture=(ImageView)findViewById(R.id.picture2);
                            }
                            else if(code==2){
                                frameLayout=(FrameLayout) findViewById(R.id.part3);
                                frameLayout.setVisibility(View.VISIBLE);
                                picture=(ImageView) findViewById(R.id.picture3);
                            }
                            picture.setImageBitmap(BitmapFactory.decodeFile(file.getPath()));
                            ImageViewState[code]=true;
                            path[code] = file.getPath();



                            imagetotal++;
                        }
                    });
                };
            }.start();
        }
    }

    //拍照选图片失败回调
    @Override
    public void onFail(String message) {
        Toast.makeText(getApplicationContext(), "选择照片失败",Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        cameraProxy.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
//        cameraProxy.onSaveInstanceState(outState);
    }


    //请求ImageView控件 0为第一张图片,1为第二个图片,2为第三个图片
    private int requestImageView(){
        int code=0;
        for(;code<3;code++){
            if(ImageViewState[code]==false)
                break;
        }
        return code;
    }

    private void location() {
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
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

                time = df.format(date);//定位时间

                aMapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                aMapLocation.getCountry();//国家信息
                aMapLocation.getProvince();//省信息
                aMapLocation.getCity();//城市信息
                aMapLocation.getDistrict();//城区信息
                aMapLocation.getStreet();//街道信息
                address = aMapLocation.getProvince() + aMapLocation.getCity() + aMapLocation.getDistrict();
                TextView location=(TextView) findViewById(R.id.location);
                location.setText(address);
                aMapLocation.getStreetNum();//街道门牌号信息
                aMapLocation.getCityCode();//城市编码
                aMapLocation.getAdCode();//地区编码

                // 如果不设置标志位，此时再拖动地图时，它会不断将地图移动到当前的位置
//                if (isFirstLoc) {
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
                Toast.makeText(getApplicationContext(), buffer.toString(), Toast.LENGTH_LONG).show();
//                    isFirstLoc = false;
//                }


            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
                Toast.makeText(getContext(), "定位失败", Toast.LENGTH_LONG).show();
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


}
