package io.kodular.nsmarttechnologies6.RentEasy.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.razorpay.PaymentResultListener;

import io.kodular.nsmarttechnologies6.RentEasy.R;
import io.kodular.nsmarttechnologies6.RentEasy.Utility.NetworkChangeListener;
import io.kodular.nsmarttechnologies6.RentEasy.fragment.HomeFragment;
import io.kodular.nsmarttechnologies6.RentEasy.fragment.PayRentFragment;
import io.kodular.nsmarttechnologies6.RentEasy.fragment.PostPropertyFragment;
import io.kodular.nsmarttechnologies6.RentEasy.fragment.ProfileFragment;
import io.kodular.nsmarttechnologies6.RentEasy.fragment.SearchFragment;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener , PaymentResultListener {


    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    SharedPreferences sharedPreferences; // to get is user a pro user
    private static final String SHARED_PREF_PAID = "myPaid";
    private static final String KEY_IS_PAID = "isPaid";

    Activity mActivity;
    public String Tag = "";
    boolean isFirstTime = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        init();
    }

    private void init() {
        mActivity = this;
        setClickListener();
        loadFragment(HomeFragment.class.getName(), new HomeFragment());
        sharedPreferences = getSharedPreferences(SHARED_PREF_PAID,MODE_PRIVATE);
    }

    private void setClickListener() {
        LinearLayout homeLay = findViewById(R.id.home_lay);
        LinearLayout searchLay = findViewById(R.id.search_lay);
        LinearLayout addPropertyLay = findViewById(R.id.add_property_lay);
        LinearLayout rentPayLay = findViewById(R.id.pay_rent_lay);
        LinearLayout profileLay = findViewById(R.id.profile_lay);

        homeLay.setOnClickListener(this);
        searchLay.setOnClickListener(this);
        addPropertyLay.setOnClickListener(this);
        rentPayLay.setOnClickListener(this);
        profileLay.setOnClickListener(this);
    }

    public void loadFragment(String tag, Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (isFirstTime) {
            transaction.add(R.id.fragment_container, fragment);
        } else {
            transaction.replace(R.id.fragment_container, fragment);
        }
        if (!isFirstTime) {
            Tag = tag;
            transaction.addToBackStack(tag);
        } else {
            Tag = tag;
            isFirstTime = false;
        }
        transaction.commit();
        getSupportFragmentManager().executePendingTransactions();
    }


    public void showAlertDialogExit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
        builder.setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog, id) -> {
                    finishAffinity();
                })
                .setNegativeButton("No", (dialog, id) -> dialog.cancel());
        AlertDialog alert = builder.create();
        alert.show();
    }


    public void resetAll(String type) {
        TextView tvHome = findViewById(R.id.tv_home);
        TextView tvSearch = findViewById(R.id.tv_search);
        TextView tvAddProperty = findViewById(R.id.tv_add_property);
        TextView tvRent = findViewById(R.id.tv_rent);
        TextView tvProfile = findViewById(R.id.tv_profile);

        ImageView ivHome = findViewById(R.id.iv_home);
        ImageView ivSearch = findViewById(R.id.iv_search);
        ImageView ivAddProperty = findViewById(R.id.iv_add_property);
        ImageView ivRent = findViewById(R.id.iv_rent);
        ImageView ivProfile = findViewById(R.id.iv_profile);


        tvHome.setTextColor(getResources().getColor(R.color.primary_text_color));
        tvSearch.setTextColor(getResources().getColor(R.color.primary_text_color));
        tvAddProperty.setTextColor(getResources().getColor(R.color.primary_text_color));
        tvRent.setTextColor(getResources().getColor(R.color.primary_text_color));
        tvProfile.setTextColor(getResources().getColor(R.color.primary_text_color));

        ivHome.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.primary_text_color)));
        ivSearch.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.primary_text_color)));
        ivAddProperty.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.primary_text_color)));
        ivRent.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.primary_text_color)));
        ivProfile.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.primary_text_color)));

        switch (type) {
            case "home":
                ivHome.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
                tvHome.setTextColor(getResources().getColor(R.color.colorPrimary));
                break;
            case "search":
                ivSearch.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
                tvSearch.setTextColor(getResources().getColor(R.color.colorPrimary));
                break;
            case "add_property":
                ivAddProperty.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
                tvAddProperty.setTextColor(getResources().getColor(R.color.colorPrimary));
                break;
            case "rent":
                ivRent.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
                tvRent.setTextColor(getResources().getColor(R.color.colorPrimary));
                break;
            case "profile":
                ivProfile.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
                tvProfile.setTextColor(getResources().getColor(R.color.colorPrimary));
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_lay:
                resetAll("home");
                loadFragment(HomeFragment.class.getName(), new HomeFragment());
                break;
            case R.id.search_lay:
                resetAll("search");
                loadFragment(SearchFragment.class.getName(), new SearchFragment());
                break;
            case R.id.add_property_lay:
                resetAll("add_property");
                loadFragment(PostPropertyFragment.class.getName(), new PostPropertyFragment());
                break;
            case R.id.pay_rent_lay:
                resetAll("rent");
                loadFragment(PayRentFragment.class.getName(), new PayRentFragment());
                break;
            case R.id.profile_lay:
                resetAll("profile");
                loadFragment(ProfileFragment.class.getName(), new ProfileFragment());
                break;
        }
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

    @Override
    public void onPaymentSuccess(String s) {
        String ok = "true";
        Toast.makeText(HomeActivity.this,"Success! you are a pro user. PLease re-start the app if the ads are not blocked.",Toast.LENGTH_LONG).show();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_IS_PAID,ok);
        editor.apply();
        io.RentEasy.views.RegularTextView goProBtn = findViewById(R.id.tv_goPro);
        io.RentEasy.views.RegularTextView description = findViewById(R.id.tv_proDescription);
        io.RentEasy.views.MediumTextView pro = findViewById(R.id.tv_pro);
        ImageView ivPro = findViewById(R.id.iv_pro);
        LinearLayout indicator = findViewById(R.id.ads_blocked);
        goProBtn.setVisibility(View.GONE);
        description.setText("Your are now a pro user. Find properties for rent ad-free. Note you need to re-buy the pro plan if you uninstall the app.");
        pro.setText("Pro User");
        ivPro.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
        indicator.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(HomeActivity.this,"Sorry! your payment has failed.",Toast.LENGTH_SHORT).show();
    }
}