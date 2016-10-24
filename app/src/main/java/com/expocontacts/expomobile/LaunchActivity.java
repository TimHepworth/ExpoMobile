package com.expocontacts.expomobile;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class LaunchActivity extends AppCompatActivity {

    private View mDecorView;
    private CountDownTimer mTimer;
    private ImageView mLoadingImageView;
    private int mNumTicks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //  Set to fullscreen for the splash screen

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mDecorView = getWindow().getDecorView();
        mDecorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE |
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        //  Inflate the layout

        setContentView(R.layout.activity_launch);

        //  Change the background colour according to the site settings

        getWindow().getDecorView().setBackgroundColor(AppSettings.SPLASH_BACKGROUND_COLOR);

        startLoadingAnimation();

        //  Load the data (if necessary) from the server

        Toast.makeText(this, "Loading data ...", Toast.LENGTH_SHORT);

        GeneralUtils.get(this).LoadData(LaunchActivity.this, false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mTimer.cancel();
    }

    private void startLoadingAnimation() {

        //  Initiate the loading animation

        mLoadingImageView = (ImageView) findViewById(R.id.loadingImageView);
        mTimer = new CountDownTimer(30000, 500) {

            public void onTick(long millisUntilFinished) {
                mNumTicks++;

                if (mNumTicks % 4 == 0) {
                    mLoadingImageView.setImageResource(R.drawable.loading_1);
                } else if (mNumTicks % 4 == 1) {
                    mLoadingImageView.setImageResource(R.drawable.loading_2);
                } else if (mNumTicks % 4 == 2) {
                    mLoadingImageView.setImageResource(R.drawable.loading_3);
                } else {
                    mLoadingImageView.setImageResource(R.drawable.loading_4);
                }
            }

            public void onFinish() {
            }

        }.start();
    }

}
