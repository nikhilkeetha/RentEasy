package io.kodular.nsmarttechnologies6.RentEasy.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import io.kodular.nsmarttechnologies6.RentEasy.R;
import io.kodular.nsmarttechnologies6.RentEasy.Utility.AutoSliders;
import io.kodular.nsmarttechnologies6.RentEasy.Utility.Category;
import io.kodular.nsmarttechnologies6.RentEasy.Utility.HomeItems;
import io.kodular.nsmarttechnologies6.RentEasy.Utility.Review;
import io.kodular.nsmarttechnologies6.RentEasy.activity.CityChooserActivity;
import io.kodular.nsmarttechnologies6.RentEasy.activity.HomeActivity;
import io.kodular.nsmarttechnologies6.RentEasy.activity.NotificationsActivity;
import io.kodular.nsmarttechnologies6.RentEasy.activity.SearchListActivity;
import io.kodular.nsmarttechnologies6.RentEasy.adapter.TestnomialAdapter;
import io.kodular.nsmarttechnologies6.RentEasy.adapter.HomeCategoryAdapter;
import io.kodular.nsmarttechnologies6.RentEasy.adapter.HomeItemAdapter;
import io.kodular.nsmarttechnologies6.RentEasy.adapter.HomeAutoSliderAdapter;
import io.kodular.nsmarttechnologies6.RentEasy.support.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static android.app.Activity.RESULT_FIRST_USER;
import static android.content.Context.MODE_PRIVATE;

public class HomeFragment extends Fragment implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {

    private AdView mAdView; // banner ad view
    SharedPreferences sharedPreferences,ActiveSp,CitySp;
    DatabaseReference getCurrentVersion,getListingData,getCategoryData,getAd,totalActiveUsers;
    FusedLocationProviderClient fusedLocationProviderClient;
    String currentVersion,version="37",updateNeeded="f";
    String CurrentLocationPincode="null",currentVillageNm;
    String activeUsersfromDB="";
    Dialog dialog;
    private static final String SHARED_PREF_NM = "myPref",KEY_NM = "name",SP_DATE = "myDate",KEY_DATE = "date",SP_CITY = "myCity",KEY_CITY = "city",KEY_CITYID = "cityId";
    boolean onceCategory=true,onceListing=true,onceActiveUser=true,loadOnce=true,loadOnce2=true;

    @SuppressLint("StaticFieldLeak")
    private static HomeFragment fragment;
    private Activity mActivity;
    private View rootView;
    ProgressBar pbCamera,pbProperties,pbVehicles,pbTools,pbParty;
    io.RentEasy.views.RegularTextView cityNM;

    List<Review> list2;
    SliderView sliderView,sliderView2;

    List<Category> list3;
    HomeCategoryAdapter adapter3;

    List<HomeItems> list4,list5,list6,list7,list8;
    HomeItemAdapter adapter4,adapter5,adapter6,adapter7,adapter8;

    int[] images3 = {R.drawable.home__1_,
            R.drawable.function_hall,
            R.drawable.cars,
            R.drawable.camera_photography,R.drawable.shops,R.drawable.bike}; // category images

    int var = 0,var1=0,var2=0,var3=0,var4=0,var5=0,auto=0,testmonials=0;

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

        pbCamera = rootView.findViewById(R.id.pb_camera);
        pbProperties = rootView.findViewById(R.id.pb_properties);
        pbVehicles = rootView.findViewById(R.id.pb_vehicles);
        pbTools = rootView.findViewById(R.id.pb_tools);
        pbParty = rootView.findViewById(R.id.pb_party);
        cityNM = rootView.findViewById(R.id.tv_city_nm);

        /*
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(mActivity);
        if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getLocation();
            pbRecommended.setVisibility(View.VISIBLE);
            recommendedTitle.setVisibility(View.VISIBLE);
        } else {
            ActivityCompat.requestPermissions(mActivity,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);
            pbRecommended.setVisibility(View.GONE);
            recommendedTitle.setVisibility(View.GONE);
        }

         */

        CitySp = getActivity().getSharedPreferences(SP_CITY,MODE_PRIVATE);
        String city = CitySp.getString(KEY_CITY,"null");
        String cityID = CitySp.getString(KEY_CITYID,"1");
        if (city.equals("null")) {
            cityNM.setText("Hyderabad");
        }else {
            cityNM.setText(city);
        }

        setClickListener();
        setUpAutoScroll();
        setUpCategoryItems();
        setUpHappyUser();
        getCategoryData(cityID);

        // load ad
        mAdView = rootView.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        sharedPreferences = getActivity().getSharedPreferences(SHARED_PREF_NM,MODE_PRIVATE);
        String Nm = sharedPreferences.getString(KEY_NM,null);
        io.RentEasy.views.MediumTextView welcome = rootView.findViewById(R.id.tv_user_name);
        if (Nm !=null){
            if (Nm.contains(" ")) {
                String[] split = Nm.split(" ");
                welcome.setText("Hi! " + split[0]+",");
            } else {
                welcome.setText("Hi! " + Nm+",");
            }
        }


        getCurrentVersion = FirebaseDatabase.getInstance().getReference().child("Server"); // gets the current version from firebase database
        getCurrentVersion.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("update needed")) {
                    updateNeeded=dataSnapshot.child("update needed").getValue().toString();
                }

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

        totalActiveUsers=FirebaseDatabase.getInstance().getReference().child("Server");
        totalActiveUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (onceActiveUser==true) {
                    onceActiveUser=false;
                    activeUsersfromDB = dataSnapshot.child("active users").getValue().toString();
                    activeUsers();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /*
    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if (location != null) {
                    try {
                        Geocoder geocoder = new Geocoder(mActivity, Locale.getDefault());
                        List<Address> addresses = geocoder.getFromLocation(
                                location.getLatitude(),location.getLongitude(),1
                        );
                        currentVillageNm = addresses.get(0).getLocality();
                        CurrentLocationPincode = addresses.get(0).getPostalCode();
                        if (CurrentLocationPincode.equals("508204")) {
                            ImageView ivAd = rootView.findViewById(R.id.ivAd);
                            ivAd.setVisibility(View.VISIBLE);
                            getAd =FirebaseDatabase.getInstance().getReference().child("Server");
                            getAd.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.hasChild("hnr ad url")) {
                                        String url = dataSnapshot.child("hnr ad url").getValue().toString();
                                        Glide
                                                .with(mActivity)
                                                .load(url)
                                                .into(ivAd);
                                        phNm = dataSnapshot.child("hnr ad phno").getValue().toString();
                                    }else {
                                        ivAd.setVisibility(View.GONE);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                            ivAd.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Uri uri= Uri.parse(phNm);
                                    startActivity(new Intent(Intent.ACTION_VIEW,uri));
                                }
                            });
                        }
                        if (!CurrentLocationPincode.equals("null")) {
                            getListingData();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

     */

    private void activeUsers() {

        SimpleDateFormat sdf = new SimpleDateFormat("dd,MM,yyyy");
        String currentDateandTime = sdf.format(new Date());

        ActiveSp = getActivity().getSharedPreferences(SP_DATE,MODE_PRIVATE);
        String lastOpenedDate = ActiveSp.getString(KEY_DATE,null);

        String[] split = activeUsersfromDB.split("/");
        String activeUsersCount = split[0];
        String date=split[1];

        String finalUsers="1";


        String[] splitCurrentDate = currentDateandTime.split(",");
        String curYear=splitCurrentDate[2];
        String curMonth=splitCurrentDate[1];
        String curDay=splitCurrentDate[0];


        String[] splitDBDate=date.split(",");
        String year=splitDBDate[2];
        String month=splitDBDate[1];
        String day=splitDBDate[0];

        if (curYear.equals(year)) {
            if (curMonth.equals(month)) {
                if (curDay.equals(day)) {
                    int add = Integer.parseInt(activeUsersCount) + 1;
                    finalUsers = String.valueOf(add);
                }else {
                    HashMap hashMap = new HashMap();
                    hashMap.put("active users",finalUsers+"/"+currentDateandTime);
                    totalActiveUsers.updateChildren(hashMap);
                }
            }else {
                HashMap hashMap = new HashMap();
                hashMap.put("active users",finalUsers+"/"+currentDateandTime);
                totalActiveUsers.updateChildren(hashMap);
            }
        }else {
            HashMap hashMap = new HashMap();
            hashMap.put("active users",finalUsers+"/"+currentDateandTime);
            totalActiveUsers.updateChildren(hashMap);
        }

        if (lastOpenedDate!=null) {
            if (!lastOpenedDate.equals(currentDateandTime)) {
                SharedPreferences.Editor editor = ActiveSp.edit();
                editor.putString(KEY_DATE,currentDateandTime);
                editor.apply();
                // increment  for active users
                HashMap hashMap = new HashMap();
                hashMap.put("active users",finalUsers+"/"+currentDateandTime);
                totalActiveUsers.updateChildren(hashMap);
            }
        }else {
            SharedPreferences.Editor editor = ActiveSp.edit();
            editor.putString(KEY_DATE,currentDateandTime);
            editor.apply();
            // increment  for active users
            HashMap hashMap = new HashMap();
            hashMap.put("active users",finalUsers+"/"+currentDateandTime);
            totalActiveUsers.updateChildren(hashMap);
        }

    }

    private void getCategoryData(String Id) {

        pbProperties.setVisibility(View.VISIBLE);
        pbParty.setVisibility(View.VISIBLE);
        pbTools.setVisibility(View.VISIBLE);
        //pbVehicles.setVisibility(View.VISIBLE);
        //pbCamera.setVisibility(View.VISIBLE);


        var = 0;var1=0;var2=0;var3=0;var4=0;var5=0;

        list4 = new ArrayList<>();
        list5 = new ArrayList<>();
        list6 = new ArrayList<>();
        list7 = new ArrayList<>();
        list8 = new ArrayList<>();

        String nmCity = cityNM.getText().toString();


        getCategoryData = FirebaseDatabase.getInstance().getReference().child("home items");
        getCategoryData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (onceCategory==true){
                    onceCategory=false;
                    // data of houses
                    String total = String.valueOf(dataSnapshot.child("1").getChildrenCount());
                    for (DataSnapshot ds : dataSnapshot.child("1").getChildren()) {
                        var++;
                        String pincode = ds.child("0").getValue().toString();
                        String id = ds.child("1").getValue().toString();
                        getListingData = FirebaseDatabase.getInstance().getReference().child("listings data").child(pincode).child(id);
                        getListingData.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.hasChild("is deleted")) {
                                    // if the listing is deleted by owner then cross
                                } else {
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
                                    
                                    String ciID = dataSnapshot.child("20").getValue().toString();
                                    String areaId = dataSnapshot.child("21").getValue().toString();
                                    if (Id.equals(ciID)) {
                                        list5.add(new HomeItems(Rate,title,imageUrl,pincode,id,areaId,nmCity));
                                    }
                                    if (Integer.parseInt(total) == var) {
                                        pbProperties.setVisibility(View.GONE);
                                        setUpHouses();
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                    //data of photographers
                    if (dataSnapshot.hasChild("2")) {
                        String total1 = String.valueOf(dataSnapshot.child("2").getChildrenCount());
                        for (DataSnapshot ds : dataSnapshot.child("2").getChildren()) {
                            var1++;
                            String pincode = ds.child("0").getValue().toString();
                            String id = ds.child("1").getValue().toString();
                            getListingData = FirebaseDatabase.getInstance().getReference().child("listings data").child(pincode).child(id);
                            getListingData.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.hasChild("is deleted")) {
                                        // if the listing is deleted by owner then cross
                                    } else {
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

                                        String ciID = dataSnapshot.child("20").getValue().toString();
                                        String areaId = dataSnapshot.child("21").getValue().toString();
                                        if (Id.equals(ciID)) {
                                            list4.add(new HomeItems(Rate, title, imageUrl, pincode, id, areaId, nmCity));
                                        }
                                        if (Integer.parseInt(total1) == var1) {
                                            pbCamera.setVisibility(View.GONE);
                                            setPhotographersItems();
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    }

                    /*

                    // data of vehicles
                    String total2 = String.valueOf(dataSnapshot.child("3").getChildrenCount());
                    for (DataSnapshot ds : dataSnapshot.child("3").getChildren()) {
                        var2++;
                        String pincode = ds.child("0").getValue().toString();
                        String id = ds.child("1").getValue().toString();
                        getListingData = FirebaseDatabase.getInstance().getReference().child("listings data").child(pincode).child(id);
                        getListingData.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.hasChild("is deleted")) {
                                    // if the listing is deleted by owner then cross
                                } else {
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
                                    String ciID = dataSnapshot.child("20").getValue().toString();
                                    String areaId = dataSnapshot.child("21").getValue().toString();
                                    if (Id.equals(ciID)) {
                                        list6.add(new HomeItems(Rate,title,imageUrl,pincode,id,areaId,nmCity));
                                    }
                                    if (Integer.parseInt(total2) == var2) {
                                        pbVehicles.setVisibility(View.GONE);
                                        setUpVechicles();
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                    */

                    //data of function halls
                    String total3 = String.valueOf(dataSnapshot.child("4").getChildrenCount());
                    for (DataSnapshot ds : dataSnapshot.child("4").getChildren()) {
                        var3++;
                        String pincode = ds.child("0").getValue().toString();
                        String id = ds.child("1").getValue().toString();
                        getListingData = FirebaseDatabase.getInstance().getReference().child("listings data").child(pincode).child(id);
                        getListingData.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.hasChild("is deleted")) {
                                    // if the listing is deleted by owner then cross
                                } else {
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
                                    String ciID = dataSnapshot.child("20").getValue().toString();
                                    String areaId = dataSnapshot.child("21").getValue().toString();
                                    if (Id.equals(ciID)) {
                                        list7.add(new HomeItems(Rate, title, imageUrl, pincode, id, areaId, nmCity));
                                    }
                                    if (Integer.parseInt(total3) == var3) {
                                        pbTools.setVisibility(View.GONE);
                                        setUpFunctionHalls();
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                    //data of shops
                    String total4 = String.valueOf(dataSnapshot.child("5").getChildrenCount());
                    for (DataSnapshot ds : dataSnapshot.child("5").getChildren()) {
                        var4++;
                        String pincode = ds.child("0").getValue().toString();
                        String id = ds.child("1").getValue().toString();
                        getListingData = FirebaseDatabase.getInstance().getReference().child("listings data").child(pincode).child(id);
                        getListingData.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.hasChild("is deleted")) {
                                    // if the listing is deleted by owner then cross
                                } else {
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
                                    String ciID = dataSnapshot.child("20").getValue().toString();
                                    String areaId = dataSnapshot.child("21").getValue().toString();
                                    if (Id.equals(ciID)) {
                                        list8.add(new HomeItems(Rate,title,imageUrl,pincode,id,areaId,nmCity));
                                    }
                                    if (Integer.parseInt(total4) == var4) {
                                        pbParty.setVisibility(View.GONE);
                                        setUpShops();
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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

        ImageView ivWhatsappHelp = rootView.findViewById(R.id.iv_whatsapp_help);
        ivWhatsappHelp.setOnClickListener(this);

        RelativeLayout cityLay = rootView.findViewById(R.id.city_change_lay);
        cityLay.setOnClickListener(this);
    }

    private void setUpCategoryItems() {
        list3 = new ArrayList<>();
        list3.add(new Category("Houses"));
        list3.add(new Category("Banquets"));
        list3.add(new Category("Cars"));
        list3.add(new Category("Shoots"));
        list3.add(new Category("Shops"));
        list3.add(new Category("Bikes"));
        RecyclerView recyclerView = rootView.findViewById(R.id.recycler_post_property);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity, RecyclerView.HORIZONTAL, false));
        adapter3=new HomeCategoryAdapter(list3, getActivity(),images3);
        recyclerView.setAdapter(adapter3);
        adapter3.notifyDataSetChanged();
    }

    private void setUpAutoScroll() {
        List<AutoSliders> listAuto;
        listAuto = new ArrayList<>();
        sliderView = rootView.findViewById(R.id.image_slider);

        DatabaseReference getUrls = FirebaseDatabase.getInstance().getReference();
        getUrls.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (loadOnce) {
                    loadOnce=false;
                    if (dataSnapshot.hasChild("auto slider")) {
                        String tota = String.valueOf(dataSnapshot.child("auto slider").getChildrenCount());
                        for (DataSnapshot ds : dataSnapshot.child("auto slider").getChildren()) {
                            auto++;
                            if (ds.getValue().toString().equals("null")) {
                                RelativeLayout autoLay = rootView.findViewById(R.id.auto_scroll_lay);
                                autoLay.setVisibility(View.GONE);
                            }else {
                                listAuto.add(new AutoSliders(ds.getValue().toString()));
                                if (auto == Integer.parseInt(tota)) {
                                    HomeAutoSliderAdapter homeSliderAdapter = new HomeAutoSliderAdapter(listAuto, getActivity());
                                    sliderView.setSliderAdapter(homeSliderAdapter);
                                    sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
                                    sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
                                    sliderView.startAutoCycle();
                                }
                            }
                        }
                    }else {
                        RelativeLayout autoLay = rootView.findViewById(R.id.auto_scroll_lay);
                        autoLay.setVisibility(View.GONE);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setUpHappyUser() {
        list2 = new ArrayList<>();

        DatabaseReference getUrls = FirebaseDatabase.getInstance().getReference().child("testimonials");
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
                            sliderView2 = rootView.findViewById(R.id.testnomial_slider);
                            TestnomialAdapter testnomialAdapter = new TestnomialAdapter(list2,getActivity());
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

    private void setPhotographersItems() {
        RecyclerView recyclerView = rootView.findViewById(R.id.recycler_cameras);
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity, RecyclerView.HORIZONTAL, false));
        adapter4 = new HomeItemAdapter(list4, getActivity());
        recyclerView.setAdapter(adapter4);
        adapter4.notifyDataSetChanged();
        TextView tv_display = rootView.findViewById(R.id.tv_diplay_photograaphers);
        tv_display.setVisibility(View.VISIBLE);
    }

    private void setUpHouses() {
        RecyclerView recyclerView = rootView.findViewById(R.id.recycler_properties);
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity, RecyclerView.HORIZONTAL, false));
        adapter5 = new HomeItemAdapter(list5, getActivity());
        recyclerView.setAdapter(adapter5);
        adapter5.notifyDataSetChanged();
    }

    private void setUpVechicles() {
        RecyclerView recyclerView = rootView.findViewById(R.id.recycler_vehicles);
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity, RecyclerView.HORIZONTAL, false));
        adapter6=new HomeItemAdapter(list6, getActivity());
        recyclerView.setAdapter(adapter6);
        adapter6.notifyDataSetChanged();
    }

    private void setUpFunctionHalls() {
        RecyclerView recyclerView = rootView.findViewById(R.id.recycler_construction);
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity, RecyclerView.HORIZONTAL, false));
        adapter7 = new HomeItemAdapter(list7, getActivity());
        recyclerView.setAdapter(adapter7);
        adapter7.notifyDataSetChanged();
    }

    private void setUpShops() {
        RecyclerView recyclerView = rootView.findViewById(R.id.recycler_function);
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity, RecyclerView.HORIZONTAL, false));
        adapter8 = new HomeItemAdapter(list8, getActivity());
        recyclerView.setAdapter(adapter8);
        adapter8.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_like:
                Intent intent = new Intent(getActivity(), SearchListActivity.class);
                intent.putExtra("pincode", "null");
                intent.putExtra("is_Avialable", "null");
                intent.putExtra("category","null");
                intent.putExtra("isLiked","true");
                intent.putExtra("home-category","null");
                getActivity().startActivity(intent);
                break;
            case R.id.iv_notification:
                Utils.startActivity(mActivity, NotificationsActivity.class);
                break;
            case R.id.iv_whatsapp_help:
                gotoUrl("https://wa.me/message/GPYXADT7J2MDM1");
                break;
            case R.id.city_change_lay:
                Intent i = new Intent(getActivity().getApplicationContext(), CityChooserActivity.class);
                i.putExtra("search","false");
                startActivityForResult(new Intent(i),999);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 999 && resultCode == RESULT_FIRST_USER) {
            String res = data.getStringExtra("city");
            cityNM.setText(res);
            if (res.equals("Hyderabad")) {
                cityChanged(res,"1");
            }
            if (res.equals("Visakhapatnam")) {
                cityChanged(res,"2");
            }
            if (res.equals("Delhi")) {
                cityChanged(res,"3");
            }
            if (res.equals("Bengaluru")) {
                cityChanged(res,"4");
            }
            if (res.equals("Mumbai")) {
                cityChanged(res,"5");
            }
        }
    }

    private void cityChanged(String city,String id) {
        SharedPreferences.Editor editor = CitySp.edit();
        editor.putString(KEY_CITY,city);
        editor.putString(KEY_CITYID,id);
        editor.apply();
        onceCategory=true;
        getCategoryData(id);
    }

    private void showPopuhpMenu(View view) {
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

        io.RentEasy.views.MediumTextView close = dialog.findViewById(R.id.tv_close_dialog);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        if (updateNeeded.equals("t")) {
            close.setVisibility(View.GONE);
        }

        dialog.show();
    }

    public void setLocal(Activity activity,String langCode) {
        Locale locale = new Locale(langCode);
        locale.setDefault(locale);
        Resources resources = activity.getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration (config, resources.getDisplayMetrics());
    }
}
