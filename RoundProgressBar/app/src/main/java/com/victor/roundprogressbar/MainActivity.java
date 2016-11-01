package com.victor.roundprogressbar;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.victor.myprogressbar.RoundProgressBar;


public class MainActivity extends AppCompatActivity {

    private RoundProgressBar progressBar;
    private int progress = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = (RoundProgressBar) findViewById(R.id.progress);
        handler.sendEmptyMessageDelayed(1, 1000);

        findViewById(R.id.reset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progress = 1;
                progressBar.clearProgress();
                handler.sendEmptyMessageDelayed(1, 1000);
            }
        });
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            progressBar.updateProgress(progress);
            if (progress < 100) {
                progress = progress * 2;
                handler.sendEmptyMessageDelayed(1, 1000);
            }
        }
    };
}
