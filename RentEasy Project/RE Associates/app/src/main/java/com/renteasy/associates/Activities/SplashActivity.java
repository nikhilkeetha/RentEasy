package com.renteasy.associates.Activities;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.renteasy.associates.IntroSlider;
import com.renteasy.associates.R;
import com.renteasy.associates.support.Utils;

public class SplashActivity extends AppCompatActivity {

    Activity mActivity;
    SharedPreferences sharedPreferences; // initilize shared preferences to check is user signed in or not

    private static final String SHARED_PREF_NM = "myPref"; //is signed in Name
    private static final String KEY_PHONENO = "number";  //if signed in user phone number
    private static final String ONESIGNAL_APP_ID = "337c0c69-e4d3-41de-9bf5-4c21b920c1df";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        init();
    }

    private void init() {
        mActivity = this;
        sharedPreferences = getSharedPreferences(SHARED_PREF_NM,MODE_PRIVATE);
        Intent i =new Intent(SplashActivity.this, StatisticsActivity.class);

        ImageView ivImage = findViewById(R.id.iv_image);
        Animation animZoomin = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.zoom_in);
        if (ivImage != null)
            ivImage.startAnimation(animZoomin);


        String phoneNm = sharedPreferences.getString(KEY_PHONENO,null);
        new Handler().postDelayed(() -> {
            if (phoneNm != null)
                Utils.startActivityFinish(mActivity, StatisticsActivity.class); // if user signed in goes to Home activity
            else
                startActivity(i);
        }, 2000);
    }
}