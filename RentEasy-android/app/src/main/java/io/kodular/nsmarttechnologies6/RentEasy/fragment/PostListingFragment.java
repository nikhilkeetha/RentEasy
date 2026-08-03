package io.kodular.nsmarttechnologies6.RentEasy.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import io.kodular.nsmarttechnologies6.RentEasy.R;

import io.kodular.nsmarttechnologies6.RentEasy.activity.BrandListActivity;
import io.kodular.nsmarttechnologies6.RentEasy.activity.CityChooserActivity;
import io.kodular.nsmarttechnologies6.RentEasy.activity.HomeActivity;

import io.kodular.nsmarttechnologies6.RentEasy.activity.PlacesSearcherActivity;
import io.kodular.nsmarttechnologies6.RentEasy.support.Utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_FIRST_USER;
import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static io.kodular.nsmarttechnologies6.RentEasy.R.drawable.btn_radio_off_mtrl;
import static io.kodular.nsmarttechnologies6.RentEasy.R.drawable.ic_check_circle;
import static io.kodular.nsmarttechnologies6.RentEasy.R.drawable.toast_popup_bg;

public class PostListingFragment extends Fragment implements View.OnClickListener {

    LinearLayout categoryLay,detailsLay,imagesLay,locationLay,priceLay;
    io.RentEasy.views.MediumTextView tvHeaderTitle,tvButton ,t1,t2,t4,t5,t6;
    CardView cv1,cv2,cv4,cv5,cv6;
    ImageView c1,c2,c4,c5,c6;
    int layout=1,cityId=0,brandId,areaId=0,clr=0;
    String Category="",roof="N";
    RelativeLayout cityLay,landmarkLay,sl1,sl2,sl3,sl4,sl5,sl6,SelectBrand;
    io.RentEasy.views.RegularTextView tvSelectedCity,tvSelectedBrand,tvSeletcdArea;
    Spinner s1,s2,s3,s4,s5,s6;
    boolean isBrandSelected=false;
    EditText etRentAmount;
    TextInputLayout inputDeposit;
    TextInputEditText etDeposit;
    String pincode;

    private AdView mAdView;
    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NM = "myPref",KEY_PHONENO = "number";
    Dialog dialog;
    int SELECT_PHOTO = 1;
    Uri uri1,uri2,uri3,uri4,uri5;
    int var = 0; // to check how many images added
    int id=1;
    boolean isStarted = false,I1Added=false,I2Added=false,I3Added=false,I4Added=false,I5Added=false,isLocationAdded=false,data=true,ownerData=true,categoryData=true,serverData=true;

    String I1Url = "No";
    String I2Url = "No";
    String I3Url = "No";
    String I4Url = "No";
    String I5Url = "No";
    String rent = "",cycle="m";
    String Status = "A";
    String UserPhnNm = "";
    String Lat = "20.5937";
    String Lng = "78.9629";

    TextInputEditText etAddress,etTitle,etDes;
    io.RentEasy.views.RegularEditText etName,etPho;
    FusedLocationProviderClient fusedLocationProviderClient;
    boolean develper =true;

    @SuppressLint("StaticFieldLeak")
    private static PostListingFragment fragment;
    private Activity mActivity;
    private View rootView;

    DatabaseReference postListing,addUserData,updateServer,developerAccount;

    private FirebaseStorage storage;
    private StorageReference storageReference;


    public static PostListingFragment newInstance() {
        return fragment;
    }

    public static PostListingFragment getInstance() {
        if (fragment == null)
            return new PostListingFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_post_listing, container, false);
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
                showAlertDialogExit();
            }
            return true;
        });
    }

    @SuppressLint("SetTextI18n")
    private void init() {
        mActivity = getActivity();

        cv1=rootView.findViewById(R.id.cv_1);
        cv2=rootView.findViewById(R.id.cv_2);
        cv4=rootView.findViewById(R.id.cv_4);
        cv5=rootView.findViewById(R.id.cv_5);
        cv6=rootView.findViewById(R.id.cv_6);

        c1=rootView.findViewById(R.id.check1);
        c2=rootView.findViewById(R.id.check2);
        c4=rootView.findViewById(R.id.check4);
        c5=rootView.findViewById(R.id.check5);
        c6=rootView.findViewById(R.id.check6);

        t1=rootView.findViewById(R.id.tv_itm_1);
        t2=rootView.findViewById(R.id.tv_itm_2);
        t4=rootView.findViewById(R.id.tv_itm_4);
        t5=rootView.findViewById(R.id.tv_itm_5);
        t6=rootView.findViewById(R.id.tv_itm_6);

        SelectBrand=rootView.findViewById(R.id.select_brand_lay);
        tvSelectedBrand=rootView.findViewById(R.id.tv_selected_brand);
        etRentAmount=rootView.findViewById(R.id.et_rent_amount);
        inputDeposit=rootView.findViewById(R.id.et_deposit_lay);
        etDeposit=rootView.findViewById(R.id.input_deposit);
        tvSelectedCity=rootView.findViewById(R.id.tv_selected_city);
        tvSeletcdArea=rootView.findViewById(R.id.tv_selected_landmark);

        setClickListener();
        dialog =new Dialog(getActivity());
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(mActivity);

        mAdView = rootView.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        sharedPreferences = getActivity().getSharedPreferences(SHARED_PREF_NM,MODE_PRIVATE);
        UserPhnNm = sharedPreferences.getString(KEY_PHONENO,null);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        etTitle = rootView.findViewById(R.id.et_title);
        etDes = rootView.findViewById(R.id.et_description);
        etAddress = rootView.findViewById(R.id.et_address);

        etName = rootView.findViewById(R.id.et_ownerName);
        etPho = rootView.findViewById(R.id.et_ownerPho);

        categoryLay =rootView.findViewById(R.id.category_layout);
        detailsLay=rootView.findViewById(R.id.details_layout);
        imagesLay=rootView.findViewById(R.id.images_layout);
        locationLay=rootView.findViewById(R.id.location_layout);
        priceLay=rootView.findViewById(R.id.price_layout);

        tvHeaderTitle=rootView.findViewById(R.id.tv_header_title);
        tvButton=rootView.findViewById(R.id.tv_button);

        s1=rootView.findViewById(R.id.spinner_1);
        s2=rootView.findViewById(R.id.spinner_2);
        s3=rootView.findViewById(R.id.spinner_3);
        s4=rootView.findViewById(R.id.spinner_4);
        s5=rootView.findViewById(R.id.spinner_5);
        s6=rootView.findViewById(R.id.spinner_6);

        sl1=rootView.findViewById(R.id.lay_spin_1);
        sl2=rootView.findViewById(R.id.lay_spin_2);
        sl3=rootView.findViewById(R.id.lay_spin_3);
        sl4=rootView.findViewById(R.id.lay_spin_4);
        sl5=rootView.findViewById(R.id.lay_spin_5);
        sl6=rootView.findViewById(R.id.lay_spin_6);

    }

    private void setClickListener() {
        RelativeLayout backLay = rootView.findViewById(R.id.back_lay);
        backLay.setOnClickListener(this);
        LinearLayout postLay = rootView.findViewById(R.id.post_lay);
        postLay.setOnClickListener(this);
        RelativeLayout imageLay = rootView.findViewById(R.id.imageLay);
        imageLay.setOnClickListener(this);
        CardView cvMap =rootView.findViewById(R.id.cv_map);
        cvMap.setOnClickListener(this);
        ImageView ivMenu = rootView.findViewById(R.id.iv_help);
        ivMenu.setOnClickListener(this);
        Spinner rentCycle = rootView.findViewById(R.id.RentCycle);
        rentCycle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String get = rentCycle.getSelectedItem().toString();
                if (get.equals("Month")) {
                    cycle="m";
                } else if (get.equals("Day")) {
                    cycle="d";
                } else {
                    cycle="h";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        cityLay=rootView.findViewById(R.id.city_lay);
        cityLay.setOnClickListener(this);
        landmarkLay=rootView.findViewById(R.id.search_landmark_lay);
        landmarkLay.setOnClickListener(this);

        SelectBrand.setOnClickListener(this);

        cv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reSetCategoryItems("1");
            }
        });

        cv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reSetCategoryItems("2");
            }
        });

        cv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reSetCategoryItems("4");
            }
        });

        cv5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reSetCategoryItems("5");
            }
        });

        cv6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reSetCategoryItems("6");
            }
        });

    }

    public void reSetCategoryItems(String item) {

        t1.setTextColor(getResources().getColor(R.color.hint_color));
        t2.setTextColor(getResources().getColor(R.color.hint_color));
        t4.setTextColor(getResources().getColor(R.color.hint_color));
        t5.setTextColor(getResources().getColor(R.color.hint_color));
        t6.setTextColor(getResources().getColor(R.color.hint_color));


        c1.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.hint_color)));
        c2.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.hint_color)));
        c4.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.hint_color)));
        c5.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.hint_color)));
        c6.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.hint_color)));

        c1.setBackground(getResources().getDrawable(R.drawable.ic_circle));
        c2.setBackground(getResources().getDrawable(R.drawable.ic_circle));
        c4.setBackground(getResources().getDrawable(R.drawable.ic_circle));
        c5.setBackground(getResources().getDrawable(R.drawable.ic_circle));
        c6.setBackground(getResources().getDrawable(R.drawable.ic_circle));

        sl1.setVisibility(View.GONE);
        sl2.setVisibility(View.GONE);
        sl3.setVisibility(View.GONE);
        sl4.setVisibility(View.GONE);
        sl5.setVisibility(View.GONE);
        sl6.setVisibility(View.GONE);

        SelectBrand.setVisibility(View.GONE);

        CheckBox cbRoof = rootView.findViewById(R.id.roofCb);
        cbRoof.setVisibility(View.GONE);

        String selection[] = {"Select Building Type","Individual","Apartment"};
        ArrayAdapter spinnerArrayAdapter;
        Spinner spinner = rootView.findViewById(R.id.spinner_1);
        Spinner spinner1=rootView.findViewById(R.id.spinner_2);

        spinnerArrayAdapter = new ArrayAdapter<>(mActivity,
                android.R.layout.simple_spinner_dropdown_item,selection);
        spinner.setAdapter(spinnerArrayAdapter);


        switch (item) {
            case "1":
                Category="1";
                t1.setTextColor(getResources().getColor(R.color.colorPrimary));
                c1.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
                c1.setBackground(getResources().getDrawable(R.drawable.ic_circle_tick));

                //changing details question
                sl1.setVisibility(View.VISIBLE);
                sl2.setVisibility(View.VISIBLE);
                sl3.setVisibility(View.VISIBLE);
                sl4.setVisibility(View.VISIBLE);
                sl5.setVisibility(View.VISIBLE);
                sl6.setVisibility(View.VISIBLE);

                break;
            case "2":
                Category="2";
                t2.setTextColor(getResources().getColor(R.color.colorPrimary));
                c2.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
                c2.setBackground(getResources().getDrawable(R.drawable.ic_circle_tick));

                sl1.setVisibility(View.VISIBLE);
                sl3.setVisibility(View.VISIBLE);
                sl5.setVisibility(View.VISIBLE);
                sl6.setVisibility(View.VISIBLE);

                break;
            case "4":
                Category="4";
                t4.setTextColor(getResources().getColor(R.color.colorPrimary));
                c4.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
                c4.setBackground(getResources().getDrawable(R.drawable.ic_circle_tick));

                sl1.setVisibility(View.VISIBLE);

                String selection3[] = {"Select Type","Ac","non-Ac"};
                ArrayAdapter spinnerArrayAdapter3;

                spinnerArrayAdapter3 = new ArrayAdapter<>(mActivity,
                        android.R.layout.simple_spinner_dropdown_item,selection3);
                spinner.setAdapter(spinnerArrayAdapter3);

                break;
            case "5":
                Category="5";
                t5.setTextColor(getResources().getColor(R.color.colorPrimary));
                c5.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
                c5.setBackground(getResources().getDrawable(R.drawable.ic_circle_tick));

                SelectBrand.setVisibility(View.VISIBLE);

                break;
            case "6":
                Category="6";
                t6.setTextColor(getResources().getColor(R.color.colorPrimary));
                c6.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
                c6.setBackground(getResources().getDrawable(R.drawable.ic_circle_tick));

                sl1.setVisibility(View.VISIBLE);
                sl2.setVisibility(View.VISIBLE);

                String fuelType[] = {"Petrol","Diesel","Electric"};
                ArrayAdapter typeAdpter;
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (position==2) {
                            sl2.setVisibility(View.GONE);
                        }else {
                            sl2.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                typeAdpter = new ArrayAdapter<>(mActivity,
                        android.R.layout.simple_spinner_dropdown_item,fuelType);
                spinner.setAdapter(typeAdpter);

                String tranType[] = {"Manual","Automatic"};
                ArrayAdapter tranAdapter;

                tranAdapter = new ArrayAdapter<>(mActivity,
                        android.R.layout.simple_spinner_dropdown_item,tranType);
                spinner1.setAdapter(tranAdapter);


                cbRoof.setVisibility(View.VISIBLE);
                SelectBrand.setVisibility(View.VISIBLE);

                break;
        }
    }

    public void reSetSteps(String layout) {

        categoryLay.setVisibility(View.GONE);
        detailsLay.setVisibility(View.GONE);
        imagesLay.setVisibility(View.GONE);
        locationLay.setVisibility(View.GONE);
        priceLay.setVisibility(View.GONE);
        tvButton.setText("Next");


        switch (layout) {
            case "category":
                categoryLay.setVisibility(View.VISIBLE);
                tvHeaderTitle.setText("Select Category");
                break;
            case "details":
                detailsLay.setVisibility(View.VISIBLE);
                tvHeaderTitle.setText("Details");
                break;
            case "images":
                imagesLay.setVisibility(View.VISIBLE);
                tvHeaderTitle.setText("Select Images");
                break;
            case "location":
                locationLay.setVisibility(View.VISIBLE);
                tvHeaderTitle.setText("Select Location");
                break;
            case "price":
                priceLay.setVisibility(View.VISIBLE);
                tvHeaderTitle.setText("Price");
                tvButton.setText("Post Now");
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.select_brand_lay:
                Intent i = new Intent(getActivity().getApplicationContext(),BrandListActivity.class);
                if (Category.equals("5")) {
                    i.putExtra("bOrC","bike");
                } else if (Category.equals("6")) {
                    i.putExtra("bOrC","car");
                }
                startActivityForResult(i,9065);
                break;
            case R.id.city_lay:
                Intent inte = new Intent(getActivity().getApplicationContext(),CityChooserActivity.class);
                inte.putExtra("search","false");
                startActivityForResult(new Intent(inte),999);
                tvSeletcdArea.setText("Select area");
                areaId=0;
                pincode="";
                break;
            case R.id.search_landmark_lay:
                if (cityId==0) {
                    Toast("Please select city");
                }else {
                    Intent inte2 = new Intent(getActivity().getApplicationContext(), PlacesSearcherActivity.class);
                    inte2.putExtra("post","true");
                    inte2.putExtra("city",tvSelectedCity.getText().toString());
                    startActivityForResult(new Intent(inte2),123);
                }
                break;
            case R.id.back_lay:
                if (layout==2){
                    showAlertDialogExit();
                }else if (layout==3){
                    reSetSteps("details");
                    layout--;
                }else if (layout==4){
                    reSetSteps("images");
                    layout--;
                }else if (layout==5){
                    layout--;
                    reSetSteps("location");
                }
                break;
            case R.id.post_lay:
                if (layout==1){
                    if (!Category.equals("")) {
                        reSetSteps("details");
                        layout++;
                    }else {
                        Toast("Please select category");
                    }
                }else if (layout==2){
                    if (Category.equals("1")) {
                        if (s1.getSelectedItemPosition()==0) {
                            Toast("Select Building Type");
                        }else if (s2.getSelectedItemPosition()==0) {
                            Toast("Select No of rooms");
                        }else if (s3.getSelectedItemPosition()==0) {
                            Toast("Select Parking");
                        }else if (s5.getSelectedItemPosition()==0) {
                            Toast("Select Facing");
                        }else if (s6.getSelectedItemPosition()==0) {
                            Toast("Select floor number");
                        }else {
                            check();
                        }
                    }else if (Category.equals("2")) {
                        if (s1.getSelectedItemPosition()==0) {
                            Toast("Select Building Type");
                        }else if (s3.getSelectedItemPosition()==0) {
                            Toast("Select Parking");
                        }else if (s5.getSelectedItemPosition()==0) {
                            Toast("Select Facing");
                        }else if (s6.getSelectedItemPosition()==0) {
                            Toast("Select floor number");
                        }else {
                            check();
                        }
                    }
                    else if (Category.equals("4")) {
                        if (s1.getSelectedItemPosition()==0) {
                            Toast("Select Type");
                        }else {
                            check();
                        }
                    }else if (Category.equals("5")) {
                        if (isBrandSelected==false) {
                            Toast("Please select your bike brand");
                        }else {
                            check();
                        }
                    }else if (Category.equals("6")) {
                        if (isBrandSelected==false) {
                            Toast("Please select your car brand");
                        }else {
                            check();
                        }
                    }
                }else if (layout==3){
                    /*
                    if (I1Added==false) {
                        Toast("Please select minimum 1 image.");
                    }else {
                        reSetSteps("location");
                        layout++;
                    } // disable this (if) condition when its for developers

                     */

                    //only for developers
                    reSetSteps("location");
                    layout++;
                    //only for developers

                }else if (layout==4){
                    if (cityId==0) {
                        Toast("Please Select city");
                    }else if (areaId==0) {
                        Toast("Please select area");
                    }else if (etAddress.getText().toString().trim().isEmpty()) {
                        Toast("Please enter address.");
                    }else if (isLocationAdded == false) {
                        Toast.makeText(mActivity, "Please mark location on map.", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        reSetSteps("price");
                        layout++;
                    }
                }else if (layout==5){
                    if (etRentAmount.getText().toString().isEmpty()) {
                        Toast("Please enter rent amount");
                    }else if (!Category.equals("3") && !Category.equals("7") && !Category.equals("8")) {
                        if (etDeposit.getText().toString().isEmpty()) {
                            Toast("Please enter security deposit amount");
                        }

                        //only for developers
                        else if (etName.getText().toString().isEmpty()) {
                            Toast.makeText(mActivity, "please enter owner name", Toast.LENGTH_SHORT).show();
                        }
                        else if (etPho.getText().toString().isEmpty()) {
                            Toast.makeText(mActivity, "please enter owner phone number", Toast.LENGTH_SHORT).show();
                        }
                        else if (etPho.getText().toString().length()<10) {
                            Toast.makeText(mActivity, "please enter a vaild phone number", Toast.LENGTH_SHORT).show();
                        }
                        //only for developers

                        else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setMessage("Are you sure you want to post?\nNote: You cannot change category and address after posting.")
                                    .setCancelable(false)
                                    .setPositiveButton("Post", (dialog, id) -> {
                                        openUploadingDialog();
                                        if (I1Added == true) {
                                            try {
                                                uploadPicture1();
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        } else {
                                            //go to direct step after uploading images
                                            postListing();
                                        }
                                    })
                                    .setNegativeButton("No", (dialog, id) -> dialog.cancel());
                            AlertDialog alert = builder.create();
                            alert.show();
                        }
                    }

                    //only for developers
                    else if (etName.getText().toString().isEmpty()) {
                        Toast.makeText(mActivity, "please enter owner name", Toast.LENGTH_SHORT).show();
                    }
                    else if (etPho.getText().toString().isEmpty()) {
                        Toast.makeText(mActivity, "please enter owner phone number", Toast.LENGTH_SHORT).show();
                    }
                    else if (etPho.getText().toString().length()<10) {
                        Toast.makeText(mActivity, "please enter a vaild phone number", Toast.LENGTH_SHORT).show();
                    }
                    //only for developers

                    else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setMessage("Are you sure you want to post?\nNote: You cannot change category and address after posting.")
                                .setCancelable(false)
                                .setPositiveButton("Post", (dialog, id) -> {
                                    openUploadingDialog();
                                    if (I1Added == true) {
                                        try {
                                            uploadPicture1();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        //go to direct step after uploading images
                                        postListing();
                                    }
                                })
                                .setNegativeButton("No", (dialog, id) -> dialog.cancel());
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                }

                break;
            case R.id.imageLay:
                if (var == 5) {
                    Toast.makeText(mActivity, "Sorry! you can add only 5 images.", Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent,SELECT_PHOTO);
                }
                break;
            case R.id.cv_map:
                mapPickerDialog();
                break;
            case R.id.iv_help:
                Uri uri= Uri.parse("https://www.youtube.com/playlist?list=PLXwGZPQZ3TK6FWkwWiemHiFXU9QrihWJ0");
                startActivity(new Intent(Intent.ACTION_VIEW,uri));
                break;
        }
    }

    private void check() {
        if (etTitle.getText().toString().trim().isEmpty()) {
            Toast("Please enter title.");
        } else if (etDes.getText().toString().trim().isEmpty()) {
            Toast("Please enter description.");
        } else if (etDes.getText().toString().length()<20) {
            Toast("Description should be minimum 20 characters.");
        } else {
            reSetSteps("images");
            layout++;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_PHOTO && resultCode == RESULT_OK && data !=null && data.getData() !=null) {
            if (var == 0) {
                uri1 = data.getData();
                ImageView imageView = rootView.findViewById(R.id.image1);
                imageView.setImageURI(uri1);
                var++;
                isStarted = true;
                I1Added = true;
            }
            else if (var == 1) {
                uri2 = data.getData();
                ImageView imageView2 = rootView.findViewById(R.id.image2);
                imageView2.setImageURI(uri2);
                var++;
                I2Added = true;
            }
            else if (var == 2) {
                uri3 = data.getData();
                ImageView imageView3 = rootView.findViewById(R.id.image3);
                imageView3.setImageURI(uri3);
                var++;
                I3Added = true;
            }
            else if (var == 3) {
                uri4 = data.getData();
                ImageView imageView4 = rootView.findViewById(R.id.image4);
                imageView4.setImageURI(uri4);
                var++;
                I4Added = true;
            }
            else if (var == 4) {
                uri5 = data.getData();
                ImageView imageView5 = rootView.findViewById(R.id.image5);
                imageView5.setImageURI(uri5);
                var++;
                I5Added = true;
            }
            else if (var == 5) {
                Toast toast = Toast.makeText(getActivity(), "You can add only 5 images." ,Toast.LENGTH_LONG);
                toast.show();
            }
        }
        else if (requestCode == 999 && resultCode == RESULT_FIRST_USER) {
            tvSelectedCity.setText(data.getStringExtra("city"));
            String city = data.getStringExtra("city");
            if (city.equals("Hyderabad")) {cityId=1;}
            else if (city.equals("Visakhapatnam")){cityId=2;}
            else if (city.equals("Delhi")) {cityId=3;}
            else if (city.equals("Bengaluru")) {cityId=4;}
            else if (city.equals("Mumbai")) {cityId=5;}
        }else if (requestCode == 9065 && resultCode == RESULT_CANCELED) {
            tvSelectedBrand.setText(data.getStringExtra("brand"));
            brandId= Integer.parseInt(data.getStringExtra("id"));
            isBrandSelected=true;
        }else if (requestCode == 123 && resultCode == 10) {
            areaId=Integer.parseInt(data.getStringExtra("areaId"));
            tvSeletcdArea.setText(data.getStringExtra("area"));
            pincode=data.getStringExtra("pincode");
        }
    }

    private void Toast(String text) {
        Toast.makeText(mActivity, text, Toast.LENGTH_SHORT).show();
    }

    private void uploadPicture1() throws IOException {
        Bitmap bmp = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri1);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        byte[] data = baos.toByteArray();
        final String randomkey = UUID.randomUUID().toString();
        StorageReference riversRef = storageReference.child("pic/" + randomkey);
        riversRef.putBytes(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                I1Url = uri.toString();
                                if (I2Added == true) {
                                    try {
                                        uploadPicture2();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }else {
                                    //go to direct step after uploading images
                                   postListing();
                                }
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.dismiss();
                        Toast.makeText(mActivity,"upload failed",Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void uploadPicture2() throws IOException {
        Bitmap bmp = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri2);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        byte[] data = baos.toByteArray();
        final String randomkey = UUID.randomUUID().toString();
        StorageReference riversRef = storageReference.child("pic/" + randomkey);
        riversRef.putBytes(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                I2Url = uri.toString();
                                if (I3Added == true) {
                                    try {
                                        uploadPicture3();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }else {
                                    //go to direct step after uploading images
                                    postListing();
                                }
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.dismiss();
                        Toast.makeText(mActivity,"upload failed",Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void uploadPicture3() throws IOException {
        Bitmap bmp = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri3);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        byte[] data = baos.toByteArray();
        final String randomkey = UUID.randomUUID().toString();
        StorageReference riversRef = storageReference.child("pic/" + randomkey);
        riversRef.putBytes(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                I3Url = uri.toString();
                                if (I4Added = true) {
                                    try {
                                        uploadPicture4();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }else {
                                    //go to direct step after uploading images
                                    postListing();
                                }
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.dismiss();
                        Toast.makeText(mActivity,"upload failed",Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void uploadPicture4() throws IOException {
        Bitmap bmp = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri4);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        byte[] data = baos.toByteArray();
        final String randomkey = UUID.randomUUID().toString();
        StorageReference riversRef = storageReference.child("pic/" + randomkey);
        riversRef.putBytes(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                I4Url = uri.toString();
                                if (I5Added == true) {
                                    try {
                                        uploadPicture5();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }else {
                                    //go to direct step after uploading images
                                    postListing();
                                }
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.dismiss();
                        Toast.makeText(mActivity,"upload failed",Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void uploadPicture5() throws IOException {
        Bitmap bmp = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri5);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 20, baos);
        byte[] data = baos.toByteArray();
        final String randomkey = UUID.randomUUID().toString();
        StorageReference riversRef = storageReference.child("pic/" + randomkey);
        riversRef.putBytes(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                I5Url = uri.toString();
                                //go to direct step after uploading images
                                postListing();
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.dismiss();
                        Toast.makeText(mActivity,"upload failed",Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void openSuccessDialog() {
        dialog.setContentView(R.layout.posted_success_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);

        AppCompatTextView tvOkay = dialog.findViewById(R.id.okay);

        tvOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Utils.startActivity(mActivity, HomeActivity.class);
            }
        });

        dialog.show();
    }

    private void mapPickerDialog() {
        dialog.setContentView(R.layout.loaction_picker_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        MapView mMapView = dialog.findViewById(R.id.mapView);
        MapsInitializer.initialize(getActivity());

        mMapView.onCreate(dialog.onSaveInstanceState());
        mMapView.onResume();

        mMapView.getMapAsync(googleMap -> {
            LatLng posisiabsen = new LatLng(Double.parseDouble(Lat),  Double.parseDouble(Lng)); ////your lat lng
            googleMap.addMarker(new MarkerOptions().position(posisiabsen).title("ad"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(posisiabsen));
            googleMap.getUiSettings().setZoomControlsEnabled(true);
            //googleMap.setMyLocationEnabled(true);
            //googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            isLocationAdded =true;
            googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    googleMap.clear();
                    googleMap.addMarker(new MarkerOptions().position(latLng).title(String.valueOf(latLng)));
                    Lat = String.valueOf(latLng.latitude);
                    Lng = String.valueOf(latLng.longitude);
                }
            });
        });
        dialog.show();

        io.RentEasy.views.MediumTextView addBtn = dialog.findViewById(R.id.tv_add);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                io.RentEasy.views.MediumTextView map = rootView.findViewById(R.id.t_adOnMap);
                map.setText("Marked on Map");
                ImageView mark = rootView.findViewById(R.id.i_mark);
                mark.setImageDrawable(getResources().getDrawable(ic_check_circle));
                dialog.dismiss();
                isLocationAdded = true;
            }
        });
    }

    private void showAlertDialogExit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Are you sure you want to go back without posting?")
                .setCancelable(false)
                .setPositiveButton("Discard", (dialog, id) -> {
                    ((HomeActivity) mActivity).resetAll("home");
                    ((HomeActivity) mActivity).loadFragment(HomeFragment.class.getName(), new HomeFragment());
                })
                .setNegativeButton("No", (dialog, id) -> dialog.cancel());
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void openUploadingDialog() {
        dialog.setContentView(R.layout.uploading_dilalog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.show();
    }

    private void postListing() {
        String temp = "1";

        rent = etRentAmount.getText().toString();

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        String currentDateandTime = sdf.format(new Date());

        CheckBox cbRoof = rootView.findViewById(R.id.roofCb);

        if (cbRoof.isChecked()) {
            roof="A";
        }

        //uploading listing data
       postListing = FirebaseDatabase.getInstance().getReference().child("listings data");
       postListing.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               if (data == true) {
                   data =false;
                   if (dataSnapshot.child(pincode).hasChild("1")) {
                       String total = String.valueOf(dataSnapshot.child(pincode).getChildrenCount());
                       int sum = Integer.parseInt(total) + Integer.parseInt(temp);
                       id = sum;
                       if (!I1Url.equals("No")) {
                           String url = I1Url.replace("https://firebasestorage.googleapis.com/v0/b/renteasy-f31ba.appspot.com/o/","");
                           postListing.child(pincode).child(String.valueOf(sum)).child("0").setValue(url);
                       }
                       if (!I2Url.equals("No")) {
                           String url = I2Url.replace("https://firebasestorage.googleapis.com/v0/b/renteasy-f31ba.appspot.com/o/","");
                           postListing.child(pincode).child(String.valueOf(sum)).child("1").setValue(url);
                       }
                       if (!I3Url.equals("No")) {
                           String url = I3Url.replace("https://firebasestorage.googleapis.com/v0/b/renteasy-f31ba.appspot.com/o/","");
                           postListing.child(pincode).child(String.valueOf(sum)).child("2").setValue(url);
                       }
                       if (!I4Url.equals("No")) {
                           String url = I4Url.replace("https://firebasestorage.googleapis.com/v0/b/renteasy-f31ba.appspot.com/o/","");
                           postListing.child(pincode).child(String.valueOf(sum)).child("3").setValue(url);
                       }
                       if (!I5Url.equals("No")) {
                           String url = I5Url.replace("https://firebasestorage.googleapis.com/v0/b/renteasy-f31ba.appspot.com/o/","");
                           postListing.child(pincode).child(String.valueOf(sum)).child("4").setValue(url);
                       }
                       postListing.child(pincode).child(String.valueOf(sum)).child("5").setValue(rent+","+cycle);
                       postListing.child(pincode).child(String.valueOf(sum)).child("6").setValue(etTitle.getText().toString().trim());
                       postListing.child(pincode).child(String.valueOf(sum)).child("7").setValue(etDes.getText().toString().trim());
                       postListing.child(pincode).child(String.valueOf(sum)).child("8").setValue(currentDateandTime); /// current date in user phone
                       postListing.child(pincode).child(String.valueOf(sum)).child("9").setValue(Status);
                       postListing.child(pincode).child(String.valueOf(sum)).child("10").setValue(Category); //selected category id
                       postListing.child(pincode).child(String.valueOf(sum)).child("13").setValue(etAddress.getText().toString().trim());
                       postListing.child(pincode).child(String.valueOf(sum)).child("14").setValue(Lat);
                       postListing.child(pincode).child(String.valueOf(sum)).child("15").setValue(Lng);

                       postListing.child(pincode).child(String.valueOf(sum)).child("20").setValue(String.valueOf(cityId)); // city id
                       postListing.child(pincode).child(String.valueOf(sum)).child("21").setValue(String.valueOf(areaId)); // city area id

                       if (Category.equals("1")) {
                           postListing.child(pincode).child(String.valueOf(sum)).child("23").child("1").setValue(s1.getSelectedItemPosition());
                           postListing.child(pincode).child(String.valueOf(sum)).child("23").child("2").setValue(s2.getSelectedItemPosition());
                           postListing.child(pincode).child(String.valueOf(sum)).child("23").child("3").setValue(s3.getSelectedItemPosition());
                           postListing.child(pincode).child(String.valueOf(sum)).child("23").child("4").setValue(s4.getSelectedItemPosition());
                           postListing.child(pincode).child(String.valueOf(sum)).child("23").child("5").setValue(s5.getSelectedItemPosition());
                           postListing.child(pincode).child(String.valueOf(sum)).child("23").child("6").setValue(s6.getSelectedItemPosition());
                           postListing.child(pincode).child(String.valueOf(sum)).child("22").setValue(etDeposit.getText().toString());
                       }else if (Category.equals("2")) {
                           postListing.child(pincode).child(String.valueOf(sum)).child("23").child("1").setValue(s1.getSelectedItemPosition());
                           postListing.child(pincode).child(String.valueOf(sum)).child("23").child("3").setValue(s3.getSelectedItemPosition());
                           postListing.child(pincode).child(String.valueOf(sum)).child("23").child("5").setValue(s5.getSelectedItemPosition());
                           postListing.child(pincode).child(String.valueOf(sum)).child("23").child("6").setValue(s6.getSelectedItemPosition());
                           postListing.child(pincode).child(String.valueOf(sum)).child("22").setValue(etDeposit.getText().toString());
                       }else if (Category.equals("4")) {
                           postListing.child(pincode).child(String.valueOf(sum)).child("23").child("1").setValue(s1.getSelectedItemPosition());
                           postListing.child(pincode).child(String.valueOf(sum)).child("22").setValue(etDeposit.getText().toString());
                       }else if (Category.equals("5")) {
                           postListing.child(pincode).child(String.valueOf(sum)).child("23").child("b").setValue(brandId); //bike brand id
                           postListing.child(pincode).child(String.valueOf(sum)).child("22").setValue(etDeposit.getText().toString());
                       }else if (Category.equals("6")) {
                           postListing.child(pincode).child(String.valueOf(sum)).child("23").child("1").setValue(s1.getSelectedItemPosition());
                           if (s1.getSelectedItemPosition()!=2) {
                               postListing.child(pincode).child(String.valueOf(sum)).child("23").child("2").setValue(s2.getSelectedItemPosition());
                           }
                           postListing.child(pincode).child(String.valueOf(sum)).child("23").child("b").setValue(brandId); //car brand id
                           postListing.child(pincode).child(String.valueOf(sum)).child("22").setValue(etDeposit.getText().toString());
                           postListing.child(pincode).child(String.valueOf(sum)).child("23").child("r").setValue(roof);
                       }


                       //postListing.child(pincode).child(String.valueOf(sum)).child("17").setValue(UserPhnNm); //disable for developers

                       //only for developers
                       postListing.child(pincode).child(String.valueOf(sum)).child("17").setValue(etPho.getText().toString());
                       postListing.child(pincode).child(String.valueOf(sum)).child("24").setValue("t"); //verified ad tag only for developers
                       //only for developers


                   } else {
                       if (!I1Url.equals("No")) {
                           String url = I1Url.replace("https://firebasestorage.googleapis.com/v0/b/renteasy-f31ba.appspot.com/o/","");
                           postListing.child(pincode).child("1").child("0").setValue(url);
                       }
                       if (!I2Url.equals("No")) {
                           String url = I2Url.replace("https://firebasestorage.googleapis.com/v0/b/renteasy-f31ba.appspot.com/o/","");
                           postListing.child(pincode).child("1").child("1").setValue(url);
                       }
                       if (!I3Url.equals("No")) {
                           String url = I3Url.replace("https://firebasestorage.googleapis.com/v0/b/renteasy-f31ba.appspot.com/o/","");
                           postListing.child(pincode).child("1").child("2").setValue(url);
                       }
                       if (!I4Url.equals("No")) {
                           String url = I4Url.replace("https://firebasestorage.googleapis.com/v0/b/renteasy-f31ba.appspot.com/o/","");
                           postListing.child(pincode).child("1").child("3").setValue(url);
                       }
                       if (!I5Url.equals("No")) {
                           String url = I5Url.replace("https://firebasestorage.googleapis.com/v0/b/renteasy-f31ba.appspot.com/o/","");
                           postListing.child(pincode).child("1").child("4").setValue(url);
                       }
                       postListing.child(pincode).child("1").child("5").setValue(rent+","+cycle);
                       postListing.child(pincode).child("1").child("6").setValue(etTitle.getText().toString().trim());
                       postListing.child(pincode).child("1").child("7").setValue(etDes.getText().toString().trim());
                       postListing.child(pincode).child("1").child("8").setValue(currentDateandTime); /// current date in user phone
                       postListing.child(pincode).child("1").child("9").setValue(Status);
                       postListing.child(pincode).child("1").child("10").setValue(Category); //selected category id
                       postListing.child(pincode).child("1").child("13").setValue(etAddress.getText().toString().trim());
                       postListing.child(pincode).child("1").child("14").setValue(Lat);
                       postListing.child(pincode).child("1").child("15").setValue(Lng);

                       postListing.child(pincode).child("1").child("20").setValue(String.valueOf(cityId)); // city id
                       postListing.child(pincode).child("1").child("21").setValue(String.valueOf(areaId)); // city area id

                       if (Category.equals("1")) {
                           postListing.child(pincode).child("1").child("23").child("1").setValue(s1.getSelectedItemPosition());
                           postListing.child(pincode).child("1").child("23").child("2").setValue(s2.getSelectedItemPosition());
                           postListing.child(pincode).child("1").child("23").child("3").setValue(s3.getSelectedItemPosition());
                           postListing.child(pincode).child("1").child("23").child("4").setValue(s4.getSelectedItemPosition());
                           postListing.child(pincode).child("1").child("23").child("5").setValue(s5.getSelectedItemPosition());
                           postListing.child(pincode).child("1").child("23").child("6").setValue(s6.getSelectedItemPosition());
                           postListing.child(pincode).child("1").child("22").setValue(etDeposit.getText().toString());
                       }else if (Category.equals("2")) {
                           postListing.child(pincode).child("1").child("23").child("1").setValue(s1.getSelectedItemPosition());
                           postListing.child(pincode).child("1").child("23").child("3").setValue(s3.getSelectedItemPosition());
                           postListing.child(pincode).child("1").child("23").child("5").setValue(s5.getSelectedItemPosition());
                           postListing.child(pincode).child("1").child("23").child("6").setValue(s6.getSelectedItemPosition());
                           postListing.child(pincode).child("1").child("22").setValue(etDeposit.getText().toString());
                       }else if (Category.equals("4")) {
                           postListing.child(pincode).child("1").child("23").child("1").setValue(s1.getSelectedItemPosition());
                           postListing.child(pincode).child("1").child("22").setValue(etDeposit.getText().toString());
                       }else if (Category.equals("5")) {
                           postListing.child(pincode).child("1").child("23").child("b").setValue(brandId); //bike brand id
                           postListing.child(pincode).child("1").child("22").setValue(etDeposit.getText().toString());
                       }else if (Category.equals("6")) {
                           postListing.child(pincode).child("1").child("23").child("1").setValue(s1.getSelectedItemPosition());
                           if (s1.getSelectedItemPosition()!=2) {
                               postListing.child(pincode).child("1").child("23").child("2").setValue(s2.getSelectedItemPosition());
                           }
                           postListing.child(pincode).child("1").child("23").child("b").setValue(brandId); //car brand id
                           postListing.child(pincode).child("1").child("22").setValue(etDeposit.getText().toString());
                           postListing.child(pincode).child("1").child("23").child("r").setValue(roof);
                       }

                       //postListing.child(pincode).child("1").child("17").setValue(UserPhnNm); //disable for developers

                       //only for developers
                       postListing.child(pincode).child("1").child("17").setValue(etPho.getText().toString());
                       postListing.child(pincode).child("1").child("24").setValue("t"); //verified ad tag only for developers
                       //only for developers


                   }
               }

           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
       });

        //uploading user data to owned listings
        addUserData = FirebaseDatabase.getInstance().getReference().child("users data").child(UserPhnNm);
        addUserData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (ownerData ==true){
                    ownerData =false;
                    if (dataSnapshot.hasChild("owned listings")) {
                        String total = String.valueOf(dataSnapshot.child("owned listings").getChildrenCount());
                        int sum = Integer.parseInt(total) + Integer.parseInt(temp);
                        addUserData.child("owned listings").child(String.valueOf(sum)).child("0").setValue(pincode);
                        addUserData.child("owned listings").child(String.valueOf(sum)).child("1").setValue(id);
                    } else {
                        addUserData.child("owned listings").child("1").child("0").setValue(pincode);
                        addUserData.child("owned listings").child("1").child("1").setValue(id);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        //only for developers
        developerAccount = FirebaseDatabase.getInstance().getReference().child("users data");
        developerAccount.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (develper==true){
                    develper=false;
                    if (dataSnapshot.hasChild(etPho.getText().toString())) {
                        if (dataSnapshot.child(etPho.getText().toString()).hasChild("owned listings")) {
                            String total = String.valueOf(dataSnapshot.child(etPho.getText().toString()).child("owned listings").getChildrenCount());
                            int sum = Integer.parseInt(total) + Integer.parseInt(temp);
                            developerAccount.child(etPho.getText().toString()).child("owned listings").child(String.valueOf(sum)).child("0").setValue(pincode);
                            developerAccount.child(etPho.getText().toString()).child("owned listings").child(String.valueOf(sum)).child("1").setValue(id);
                        } else {
                            developerAccount.child(etPho.getText().toString()).child("owned listings").child("1").child("0").setValue(pincode);
                            developerAccount.child(etPho.getText().toString()).child("owned listings").child("1").child("1").setValue(id);
                        }
                    }else {
                        developerAccount.child(etPho.getText().toString()).child("0").setValue(etName.getText().toString());
                        developerAccount.child(etPho.getText().toString()).child("1").setValue(etPho.getText().toString());
                        developerAccount.child(etPho.getText().toString()).child("owned listings").child("1").child("0").setValue(pincode);
                        developerAccount.child(etPho.getText().toString()).child("owned listings").child("1").child("1").setValue(id);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //only for developers

        //update data to server
        updateServer = FirebaseDatabase.getInstance().getReference().child("Server");
        updateServer.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (serverData ==true) {
                    serverData =false;
                    String totalCategory = dataSnapshot.child(Category).getValue().toString();
                    int sumCategory = Integer.parseInt(totalCategory) + Integer.parseInt(temp);
                    updateServer.child(Category).setValue(String.valueOf(sumCategory));

                    String totalListings = dataSnapshot.child("total listings").getValue().toString();
                    int sumListings = Integer.parseInt(totalListings) + Integer.parseInt(temp);
                    updateServer.child("total listings").setValue(String.valueOf(sumListings));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                openSuccessDialog();
            }
        }, 3000);

    }

}