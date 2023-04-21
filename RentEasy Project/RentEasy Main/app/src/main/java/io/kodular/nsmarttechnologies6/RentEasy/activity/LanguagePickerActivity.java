package io.kodular.nsmarttechnologies6.RentEasy.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.Locale;

import io.kodular.nsmarttechnologies6.RentEasy.R;
import io.kodular.nsmarttechnologies6.RentEasy.Utility.NetworkChangeListener;
import io.kodular.nsmarttechnologies6.RentEasy.support.Utils;

public class LanguagePickerActivity extends AppCompatActivity {

    ImageView ivBack,check1,check2,check3;
    io.RentEasy.views.MediumTextView tvChange,tvEng,tvHin,tvTel;
    RelativeLayout itemEng,itemHin,itemTel;
    String selection="";
    SharedPreferences sp;
    private static final String SHARED_PREF_NM = "myLanguage";
    private static final String KEY_LAN = "lan";
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_picker);


        //assianing variables
        ivBack=findViewById(R.id.iv_back);
        check1=findViewById(R.id.check1);
        check2=findViewById(R.id.check2);
        check3=findViewById(R.id.check3);
        tvChange=findViewById(R.id.tv_change);
        tvEng=findViewById(R.id.tv_eng);
        tvHin=findViewById(R.id.tv_hin);
        tvTel=findViewById(R.id.tv_tel);
        itemEng=findViewById(R.id.item_eng);
        itemHin=findViewById(R.id.item_hin);
        itemTel=findViewById(R.id.item_tel);

        sp=getSharedPreferences(SHARED_PREF_NM,MODE_PRIVATE);
        String t = sp.getString(KEY_LAN,null);
        if (t!=null) {
            if (t.equals("h")) {
                reSetAll("hin");
            }else {
                reSetAll("tel");
            }
        }else {
            reSetAll("eng");
        }

        //onclick
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        itemEng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reSetAll("eng");
            }
        });

        itemHin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reSetAll("hin");
            }
        });

        itemTel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reSetAll("tel");
            }
        });

        tvChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sp.edit();
                if (selection.equals("hi")) {
                    editor.putString(KEY_LAN,"h");
                }else if (selection.equals("te")) {
                    editor.putString(KEY_LAN,"t");
                }else {
                    editor.putString(KEY_LAN,null);

                }
                editor.apply();

                setLocal(LanguagePickerActivity.this,selection);
                finish();
                startActivity(getIntent());
                if (!getIntent().getStringExtra("first").equals(null)) {
                    Utils.startActivity(LanguagePickerActivity.this,IntroSlider.class);
                }
            }
        });
    }

    public void reSetAll(String lan) {
        itemEng.setBackground(getResources().getDrawable(R.drawable.boder_language_selector));
        itemHin.setBackground(getResources().getDrawable(R.drawable.boder_language_selector));
        itemTel.setBackground(getResources().getDrawable(R.drawable.boder_language_selector));
        check1.setBackground(getResources().getDrawable(R.drawable.ic_circle));
        check2.setBackground(getResources().getDrawable(R.drawable.ic_circle));
        check3.setBackground(getResources().getDrawable(R.drawable.ic_circle));
        check1.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.hint_color)));
        check2.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.hint_color)));
        check3.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.hint_color)));
        tvEng.setTextColor(getResources().getColor(R.color.hint_color));
        tvHin.setTextColor(getResources().getColor(R.color.hint_color));
        tvTel.setTextColor(getResources().getColor(R.color.hint_color));

        switch (lan) {
            case"eng" :
                selection="";
                check1.setBackground(getResources().getDrawable(R.drawable.ic_circle_tick));
                check1.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
                tvEng.setTextColor(getResources().getColor(R.color.colorPrimary));
                itemEng.setBackground(getResources().getDrawable(R.drawable.border_language_seleted));
                break;
            case"hin" :
                selection="hi";
                check2.setBackground(getResources().getDrawable(R.drawable.ic_circle_tick));
                check2.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
                tvHin.setTextColor(getResources().getColor(R.color.colorPrimary));
                itemHin.setBackground(getResources().getDrawable(R.drawable.border_language_seleted));
                break;
            case"tel" :
                selection="te";
                check3.setBackground(getResources().getDrawable(R.drawable.ic_circle_tick));
                check3.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
                tvTel.setTextColor(getResources().getColor(R.color.colorPrimary));
                itemTel.setBackground(getResources().getDrawable(R.drawable.border_language_seleted));
                break;
        }

    }

    public void setLocal(Activity activity, String langCode) {
        Locale locale = new Locale(langCode);
        locale.setDefault(locale);
        Resources resources = activity.getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration (config, resources.getDisplayMetrics());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onStart() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener, filter);
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }
}