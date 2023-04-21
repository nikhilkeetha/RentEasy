package com.renteasy.associates.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.renteasy.associates.Adapter.PromoteAdapter;
import com.renteasy.associates.R;
import com.renteasy.associates.support.Utils;
import com.renteasy.associates.views.MediumTextView;
import com.renteasy.associates.views.RegularTextView;

import java.util.Collections;

public class StatisticsActivity extends AppCompatActivity {

    MediumTextView tvTotal,tvRecent1,tvRecent2,tvOwners,tvRenters,store,ads,frnd,net,pro,properties,cameras,vehicles,tools,cloths,functions,music,gadgets,books,connected,connectedOffline,activeUsers,date,lastActive;
    Button promote,spam,booking,call;
    DatabaseReference server;
    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_RECENT = "myRecent";
    private static final String KEY_TOTAL_LISTINGS = "listings";
    private static final String KEY_TOTAL_PRO = "pro";
    private static final String KEY_TOTAL_ACTIVE="active";
    private static final String KEY_OWNERS="owners";
    private static final String KEY_RENTERS="renters";
    String lastListings,lastPro,lastActiveUsers,lastOwners,lastRenters;
    RegularTextView tvRecentOwners,tvRecentRenters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        sharedPreferences = getSharedPreferences(SHARED_PREF_RECENT,MODE_PRIVATE);
        lastListings = sharedPreferences.getString(KEY_TOTAL_LISTINGS,null);
        lastPro = sharedPreferences.getString(KEY_TOTAL_PRO,null);
        lastActiveUsers=sharedPreferences.getString(KEY_TOTAL_ACTIVE,null);
        lastOwners=sharedPreferences.getString(KEY_OWNERS,null);
        lastRenters=sharedPreferences.getString(KEY_RENTERS,null);

        //assaing variables
        promote = findViewById(R.id.btn_promote);
        spam=findViewById(R.id.btn_spam);
        booking=findViewById(R.id.btn_booking);
        call=findViewById(R.id.btn_call_requests);

        tvTotal=findViewById(R.id.tv_total_listings);
        tvRecent1=findViewById(R.id.tv_new_listings);
        tvRecent2=findViewById(R.id.tv_new_pro);
        tvOwners=findViewById(R.id.tv_owners);
        tvRenters=findViewById(R.id.tv_renters);
        store=findViewById(R.id.tv_store);
        ads=findViewById(R.id.tv_ads);
        frnd=findViewById(R.id.tv_frnd);
        net=findViewById(R.id.tv_net);
        pro=findViewById(R.id.tv_pro_users);
        properties=findViewById(R.id.tv_properties);
        cameras=findViewById(R.id.tv_cams);
        vehicles=findViewById(R.id.tv_vechicles);
        tools=findViewById(R.id.tv_tools);
        cloths=findViewById(R.id.tv_cloths);
        functions=findViewById(R.id.tv_func);
        music=findViewById(R.id.tv_music);
        gadgets=findViewById(R.id.tv_gad);
        books=findViewById(R.id.tv_books);
        connected=findViewById(R.id.tv_connected);
        connectedOffline=findViewById(R.id.tv_connected_offline);
        activeUsers=findViewById(R.id.tv_active_users);
        date=findViewById(R.id.tv_date);
        lastActive=findViewById(R.id.tv_recent_active);
        tvRecentOwners=findViewById(R.id.tv_recent_owners);
        tvRecentRenters=findViewById(R.id.tv_recent_renters);

        //getting data from server
        server = FirebaseDatabase.getInstance().getReference().child("Server");
        server.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                tvOwners.setText("Total Owners : "+snapshot.child("total owners").getValue().toString());
                tvRenters.setText("Total Renters : "+snapshot.child("total renters").getValue().toString());
                store.setText("Users from Play store : "+snapshot.child("store").getValue().toString());
                ads.setText("Users from ads : "+snapshot.child("advertisement").getValue().toString());
                frnd.setText("Users from Friend suggested : "+snapshot.child("frnd").getValue().toString());
                net.setText("Users from Internet : "+snapshot.child("net").getValue().toString());
                tvTotal.setText("Total Listings posted : "+snapshot.child("total listings").getValue().toString());
                pro.setText("Total Pro users : "+snapshot.child("total pro users").getValue().toString());

                properties.setText("Properties : "+snapshot.child("total properties").getValue().toString());
                cameras.setText("Cameras : "+snapshot.child("total cameras").getValue().toString());
                vehicles.setText("Vehicles : "+snapshot.child("total vehicles").getValue().toString());
                tools.setText("Tools : "+snapshot.child("total tools").getValue().toString());
                cloths.setText("Cloths : "+snapshot.child("total cloths").getValue().toString());
                functions.setText("Functions : "+snapshot.child("total functions").getValue().toString());
                music.setText("Musics : "+snapshot.child("total Music").getValue().toString());
                gadgets.setText("Gadgets : "+snapshot.child("total gadgets").getValue().toString());
                books.setText("Books : "+snapshot.child("total books").getValue().toString());

                connected.setText("Total users connected : "+snapshot.child("total connected").getValue().toString());
                connectedOffline.setText("Total users connected offline : "+snapshot.child("total connected offline").getValue().toString());

                String raw = snapshot.child("active users").getValue().toString();
                String[] split = raw.split("/");
                activeUsers.setText("Active Users/day : "+split[0]);
                date.setText("Date : "+split[1]);

                if (lastListings!=null) {
                    int sub = Integer.parseInt(snapshot.child("total listings").getValue().toString()) - Integer.parseInt(lastListings);
                    tvRecent1.setText("Recently posted : "+sub);
                }

                if (lastPro!=null) {
                    int sub = Integer.parseInt(snapshot.child("total pro users").getValue().toString()) - Integer.parseInt(lastPro);
                    tvRecent2.setText("Recent buyers : "+sub);
                }

                if (lastActiveUsers!=null){
                    int sub =Integer.parseInt(split[0]) - Integer.parseInt(lastActiveUsers);
                    lastActive.setText("Recently opened : "+sub);
                }

                if (lastOwners!=null){
                    int sub =Integer.parseInt(snapshot.child("total owners").getValue().toString()) - Integer.parseInt(lastOwners);
                    tvRecentOwners.setText("Recently owners : "+sub);
                }

                if (lastRenters!=null){
                    int sub =Integer.parseInt(snapshot.child("total renters").getValue().toString()) - Integer.parseInt(lastRenters);
                    tvRecentRenters.setText("Recently renters : "+sub);
                }

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(KEY_TOTAL_LISTINGS,snapshot.child("total listings").getValue().toString());
                editor.putString(KEY_TOTAL_PRO,snapshot.child("total pro users").getValue().toString());
                editor.putString(KEY_TOTAL_ACTIVE,split[0]);
                editor.putString(KEY_OWNERS,snapshot.child("total owners").getValue().toString());
                editor.putString(KEY_RENTERS,snapshot.child("total renters").getValue().toString());
                editor.apply();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        promote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Utils.startActivity(StatisticsActivity.this,PromoteActivity.class);
            }
        });

        spam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.startActivity(StatisticsActivity.this,SpamListActivity.class);
            }
        });

        booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(StatisticsActivity.this,BookingListActivity.class);
                i.putExtra("bORc","b");
                startActivity(i);
            }
        });

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(StatisticsActivity.this,BookingListActivity.class);
                i.putExtra("bORc","c");
                startActivity(i);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}