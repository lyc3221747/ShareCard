package com.example.wyy.sharecard;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

import cn.bmob.v3.BmobUser;

import static cn.bmob.v3.Bmob.getApplicationContext;

/**
 * Created by wyy on 17-7-6.
 */

public class cardItem_adapter extends ArrayAdapter<cardItem_listview>  {
    private Context mContext;
    private final BmobUser bmobUser = BmobUser.getCurrentUser();
    private RequestManager glideRequest;
    private int resourceId;
    private cardItem_listview cardItem_listview;

    public cardItem_adapter(Context context,int resource,List<cardItem_listview> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        cardItem_listview = getItem(position);

        View view = LayoutInflater.from(getContext()).inflate(resourceId, parent,false);

        mContext=getContext();

        ImageView head_image = (ImageView) view.findViewById(R.id.head_image);
        TextView user_name =(TextView) view.findViewById(R.id.name);

        //19.3.17
//        TextView number =(TextView) view.findViewById(R.id.number);
        TextView distance =(TextView) view.findViewById(R.id.distance);
        ImageView card_pic = (ImageView) view.findViewById(R.id.card_pic);
        TextView price =(TextView) view.findViewById(R.id.price);
        TextView card_name =(TextView) view.findViewById(R.id.card_name);
        Button contact = (Button) view.findViewById(R.id.contact);
        Button buy = (Button) view.findViewById(R.id.buy);
        glideRequest = Glide.with(getContext());
        glideRequest.load(cardItem_listview.getHead_url()).transform(new GlideCircleTransform(getContext())).into(head_image);
        Glide.with(getContext()).load(cardItem_listview.getCard_pic()).transform(new GlideRoundTransform(getContext(),15)).into(card_pic);
        user_name.setText(cardItem_listview.getUser_name());

        //19.3.17
//        number.setText("刷刷"+ Integer.toString(cardItem_listview.getNumber())+"单");
        DecimalFormat df=new DecimalFormat("#0.00");
        distance.setText(df.format(cardItem_listview.getDistance())+"km");
        price.setText(Double.toString(cardItem_listview.getPrice())+"元");
        card_name.setText(cardItem_listview.getCard_name());

//        Log.e("Id", getItem(position).getCard_id());

        //点击头像跳转个人主界面
        LinearLayout User_page=(LinearLayout) view.findViewById(R.id.User_page);
        User_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent=new Intent(mContext,BlogActivity.class);
                //mContext.startActivity(intent);
                Intent intent=new Intent(getContext(),CardDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("cardItem_listview", getItem(position));
                intent.putExtras(bundle);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.putExtra("card_id",getItem(position).getCard_id());
//                Log.e("tag", Integer.toString(position));
//                Log.e("tag", cardItem_listview.getCard_id());
                mContext.startActivity(intent);
            }
        });

        //点击图片跳转活动
        card_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),CardDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("cardItem_listview", getItem(position));
                intent.putExtras(bundle);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.putExtra("card_id",getItem(position).getCard_id());
//                Log.e("tag", Integer.toString(position));
//                Log.e("tag", cardItem_listview.getCard_id());
                mContext.startActivity(intent);

            }
        });

        //联系卡主
        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
                //Toast.makeText(getContext(),"联系卡主",Toast.LENGTH_SHORT).show();
////                Intent intent=new Intent(getContext(),ConfirmOrderActivity.class);
////                Bundle bundle = new Bundle();
////                bundle.putSerializable("cardItem_listview", getItem(position));
////                intent.putExtras(bundle);
////                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//////                intent.putExtra("card_id",getItem(position).getCard_id());
//////                Log.e("tag", Integer.toString(position));
//////                Log.e("tag", cardItem_listview.getCard_id());
////                mContext.startActivity(intent);
//                Toast.makeText(getApplicationContext(),"暂未开放此功能",Toast.LENGTH_SHORT).show();

                String tel = cardItem_listview.getTele();
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + tel));
                mContext.startActivity(intent);
            }
        });

        //我要约卡
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent=new Intent(getContext(),ConfirmOrderActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("cardItem_listview", getItem(position));
//                intent.putExtras(bundle);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////                intent.putExtra("card_id",getItem(position).getCard_id());
////                Log.e("tag", Integer.toString(position));
////                Log.e("tag", cardItem_listview.getCard_id());
//                mContext.startActivity(intent);




                final BmobUser bmobUser = BmobUser.getCurrentUser();
                if (bmobUser != null) {
                    //已登录

                    MyUser userInfo = BmobUser.getCurrentUser(MyUser.class);
                    if (userInfo.getObjectId().equals(getItem(position).getUser_id())) {
                        Toast.makeText(getApplicationContext(),"不能购买自己的卡券",Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Intent intent1=new Intent(getApplicationContext(),ConfirmOrderActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("cardItem_listview", getItem(position));
                    intent1.putExtras(bundle);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    Intent intent1=new Intent(CardDetailActivity.this,ConfirmOrderActivity.class);
//                    intent1.putExtra("card_id", card.getCard_id());
                    mContext.startActivity(intent1);

                } else {
                    //未登录,转到登录
                    Toast.makeText(getApplicationContext(),"请先登录",Toast.LENGTH_SHORT).show();
                    Intent intent2 = new Intent(getApplicationContext(), LoginActivity.class);
                    mContext.startActivity(intent2);
                }
            }
        });


        return  view;
    }

}
