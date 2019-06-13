package com.example.wyy.sharecard;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;

public class OutActivity extends AppCompatActivity {

    private List<outItem_listview> outList=new ArrayList<>();;
    private outItem_adapter adapter;
    private ListView listView;

    private final BmobUser bmobUser = BmobUser.getCurrentUser();
    private MyUser userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_out);

        if (bmobUser == null) {
            //未登录
            Toast.makeText(getApplicationContext(), "未登录，请先登录", Toast.LENGTH_SHORT).show();
            Intent intent2 = new Intent(getApplication(), LoginActivity.class);
            startActivity(intent2);
        }
        else {
            userInfo = BmobUser.getCurrentUser(MyUser.class);
            //Toast.makeText(getApplicationContext(), "已登录", Toast.LENGTH_SHORT).show();
        }

        // back 监听
        Button back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("Resume", "重启已约出");
        //在activity执行onResume时执行，重新初始化
        adapter=new outItem_adapter(getApplicationContext(),R.layout.out_item_layout,outList);
        listView=(ListView) findViewById(R.id.out_list_view);

        initCardList();
        listView.setAdapter(adapter);
        setListViewHeightBasedOnChildren(listView);
        adapter.notifyDataSetChanged();
    }

    private void initCardList() {
        outList.clear();

        BmobQuery<Order> query = new BmobQuery<Order>();
        query.addWhereContains("owner_id",userInfo.getObjectId());
        query.findObjects(new FindListener<Order>() {
            @Override
            public void done(List<Order> list, BmobException e) {
                if (e == null) {
                    for (final Order o : list) {
                        //Log.e("tag", "查到已约出订单: " + o.getTotal() + "..." + o.getCreatedAt());
                        BmobQuery<card> query2 = new BmobQuery<card>();
                        query2.getObject(o.getCard_id(), new QueryListener<card>() {

                            @Override
                            public void done(card card, BmobException e) {
                                if(e == null) {
                                    Log.e("tag", "查到已约出: " + card.getName() + o.getTel());
                                    outItem_listview temp = new outItem_listview(card.getName() + "(" + o.getTotal() + ")", o.getReceiver_name(), o.getCreatedAt(), o.getTel());
                                    outList.add(temp);
                                    setListViewHeightBasedOnChildren(listView);
                                    adapter.notifyDataSetChanged();
                                }
                            }


                        });

                    }
                }
                else{

                }
            }
        });

    }


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
