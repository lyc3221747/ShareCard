package com.example.wyy.sharecard;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;

import java.io.Serializable;
import java.util.List;

import cn.bmob.v3.BmobUser;

/**
 * Created by z on 2019/3/28.
 */

public class publishItem_adapter extends ArrayAdapter<publishItem_listview> {
    private Context mContext;
    private final BmobUser bmobUser = BmobUser.getCurrentUser();
    private RequestManager glideRequest;
    private int resourceId;
    private publishItem_listview publishItem_listview;

    public publishItem_adapter(Context context, int resource, List<publishItem_listview> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        publishItem_listview = getItem(position);

        View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);

        mContext = getContext();

        TextView card_name =(TextView) view.findViewById(R.id.publish_item_card_name);
        TextView card_state =(TextView) view.findViewById(R.id.publish_item_card_state);

        card_name.setText(publishItem_listview.getCard_name());
        card_state.setText(publishItem_listview.getState());



        //点击跳转
        LinearLayout publish_page=(LinearLayout) view.findViewById(R.id.publish);
        publish_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent=new Intent(mContext,BlogActivity.class);
                //mContext.startActivity(intent);
                Intent intent=new Intent(getContext(),PublishDetailActivity.class);

//                Bundle bundle = new Bundle();
//                bundle.putSerializable("publishItem_listview", (Serializable) getItem(position));
//                intent.putExtras(bundle);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                intent.putExtra("card_id",getItem(position).getCard_id());
                Log.e("tag", Integer.toString(position));
                Log.e("tag", getItem(position).getCard_id());
                mContext.startActivity(intent);
            }
        });




        return view;
    }

}
