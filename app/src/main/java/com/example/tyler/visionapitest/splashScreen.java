package com.example.tyler.visionapitest;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class splashScreen extends AppCompatActivity {


    private final Handler mHideHandler = new Handler();
    private View mContentView;

    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.iv_veaLogo);
        hide();
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                skip();
            }
        });


        //making the splash screen
        Thread xyz = new Thread() {
            public void run() {
                try {
                    sleep(4000);  // last 4 seconds
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    // moves to home screen after three seconds
                    Intent intent = new Intent(splashScreen.this, loginScreen.class);
                    startActivity(intent);
                    finish();
                }
            }

        };
        //starts the thread
        xyz.start();

    }


    // SKIP FOR TESTING
    private void skip() {
        Intent intent = new Intent(splashScreen.this, loginScreen.class);
        startActivity(intent);
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
    }


}
