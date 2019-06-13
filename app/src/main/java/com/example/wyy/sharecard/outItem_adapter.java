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

public class outItem_adapter extends ArrayAdapter<outItem_listview> {
    private Context mContext;
    private final BmobUser bmobUser = BmobUser.getCurrentUser();
    private RequestManager glideRequest;
    private int resourceId;
    private outItem_listview outItem_listview;

    public outItem_adapter(Context context, int resource, List<outItem_listview> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        outItem_listview = getItem(position);

        View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);

        mContext = getContext();

        TextView card_name =(TextView) view.findViewById(R.id.out_item_card_name);
        TextView user_name =(TextView) view.findViewById(R.id.out_item_user_name);
        TextView time =(TextView) view.findViewById(R.id.out_item_card_time);
        TextView num =(TextView) view.findViewById(R.id.out_item_card_num);

        card_name.setText(outItem_listview.getCard_name());
        user_name.setText(outItem_listview.getUser_name());
        time.setText(outItem_listview.getTime());
        num.setText(outItem_listview.getUser_num());








        return view;
    }

}
