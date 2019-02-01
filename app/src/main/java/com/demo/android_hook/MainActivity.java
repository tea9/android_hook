package com.demo.android_hook;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendAppMsg();
            }
        });
    }

    public void sendAppMsg() {
        Intent intent = new Intent();
        intent.setAction("com.sytpay.alipay.start");
        intent.putExtra("mark","");
        intent.putExtra("money",0.1);
//        intent.putExtra("uuid","");
        sendBroadcast(intent);
    }
}
