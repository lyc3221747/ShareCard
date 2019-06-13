package com.example.wyy.sharecard;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.githang.statusbar.StatusBarCompat;

public class EditOrderActivity extends CheckPermissionsActivity {
    private String data;
    private String Choice;
    private Intent intent;
    private Intent intent1=new Intent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_order);
        StatusBarCompat.setStatusBarColor(this, Color.parseColor("#FFFFFF"));
        intent =getIntent();
        data=intent.getStringExtra("data");
        Choice=intent.getStringExtra("choice");

        TextView title=(TextView) findViewById(R.id.title);
        title.setText(Choice);

        final EditText edit=(EditText) findViewById(R.id.edit);
        edit.setText(data);


        //保存修改内容
        TextView save=(TextView) findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data=edit.getText().toString();
                intent1.putExtra("data",data);
                setResult(RESULT_OK,intent1);
                finish();
            }
        });


        //取消修改
        TextView back=(TextView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED,intent);
                finish();
            }
        });



    }
}
