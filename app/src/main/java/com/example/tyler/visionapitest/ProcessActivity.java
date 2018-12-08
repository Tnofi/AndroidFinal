package com.example.tyler.visionapitest;

import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProcessActivity extends AppCompatActivity {

    @BindView(R.id.pb) ProgressBar progressBar;
    @BindView(R.id.txtProgress) TextView progressText;

    int mProgressStatus = 0;
    Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process);
        ButterKnife.bind(this);
        //Messing with handler threads, the loading bar doesn't have an actual purpose other than appearance at the moment
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(mProgressStatus < 100){
                    mProgressStatus++;
                    SystemClock.sleep(50);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(mProgressStatus);
                        }
                    });
                }
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        progressText.setText("Complete!");
                        finish();
                    }
                });
            }
        }).start();
    }
}
