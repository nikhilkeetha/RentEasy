package io.kodular.nsmarttechnologies6.RentEasy.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.ViewTarget;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

import io.kodular.nsmarttechnologies6.RentEasy.R;
import io.kodular.nsmarttechnologies6.RentEasy.Utility.NetworkChangeListener;
import io.kodular.nsmarttechnologies6.RentEasy.adapter.HomePropertySliderAdapter;
import io.kodular.nsmarttechnologies6.RentEasy.adapter.HomeRecommendedPropertiesAdapter;
import io.kodular.nsmarttechnologies6.RentEasy.support.Utils;

import me.angeldevil.autoscrollviewpager.AutoScrollViewPager;

public class RecommendedPropertyDetailActivity extends AppCompatActivity implements View.OnClickListener, PaymentResultListener {

    //ad initilaizing
    private AdView mAdView;
    private RewardedAd mRewardedAd;
    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_PAID = "myPaid";
    private static final String KEY_IS_PAID = "isPaid";

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    Activity mActivity;

    private String propertyId;
    private String pincode;
    private String H_or_S;

    boolean isAdLoaded = false;

    String phNm = "";
    String msg = "";

    String lat = "";
    String lng = "";
    String isPro;

    Dialog dialog;

    DatabaseReference propertydetails;

    io.RentEasy.views.MediumTextView tvTitle;
    io.RentEasy.views.RegularTextView tvRate;
    io.RentEasy.views.RegularTextView tvPropertyType;
    io.RentEasy.views.RegularTextView tvRooms;
    io.RentEasy.views.RegularTextView tvFacing;
    io.RentEasy.views.RegularTextView tvFloor;
    io.RentEasy.views.RegularTextView tvPropertyAddress;
    io.RentEasy.views.RegularTextView tvOwnerName;
    io.RentEasy.views.RegularTextView tvStatus;
    io.RentEasy.views.RegularTextView tvParking;
    ImageView ivPropertyType;
    ImageView ivStatus;

    ImageView iv1;
    ImageView iv2;
    ImageView iv3;
    ImageView iv4;
    ImageView iv5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommended_property_detail);
        init();
        openLoadingDialog();



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




        String uri = "@drawable/ic_apartment";
        String uri2 = "@drawable/ic_close";

        tvRate = findViewById(R.id.tv_rate);
        tvPropertyType = findViewById(R.id.tv_propertyType);
        tvRooms = findViewById(R.id.tv_rooms);
        tvFacing = findViewById(R.id.tv_Facing);
        tvFloor = findViewById(R.id.tv_Floor);
        tvPropertyAddress = findViewById(R.id.tv_propertyAddress);
        tvOwnerName = findViewById(R.id.tv_owner_name);
        tvTitle = findViewById(R.id.PropertyTitle);
        tvStatus = findViewById(R.id.tv_status);
        tvParking = findViewById(R.id.tv_parking);
        iv1 = findViewById(R.id.image1);
        iv2 = findViewById(R.id.image2);
        iv3 = findViewById(R.id.image3);
        iv4 = findViewById(R.id.image4);
        iv5 = findViewById(R.id.image5);

        propertyId = getIntent().getStringExtra("propertyId");
        ivPropertyType = findViewById(R.id.iv_proeprtyType);
        ivStatus = findViewById(R.id.iv_status);

        pincode = getIntent().getStringExtra("pincode");
        H_or_S = getIntent().getStringExtra("H_or_S");
        if ( H_or_S.equals("H") ){
            tvTitle.setText("House for Rent");
        }else {
            tvTitle.setText("Shop for Rent");
        }

        //get property data from firebase
        propertydetails = FirebaseDatabase.getInstance().getReference().child(pincode + H_or_S);
        propertydetails.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String datalist = dataSnapshot.child(propertyId).getValue().toString();
                String removeLeftChar = datalist.replace("[","");
                String removeRightChar = removeLeftChar.replace("]","");
                String[] split = removeRightChar.split(",");
                String noImageUrl = "https://res.cloudinary.com/nsmarttechnologies/image/upload/v1607406483/S_7_omb4tr.jpg";
                String I1raw = split[0];
                String I1raw2 = I1raw.replace("\"","");
                String I1Url = I1raw2.replace("\\","");
                if ( I1Url.equals("No")) {
                    Glide
                            .with(mActivity)
                            .load(noImageUrl)
                            .into(iv1);
                }else {
                    Glide
                            .with(mActivity)
                            .load(I1Url)
                            .into(iv1);
                }

                String I2raw = split[1];
                String I2raw2 = I2raw.replace("\"","");
                String I2Url = I2raw2.replace("\\","");
                if ( I2Url.equals("No")) {
                    Glide
                            .with(mActivity)
                            .load(noImageUrl)
                            .into(iv2);
                }else {
                    Glide
                            .with(mActivity)
                            .load(I2Url)
                            .into(iv2);
                }

                String I3raw = split[2];
                String I3raw2 = I3raw.replace("\"","");
                String I3Url = I3raw2.replace("\\","");
                if ( I3Url.equals("No")) {
                    Glide
                            .with(mActivity)
                            .load(noImageUrl)
                            .into(iv3);
                }else {
                    Glide
                            .with(mActivity)
                            .load(I3Url)
                            .into(iv3);
                }

                String I4raw = split[3];
                String I4raw2 = I4raw.replace("\"","");
                String I4Url = I4raw2.replace("\\","");
                if ( I4Url.equals("No")) {
                    Glide
                            .with(mActivity)
                            .load(noImageUrl)
                            .into(iv4);
                }else {
                    Glide
                            .with(mActivity)
                            .load(I4Url)
                            .into(iv4);
                }

                String I5raw = split[4];
                String I5raw2 = I5raw.replace("\"","");
                String I5Url = I5raw2.replace("\\","");
                if ( I5Url.equals("No")) {
                    Glide
                            .with(mActivity)
                            .load(noImageUrl)
                            .into(iv5);
                    dialog.dismiss();
                }else {
                    Glide
                            .with(mActivity)
                            .load(I5Url)
                            .into(iv5);
                    dialog.dismiss();
                }

                String PropertyTypeRaw = split[5];
                String PropertyType = PropertyTypeRaw.replace("\"","");

                int imageResource = getResources().getIdentifier(uri, null, getPackageName());
                Drawable res = getResources().getDrawable(imageResource);
                if ( PropertyType.equals("Individual") ){
                }else{
                    ivPropertyType.setBackground(res);
                }
                tvPropertyType.setText(PropertyType);
                String RoomsRaw = split[6];
                String Rooms = RoomsRaw.replace("\"","");
                tvRooms.setText(Rooms);
                String FacingRaw = split[7];
                String Facing = FacingRaw.replace("\"","");
                tvFacing.setText(Facing);
                String AvilableRaw = split[8];
                String Avilable = AvilableRaw.replace("\"","");
                int imageResource2 = getResources().getIdentifier(uri2, null, getPackageName());
                Drawable res2 = getResources().getDrawable(imageResource2);
                if ( Avilable.equals("N") ){
                    ivStatus.setBackground(res2);
                    ivStatus.setBackgroundTintList(getResources().getColorStateList(R.color.red));;
                    tvStatus.setText("Not Avialable");
                }
                String FloorRaw = split[9];
                String Floor = FloorRaw.replace("\"","");
                tvFloor.setText(Floor);
                String ParkingRaw = split[10];
                String Parking = ParkingRaw.replace("\"","");
                if ( Parking.equals("nn") ) {
                    tvParking.setText("No Parking");
                }else if ( Parking.equals("ny") ){
                    tvParking.setText("Both Bike & Car");
                }
                String AddressRaw = split[11];
                String Address = AddressRaw.replace("\"","");
                tvPropertyAddress.setText(Address);
                String LatRaw = split[13];
                String Lat = LatRaw.replace("\"","");
                String LngRaw = split[14];
                String Lng = LngRaw.replace("\"","");
                String RateRaw = split[15];
                String Rate = RateRaw.replace("\"","");
                tvRate.setText("₹ "  + Rate + " per Month");
                String OwnerRaw = split[17];
                String Owner = OwnerRaw.replace("\"","");
                tvOwnerName.setText(Owner);
                String PhnNoRaw = split[18];
                String PhnNo = PhnNoRaw.replace("\"","");
                phNm = "tel:" + PhnNo;
                msg = PhnNo;
                lat = Lat;
                lng = Lng;
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Utils.hideKeyboard(mActivity);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void init() {
        dialog =new Dialog(this);
        mActivity = this;
        RelativeLayout backLay = findViewById(R.id.back_lay);
        backLay.setOnClickListener(this);
        io.RentEasy.views.RegularTextView tvCall = findViewById(R.id.tv_call);
        tvCall.setOnClickListener(this);
        CardView cvMap = findViewById(R.id.cv_map);
        cvMap.setOnClickListener(this);
        //load banner
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        sharedPreferences = this.getSharedPreferences(SHARED_PREF_PAID,MODE_PRIVATE);
        isPro = sharedPreferences.getString(KEY_IS_PAID,null);
        if (isPro != null) {
            mAdView.setVisibility(View.GONE);
        }
    }
    private void openLoadingDialog() {
        dialog.setContentView(R.layout.loading_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.show();
    }

    private void openAdChooseDialog() {
        dialog.setContentView(R.layout.watch_or_block_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);

        TextView watchAd = dialog.findViewById(R.id.tv_ad);  //when user click watch ad to get owner contact details
        watchAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRewardedAd != null) {
                    Activity activityContext = RecommendedPropertyDetailActivity.this;
                    mRewardedAd.show(activityContext, new OnUserEarnedRewardListener() {
                        @Override
                        public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                            // Handle the reward.
                            int rewardAmount = rewardItem.getAmount();
                            String rewardType = rewardItem.getType();
                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            intent.setData(Uri.parse(phNm));
                            startActivity(intent);
                        }
                    });
                } else {
                    Toast.makeText(RecommendedPropertyDetailActivity.this,"Sorry ad failed to show, try after some time.", Toast.LENGTH_SHORT).show();
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
                    checkout.open(RecommendedPropertyDetailActivity.this,object);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_lay:
                onBackPressed();
                break;
            case R.id.tv_call:
                if (isPro == null) {
                    openAdChooseDialog();
                }else {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse(phNm));
                    startActivity(intent);
                }

                break;
            case R.id.cv_map:
                String geoUri = "http://maps.google.com/maps?q=loc:" + lat + "," + lng ;
                Intent intent2 = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
                intent2.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                startActivity(intent2);
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
        Toast.makeText(RecommendedPropertyDetailActivity.this,"Success! you are a pro user. PLease re-start the app if the ads are not blocked.",Toast.LENGTH_LONG).show();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_IS_PAID,ok);
        editor.apply();
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse(phNm));
        startActivity(intent);
        isPro = "true";
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(RecommendedPropertyDetailActivity.this,"Sorry! your payment has failed.",Toast.LENGTH_SHORT).show();
    }
}