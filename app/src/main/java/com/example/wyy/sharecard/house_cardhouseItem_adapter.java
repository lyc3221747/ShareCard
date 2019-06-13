package com.example.wyy.sharecard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import java.util.List;

/**
 * Created by wyy on 17-7-8.
 */

public class house_cardhouseItem_adapter extends ArrayAdapter<house_cardhouseItem> {
    private RequestManager glideRequest;
    private int resourceId;

    public house_cardhouseItem_adapter(Context context, int resource, List<house_cardhouseItem> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        house_cardhouseItem house_CardhouseItem=getItem(position);
        View view= LayoutInflater.from(getContext()).inflate(resourceId,parent,false);

        ImageView imageView=(ImageView) view.findViewById(R.id.house_cardhouse_image);
        TextView cardhouseName=(TextView) view.findViewById(R.id.cardhouse_name);
        TextView cardhouseDistance=(TextView) view.findViewById(R.id.carhouse_distance);
        TextView cardhouseText=(TextView) view.findViewById(R.id.cardhouse_text);
        glideRequest = Glide.with(getContext());
        glideRequest.load(house_CardhouseItem.getUrl()).transform(new GlideRoundTransform(getContext())).into(imageView);
        cardhouseName.setText(house_CardhouseItem.getName());
        cardhouseDistance.setText("距你"+Double.toString(house_CardhouseItem.getDistance())+"km");
        cardhouseText.setText(house_CardhouseItem.getText());
        return view;
    }
}
