package io.kodular.nsmarttechnologies6.RentEasy.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.inmobi.sdk.InMobiSdk;
import com.inmobi.sdk.SdkInitializationListener;
import com.onesignal.OneSignal;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

import io.kodular.nsmarttechnologies6.RentEasy.R;
import io.kodular.nsmarttechnologies6.RentEasy.support.Utils;
import pl.droidsonroids.gif.GifImageView;

import static android.content.ContentValues.TAG;

public class SplashActivity extends AppCompatActivity {

    Activity mActivity;
    SharedPreferences sharedPreferences,sp; // initilize shared preferences to check is user signed in or not

    private static final String SHARED_PREF_NM = "myPref"; //is signed in Name
    private static final String KEY_PHONENO = "number";  //if signed in user phone number
    private static final String ONESIGNAL_APP_ID = "337c0c69-e4d3-41de-9bf5-4c21b920c1df";

    private static final String SHARED_PREF_LAN = "myLanguage";
    private static final String KEY_LAN = "lan";


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


        JSONObject consentObject = new JSONObject();
        try {
            // Provide correct consent value to sdk which is obtained by User
            consentObject.put(InMobiSdk.IM_GDPR_CONSENT_AVAILABLE, true);
            // Provide 0 if GDPR is not applicable and 1 if applicable
            consentObject.put("gdpr", "0");
            // Provide user consent in IAB format
        } catch (JSONException e) {
            e.printStackTrace();
        }
        InMobiSdk.init(this, "d83dba2f37f74cea9b8199d9b9f28486", consentObject, new SdkInitializationListener() {
            @Override
            public void onInitializationComplete(@Nullable Error error) {
                if (null != error) {
                    Log.e(TAG, "InMobi Init failed -" + error.getMessage());
                } else {
                    Log.d(TAG, "InMobi Init Successful");
                }
            }
        });

    }

    private void init() {
        mActivity = this;
        sharedPreferences = getSharedPreferences(SHARED_PREF_NM,MODE_PRIVATE);
        Intent i = new Intent(getApplicationContext(), IntroSlider.class); // add introslider


        // app language changer

        sp=getSharedPreferences(SHARED_PREF_LAN,MODE_PRIVATE);
        String t = sp.getString(KEY_LAN,null);

        String phoneNm = sharedPreferences.getString(KEY_PHONENO,null);

        new Handler().postDelayed(() -> {
            if (phoneNm != null)
                Utils.startActivity(mActivity, HomeActivity.class);
            // if user signed in goes to Home activity
            else
                startActivity(i); // if not then opens Intro activity
        }, 2000);
    }
}