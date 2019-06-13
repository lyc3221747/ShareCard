package com.example.wyy.sharecard;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by wyy on 17-7-7.
 */

public class cardhouseItem_adapter extends RecyclerView.Adapter<cardhouseItem_adapter.MyViewHolder>{
    private List<cardhouseItem> cardhouseItemList;
    private Context context;

    public cardhouseItem_adapter(List<cardhouseItem> cardhouseItemList) {
        this.cardhouseItemList=cardhouseItemList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        context=parent.getContext();
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                parent.getContext()).inflate(R.layout.cardhouse_item_layout, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position)
    {
        holder.btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,"第"+Integer.toString(position)+"个",Toast.LENGTH_SHORT).show();
            }
        });
        Picasso.with(context).load(cardhouseItemList.get(position).getImage_url()).into(holder.cardhouse_image);
    }

    @Override
    public int getItemCount()
    {
        return cardhouseItemList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        Button btn_add;
        ImageView cardhouse_image;

        public MyViewHolder(View view)
        {
            super(view);
            btn_add = (Button) view.findViewById(R.id.btn_add);
            cardhouse_image = (ImageView) view.findViewById(R.id.cardHouse_image);
        }
    }
}
