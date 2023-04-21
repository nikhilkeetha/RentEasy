package io.kodular.nsmarttechnologies6.RentEasy.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import io.kodular.nsmarttechnologies6.RentEasy.R;
import io.kodular.nsmarttechnologies6.RentEasy.Utility.HomeProperty;
import io.kodular.nsmarttechnologies6.RentEasy.Utility.Property;
import io.kodular.nsmarttechnologies6.RentEasy.Utility.Review;
import io.kodular.nsmarttechnologies6.RentEasy.activity.HomeActivity;
import io.kodular.nsmarttechnologies6.RentEasy.activity.NotificationsActivity;
import io.kodular.nsmarttechnologies6.RentEasy.activity.RecommendedPropertyActivity;
import io.kodular.nsmarttechnologies6.RentEasy.activity.RewardsActivity;
import io.kodular.nsmarttechnologies6.RentEasy.activity.SplashActivity;
import io.kodular.nsmarttechnologies6.RentEasy.adapter.HomeHappyUserAdapter;
import io.kodular.nsmarttechnologies6.RentEasy.adapter.HomePostPropertiesAdapter;
import io.kodular.nsmarttechnologies6.RentEasy.adapter.HomePropertySliderAdapter;
import io.kodular.nsmarttechnologies6.RentEasy.adapter.HomeRecommendedPropertiesAdapter;
import io.kodular.nsmarttechnologies6.RentEasy.adapter.RecommendedPropertyAdapter;
import io.kodular.nsmarttechnologies6.RentEasy.support.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import me.angeldevil.autoscrollviewpager.AutoScrollViewPager;

import static android.content.Context.MODE_PRIVATE;

public class HomeFragment extends Fragment implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {

    private AdView mAdView; // banner ad view

    SharedPreferences sharedPreferences;
    DatabaseReference getCurrentVersion;
    String currentVersion,version="32";
    Dialog dialog;
    private static final String SHARED_PREF_NM = "myPref";
    private static final String KEY_NM = "name";

    @SuppressLint("StaticFieldLeak")
    private static HomeFragment fragment;
    private Activity mActivity;
    private View rootView;

    List<HomeProperty> list;
    HomeRecommendedPropertiesAdapter adapter;

    HomeHappyUserAdapter adapter2;
    List<Review> list2;
    SliderView sliderView;

    int[] images = {R.drawable.one,
    R.drawable.two,
    R.drawable.three,
    R.drawable.four}; // image slider image ids

    int[] images2 = {R.drawable.review1,
            R.drawable.review2,
            R.drawable.review3}; // user review icons


    public static HomeFragment newInstance() {
        fragment = new HomeFragment();
        return fragment;
    }

    public static HomeFragment getInstance() {
        if (fragment == null)
            return new HomeFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        init();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        requireView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                ((HomeActivity) Objects.requireNonNull(mActivity)).showAlertDialogExit();
                return true;
            }
            return false;
        });
        ((HomeActivity) mActivity).resetAll("home");
    }

    @SuppressLint("SetTextI18n")
    private void init() {
        mActivity = getActivity();
        dialog =new Dialog(mActivity);
        setClickListener();
        setUpAutoScroll();
        setUpPostProperty();
        getData();
        setUpRecommendedProperty();
        setUpHappyUser();
        // load ad
        mAdView = rootView.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        sharedPreferences = getActivity().getSharedPreferences(SHARED_PREF_NM,MODE_PRIVATE);
        String Nm = sharedPreferences.getString(KEY_NM,null);
        TextView welcome = rootView.findViewById(R.id.tv_user_name);
        if (Nm !=null){
            welcome.setText("Hello " + Nm);
        }
        getCurrentVersion = FirebaseDatabase.getInstance().getReference().child("Server"); // gets the current version from firebase database
        getCurrentVersion.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("version")) {
                    currentVersion = dataSnapshot.child("version").getValue().toString();
                    if (Integer.parseInt(currentVersion) > Integer.parseInt(version)) {
                        openUpdateDialog();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getData() { // home recommedned properties data
        list = new ArrayList<>();

        list.add(new HomeProperty("₹5000 per month","2BHK in Arjun Nagar, 122001, Haryana.","https://firebasestorage.googleapis.com/v0/b/renteasy-f31ba.appspot.com/o/7789455.png?alt=media&token=7203f096-bd5e-4847-b92a-48eb434ec3e3","122001","1","H"));
        list.add(new HomeProperty("₹9000 per month","2BHK in Tambaram, 600063, Tamil Nadu.","https://firebasestorage.googleapis.com/v0/b/renteasy-f31ba.appspot.com/o/7202133.png?alt=media&token=c04f68fc-2a2e-4b3e-9f62-7e3fc0b01a5c","600063","1","H"));
        list.add(new HomeProperty("₹24000 per month","1BHK in Chembur, 400080, Maharastra.","https://firebasestorage.googleapis.com/v0/b/renteasy-f31ba.appspot.com/o/1667084.png?alt=media&token=ff91e933-7640-4e48-9244-354a8cb73a31","400080","1","H"));
        list.add(new HomeProperty("₹9000 per month","2BHK in Arjun Nagar, 122001, Haryana.","https://firebasestorage.googleapis.com/v0/b/renteasy-f31ba.appspot.com/o/4097431.png?alt=media&token=50961fa8-7521-4eed-9681-1e5ad50fbc1a","122001","4","H"));
        list.add(new HomeProperty("₹7000 per month","Shop in Secunderabad, 500003, Telangana.","https://firebasestorage.googleapis.com/v0/b/renteasy-f31ba.appspot.com/o/3457809.png?alt=media&token=390ee4f9-a79e-4d83-a461-3af255701a35","500003","1","S"));
    }


    private void setClickListener() {
        RelativeLayout rewardsLay = rootView.findViewById(R.id.rewards_lay);
        rewardsLay.setOnClickListener(this);
        ImageView iv_like = rootView.findViewById(R.id.iv_like);
        iv_like.setOnClickListener(this);
        ImageView iv_notification = rootView.findViewById(R.id.iv_notification);
        iv_notification.setOnClickListener(this);
        ImageView iv_menu = rootView.findViewById(R.id.iv_menu);
        iv_menu.setOnClickListener(this);
    }


    private void setUpAutoScroll() {
        sliderView = rootView.findViewById(R.id.image_slider);
        HomePropertySliderAdapter homeSliderAdapter = new HomePropertySliderAdapter(images);
        sliderView.setSliderAdapter(homeSliderAdapter);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION);
        sliderView.startAutoCycle();
    }

    private void setUpPostProperty() {
        RecyclerView recyclerView = rootView.findViewById(R.id.recycler_post_property);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity, RecyclerView.HORIZONTAL, false));
        recyclerView.setAdapter(new HomePostPropertiesAdapter(mActivity));
    }

    private void setUpRecommendedProperty() {
        RecyclerView recyclerView = rootView.findViewById(R.id.recycler_recommended_property);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity, RecyclerView.HORIZONTAL, false));
        adapter=new HomeRecommendedPropertiesAdapter(list, getActivity());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void setUpHappyUser() {
        list2 = new ArrayList<>();
        list2.add(new Review("I found a 2BHK for rent using renteasy. I have got transfered then i dont know about the new place then i installed renteasy app.","Rahul varma","Mumbai"));
        list2.add(new Review("Paying rent online is good and very easy with this app. This app notifies every month to pay our rent.\n","Shoba Iyer","Bangulore"));
        list2.add(new Review("I recommend this app for posting a house for rent. I have a property which has no tenants from last 6 month so i tried all the ways but RentEasy helped me a lot.","Srinivas","Hyderabad"));
        RecyclerView recyclerView = rootView.findViewById(R.id.recycler_happy_users);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity, RecyclerView.VERTICAL, false));
        adapter2=new HomeHappyUserAdapter(list2, getActivity(),images2);
        recyclerView.setAdapter(adapter2);
        adapter2.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rewards_lay:
                Utils.startActivity(mActivity, RewardsActivity.class);
                break;
            case R.id.iv_like:
                Utils.startActivity(mActivity, RecommendedPropertyActivity.class);
                break;
            case R.id.iv_notification:
                Utils.startActivity(mActivity, NotificationsActivity.class);
                break;
            case R.id.iv_menu:
                showPopupMenu(v);
                break;
        }
    }

    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
        popupMenu.inflate(R.menu.popup_menu_home);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_popup_feedback:
                gotoUrl("https://docs.google.com/forms/d/e/1FAIpQLSd-HtD-lKsxxJFquR7V917IP6N8lbdcT9NI59HKQS2THlQM2g/viewform?usp=sf_link");
                return true;

            default:
                return false;
        }
    }
    private void gotoUrl(String s) {
        Uri uri= Uri.parse(s);
        startActivity(new Intent(Intent.ACTION_VIEW,uri));
    }
    private void openUpdateDialog() {
        dialog.setContentView(R.layout.update_app_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        io.RentEasy.views.MediumTextView update = dialog.findViewById(R.id.tv_update);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoUrl("https://play.google.com/store/apps/details?id=io.kodular.nsmarttechnologies6.RentEasy");
            }
        });

        dialog.show();
    }
}
