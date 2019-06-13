package com.example.wyy.sharecard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.resource.bitmap.RecyclableBufferedInputStream;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by sir on 17-7-8.
 */

public class messageItem_adapter extends ArrayAdapter<messageItem>{
    private RequestManager glideRequest;
    private int resourceId;

    public messageItem_adapter(Context context, int resource, List<messageItem> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        messageItem messageItem = getItem(position);

        View view = LayoutInflater.from(getContext()).inflate(resourceId, parent,false);

        ImageView user_image = (ImageView) view.findViewById(R.id.message_item_user_image);
        ImageView card_image = (ImageView) view.findViewById(R.id.message_item_card_image);
        TextView user_name = (TextView) view.findViewById(R.id.message_item_user_name);
        TextView message = (TextView) view.findViewById(R.id.message_item_message);

        glideRequest = Glide.with(getContext());
        glideRequest.load(messageItem.getUser_image_url()).transform(new GlideCircleTransform(getContext())).into(user_image);
        Picasso.with(getContext()).load(messageItem.getCard_image_url()).into(card_image);
        user_name.setText(messageItem.getUser());
        message.setText(messageItem.getMessage());

        return view;
    }

}

