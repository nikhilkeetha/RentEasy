package io.kodular.nsmarttechnologies6.RentEasy.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.gms.common.util.NumberUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.transition.MaterialSharedAxis;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.kodular.nsmarttechnologies6.RentEasy.R;
import io.kodular.nsmarttechnologies6.RentEasy.Utility.NetworkChangeListener;
import io.kodular.nsmarttechnologies6.RentEasy.Utility.Property;
import io.kodular.nsmarttechnologies6.RentEasy.adapter.RecommendedPropertyAdapter;
import io.kodular.nsmarttechnologies6.RentEasy.support.Utils;

import static android.graphics.Color.BLACK;
import static io.kodular.nsmarttechnologies6.RentEasy.R.drawable.ic_check_circle;
import static io.kodular.nsmarttechnologies6.RentEasy.R.drawable.ic_list;
import static io.kodular.nsmarttechnologies6.RentEasy.R.drawable.ic_map;

public class RecommendedPropertyActivity extends AppCompatActivity implements View.OnClickListener , PaymentResultListener {
    private AdView mAdView;
    private RewardedAd mRewardedAd;

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    Activity mActivity;

    Dialog dialog;

    DatabaseReference getPropertiesList;
    DatabaseReference getNoOfProperties;

    List<Property>list;

    Activity context;
    RecommendedPropertyAdapter adapter;
    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_PAID = "myPaid";
    private static final String KEY_IS_PAID = "isPaid";

    private String pincode;
    String H_or_S;
    private String isAvilable;;
    String isPro;
    String Type,Rooms,Min="0",Max="900000",total,id2=null,id3=null;
    int Total;
    int count=0;

    boolean isAdLoaded = false;
    boolean isMapView = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommended_property);
        init();


            //load reward ad
            AdRequest adRequest2 = new AdRequest.Builder().build();

            RewardedAd.load(this, "ca-app-pub-1019423261424750/4602841488",
                    adRequest2, new RewardedAdLoadCallback() {
                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            // Handle the error.

                            mRewardedAd = null;
                        }

                        @Override
                        public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                            mRewardedAd = rewardedAd;
                            isAdLoaded=true;
                        }
                    });
            io.RentEasy.views.RegularTextView tvNoIndicator = findViewById(R.id.tv_no_indicator);
            io.RentEasy.views.MediumTextView tvTitleBar = findViewById(R.id.title_bar);
            RecyclerView recyclerView = findViewById(R.id.recycler_view);
            ProgressBar loading = findViewById(R.id.loading);
            pincode = getIntent().getStringExtra("pincode");
            H_or_S = getIntent().getStringExtra("H_or_S");
            isAvilable = getIntent().getStringExtra("is_Avialable");
            LinearLayout bottom = findViewById(R.id.bottom_lay);
            if (H_or_S.equals("H")) {
                tvTitleBar.setText("Houses for Rent in " + pincode);
            }else {
                tvTitleBar.setText("Shops for Rent in " + pincode);
                bottom.setVisibility(View.INVISIBLE);
            }

            getNoOfProperties = FirebaseDatabase.getInstance().getReference().child(pincode + H_or_S);
            getNoOfProperties.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild("no of " + H_or_S + " Registered")) {
                        String totalRaw = dataSnapshot.child("no of " + H_or_S + " Registered").getValue().toString();
                        total = totalRaw.replace("\"","");
                        Total = Integer.parseInt(total);
                        tvNoIndicator.setText("Total " + total + " Properties Found");
                        list = new ArrayList<>();
                        getPropertiesList = FirebaseDatabase.getInstance().getReference().child(pincode + H_or_S);
                        getPropertiesList.addValueEventListener(new ValueEventListener() {
                            @RequiresApi(api = Build.VERSION_CODES.N)
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                    String datalist = dataSnapshot1.getValue().toString();
                                    String removeLeftChar = datalist.replace("[","");
                                    String removeRightChar = removeLeftChar.replace("]","");
                                    String value1 = removeRightChar.replace("\"","");
                                    boolean isNumeric = value1.chars().allMatch( Character::isDigit );
                                    if (isNumeric){
                                        getRecommendedProperty();
                                        recyclerView.setVisibility(View.VISIBLE);
                                        loading.setVisibility(View.GONE);
                                    }else {
                                        String[] split = value1.split(",");
                                        String I1raw = split[0];
                                        String I1Url = I1raw.replace("\\","");
                                        String Rate = split[15];
                                        String OwnerNm = split[17];
                                        String Rooms = split[6];
                                        String Address = split[11];
                                        String Description = Rooms + " near " + Address;
                                        String PhnNo = split[18];
                                        String id = split[16];
                                        String status = split[8];
                                        if (isAvilable.equals("n")) {
                                            if (status.equals("A")) {
                                                list.add(new Property(Rate,Description,OwnerNm,I1Url,PhnNo,pincode,H_or_S,id));
                                            }
                                        } else {
                                            list.add(new Property(Rate,Description,OwnerNm,I1Url,PhnNo,pincode,H_or_S,id));
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    } else {
                        tvNoIndicator.setText("Total 0 Properties Found");
                        openNoPropertiesDialog();
                        loading.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            final View.OnClickListener btnListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:8367530592"));
                    try
                    {
                        startActivity(intent);
                    }
                    catch (SecurityException s)
                    {

                    }
                }
            };
        }

        @Override
        public void onBackPressed() {
            super.onBackPressed();
            Utils.hideKeyboard(mActivity);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }

        private void init() {
            mActivity = this;

            RelativeLayout backLay = findViewById(R.id.back_lay);
            backLay.setOnClickListener(this);
            RelativeLayout filterLay = findViewById(R.id.filter_lay);
            filterLay.setOnClickListener(this);
            RelativeLayout mapLay = findViewById(R.id.mapLay);
            mapLay.setOnClickListener(this);

            dialog =new Dialog( this);
            mAdView = findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
            sharedPreferences = this.getSharedPreferences(SHARED_PREF_PAID,MODE_PRIVATE);
            isPro = sharedPreferences.getString(KEY_IS_PAID,null);
            if (isPro != null) {
                mAdView.setVisibility(View.GONE);
            }
        }


        private void getRecommendedProperty() {
            RecyclerView recyclerView = findViewById(R.id.recycler_view);
            recyclerView.setLayoutManager(new LinearLayoutManager(mActivity, RecyclerView.VERTICAL, false));
            adapter=new RecommendedPropertyAdapter(list, mActivity);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.back_lay:
                    onBackPressed();
                    break;
                case R.id.filter_lay:
                    if (isPro == null) {
                        openAdChooseDialog();
                    }else {
                        openFilterDialog();
                    }
                    break;
                case R.id.mapLay:
                    //opens map view of all properties in a particular area.
                    ImageView map = findViewById(R.id.iv_map);
                    io.RentEasy.views.RegularTextView mapView = findViewById(R.id.tv_mapView);
                    RecyclerView recyclerView = findViewById(R.id.recycler_view);
                    if (isMapView == true) {
                        isMapView = false;
                        map.setBackground(getResources().getDrawable(ic_list));
                        map.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.primary_text_color)));
                        mapView.setText("ListView");
                        recyclerView.setVisibility(View.GONE);
                    }else {
                        isMapView = true;
                        map.setBackground(getResources().getDrawable(ic_map));
                        map.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.primary_text_color)));
                        mapView.setText("MapView");
                        recyclerView.setVisibility(View.VISIBLE);
                    }

                    break;
            }
        }


        private void openFilterDialog() {
            dialog.setContentView(R.layout.filter_dialog);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCancelable(true);

            io.RentEasy.views.MediumTextView display = dialog.findViewById(R.id.tv_displayRooms);
            RelativeLayout spinner = dialog.findViewById(R.id.spinnerLay);

            if (H_or_S.equals("S")) {
                display.setVisibility(View.GONE);
                spinner.setVisibility(View.GONE);
            }

            //Initialize components
            Spinner type = dialog.findViewById(R.id.SpinnerType);
            Spinner rooms = dialog.findViewById(R.id.SpinnerRooms);
            TextInputEditText min = dialog.findViewById(R.id.et_minRent);
            TextInputEditText max = dialog.findViewById(R.id.et_maxRent);
            io.RentEasy.views.RegularTextView noFilters = findViewById(R.id.tv_noFilters);

            if (id2 != null) {
             rooms.setSelection(Integer.parseInt(id2));
            }
            if (id3 != null) {
                type.setSelection(Integer.parseInt(id3));
            }

            io.RentEasy.views.RegularTextView apply = dialog.findViewById(R.id.tv_applyFilter);
            apply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Type = type.getSelectedItem().toString();
                    Rooms = rooms.getSelectedItem().toString();
                    id2 = String.valueOf(rooms.getSelectedItemPosition());
                    id3 = String.valueOf(type.getSelectedItemPosition());
                    if (id2 != null) {
                        if (count == 2) {
                            count = 0;
                        }else {
                            count++;
                        }
                    }
                    if (id3 != null) {
                        if (count == 2) {
                            count = 0;
                        }else {
                            count++;
                        }
                    }
                    if (count != 0) {
                        noFilters.setText("Filter" + "(" + count + ")");
                    }
                    if (min.getText().toString().trim().isEmpty()) {
                        Min = "0";
                    } else {
                        Min = min.getText().toString().trim();
                    }
                    if (max.getText().toString().trim().isEmpty()) {
                        Max = "900000";
                    } else {
                        Max = min.getText().toString().trim();
                    }
                    //code here to apply filters
                    RecyclerView recyclerView = findViewById(R.id.recycler_view);
                    ProgressBar loading = findViewById(R.id.loading);
                    recyclerView.setVisibility(View.GONE);
                    loading.setVisibility(View.VISIBLE);
                    list = new ArrayList<>();
                    getPropertiesList = FirebaseDatabase.getInstance().getReference().child(pincode + H_or_S);
                    getPropertiesList.addValueEventListener(new ValueEventListener() {
                        @RequiresApi(api = Build.VERSION_CODES.N)
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                String datalist = dataSnapshot1.getValue().toString();
                                String removeLeftChar = datalist.replace("[","");
                                String removeRightChar = removeLeftChar.replace("]","");
                                String value1 = removeRightChar.replace("\"","");
                                boolean isNumeric = value1.chars().allMatch( Character::isDigit );
                                if (isNumeric){ // after all properties loaded.
                                    getRecommendedProperty();
                                    recyclerView.setVisibility(View.VISIBLE);
                                    loading.setVisibility(View.GONE);
                                }else {
                                    String[] split = value1.split(",");
                                    String I1raw = split[0];
                                    String I1Url = I1raw.replace("\\","");
                                    String Rate = split[15];
                                    String OwnerNm = split[17];
                                    String rooms = split[6];
                                    String Address = split[11];
                                    String Description = rooms + " near " + Address;
                                    String PhnNo = split[18];
                                    String id = split[16];
                                    String type = split[5];
                                    String status = split[8];
                                    boolean isTypeOk = false;
                                    boolean isRoomsOk = false;
                                    boolean isMinOk = false;
                                    boolean isStatusOk = false;
                                    if (isAvilable.equals("n")) {
                                        if (status.equals("A")) {
                                            isStatusOk=true;
                                        }
                                    } else {
                                        isStatusOk=true;
                                    }
                                    if (Type.equals(type)) {
                                        if (isStatusOk==true) {
                                            isTypeOk = true;
                                        }
                                    } else if (Type.equals("Both")) {
                                        if (isStatusOk==true) {
                                            isTypeOk = true;
                                        }
                                    }
                                    if (Rooms.equals(rooms)) {
                                        if (isTypeOk==true) {
                                            isRoomsOk=true;
                                        }
                                    } else if (Rooms.equals("All")) {
                                        if (isTypeOk==true) {
                                            isRoomsOk=true;
                                        }
                                    }
                                    if (Integer.parseInt(Min) <= Integer.parseInt(Rate)) {
                                        if (isRoomsOk==true) {
                                            isMinOk=true;
                                        }
                                    }
                                    if (Integer.parseInt(Max) >= Integer.parseInt(Rate)) {
                                        if (isMinOk==true) {
                                            list.add(new Property(Rate,Description,OwnerNm,I1Url,PhnNo,pincode,H_or_S,id));
                                        }
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    dialog.dismiss();
                }
            });

            dialog.show();
        }

        private void openAdChooseDialog() {
            dialog.setContentView(R.layout.watch_or_block_dialog);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCancelable(true);

            io.RentEasy.views.RegularTextView display = dialog.findViewById(R.id.display);
            display.setText("To Filter the properties,");

            TextView watchAd = dialog.findViewById(R.id.tv_ad);  //when user click watch ad to get owner contact details
            watchAd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mRewardedAd != null) {
                        Activity activityContext = RecommendedPropertyActivity.this;
                        mRewardedAd.show(activityContext, new OnUserEarnedRewardListener() {
                            @Override
                            public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                                // Handle the reward.
                                int rewardAmount = rewardItem.getAmount();
                                String rewardType = rewardItem.getType();
                                openFilterDialog();
                            }
                        });
                    } else {
                        Toast.makeText(RecommendedPropertyActivity.this,"Sorry ad failed to show, try after some time.", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            TextView blockAd = dialog.findViewById(R.id.tv_block); //when user click block ad payment to get owner contact details
            blockAd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String price = "50";
                    int amount = Math.round(Float.parseFloat(price)*100);
                    Checkout checkout = new Checkout();
                    checkout.setKeyID("rzp_live_g7i7onGCVMJrUN");
                    JSONObject object = new JSONObject();
                    try {
                        object.put("name","RentEasy Pro");
                        object.put("description","Go Ad-Free with just Rs.50.00");
                        object.put("currency","INR");
                        object.put("amount",amount);
                        checkout.open(RecommendedPropertyActivity.this,object);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    dialog.dismiss();
                }
            });

            dialog.show();
        }

        private void openNoPropertiesDialog() {
            dialog.setContentView(R.layout.no_properties_dialog);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCancelable(false);

            AppCompatTextView textViewOkay = dialog.findViewById(R.id.okay);
            textViewOkay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Utils.startActivity(mActivity, HomeActivity.class);

                }
            });

            dialog.show();
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
            Toast.makeText(RecommendedPropertyActivity.this,"Success! you are a pro user. PLease re-start the app if the ads are not blocked.",Toast.LENGTH_LONG).show();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(KEY_IS_PAID,ok);
            editor.apply();
            openFilterDialog();
            isPro = "true";
        }

        @Override
        public void onPaymentError(int i, String s) {
            Toast.makeText(RecommendedPropertyActivity.this,"Sorry! your payment has failed.",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onSaveInstanceState(Bundle outState) {
            super.onSaveInstanceState(outState);
        }

    }