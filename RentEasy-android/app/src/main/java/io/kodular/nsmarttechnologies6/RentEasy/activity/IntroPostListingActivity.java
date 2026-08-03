package io.kodular.nsmarttechnologies6.RentEasy.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

import io.kodular.nsmarttechnologies6.RentEasy.R;
import io.kodular.nsmarttechnologies6.RentEasy.Utility.NetworkChangeListener;
import io.kodular.nsmarttechnologies6.RentEasy.Utility.Review;
import io.kodular.nsmarttechnologies6.RentEasy.adapter.TestnomialAdapter;
import io.kodular.nsmarttechnologies6.RentEasy.support.Utils;

public class IntroPostListingActivity extends AppCompatActivity {

    io.RentEasy.views.MediumTextView Btn;
    ImageView ivClose;
    List<Review> list2;
    boolean loadOnce2=true;
    int testmonials=0;
    SliderView sliderView2;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    ImageView iv1,iv2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_post_listing);

        //assianing variables
        Btn=findViewById(R.id.continue_btn);
        ivClose=findViewById(R.id.ivClose);
        setUpHappyUser();
        iv1=findViewById(R.id.iv_video_english);
        iv2=findViewById(R.id.telugu_vid);


        iv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri= Uri.parse("https://youtu.be/QaNVM9-oJho"); //url of english youtube video
                startActivity(new Intent(Intent.ACTION_VIEW,uri));
            }
        });

        //getting telugu video link from server
        DatabaseReference dbLink = FirebaseDatabase.getInstance().getReference().child("Server").child("tutorial vid tel");
        dbLink.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.child("thumbnail").getValue().toString().equals("link")) {
                    Glide
                            .with(IntroPostListingActivity.this)
                            .load(dataSnapshot.child("thumbnail").getValue().toString())
                            .centerCrop()
                            .into(iv2);
                    iv2.setVisibility(View.VISIBLE);
                    iv2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Uri uri= Uri.parse(dataSnapshot.child("vid").getValue().toString());
                            startActivity(new Intent(Intent.ACTION_VIEW,uri));
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IntroPostListingActivity.this, HomeActivity.class);
                intent.putExtra("item", "home");
                startActivity(intent);
            }
        });

        Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IntroPostListingActivity.this, HomeActivity.class);
                intent.putExtra("item", "post");
                startActivity(intent);
            }
        });

    }

    private void setUpHappyUser() {
        list2 = new ArrayList<>();

        DatabaseReference getUrls = FirebaseDatabase.getInstance().getReference().child("testimonials2");
        getUrls.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (loadOnce2) {
                    loadOnce2=false;
                    String tota = String.valueOf(dataSnapshot.getChildrenCount());
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        testmonials++;
                        String des = ds.child("0").getValue().toString();
                        String nm = ds.child("1").getValue().toString();
                        String city = ds.child("2").getValue().toString();
                        String url = ds.child("3").getValue().toString();

                        list2.add(new Review(des,nm,city,url));
                        if (testmonials == Integer.parseInt(tota)) {
                            sliderView2 = findViewById(R.id.testnomial_slider);
                            TestnomialAdapter testnomialAdapter = new TestnomialAdapter(list2,IntroPostListingActivity.this);
                            sliderView2.setSliderAdapter(testnomialAdapter);
                            sliderView2.setIndicatorAnimation(IndicatorAnimationType.WORM);
                            sliderView2.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
                            sliderView2.startAutoCycle();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
    public void onBackPressed() {
        Intent intent = new Intent(IntroPostListingActivity.this, HomeActivity.class);
        intent.putExtra("item", "home");
        startActivity(intent);
    }
}