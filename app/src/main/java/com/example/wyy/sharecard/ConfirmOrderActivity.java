package com.example.wyy.sharecard;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.bumptech.glide.Glide;
import com.githang.statusbar.StatusBarCompat;

import java.util.List;

import c.b.BP;
import c.b.PListener;
import c.b.QListener;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

import static com.example.wyy.sharecard.R.id.org_price;

public class ConfirmOrderActivity extends CheckPermissionsActivity {
    private TextView receiver_name;
    private TextView tel;
    private TextView address;
    private TextView SendMethod;
    private TextView zhifu;
    private LinearLayout btn_address;
    private String address_data;

    private int  SendChoice=0;  //0未选择;1同城派送,2面对面
    private String returndata;

    private String payId;   //支付宝订单id

    private cardItem_listview card;

    private String owner_id;
    private String user_id;
    private String card_id;
    private String Card_name;
    private String Owner_name;
    private String receiver;
    private String telephone_number;
    private Integer Method;
    private String Address;
    private Double Total;

    private MyUser userInfo;


    private LocalBroadcastManager localBroadcastManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);
        StatusBarCompat.setStatusBarColor(this, Color.parseColor("#FFFFFF"));



        localBroadcastManager = LocalBroadcastManager.getInstance(this);    //广播获取实例

        final BmobUser bmobUser = BmobUser.getCurrentUser();
        if (bmobUser != null) {
            //已登录
            //Toast.makeText(getApplicationContext(),"已登录",Toast.LENGTH_SHORT).show();
            userInfo = BmobUser.getCurrentUser(MyUser.class);
            user_id = userInfo.getObjectId();
        } else {
            //未登录,转到登录
            Toast.makeText(getApplicationContext(),"请先登录",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplication(), LoginActivity.class);
            startActivity(intent);
        }

        //接收商品列表类
        Intent intent1 = getIntent();
        card = (cardItem_listview) intent1.getSerializableExtra("cardItem_listview");


        init();







        receiver_name=(TextView) findViewById(R.id.receiver_name);
        tel=(TextView) findViewById(R.id.tel);
        address=(TextView) findViewById(R.id.address);
        SendMethod=(TextView)findViewById(R.id.SendMethod);

        TextView back=(TextView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });




        //编辑姓名
        LinearLayout btn_receiver_name=(LinearLayout) findViewById(R.id.btn_receiver_name);
        btn_receiver_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConfirmOrderActivity.this,EditOrderActivity.class);
                intent.putExtra("data",receiver_name.getText().toString());
                intent.putExtra("choice","编辑姓名");
                startActivityForResult(intent,1);
            }
        });

        //编辑联系电话

        tel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConfirmOrderActivity.this,EditOrderActivity.class);
                intent.putExtra("data",tel.getText().toString());
                intent.putExtra("choice","编辑联系电话");
                startActivityForResult(intent,2);
            }
        });

        //编辑收货地址
        btn_address=(LinearLayout) findViewById(R.id.btn_address);
        btn_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConfirmOrderActivity.this,EditOrderActivity.class);
                intent.putExtra("data","");
                intent.putExtra("choice","编辑收货地址");
                startActivityForResult(intent,3);
            }
        });

        //选择配送方式

        LinearLayout btn_SendMethod=(LinearLayout) findViewById(R.id.btn_SendMethod);
        btn_SendMethod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConfirmOrderActivity.this,ChooseSendingMethodActivity.class);
                startActivityForResult(intent,4);
            }
        });

        zhifu = (TextView) findViewById(R.id.zhifu);
        zhifu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                receiver = receiver_name.getText().toString();
                telephone_number = tel.getText().toString();
                Method = SendChoice;
                Address = address_data;

                //判断信息是否输入完整
                if(receiver.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"收货人为空，请重新输入",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(telephone_number.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"联系电话为空，请重新输入",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(Method == 0)
                {
                    Toast.makeText(getApplicationContext(),"未选择配送方式，请选择配送方式",Toast.LENGTH_SHORT).show();
                    return;
                }





                final Order order = new Order();
                order.setUser_id(user_id);
                order.setOwner_id(owner_id);
                order.setCard_id(card_id);
                order.setReceiver_name(receiver);
                order.setAddress_data(Address);
                order.setTel(telephone_number);
                order.setSendMethod(Method);
                order.setTotal(Total);
                // 创建订单
                order.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e == null) {
                            //订单创建成功
                            Log.e("tag", "订单创建成功" + e.getErrorCode() + e.getMessage());









                        /*
                            //支付
                            BP.pay(Card_name, Owner_name + "的订单", Total, true, new PListener() {
                                @Override
                                public void orderId(String s) {
                                    payId = s;

                                    Order order1 = new Order();
                                    order1.setPayId(payId);
                                    order1.update(order.getObjectId(), new UpdateListener() {
                                        @Override
                                        public void done(BmobException e) {
                                            if (e == null) {
//                                    Toast.makeText(getApplicationContext(), "支付宝订单号记录成功", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });


                                }

                                @Override
                                public void succeed() {
//                        Toast.makeText(getApplicationContext(), "查询订单是否支付成功", Toast.LENGTH_SHORT).show();
                                    //查询订单是否成功
                                    BP.query(payId, new QListener() {

                                        @Override
                                        public void succeed(String s) {
//                                Toast.makeText(getApplicationContext(), "进入查询", Toast.LENGTH_SHORT).show();
                                            if (s.equals("SUCCESS")) {
                                                Toast.makeText(getApplicationContext(), "支付成功", Toast.LENGTH_SHORT).show();
                                                //更改订单状态，并添加支付成功时间

//                                                Order order2 = new Order();
//                                                order2.setState(2);
//                                                //Toast.makeText(getApplicationContext(), orderId.get(n)+"第"+n+"个", Toast.LENGTH_SHORT).show();
//                                                order2.update(order.getObjectId(), new UpdateListener() {
//                                                    @Override
//                                                    public void done(BmobException e) {
//                                                        if (e == null) {
////                                                Toast.makeText(getApplicationContext(), "改变订单状态成功", Toast.LENGTH_SHORT).show();
//                                                        }
//                                                        else {
//                                                            Toast.makeText(getApplicationContext(), "改变订单状态失败", Toast.LENGTH_SHORT).show();
//                                                        }
//                                                    }
//                                                });
                                                //更新卡券状态为0
                                                card c = new card();
                                                c.setState(0);

                                                c.update(card_id, new UpdateListener() {
                                                    @Override
                                                    public void done(BmobException e) {
                                                        if (e == null) {
                                                            //Toast.makeText(getApplicationContext(), "改变商品状态成功"+ e.getErrorCode() + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                            //CardDetailActivity.instance.finish();
                                                            //finish();

//                                                Toast.makeText(getApplicationContext(), "改变商品状态成功", Toast.LENGTH_SHORT).show();
                                                        }
                                                        else {
//                                                            Log.e("update", ":" + e.getErrorCode() + e.getMessage());
//                                                            Toast.makeText(getApplicationContext(), "改变商品状态失败"+ e.getErrorCode() + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });

                                                Intent intent1 = new Intent("支付成功");
                                                localBroadcastManager.sendBroadcast(intent1);   //发送本地广播
                                                finish();



                                            }
                                            else if ( s.equals("NOTPAY")) {
                                                Toast.makeText(getApplicationContext(), "支付失败", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void fail(int i, String s) {

                                        }
                                    });

                                }

                                @Override
                                public void fail(int i, String s) {

                                }

                                @Override
                                public void unknow() {

                                }
                            });


*/















                        } else {
                            Log.e("tag","约卡失败：" + "Id:" + card_id + e.getErrorCode() + e.getMessage());
                            Toast.makeText(getApplicationContext(),"约卡失败：" + e.getMessage(),Toast.LENGTH_SHORT).show();
                        }


                    }
                });


                //更新卡券状态为0
                card c = new card();
                c.setState(0);

                c.update(card_id, new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {

                            Log.e("tag", "改变商品状态成功" + e.getErrorCode() + e.getMessage());
                            //CardDetailActivity.instance.finish();
                            //finish();

//                                                Toast.makeText(getApplicationContext(), "改变商品状态成功", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Log.e("update", ":" + e.getErrorCode() + e.getMessage());
//                                                            Toast.makeText(getApplicationContext(), "改变商品状态失败"+ e.getErrorCode() + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                Intent intent1 = new Intent("支付成功");
                Toast.makeText(getApplicationContext(), "支付成功", Toast.LENGTH_SHORT).show();
                localBroadcastManager.sendBroadcast(intent1);   //发送本地广播
                finish();

            }
        });


    }

    private void init() {
        final ImageView card_pic = (ImageView) findViewById(R.id.card_pic);
        final ImageView owner_pic = (ImageView) findViewById(R.id.owner_pic);

        final TextView card_name = (TextView) findViewById(R.id.card_name);
        final TextView card_info = (TextView) findViewById(R.id.card_info);
        final TextView price = (TextView) findViewById(R.id.price);
        final TextView sort = (TextView) findViewById(R.id.sort);
        final TextView owner_name = (TextView) findViewById(R.id.owner_name);
        final TextView owner_tel = (TextView) findViewById(R.id.owner_tel);
        final TextView total = (TextView) findViewById(R.id.total);

        card_id = card.getCard_id();
        owner_id = card.getUser_id();
        Total = card.getPrice();
        Card_name = card.getCard_name();
        Owner_name = card.getUser_name();


        card_name.setText(card.getCard_name());
        price.setText(Double.toString(card.getPrice()));
        total.setText(Double.toString(card.getPrice()));
        owner_name.setText(card.getUser_name());
        card_info.setText(card.getCard_inf());
        owner_tel.setText("联系电话:" + card.getTele());

        switch (card.getCard_sort()) {
            case 1:
                sort.setText("卡券类型:电子券");
                break;
            case 2:
                sort.setText("卡券类型:会员卡");
                break;
            case 3:
                sort.setText("卡券类型:优惠券");
                break;

        }

        Glide.with(getApplicationContext()).load(card.getHead_url()).transform(new GlideCircleTransform(getApplicationContext())).into(owner_pic);
        Glide.with(getApplicationContext()).load(card.getCard_pic()).transform(new GlideRoundTransform(getApplicationContext())).into(card_pic);


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
//                                                owner_id = myUser.getObjectId();
//                                                Total = c.getNow_price();
//                                                Card_name = c.getName();
//                                                Owner_name = myUser.getName();
//
//
//                                                card_name.setText(c.getName());
//                                                price.setText(Double.toString(c.getNow_price()));
//                                                total.setText(Double.toString(c.getNow_price()));
//                                                owner_name.setText(myUser.getName());
//                                                card_info.setText(c.getInf());
//                                                owner_tel.setText("联系电话:" + myUser.getMobilePhoneNumber());
//
//                                                switch (c.getSort()) {
//                                                    case 1:
//                                                        sort.setText("卡券类型:电子券");
//                                                        break;
//                                                    case 2:
//                                                        sort.setText("卡券类型:会员卡");
//                                                        break;
//                                                    case 3:
//                                                        sort.setText("卡券类型:优惠券");
//                                                        break;
//
//                                                }
//
//                                                Glide.with(getApplicationContext()).load(myUser.getHead_pic().getUrl().toString()).transform(new GlideCircleTransform(getApplicationContext())).into(owner_pic);
//                                                if (p.getPic().getFilename().equals("tempCompress0.jpg")) {
//                                                    Glide.with(getApplicationContext()).load(p.getPic().getUrl().toString()).transform(new GlideRoundTransform(getApplicationContext())).into(card_pic);
//                                                }
//
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

    }


    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent intent_return){
        switch (requestCode){
            case 1:
                if(resultCode==RESULT_OK){
                    returndata=intent_return.getStringExtra("data");
                    receiver_name.setText(returndata);
                    break;
                }
                else
                    break;

            case 2:
                if (resultCode==RESULT_OK){
                    returndata=intent_return.getStringExtra("data");
                    tel.setText(returndata);
                    break;
                }
                else
                    break;
            case 3:
                if (resultCode==RESULT_OK){
                    address_data=intent_return.getStringExtra("data");
                    address.setText("收货地址 :"+address_data);
                    break;
                }
                else
                    break;

            case  4:
                if (resultCode==RESULT_OK){
                    returndata=intent_return.getStringExtra("Choice");
                    SendMethod.setText(returndata);
                    if(returndata.equals("同城喵哥派送")) {
                        SendChoice = 1;
                        btn_address.setVisibility(View.VISIBLE);
                    }
                    else {
                        SendChoice = 2;
                        btn_address.setVisibility(View.GONE);
                    }
                    break;
                }
                else if (requestCode==RESULT_CANCELED)
                    break;

        }
    }
}
