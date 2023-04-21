package io.kodular.nsmarttechnologies6.RentEasy.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.onesignal.OneSignal;

import io.kodular.nsmarttechnologies6.RentEasy.R;

import io.kodular.nsmarttechnologies6.RentEasy.support.Preference;
import io.kodular.nsmarttechnologies6.RentEasy.support.Utils;

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
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);

        // OneSignal Initialization
        OneSignal.initWithContext(this);
        OneSignal.setAppId(ONESIGNAL_APP_ID);
        OneSignal.unsubscribeWhenNotificationsAreDisabled(true);
    }

    private void init() {
        mActivity = this;
        sharedPreferences = getSharedPreferences(SHARED_PREF_NM,MODE_PRIVATE);

        ImageView ivImage = findViewById(R.id.iv_image);
        Animation animZoomin = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_in);
        if (ivImage != null)
            ivImage.startAnimation(animZoomin);


        String phoneNm = sharedPreferences.getString(KEY_PHONENO,null);
        new Handler().postDelayed(() -> {
            if (phoneNm != null)
                Utils.startActivityFinish(mActivity, MainActivity.class); // if user signed in goes to Home activity
            else
                Utils.startActivityFinish(mActivity, MainActivity.class); // if not then opens Login activity
        }, 2000);
    }
}