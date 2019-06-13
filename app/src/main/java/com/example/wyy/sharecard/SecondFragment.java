package com.example.wyy.sharecard;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.githang.statusbar.StatusBarCompat;
import com.yyydjk.library.BannerLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wyy on 17-7-5.
 */

public class SecondFragment extends Fragment {

    private List<cardhouseItem> cardhouseItemList = new ArrayList<>();

    private List<house_cardhouseItem> house_cardhouseItemList = new ArrayList<>();
    private house_cardhouseItem_adapter house_cardhouseItem_Adapter;
    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_second, container, false);
        StatusBarCompat.setStatusBarColor(getActivity(), Color.parseColor("#FFFFFF"));
        //轮播图
        BannerLayout bannerLayout = (BannerLayout) view.findViewById(R.id.banner);
        final List<String> urls = new ArrayList<>();
        urls.add("http://p0.meituan.net/460.280/deal/5b2b4bc4ecca38f2c3c217dabe95014360360.jpg@460w_280h_1e_1c");
        urls.add("http://p1.meituan.net/dpdeal/c103f03a9e2b24ae1b00f7dac3dc70c0963357.jpg@460w_280h_1e_1c");
        urls.add("http://p0.meituan.net/hotel/6939cd10be90902c3438f2021755efd1237244.jpg@460w_280h_1e_1c");
        //urls.add("http://p1.meituan.net/zeus/b1efba736207c9656eba297f5f5851b184672.jpg@460w_280h_1e_1c");
        //urls.add("http://p0.meituan.net/dpdeal/d9f6c37d6c7a3b026f869af95e67636c49280.jpg@460w_280h_1e_1c");
        bannerLayout.setImageLoader(new GlideImageLoader());
        bannerLayout.setViewUrls(urls);



        //感兴趣的卡窝

/*

        initRecyclerView();
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        cardhouseItem_adapter adapter = new cardhouseItem_adapter(cardhouseItemList);
        recyclerView.setAdapter(adapter);

*/


        //刷刷卡窝
        //Listview------------------

        house_cardhouseItem_Adapter = new house_cardhouseItem_adapter(getContext(), R.layout.house_cardhouse_item_layout, house_cardhouseItemList);
        listView = (ListView) view.findViewById(R.id.listView);
        inithouse_cardhouseItemList();
        listView.setAdapter(house_cardhouseItem_Adapter);
        setListViewHeightBasedOnChildren(listView);
        TextView textView = (TextView) view.findViewById(R.id.title);
        textView.setFocusable(true);
        textView.setFocusableInTouchMode(true);
        textView.requestFocus();
        //刷刷卡窝监听
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                house_cardhouseItem houseCardhouseItem=house_cardhouseItemList.get(position);
                Toast.makeText(getContext(),"第"+Integer.toString(position)+"个",Toast.LENGTH_SHORT).show();
            }
        });


        return view;
    }

    //初始化刷刷卡窝
    private void inithouse_cardhouseItemList(){
        house_cardhouseItemList.add(new house_cardhouseItem("123",
                "http://www.mbachina.com/uploads/album/201511/11/1447203804163372.jpg",
                "大学窝",1.2,"你的同校伙伴在召唤你哦~"));
        house_cardhouseItemList.add(new house_cardhouseItem("123",
                "http://imgsrc.baidu.com/imgad/pic/item/b2de9c82d158ccbfe3b0901f13d8bc3eb1354182.jpg",
                "美食窝",0.8,"美食窝的小伙伴在召唤你哦~"));
        house_cardhouseItemList.add(new house_cardhouseItem("123",
                "http://pic7.qiyipic.com/common/lego/20140816/0e442e2b5ce3451f8937f26c392b707f.jpg?src=focustat_6_20130410_15",
                "娱乐窝",0.7,"娱乐窝的小伙伴在召唤你哦~"));
        house_cardhouseItemList.add(new house_cardhouseItem("123",
                "http://img.mp.sohu.com/upload/20170519/f4d03fe260e341989a3d3a776f94e355_th.png",
                "美丽窝",0.6,"美丽窝的小伙伴在召唤你哦~"));
    }

    //初始化你可能感兴趣的卡窝
    private void initRecyclerView() {
        for (int i = 0; i < 3; i++) {
            cardhouseItem cardhouseItem = new cardhouseItem("12345",
                    "http://img.mp.itc.cn/upload/20170417/56e3757c3f1d4324a1035750c29acc5d_th.jpeg");
            cardhouseItemList.add(cardhouseItem);
            cardhouseItem = new cardhouseItem("12345",
                    "http://image.cnpp.cn/upload/images/20160509/1462781340_10416_8.jpg");
            cardhouseItemList.add(cardhouseItem);
        }
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