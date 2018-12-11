package com.example.oreooo.timming;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    TimingView mView;
    Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mView = (TimingView)findViewById(R.id.view_circle);
        mButton = (Button)findViewById(R.id.btn_startTiming);
        mButton.setText("计时");
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mView.initClockTiming();
            }
        });

    }
}
