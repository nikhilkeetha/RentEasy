package io.kodular.nsmarttechnologies6.RentEasy.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.kodular.nsmarttechnologies6.RentEasy.R;
import io.kodular.nsmarttechnologies6.RentEasy.Utility.MyPostings;
import io.kodular.nsmarttechnologies6.RentEasy.Utility.NetworkChangeListener;
import io.kodular.nsmarttechnologies6.RentEasy.Utility.SearchList;
import io.kodular.nsmarttechnologies6.RentEasy.Utility.SearchList2;
import io.kodular.nsmarttechnologies6.RentEasy.adapter.MyPostingsItemAdapter;
import io.kodular.nsmarttechnologies6.RentEasy.adapter.SearchListItemAdapter;
import io.kodular.nsmarttechnologies6.RentEasy.support.Utils;

public class MyPostingsActivity extends AppCompatActivity implements View.OnClickListener{

    private AdView mAdView;
    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NM = "myPref";
    private static final String KEY_PHONENO = "number";

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    Activity mActivity;

    DatabaseReference getOwnedListings,getListingsData,loadData;

    List<MyPostings> list;
    List<SearchList2> temp1;

    MyPostingsItemAdapter adapter;

    Dialog dialog;

    io.RentEasy.views.RegularTextView tvIndicator;

    String PhnNm = "8367530592";
    int var = 0,items=0,totalDeleted = 0,currentItems,totalItems,scrolledOutItems;
    boolean onceLoaded = true,lastLoading=true,isFinished=false,isScrolling=false,firstAdapter=true,once1=true,once2=true;
    RecyclerView recyclerView;
    ProgressBar loading;
    LinearLayoutManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_postings);
        init();
        sharedPreferences = getSharedPreferences(SHARED_PREF_NM,MODE_PRIVATE);
        PhnNm = sharedPreferences.getString(KEY_PHONENO,"8367530592");
        tvIndicator = findViewById(R.id.tv_indicator);
        recyclerView = findViewById(R.id.recycler_view);
        loading = findViewById(R.id.loading);

        getMyAds();

    }

    private void getMyAds() {
        list = new ArrayList<>();
        temp1 = new ArrayList<>();
        getOwnedListings=FirebaseDatabase.getInstance().getReference().child("users data").child(PhnNm);
        getOwnedListings.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (onceLoaded=true) {
                    onceLoaded=false;
                    if (dataSnapshot.hasChild("owned listings")) {
                        String total = String.valueOf(dataSnapshot.child("owned listings").getChildrenCount());
                        tvIndicator.setText("You have posted " + total + " ads");
                        for (DataSnapshot ds : dataSnapshot.child("owned listings").getChildren()) {
                            var++;
                            String pincode = ds.child("0").getValue().toString();
                            String id = ds.child("1").getValue().toString();
                            getListingsData = FirebaseDatabase.getInstance().getReference().child("listings data").child(pincode).child(id);
                            String Total = total;
                            getListingsData.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.hasChild("is deleted")) {
                                        // if the listing is deleted by owner then cross
                                        totalDeleted = totalDeleted + 1;
                                        int sub = Integer.valueOf(total) - totalDeleted;
                                        tvIndicator.setText("You have posted " + sub + " ads");
                                    } else {
                                        temp1.add(new SearchList2(pincode,id));
                                        items++;
                                    }
                                    if (Integer.parseInt(Total) == var) {
                                        if (items+totalDeleted==Integer.parseInt(Total)) {
                                            items--;
                                            loadData();
                                        }
                                        if (totalDeleted == var) {
                                            tvIndicator.setVisibility(View.GONE);
                                            openNoPostingsDialog();
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    } else {
                        openNoPostingsDialog();
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
                            if (once1==true) {
                                once1 = false;
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
                                list.add(new MyPostings(Rate, title, imageUrl, pin, ID));
                                setAdapter();
                                isFinished = true;
                            }
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
                        if (once2==true) {
                            once2 = false;
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
                            list.add(new MyPostings(Rate, title, imageUrl, pin, ID));
                            if (finalI == 4) {
                                setAdapter();
                            }
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
            adapter=new MyPostingsItemAdapter(list,mActivity);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            loading.setVisibility(View.GONE);
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                        isScrolling=true;
                    }
                }

                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    currentItems=manager.getChildCount();
                    totalItems=manager.getItemCount();
                    scrolledOutItems=manager.findFirstVisibleItemPosition();

                    if (isScrolling && (currentItems+scrolledOutItems == totalItems)) {
                        isScrolling=false;
                        if (isFinished) {
                            loading.setVisibility(View.GONE);
                        }else {
                            loading.setVisibility(View.VISIBLE);
                        }
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (!isFinished) {
                                    once1=true;
                                    once2=true;
                                    loadData();
                                }
                            }
                        }, 5000);
                    }
                }
            });
        }
        else {
            adapter.notifyDataSetChanged();
            loading.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
            super.onBackPressed();
            Utils.hideKeyboard(mActivity);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void init() {
        mActivity = this;
        dialog =new Dialog( this);

        RelativeLayout backLay = findViewById(R.id.back_lay);
        backLay.setOnClickListener(this);

        //load ad
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    private void openNoPostingsDialog() {
        dialog.setContentView(R.layout.no_owned_listings_dialog);
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
    public void onClick(View v) {
        if (v.getId() == R.id.back_lay)
            onBackPressed();
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