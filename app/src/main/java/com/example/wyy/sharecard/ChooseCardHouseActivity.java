package com.example.wyy.sharecard;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ChooseCardHouseActivity extends BaseActivity {
    private String Choice;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_card_house);
        intent=new Intent();
        TextView btn_close=(TextView) findViewById(R.id.btn_close);
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED,intent);
                finish();
            }
        });
    }

    public void choice(View view){
        switch (view.getId()){
            case R.id.choice1:
                TextView choice1=(TextView) findViewById(R.id.choice1);
                Choice=choice1.getText().toString();
                intent.putExtra("Choice",Choice);
                setResult(RESULT_OK,intent);
                finish();
                break;
            case R.id.choice2:
                TextView choice2=(TextView) findViewById(R.id.choice2);
                Choice=choice2.getText().toString();
                Choice=choice2.getText().toString();
                intent.putExtra("Choice",Choice);
                setResult(RESULT_OK,intent);
                finish();
                break;
            case R.id.choice3:
                TextView choice3=(TextView) findViewById(R.id.choice3);
                Choice=choice3.getText().toString();
                Choice=choice3.getText().toString();
                intent.putExtra("Choice",Choice);
                setResult(RESULT_OK,intent);
                finish();
                break;
            case R.id.choice4:
                TextView choice4=(TextView) findViewById(R.id.choice4);
                Choice=choice4.getText().toString();
                Choice=choice4.getText().toString();
                intent.putExtra("Choice",Choice);
                setResult(RESULT_OK,intent);
                finish();
                break;
            case R.id.choice5:
                TextView choice5=(TextView) findViewById(R.id.choice5);
                Choice=choice5.getText().toString();
                Choice=choice5.getText().toString();
                intent.putExtra("Choice",Choice);
                setResult(RESULT_OK,intent);
                finish();
                break;
        }
    }
}
