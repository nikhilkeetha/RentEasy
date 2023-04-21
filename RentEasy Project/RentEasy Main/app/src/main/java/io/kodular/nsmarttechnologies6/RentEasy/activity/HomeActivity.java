package io.kodular.nsmarttechnologies6.RentEasy.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.razorpay.PaymentResultListener;

import java.util.HashMap;
import java.util.List;

import io.kodular.nsmarttechnologies6.RentEasy.R;
import io.kodular.nsmarttechnologies6.RentEasy.Utility.NetworkChangeListener;
import io.kodular.nsmarttechnologies6.RentEasy.fragment.HomeFragment;
import io.kodular.nsmarttechnologies6.RentEasy.fragment.PayRentFragment;
import io.kodular.nsmarttechnologies6.RentEasy.fragment.PostListingFragment;
import io.kodular.nsmarttechnologies6.RentEasy.fragment.ProfileFragment;
import io.kodular.nsmarttechnologies6.RentEasy.fragment.SearchFragment;
import io.kodular.nsmarttechnologies6.RentEasy.support.Utils;
import pub.devrel.easypermissions.EasyPermissions;

 public class HomeActivity extends AppCompatActivity implements View.OnClickListener , PaymentResultListener,EasyPermissions.PermissionCallbacks {


    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    SharedPreferences sharedPreferences,sp2; // to get is user a pro user
    private static final String SHARED_PREF_PAID = "myPaid";
    private static final String KEY_IS_PAID = "isPaid";

    private static final String SHARED_PREF_NM = "myPref";
    private static final String KEY_PHONENO = "number";

    Activity mActivity;
    public String Tag = "",userPhno;
    boolean isFirstTime = true,paymentSucess;
    DatabaseReference setProUser,setTotalProUsers,notificationId;
    String intro;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        init();

        intro = getIntent().getStringExtra("item");

        if (intro!=null) {
            if (intro.equals("home")) {
                resetAll("home");
                loadFragment(HomeFragment.class.getName(), new HomeFragment());
            }else if (intro.equals("post")) {
                resetAll("add_property");
                loadFragment(PostListingFragment.class.getName(), new PostListingFragment());
            }
        }

    }

    private void init() {
        mActivity = this;
        setClickListener();
        loadFragment(HomeFragment.class.getName(), new HomeFragment());
        sharedPreferences = getSharedPreferences(SHARED_PREF_PAID,MODE_PRIVATE);
        sp2 = this.getSharedPreferences(SHARED_PREF_NM,MODE_PRIVATE);
        userPhno = sp2.getString(KEY_PHONENO,null);
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


        tvHome.setTextColor(getResources().getColor(R.color.secondary_black));
        tvSearch.setTextColor(getResources().getColor(R.color.secondary_black));
        tvAddProperty.setTextColor(getResources().getColor(R.color.secondary_black));
        tvRent.setTextColor(getResources().getColor(R.color.secondary_black));
        tvProfile.setTextColor(getResources().getColor(R.color.secondary_black));

        ivHome.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.secondary_black)));
        ivSearch.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.secondary_black)));
        ivAddProperty.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.secondary_black)));
        ivRent.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.secondary_black)));
        ivProfile.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.secondary_black)));

        ivHome.setBackground(getResources().getDrawable(R.drawable.explore_1));
        ivAddProperty.setBackground(getResources().getDrawable(R.drawable.add_1));
        ivRent.setBackground(getResources().getDrawable(R.drawable.pay_1));
        ivProfile.setBackground(getResources().getDrawable(R.drawable.user_1));

        switch (type) {
            case "home":
                ivHome.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
                tvHome.setTextColor(getResources().getColor(R.color.colorPrimary));
                ivHome.setBackground(getResources().getDrawable(R.drawable.explore_2));
                break;
            case "search":
                ivSearch.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
                tvSearch.setTextColor(getResources().getColor(R.color.colorPrimary));
                break;
            case "add_property":
                ivAddProperty.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
                tvAddProperty.setTextColor(getResources().getColor(R.color.colorPrimary));
                ivAddProperty.setBackground(getResources().getDrawable(R.drawable.add_2));
                break;
            case "rent":
                ivRent.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
                tvRent.setTextColor(getResources().getColor(R.color.colorPrimary));
                ivRent.setBackground(getResources().getDrawable(R.drawable.pay_2));
                break;
            case "profile":
                ivProfile.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
                tvProfile.setTextColor(getResources().getColor(R.color.colorPrimary));
                ivProfile.setBackground(getResources().getDrawable(R.drawable.user_2));
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

                //Intent i = new Intent(HomeActivity.this,IntroPostListingActivity.class);
                //startActivity(i);


                // only for developers
                resetAll("add_property");
                loadFragment(PostListingFragment.class.getName(), new PostListingFragment());
                // only for developers

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
        setProUser = FirebaseDatabase.getInstance().getReference().child("users data").child(userPhno);
        HashMap hashMap = new HashMap();
        hashMap.put("pro","t");
        setProUser.updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(@NonNull Object o) {
                setTotalProUsers = FirebaseDatabase.getInstance().getReference().child("Server");
                setTotalProUsers.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String total = dataSnapshot.child("total pro users").getValue().toString();
                        int sum = Integer.parseInt(total) + 1;
                        HashMap hashMap1 = new HashMap();
                        hashMap1.put("total pro users",sum);
                        if (paymentSucess==true) {
                            paymentSucess=false;
                            setTotalProUsers.updateChildren(hashMap1).addOnSuccessListener(new OnSuccessListener() {
                                @Override
                                public void onSuccess(@NonNull Object o) {
                                    String ok = "true";
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString(KEY_IS_PAID,ok);
                                    editor.apply();
                                    io.RentEasy.views.RegularTextView goProBtn = findViewById(R.id.tv_goPro);
                                    io.RentEasy.views.RegularTextView description = findViewById(R.id.tv_proDescription);
                                    io.RentEasy.views.MediumTextView pro = findViewById(R.id.tv_pro);
                                    ImageView ivPro = findViewById(R.id.iv_pro);
                                    LinearLayout indicator = findViewById(R.id.ads_blocked);
                                    goProBtn.setVisibility(View.GONE);
                                    description.setText("Your are now a pro user. Find listings for rent ad-free. \nNote: Only video ads will be blocked.");
                                    pro.setText("Pro User");
                                    ivPro.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                                    indicator.setVisibility(View.VISIBLE);
                                    Toast.makeText(HomeActivity.this,"Success! you are a pro user. Please re-start the app if the ads are not blocked.",Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });


    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(HomeActivity.this,"Sorry! your payment has failed.",Toast.LENGTH_SHORT).show();
    }

     @Override
     public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

     }

     @Override
     public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
         AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
         builder.setMessage("Please enable location permission to continue using the app.")
                 .setCancelable(false)
                 .setPositiveButton("Enable", (dialog, id) -> {
                     EasyPermissions.requestPermissions(HomeActivity.this,
                             "App needs access to your location.",
                             101,
                             Manifest.permission.ACCESS_FINE_LOCATION);
                 })
                 .setNegativeButton("Exit App", (dialog, id) -> finishAffinity());
         AlertDialog alert = builder.create();
         alert.show();
     }

 }