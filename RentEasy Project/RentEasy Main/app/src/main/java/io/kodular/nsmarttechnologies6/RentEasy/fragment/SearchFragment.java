package io.kodular.nsmarttechnologies6.RentEasy.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import io.kodular.nsmarttechnologies6.RentEasy.R;
import io.kodular.nsmarttechnologies6.RentEasy.activity.CityChooserActivity;
import io.kodular.nsmarttechnologies6.RentEasy.activity.HomeActivity;
import io.kodular.nsmarttechnologies6.RentEasy.activity.SearchListActivity;
import pub.devrel.easypermissions.EasyPermissions;

import static android.app.Activity.RESULT_FIRST_USER;
import static android.content.Context.LOCATION_SERVICE;
import static android.content.Context.MODE_PRIVATE;

public class SearchFragment extends Fragment implements View.OnClickListener {

    private AdView mAdView;
    boolean isLocationChoosed = false, isCategorySelected = false;
    String selectedCategory, currentVillageNm, searchedAddress = "", Cb = "n", address, category;
    TextInputEditText pincode;
    Dialog dialog;
    CardView cvSugession;
    LinearLayout detailsLay,OrLay;
    ProgressBar loading;
    io.RentEasy.views.RegularTextView tvNotFound,tvSelectedCity;
    RelativeLayout CityLay;

    SharedPreferences sharedPreferences; // to get user last search details
    private static final String SHARED_PREF_NM = "mySearch";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_CATEGORY = "category";
    FusedLocationProviderClient fusedLocationProviderClient;

    @SuppressLint("StaticFieldLeak")
    private static SearchFragment fragment;
    private Activity mActivity;
    private View rootView;

    public static SearchFragment newInstance() {
        fragment = new SearchFragment();
        return fragment;
    }

    public static SearchFragment getInstance() {
        if (fragment == null)
            return new SearchFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_search, container, false);
        init();
        return rootView;
    }

    @SuppressLint("SetTextI18n")
    private void init() {
        mActivity = getActivity();
        dialog = new Dialog(getActivity());
        cvSugession = rootView.findViewById(R.id.cv_sugession);
        detailsLay = rootView.findViewById(R.id.location_details_lay);
        OrLay = rootView.findViewById(R.id.ORLay);
        loading = rootView.findViewById(R.id.loading);
        tvNotFound = rootView.findViewById(R.id.tv_not_found);
        setClickListener();
        mAdView = rootView.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        sharedPreferences = getActivity().getSharedPreferences(SHARED_PREF_NM, MODE_PRIVATE);
        address = sharedPreferences.getString(KEY_ADDRESS, null);
        category = sharedPreferences.getString(KEY_CATEGORY, null);

        io.RentEasy.views.MediumTextView tvAddress = rootView.findViewById(R.id.tv_recent_nm);
        io.RentEasy.views.MediumTextView tvCategory = rootView.findViewById(R.id.recent_category);

        CityLay=rootView.findViewById(R.id.city_lay);
        tvSelectedCity=rootView.findViewById(R.id.tv_selected_city);
        CityLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getActivity().getApplicationContext(), CityChooserActivity.class),999);
            }
        });

        if (address != null) {
            tvAddress.setText(address);
        } else {
            CardView cvRecent = rootView.findViewById(R.id.cv_recent_search);
            cvRecent.setVisibility(View.GONE);
            TextView display = rootView.findViewById(R.id.tv_display);
            display.setVisibility(View.GONE);
            OrLay.setVisibility(View.GONE);
        }
        if (category != null) {
            tvCategory.setText(category);
        }

        pincode = rootView.findViewById(R.id.pincode);
        pincode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                isLocationChoosed = false;
                tvNotFound.setVisibility(View.GONE);
                loading.setVisibility(View.VISIBLE);
                detailsLay.setVisibility(View.GONE);
                cvSugession.setVisibility(View.VISIBLE);
                String address = pincode.getText().toString();

            }
        });
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(mActivity);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 999 && resultCode == RESULT_FIRST_USER) {
            tvSelectedCity=rootView.findViewById(R.id.tv_selected_city);
            tvSelectedCity.setText(data.getStringExtra("city"));
        }
    }

    private void setClickListener() {
        RelativeLayout backLay = rootView.findViewById(R.id.back_lay);
        backLay.setOnClickListener(this);
        LinearLayout searchLay = rootView.findViewById(R.id.search_lay);
        searchLay.setOnClickListener(this);
        CardView cvCatergory = rootView.findViewById(R.id.cv_choose_category);
        cvCatergory.setOnClickListener(this);
        ImageView close = rootView.findViewById(R.id.iv_close);
        close.setOnClickListener(this);
        CardView cvAutoDetect = rootView.findViewById(R.id.cv_auto_location);
        cvAutoDetect.setOnClickListener(this);
        cvSugession.setOnClickListener(this);
        CardView cvRecent = rootView.findViewById(R.id.cv_recent_search);
        cvRecent.setOnClickListener(this);
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_lay:
                mActivity.onBackPressed();
                break;
            case R.id.search_lay:
                String enter = pincode.getText().toString();
                boolean isNumeric = enter.chars().allMatch(Character::isDigit);
                CheckBox cb = rootView.findViewById(R.id.cb);
                if (cb.isChecked()) {
                    Cb = "y";
                }
                if (searchedAddress.isEmpty()) {
                    if (isNumeric == false) {
                        Toast toast = Toast.makeText(getActivity(), "please select city/village", Toast.LENGTH_SHORT);
                        toast.show();
                    } else {
                        if (pincode.getText().toString().length() == 6) {
                            if (isCategorySelected == false) {
                                Toast.makeText(mActivity, "Please select Category", Toast.LENGTH_SHORT).show();
                            } else {
                                Intent intent = new Intent(getActivity(), SearchListActivity.class);
                                intent.putExtra("pincode", pincode.getText().toString().trim());
                                intent.putExtra("is_Avialable", Cb);
                                intent.putExtra("category", selectedCategory);
                                intent.putExtra("isLiked", "null");
                                intent.putExtra("home-category", "null");
                                getActivity().startActivity(intent);
                            }
                        } else {
                            Toast toast = Toast.makeText(getActivity(), "please enter a valid pincode", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }
                } else {
                    if (isCategorySelected == false) {
                        Toast.makeText(mActivity, "Please select Category", Toast.LENGTH_SHORT).show();
                    } else {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(KEY_ADDRESS, searchedAddress);
                        editor.putString(KEY_CATEGORY, selectedCategory);
                        editor.apply();
                        String[] split = searchedAddress.split(",");
                        Intent intent = new Intent(getActivity(), SearchListActivity.class);
                        intent.putExtra("pincode", split[1]);
                        intent.putExtra("is_Avialable", Cb);
                        intent.putExtra("category", selectedCategory);
                        intent.putExtra("isLiked", "null");
                        intent.putExtra("home-category", "null");
                        getActivity().startActivity(intent);
                    }
                }
                break;
            case R.id.cv_choose_category:
                openChooseDialog();
                break;
            case R.id.iv_close:
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();
                CardView cvRecent = rootView.findViewById(R.id.cv_recent_search);
                TextView display = rootView.findViewById(R.id.tv_display);
                cvRecent.setVisibility(View.GONE);
                display.setVisibility(View.GONE);
                OrLay.setVisibility(View.GONE);
                break;
            case R.id.cv_auto_location:
                if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    getLocation();
                } else {
                    ActivityCompat.requestPermissions(mActivity,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);
                }
                break;
            case R.id.cv_sugession:
                if (isLocationChoosed == true) {
                    cvSugession.setVisibility(View.GONE);
                    Toast.makeText(mActivity, "Location Selected Successful", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.cv_recent_search:
                CheckBox cb2 = rootView.findViewById(R.id.cb);
                if (cb2.isChecked()) {
                    Cb = "y";
                }
                if (address != null) {
                    String[] split = address.split(",");
                    String pincode = split[1];
                    Intent intent = new Intent(getActivity(), SearchListActivity.class);
                    intent.putExtra("pincode", pincode);
                    intent.putExtra("is_Avialable", Cb);
                    intent.putExtra("category", category);
                    intent.putExtra("isLiked", "null");
                    intent.putExtra("home-category", "null");
                    getActivity().startActivity(intent);
                }
                break;
        }
    }

    private void openChooseDialog() {
        final int[] checkedItem = {-1};
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mActivity);
        alertDialog.setTitle("Choose Category");
        final String[] listItems = new String[]{"Houses", "Cars", "Shops", "Function Halls", "Bikes", "JCB", "Cranes", "Hostel"};

        alertDialog.setSingleChoiceItems(listItems, checkedItem[0], new DialogInterface.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(DialogInterface dialog, int which) {
                io.RentEasy.views.RegularTextView tvCategory = rootView.findViewById(R.id.tv_selected_category);
                checkedItem[0] = which;
                tvCategory.setText(listItems[which]);
                selectedCategory = listItems[which];
                isCategorySelected = true;
                dialog.dismiss();
            }
        });

        alertDialog.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog customAlertDialog = alertDialog.create();

        customAlertDialog.show();
    }

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
                        pincode.setText(currentVillageNm);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private class GeoHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            String address;
            switch (msg.what) {
                case 1:
                    Bundle bundle = msg.getData();
                    address = bundle.getString("address");
                    break;
                    default:
                        address = "";
            }

            io.RentEasy.views.MediumTextView tvName = rootView.findViewById(R.id.tv_name);
            io.RentEasy.views.RegularTextView tvZipcode = rootView.findViewById(R.id.tv_zipcode);
            io.RentEasy.views.RegularTextView tvState  = rootView.findViewById(R.id.tv_state_nm);

            if (address.length() == 0) {

            }else {
                String[] split = address.split(",");
                String name = split[0];
                String zipcode = split[1];
                String state = split[2];
                if (zipcode.equals("null")) {
                    loading.setVisibility(View.GONE);
                    detailsLay.setVisibility(View.GONE);
                    tvNotFound.setVisibility(View.VISIBLE);
                }else {
                    if (zipcode.length() == 6) {
                        tvName.setText(name);
                        tvZipcode.setText(zipcode);
                        tvState.setText(state);
                        cvSugession.setVisibility(View.VISIBLE);
                        loading.setVisibility(View.GONE);
                        detailsLay.setVisibility(View.VISIBLE);
                        isLocationChoosed=true;
                        searchedAddress = name+","+zipcode+","+state;
                    }
                }
            }
        }
    }
}