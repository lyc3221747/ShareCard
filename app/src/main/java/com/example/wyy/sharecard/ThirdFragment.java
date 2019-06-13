package com.example.wyy.sharecard;


import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.support.v4.app.Fragment;
import android.widget.TextView;
import android.widget.Toast;

import com.githang.statusbar.StatusBarCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sir on 17-7-8.
 */

public class ThirdFragment extends Fragment {
    private List<messageItem> messageItemList = new ArrayList<>();
    private ListView listView;
    private messageItem_adapter messageItem_adapter;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_third, container, false);
        StatusBarCompat.setStatusBarColor(getActivity(), Color.parseColor("#FFFFFF"));
        messageItem_adapter = new messageItem_adapter(getContext(),
                R.layout.message_item_layout, messageItemList);
        listView = (ListView) view.findViewById(R.id.message_listView);
        initMessage();
        listView.setAdapter(messageItem_adapter);
        setListViewHeightBasedOnChildren(listView);
        TextView textView = (TextView) view.findViewById(R.id.title);
        textView.setFocusable(true);
        textView.setFocusableInTouchMode(true);
        textView.requestFocus();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                messageItem messageItem = messageItemList.get(position);
                Intent intent = new Intent(getContext(),ChatActivity.class);
                startActivity(intent);
                //Toast.makeText(getContext(),Integer.toString(position), Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void initMessage() {
        messageItem m = new messageItem("http://img1.imgtn.bdimg.com/it/u=3934882239,2009873357&fm=214&gp=0.jpg",
                "http://p1.meituan.net/deal/5d9541357d2662793bf939f49bfebe8293662.jpg@260w_154h_1e.webp",
                "阡阡",
                "请问是全场糕点都可以享受优惠吗... | 三天前");

        messageItem n = new messageItem("http://h.hiphotos.baidu.com/zhidao/wh%3D450%2C600/sign=0a2c833f39f33a879e38081ef36c3c0e/a08b87d6277f9e2fd81d1d071c30e924b899f391.jpg",
                "http://p1.meituan.net/deal/abc5db49a7320a0fde069813a64b3e6973301.jpg@260w_154h_1e.webp",
                "陌陌",
                "请问是全场糕点都可以享受优惠吗... | 三天前");
        messageItemList.add(m);
        messageItemList.add(n);
        messageItem_adapter.notifyDataSetChanged();
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
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }
}
