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
import android.view.View;
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
import java.util.List;

import io.kodular.nsmarttechnologies6.RentEasy.R;
import io.kodular.nsmarttechnologies6.RentEasy.Utility.MyProperty;
import io.kodular.nsmarttechnologies6.RentEasy.Utility.NetworkChangeListener;
import io.kodular.nsmarttechnologies6.RentEasy.Utility.Property;
import io.kodular.nsmarttechnologies6.RentEasy.adapter.MyPropertyAdapter;
import io.kodular.nsmarttechnologies6.RentEasy.adapter.RecommendedPropertyAdapter;
import io.kodular.nsmarttechnologies6.RentEasy.fragment.PostPropertyFragment;
import io.kodular.nsmarttechnologies6.RentEasy.support.Utils;

public class MyPropertyActivity extends AppCompatActivity implements View.OnClickListener{

    private AdView mAdView;
    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NM = "myPref";
    private static final String KEY_PHONENO = "number";

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    Activity mActivity;

    DatabaseReference getNoOfPostings;
    DatabaseReference getPostingsList;
    DatabaseReference getPropertyDetails;

    List<MyProperty> list;

    Activity context;
    MyPropertyAdapter adapter;

    Dialog dialog;

    io.RentEasy.views.RegularTextView tvIndicator;

    String PhnNm = "8367530592";
    int var = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_property);
        init();
        sharedPreferences = getSharedPreferences(SHARED_PREF_NM,MODE_PRIVATE);
        PhnNm = sharedPreferences.getString(KEY_PHONENO,"8367530592");
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        tvIndicator = findViewById(R.id.tv_indicator);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        ProgressBar loading = findViewById(R.id.loading);
        getNoOfPostings = FirebaseDatabase.getInstance().getReference().child("Phone Numbers");
        getNoOfPostings.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(PhnNm)) {
                    String raw = dataSnapshot.child(PhnNm).getValue().toString();
                    String total = raw.replace("\"","");
                    int total2 = Integer.parseInt(total);
                    tvIndicator.setText("You have posted " + total + " properties");
                    list = new ArrayList<>();
                    getPostingsList = FirebaseDatabase.getInstance().getReference().child(PhnNm);
                    getPostingsList.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                String datalist = dataSnapshot1.getValue().toString();
                                String removeLeftChar = datalist.replace("[","");
                                String removeRightChar = removeLeftChar.replace("]","");
                                String value1 = removeRightChar.replace("\"","");
                                String[] split = value1.split(",");
                                String id = split[0];
                                String pincodeBucket = split[1];
                                getPropertyDetails = FirebaseDatabase.getInstance().getReference().child(pincodeBucket);
                                getPropertyDetails.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        var++;
                                        String datalist2 = dataSnapshot.child(id).getValue().toString();
                                        String removeLeftChar2 = datalist2.replace("[","");
                                        String removeRightChar2 = removeLeftChar2.replace("]","");
                                        String value2 = removeRightChar2.replace("\"","");
                                        String[] split = value2.split(",");
                                        String I1raw = split[0];
                                        String I1Url = I1raw.replace("\\","");
                                        String Rate = split[15];
                                        String Rooms = split[6];
                                        String Address = split[11];
                                        String Description = Rooms + " at " + Address;
                                        list.add(new MyProperty(Rate,Description,I1Url,pincodeBucket,id));
                                        if (total2 == var){
                                            getMyProperty();
                                            loading.setVisibility(View.GONE);
                                            recyclerView.setVisibility(View.VISIBLE);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }else{
                    tvIndicator.setText("You have posted 0 properties");
                    openNoPostingsDialog();
                    loading.setVisibility(View.GONE);
                }
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
        mActivity = this;
        dialog =new Dialog( this);

        RelativeLayout backLay = findViewById(R.id.back_lay);
        backLay.setOnClickListener(this);
    }

    private void openNoPostingsDialog() {
        dialog.setContentView(R.layout.no_my_properties_dialog);
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

    private void getMyProperty() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity, RecyclerView.VERTICAL, false));
        adapter=new MyPropertyAdapter(list, mActivity);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
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