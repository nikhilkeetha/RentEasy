package io.kodular.nsmarttechnologies6.RentEasy.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.gms.tasks.OnSuccessListener;
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
import java.util.HashMap;
import java.util.List;

import io.kodular.nsmarttechnologies6.RentEasy.R;
import io.kodular.nsmarttechnologies6.RentEasy.Utility.NetworkChangeListener;
import io.kodular.nsmarttechnologies6.RentEasy.Utility.SearchList;
import io.kodular.nsmarttechnologies6.RentEasy.Utility.SearchList2;
import io.kodular.nsmarttechnologies6.RentEasy.adapter.SearchListItemAdapter;
import io.kodular.nsmarttechnologies6.RentEasy.support.Utils;

public class SearchListActivity extends AppCompatActivity implements View.OnClickListener , PaymentResultListener {

    private AdView mAdView;
    private RewardedAd mRewardedAd;

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    Activity mActivity;

    Dialog dialog;


    DatabaseReference getListingsData,getLikedListings,getAds,setProUser,setTotalProUsers,loadData;

    List<SearchList>list;
    List<SearchList2> temp1;

    Activity context;
    SearchListItemAdapter adapter;
    SharedPreferences sharedPreferences,sp2;
    private static final String SHARED_PREF_PAID = "myPaid";
    private static final String KEY_IS_PAID = "isPaid";
    private static final String SHARED_PREF_NM = "myPref";
    private static final String KEY_PHONENO = "number";
    private String pincode;
    private String isAvilable;
    String isPro,isLiked=null,userPhNo,category,isAdsAvailable="f";
    int var=0;
    RecyclerView recyclerView;
    io.RentEasy.views.RegularTextView tvNoIndicator;
    io.RentEasy.views.MediumTextView tvTitleBar;
    ProgressBar loading;
    RelativeLayout totalLay;
    boolean isAdLoaded = false,paymentSucess=true,isLocation=false;
    boolean once =true;
    String Token= "",total="0",totalDisplay="0";
    int totalDeleted = 0;

    boolean isScrolling = false,lastLoading=true,isFinished=false,firstAdapter=true;
    int currentItems,totalItems,scrolledOutItems,items=0;
    LinearLayoutManager manager;
    String city,area;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_list);
        init();
            // Initialize components
            recyclerView = findViewById(R.id.recycler_view);
            tvNoIndicator = findViewById(R.id.tv_no_indicator);
            tvTitleBar = findViewById(R.id.title_bar);
            loading = findViewById(R.id.loading);
            totalLay = findViewById(R.id.total_indicator);


            //getting intents from search fragment
            isLiked=getIntent().getStringExtra("isLiked");
            category=getIntent().getStringExtra("category-id");
            area=getIntent().getStringExtra("area");
            pincode = getIntent().getStringExtra("pincode");

            if (isLiked.equals("true")) {
                getFavoriteData();
            }else {
                if (getIntent().getStringExtra("city").equals("Hyderabad")) {
                    city="1";
                }else if (getIntent().getStringExtra("city").equals("Visakhapatnam")){
                    city="2";
                }else if (getIntent().getStringExtra("city").equals("New Delhi")){
                    city="3";
                }else if (getIntent().getStringExtra("city").equals("Bengaluru")) {
                    city="4";
                }else if (getIntent().getStringExtra("city").equals("Mumbai")) {
                    city="5";
                }
                if (area.equals("all")) {
                    getAllAds();
                }else {
                    getAdsData();
                }
            }

        }

        private void getAllAds() {
        isLocation=true;
        list = new ArrayList<>();
        temp1 = new ArrayList<>();
        getListingsData = FirebaseDatabase.getInstance().getReference().child("listings data");
        getListingsData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds0 : dataSnapshot.getChildren()) {
                    if (ds0.getKey().toString().equals(" 999999")) {
                        tvNoIndicator.setText("Total " + items + " ads found");
                        items--;
                        if (items==-1) {
                            openNoDataDialog(false);
                        }else {
                            new Handler().postDelayed(() -> {
                                loadData();
                            }, 3000);
                        }
                    } else {
                        for (DataSnapshot ds1 : ds0.getChildren()) {
                            if (ds1.hasChild("20")) {
                                if (ds1.child("20").getValue().toString().equals(city)) {
                                    if (ds1.child("10").getValue().toString().equals(category)) { //add this category
                                        temp1.add(new SearchList2(ds0.getKey(), ds1.getKey()));
                                        items++;
                                    }
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        }

        private void getFavoriteData() {
        isLocation=true;
        tvTitleBar.setText("Favourite Listings");
        totalLay.setVisibility(View.GONE);
        list = new ArrayList<>();
        temp1 = new ArrayList<>();
        getLikedListings = FirebaseDatabase.getInstance().getReference().child("users data").child(userPhNo);
        getLikedListings.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (once ==true) {
                    once =false;
                    if (dataSnapshot.hasChild("liked listings")) {
                        String total = String.valueOf(dataSnapshot.child("liked listings").getChildrenCount());
                        for (DataSnapshot ds1 : dataSnapshot.child("liked listings").getChildren()) {
                            var++;
                            if (ds1.hasChild("is deleted")) {
                                //if the listing is unliked by user so cross
                                totalDeleted++;
                                if (Integer.parseInt(total) == var) {
                                    if (totalDeleted==var) {
                                        openNoDataDialog(true);
                                    }
                                }
                            } else {
                                String pincode = ds1.child("0").getValue().toString();
                                String id = ds1.child("1").getValue().toString();
                                getListingsData = FirebaseDatabase.getInstance().getReference().child("listings data").child(pincode).child(id);
                                getListingsData.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.hasChild("is deleted")) {
                                            // if the listing is deleted by owner then cross
                                            totalDeleted++;
                                        } else {
                                            temp1.add(new SearchList2(pincode,id));
                                            items++;
                                        }
                                        if (Integer.parseInt(total) == var) {
                                            if (items+totalDeleted==Integer.parseInt(total)) {
                                                items--;
                                                loadData();
                                                //Toast.makeText(SearchListActivity.this, String.valueOf(temp1.size()), Toast.LENGTH_SHORT).show();
                                            }
                                            if (totalDeleted==var) {
                                                openNoDataDialog(true);
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }
                        }
                    } else {
                        openNoDataDialog(true);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

        private void getAdsData() {
        list = new ArrayList<>();
        temp1 = new ArrayList<>();
        getListingsData = FirebaseDatabase.getInstance().getReference().child("listings data");
        getListingsData.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (once ==true) {
                    once =false;
                    if (dataSnapshot.hasChild(pincode)) {
                        String total = String.valueOf(dataSnapshot.child(pincode).getChildrenCount());
                        for (DataSnapshot ds1 : dataSnapshot.child(pincode).getChildren()) {
                            var++;
                            if (ds1.hasChild("is deleted")) {
                                // if the listing is deleted by owner then cross
                                totalDeleted++;
                            } else {
                                if (ds1.hasChild("23")) {
                                    String categoryId = ds1.child("10").getValue().toString(); // gets category id
                                    String cityId = ds1.child("20").getValue().toString();
                                    String areaId = ds1.child("21").getValue().toString();
                                    if (categoryId.equals(category)) {
                                        if (cityId.equals(city)) {
                                            if (areaId.equals(area)) {
                                                temp1.add(new SearchList2(pincode,ds1.getKey()));
                                                items++;
                                            }else if (area.equals("all")) {
                                                temp1.add(new SearchList2(pincode,ds1.getKey()));
                                                items++;
                                            }
                                            else {
                                                totalDeleted++;
                                            }
                                        }else {
                                            totalDeleted++;
                                        }
                                    }else {
                                        totalDeleted++;
                                    }
                                }else {
                                    //if the ads are old type
                                    totalDeleted++;
                                }
                                /*
                                temp1.add(new SearchList2(pincode,ds1.getKey()));
                                items++;

                                 */
                            }
                            if (Integer.parseInt(total) == var) {
                                if (items+totalDeleted==Integer.parseInt(total)) {
                                    items--;
                                    loadData();
                                    tvNoIndicator.setText("Total "+(Integer.parseInt(total)-totalDeleted)+" ads found");
                                    //Toast.makeText(SearchListActivity.this, String.valueOf(temp1.size()), Toast.LENGTH_SHORT).show();
                                }
                                if (totalDeleted==var) {
                                    openNoDataDialog(false);
                                }
                            }
                        }
                    } else {
                        openNoDataDialog(false
                        );
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

        private void loadData() {
        for (int i=0; i<5;i++) {
            String pin = temp1.get(items).getPin();
            String ID = temp1.get(items).getid();
            if (items==0) {
                if (lastLoading) {
                    lastLoading=false;
                    loadData = FirebaseDatabase.getInstance().getReference().child("listings data").child(pin).child(ID);
                    loadData.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String imageUrl, Rate = "";
                            if (dataSnapshot.hasChild("0")) {
                                imageUrl = dataSnapshot.child("0").getValue().toString();
                            } else {
                                imageUrl = "No";
                            }
                            String amountRaw = dataSnapshot.child("5").getValue().toString();
                            String[] split = amountRaw.split(",");
                            String amount = split[0];
                            String cycle = split[1];
                            if (cycle.equals("m")) {
                                Rate = "₹" + amount + " per Month";
                            } else if (cycle.equals("d")) {
                                Rate = "₹" + amount + " per Day";
                            } else if (cycle.equals("h")) {
                                Rate = "₹" + amount + " per Hour";
                            }
                            String title = dataSnapshot.child("6").getValue().toString();
                            boolean verfied = false;
                            if (dataSnapshot.hasChild("24")) {
                                verfied = true;
                            }
                            list.add(new SearchList(Rate, title, imageUrl, pin, ID, verfied, isLocation));
                            setAdapter();
                            isFinished = true;
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }else {
                loadData = FirebaseDatabase.getInstance().getReference().child("listings data").child(pin).child(ID);
                int finalI = i;
                loadData.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String imageUrl, Rate = "";
                        if (dataSnapshot.hasChild("0")) {
                            imageUrl = dataSnapshot.child("0").getValue().toString();
                        } else {
                            imageUrl = "No";
                        }
                        String amountRaw = dataSnapshot.child("5").getValue().toString();
                        String[] split = amountRaw.split(",");
                        String amount = split[0];
                        String cycle = split[1];
                        if (cycle.equals("m")) {
                            Rate = "₹" + amount + " per Month";
                        } else if (cycle.equals("d")) {
                            Rate = "₹" + amount + " per Day";
                        } else if (cycle.equals("h")) {
                            Rate = "₹" + amount + " per Hour";
                        }
                        String title = dataSnapshot.child("6").getValue().toString();
                        boolean verfied = false;
                        if (dataSnapshot.hasChild("24")) {
                            verfied = true;
                        }
                        list.add(new SearchList(Rate, title, imageUrl, pin, ID, verfied, isLocation));
                        if (finalI == 4) {
                            setAdapter();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                items--;
            }
        }

    }

        private void setAdapter() {
        if (firstAdapter) {
            firstAdapter=false;

            manager = new LinearLayoutManager(this);
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.setLayoutManager(manager);
            adapter = new SearchListItemAdapter(list, mActivity);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            loading.setVisibility(View.GONE);

            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                        isScrolling = true;
                    }
                }

                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    currentItems = manager.getChildCount();
                    totalItems = manager.getItemCount();
                    scrolledOutItems = manager.findFirstVisibleItemPosition();

                    if (isScrolling && (currentItems + scrolledOutItems == totalItems)) {
                        isScrolling = false;
                        if (isFinished) {
                            loading.setVisibility(View.GONE);
                        } else {
                            loading.setVisibility(View.VISIBLE);
                        }
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (!isFinished) {
                                    loadData();
                                }
                            }
                        }, 5000);
                    }
                }
            });
        }else {
            adapter.notifyDataSetChanged();
            loading.setVisibility(View.GONE);
        }
    }

        @Override
        public void onBackPressed() {
        /*
            super.onBackPressed();
            Utils.hideKeyboard(mActivity);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
         */
            Utils.startActivity(SearchListActivity.this,HomeActivity.class);
        }

        private void init() {
            mActivity = this;
            //setting onClick listeners
            RelativeLayout backLay = findViewById(R.id.back_lay);
            backLay.setOnClickListener(this);
            RelativeLayout filterLay = findViewById(R.id.filter_lay);
            filterLay.setOnClickListener(this);

            dialog =new Dialog( this);
            //Loading Ads
            mAdView = findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);

            getAds = FirebaseDatabase.getInstance().getReference().child("Server");
            getAds.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    isAdsAvailable = dataSnapshot.child("is ads available").getValue().toString();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            loadRewardAd();

            sharedPreferences = this.getSharedPreferences(SHARED_PREF_PAID,MODE_PRIVATE);
            sp2 = this.getSharedPreferences(SHARED_PREF_NM,MODE_PRIVATE);
            isPro = sharedPreferences.getString(KEY_IS_PAID,null);
            userPhNo = sp2.getString(KEY_PHONENO,null);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.back_lay:
                    onBackPressed();
                    break;
                case R.id.filter_lay:
                    // tempory monitization removal.
                    /*
                    if (isPro == null) {
                        openAdChooseDialog();
                    }else {
                        openFilterDialog();
                    }
                     */

                     //openFilterDialog();
                    break;
            }
        }

        private void loadRewardAd() {
            AdRequest adRequest2 = new AdRequest.Builder().build();
            RewardedAd.load(this, "ca-app-pub-1019423261424750/4136891252",
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
        }

        private void openAdChooseDialog() {
            dialog.setContentView(R.layout.watch_or_block_dialog);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCancelable(true);

            io.RentEasy.views.RegularTextView display = dialog.findViewById(R.id.display);
            display.setText("To Filter the listings,");

            TextView watchAd = dialog.findViewById(R.id.tv_ad);  //when user click watch ad to get owner contact details
            watchAd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isAdsAvailable.equals("t")) {
                        if (mRewardedAd != null) {
                            Activity activityContext = SearchListActivity.this;
                            mRewardedAd.show(activityContext, new OnUserEarnedRewardListener() {
                                @Override
                                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                                    // Handle the reward.
                                    int rewardAmount = rewardItem.getAmount();
                                    String rewardType = rewardItem.getType();
                                    //openFilterDialog();

                                    //loading ad
                                    loadRewardAd();
                                }
                            });
                        } else {
                            Toast.makeText(SearchListActivity.this,"Sorry ad failed to show, try after some time.", Toast.LENGTH_SHORT).show();
                            loadRewardAd();
                        }
                    }else {
                        //openFilterDialog();
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
                        checkout.open(SearchListActivity.this,object);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    dialog.dismiss();
                }
            });

            dialog.show();
        }

        private void openNoDataDialog(boolean isLiked) {
            dialog.setContentView(R.layout.no_listings_dialog);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCancelable(false);

            io.RentEasy.views.MediumTextView title = dialog.findViewById(R.id.tv_title);
            io.RentEasy.views.RegularTextView description = dialog.findViewById(R.id.tv_description);

            AppCompatTextView textViewOkay = dialog.findViewById(R.id.okay);
            textViewOkay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });

            if (isLiked==true) {
                textViewOkay.setText("Go Back");
                title.setText("No Favorite Listings !");
                description.setText("You have not added and listing into your favorite collections. You can add any listing into favorite collection by clicking ❤.");
            }



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
            //adding user as pro user
            setProUser = FirebaseDatabase.getInstance().getReference().child("users data").child(userPhNo);
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
                                        //openFilterDialog();
                                        isPro = "true";
                                        Toast.makeText(SearchListActivity.this,"Success! you are a pro user. Please re-start the app if the ads are not blocked.",Toast.LENGTH_LONG).show();
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
            Toast.makeText(SearchListActivity.this,"Sorry! your payment has failed.",Toast.LENGTH_SHORT).show();
        }

    }