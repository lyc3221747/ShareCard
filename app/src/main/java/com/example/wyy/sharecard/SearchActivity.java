package com.example.wyy.sharecard;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.githang.statusbar.StatusBarCompat;

import java.util.ArrayList;
import java.util.List;


import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;

import static com.tencent.open.utils.Global.getContext;

public class SearchActivity extends AppCompatActivity {
    private List<cardItem_listview> cardList=new ArrayList<>();;
    private cardItem_adapter adapter;
    private ListView listView;

    private final BmobUser bmobUser = BmobUser.getCurrentUser();
    private MyUser userInfo;

    private double Latitude;//纬度
    private double Longitude;//经度

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        StatusBarCompat.setStatusBarColor(this, Color.parseColor("#fedb14"));


        if (bmobUser == null) {
            //未登录

        }
        else {
            userInfo = BmobUser.getCurrentUser(MyUser.class);
            //Toast.makeText(getApplicationContext(), "已登录", Toast.LENGTH_SHORT).show();
        }

        Intent intent = getIntent();
        Latitude = intent.getExtras().getDouble("Latitude");
        Longitude = intent.getExtras().getDouble("Longitude");



        //Listview------------------
        adapter=new cardItem_adapter(getApplicationContext(),R.layout.may_like_item_layout,cardList);
        listView=(ListView) findViewById(R.id.search_list_view);
        //initCardList();

        listView.setAdapter(adapter);
        setListViewHeightBasedOnChildren(listView);
        adapter.notifyDataSetChanged();


        // back 监听
        ImageView back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        //键盘搜索监听
        final EditText search_edit=(EditText) findViewById(R.id.search_edit);
        search_edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH){
                    //隐藏键盘
                    ((InputMethodManager) search_edit.getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(SearchActivity.this.getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                    //进行搜索
                    EditText search=(EditText) findViewById(R.id.search_edit);
                    String search_text=search.getText().toString();
                    if(search_text.isEmpty()){
                        Toast.makeText(getApplicationContext(),"请输入要搜索的商品",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        initResultList();
                    }
                    return true;
                }
                return false;
            }
        });

        // search_textview 监听
        TextView search_textview = (TextView) findViewById(R.id.search_textview);
        search_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText search_edit = (EditText) findViewById(R.id.search_edit);
                String search_text = search_edit.getText().toString();
                if(search_text.isEmpty()){
                    Toast.makeText(getApplicationContext(),"请输入要搜索的商品",Toast.LENGTH_SHORT).show();
                }
                else {
                    initResultList();
                }
            }
        });


    }

    private void initResultList() {
        cardList.clear();
        EditText search=(EditText) findViewById(R.id.search_edit);
        String search_text=search.getText().toString();
        BmobQuery<card> query1 = new BmobQuery<card>();
        query1.addWhereContains("name",search_text);
        // query.order("-sales");
        //query.setLimit(10);
        query1.findObjects(new FindListener<card>() {
            @Override
            public void done(List<card> list, BmobException e) {

                if (e == null) {
                    Log.e("tag", "卡搜索成功");
                    if (list.isEmpty()){
                        Toast.makeText(getApplicationContext(),"暂无此商品",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    for (final card c : list) {
                        Log.e("卡", "1");
                        //商品状态
                        if (c.getState() == 0)
                            continue;
                        if (bmobUser != null) {
                            if (c.getUser_id().equals(userInfo.getObjectId())) {
                                continue;
                            }
                        }


                        BmobQuery<MyUser> query2 = new BmobQuery<MyUser>();
                        query2.getObject(c.getUser_id(), new QueryListener<MyUser>() {

                            @Override
                            public void done(final MyUser myUser, BmobException e) {
                                if(e==null){
                                    Log.e("tag", "人搜索成功");
                                    BmobQuery <picture> query3 = new BmobQuery<>();
                                    query3.addWhereEqualTo("card_id", c.getObjectId());
                                    //query.order("")
                                    query3.findObjects(new FindListener<picture>() {
                                        @Override
                                        public void done(List<picture> list, BmobException e) {
                                            if (e == null) {
                                                Log.e("tag", "图搜索成功");
                                                for (picture p : list) {
                                                    if (p.getPic().getFilename().equals("tempCompress0.jpg" )) {
                                                        //距离

                                                        double latitude = c.getLatitude();
                                                        double longitude = c.getLongitude();
                                                        LatLng latLng1 = new LatLng(latitude ,longitude);
                                                        LatLng latLng2 = new LatLng(Latitude, Longitude);
                                                        double distance = AMapUtils.calculateLineDistance(latLng1,latLng2) * 0.001;  //m

                                                        cardItem_listview temp = new cardItem_listview(myUser.getHead_pic().getUrl().toString(), myUser.getName(), 11, distance, p.getPic().getUrl().toString(), c.getNow_price(), c.getName(), "110", c.getObjectId());
                                                        Log.e("name", "666"+c.getName());
                                                        temp.setLatitude(latitude);
                                                        temp.setLongitude(longitude);
                                                        temp.setCard_inf(c.getInf());
                                                        temp.setOrg_price(c.getOrg_price());
                                                        temp.setCard_time(c.getTime());
                                                        temp.setUser_id(myUser.getObjectId());
                                                        temp.setCard_sort(c.getSort());
                                                        cardList.add(temp);
                                                        Log.e("tag", "搜索成功");

                                                        setListViewHeightBasedOnChildren(listView);
//                                                        TextView textview=(TextView) findViewById(R.id.location);
//                                                        textview.setFocusable(true);
//                                                        textview.setFocusableInTouchMode(true);
//                                                        textview.requestFocus();
                                                        adapter.notifyDataSetChanged();
                                                    }
                                                }
                                            }   else {
                                                Log.e("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                                                //Toast.makeText(getApplicationContext(), "网络连接失败，请重试", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });


                                }else{
                                    Log.e("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                                    //Toast.makeText(getApplicationContext(), "网络连接失败，请重试", Toast.LENGTH_SHORT).show();
                                }
                            }

                        });




                    }
                }
                else {
                    Log.e("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                    //Toast.makeText(getApplicationContext(), "网络连接失败，请重试", Toast.LENGTH_SHORT).show();
                }
            }

//            @Override
//            public void done(List<Product> list, BmobException e) {
//
//            }
        });


//        for(int i=0;i<8;i++){
//            String url1="http://img1.imgtn.bdimg.com/it/u=3934882239,2009873357&fm=214&gp=0.jpg";
//            String url2="http://p1.meituan.net/zeus/b1efba736207c9656eba297f5f5851b184672.jpg@460w_280h_1e_1c";
//            cardItem_listview cardItem_listview=new cardItem_listview(url1,"仟仟",10,13.5,url2,6.7,"好利来30元代金券","13022222222","123456");
//            cardList.add(cardItem_listview);
//            setListViewHeightBasedOnChildren(listView);
////            TextView textView=(TextView) view.findViewById(R.id.location);
////            textView.setFocusable(true);
////            textView.setFocusableInTouchMode(true);
////            textView.requestFocus();
//            adapter.notifyDataSetChanged();
//        }

    }


//    //初始化List------------------------------------------
//    private void  initCardList(){
//
//        EditText search_edit = (EditText) findViewById(R.id.search_edit);
//        String search_text = search_edit.getText().toString();
//
//
//
//
//
//
//
//
//        for(int i=0;i<8;i++){
//            String url1="http://img1.imgtn.bdimg.com/it/u=3934882239,2009873357&fm=214&gp=0.jpg";
//            String url2="http://p1.meituan.net/zeus/b1efba736207c9656eba297f5f5851b184672.jpg@460w_280h_1e_1c";
//            cardItem_listview cardItem_listview=new cardItem_listview(url1,"仟仟",10,13.5,url2,6.7,"好利来30元代金券","13022222222","123456");
//            cardList.add(cardItem_listview);
//
//            adapter.notifyDataSetChanged();
//        }
//    }

    public void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        int tmp =listAdapter.getCount();
        Log.e("TAG", "," + Integer.toString(tmp) );
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
        params.height = totalHeight+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }

}