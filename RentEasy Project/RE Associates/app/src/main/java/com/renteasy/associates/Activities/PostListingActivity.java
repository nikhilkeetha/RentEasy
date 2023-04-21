package com.renteasy.associates.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.renteasy.associates.R;
import com.renteasy.associates.views.MediumTextView;
import com.renteasy.associates.views.RegularTextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class PostListingActivity extends AppCompatActivity {

    TextInputEditText etTitle,etDes,etAddress,etRent, etZipcode,etOwnerName,etOwnerPhn;
    CardView cvCategory,cvSubCategory,cvMap;
    Spinner rentCycle;
    RegularTextView tvCategory,tvSubCategory;
    MediumTextView tvAddMap,tvRentCycleDisplay;
    LinearLayout postBtn;
    Switch activeStatus;
    String status="A",cycle="m";
    RelativeLayout addImage,backLay;
    int varImages=0;
    int SELECT_PHOTO = 5;
    ImageView iv1,iv2,iv3,iv4,iv5;
    Uri uri1,uri2,uri3,uri4,uri5;
    boolean I1Added=false,I2Added=false,I3Added=false,I4Added=false,I5Added=false,isLocationAdded=false;
    Dialog dialog;
    String Lat = "20.5937";
    String Lng = "78.9629";
    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_listing);

        dialog =new Dialog(this);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        //variables
        etTitle = findViewById(R.id.et_title);
        etDes=findViewById(R.id.et_description);
        etAddress=findViewById(R.id.et_address);
        etRent=findViewById(R.id.et_rent);
        etZipcode =findViewById(R.id.et_pincode);
        etOwnerName=findViewById(R.id.et_ownerName);
        etOwnerPhn=findViewById(R.id.et_ownerPho);
        cvCategory=findViewById(R.id.cv_choose_category);
        cvSubCategory=findViewById(R.id.cv_choose_sub_category);
        cvMap=findViewById(R.id.cv_map);
        rentCycle=findViewById(R.id.SpinnerRentCycle);
        tvCategory=findViewById(R.id.tv_selected_category);
        tvSubCategory=findViewById(R.id.tv_selected_sub_category);
        tvAddMap=findViewById(R.id.tv_adOnMap);
        tvRentCycleDisplay=findViewById(R.id.tv_rent_cycle_display);
        postBtn=findViewById(R.id.post_lay);
        activeStatus=findViewById(R.id.active_status_switch);
        addImage=findViewById(R.id.imageLay);
        iv1=findViewById(R.id.image1);
        iv2=findViewById(R.id.image2);
        iv3=findViewById(R.id.image3);
        iv4=findViewById(R.id.image4);
        iv5=findViewById(R.id.image5);
        backLay=findViewById(R.id.back_lay);

        onClick();

    }

    private void onClick() {

        //post listing btn
        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etTitle.getText().toString().isEmpty()) {
                    Toast.makeText(PostListingActivity.this, "Please enter listing title", Toast.LENGTH_LONG).show();
                }
                if (etDes.getText().toString().isEmpty()) {
                    Toast.makeText(PostListingActivity.this, "Please enter listing description", Toast.LENGTH_LONG).show();
                }
                if (etZipcode.getText().toString().isEmpty()) {
                    Toast.makeText(PostListingActivity.this, "Please enter pincode", Toast.LENGTH_LONG).show();
                }
                if (etZipcode.getText().toString().length() < 6) {
                    Toast.makeText(PostListingActivity.this, "Please enter a valid pincode", Toast.LENGTH_LONG).show();
                }
                if (etAddress.getText().toString().isEmpty()) {
                    Toast.makeText(PostListingActivity.this, "Please enter address", Toast.LENGTH_LONG).show();
                }
                if (etRent.getText().toString().isEmpty()) {
                    Toast.makeText(PostListingActivity.this, "Please enter rent amount", Toast.LENGTH_LONG).show();
                }

            }
        });


        cvMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapPickerDialog();
            }
        });

        cvCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        cvSubCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        activeStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked==true) {
                    status="A";
                }else {
                    status="N";
                }
            }
        });


        rentCycle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position==0) {
                    cycle="m";
                    tvRentCycleDisplay.setText("Rent per Month");
                }else if (position==1) {
                    cycle="d";
                    tvRentCycleDisplay.setText("Rent per Day");
                }else if (position==2) {
                    cycle="h";
                    tvRentCycleDisplay.setText("Rent per Hour");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (varImages == 5) {
                    Toast.makeText(getApplicationContext(), "Sorry! you can add only 5 images.", Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent,SELECT_PHOTO);
                }
            }
        });

        backLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_PHOTO && resultCode == RESULT_OK && data !=null && data.getData() !=null) {
            if (varImages == 0) {
                uri1 = data.getData();
                iv1.setImageURI(uri1);
                varImages++;
                I1Added=true;
            }
            else if (varImages == 1) {
                uri2 = data.getData();
                iv2.setImageURI(uri2);
                varImages++;
                I2Added = true;
            }
            else if (varImages == 2) {
                uri3 = data.getData();
                iv3.setImageURI(uri3);
                varImages++;
                I3Added = true;
            }
            else if (varImages == 3) {
                uri4 = data.getData();
                iv4.setImageURI(uri4);
                varImages++;
                I4Added = true;
            }
            else if (varImages == 4) {
                uri5 = data.getData();
                iv5.setImageURI(uri5);
                varImages++;
                I5Added = true;
            }
            else if (varImages == 5) {
                Toast toast = Toast.makeText(getApplicationContext(), "You can add only 5 images." ,Toast.LENGTH_LONG);
                toast.show();
            }
        }
    }

    private void mapPickerDialog() {
        dialog.setContentView(R.layout.loaction_picker_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        MapView mMapView = dialog.findViewById(R.id.mapView);
        MapsInitializer.initialize(this);

        mMapView.onCreate(dialog.onSaveInstanceState());
        mMapView.onResume();

        CardView cvCurrent = dialog.findViewById(R.id.cv_current_location);
        cvCurrent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    getLocation();
                } else {
                    ActivityCompat.requestPermissions(PostListingActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},44);
                }
            }
        });

        mMapView.getMapAsync(googleMap -> {
            LatLng posisiabsen = new LatLng(20.5937,  78.9629); ////your lat lng
            googleMap.addMarker(new MarkerOptions().position(posisiabsen).title("property"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(posisiabsen));
            googleMap.getUiSettings().setZoomControlsEnabled(true);
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

        MediumTextView addBtn = dialog.findViewById(R.id.tv_add);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediumTextView map = findViewById(R.id.tv_adOnMap);
                map.setText("Marked on Map");
                ImageView mark = findViewById(R.id.iv_mark);
                mark.setImageDrawable(getResources().getDrawable(R.drawable.ic_check_circle));
                dialog.dismiss();
                isLocationAdded = true;
            }
        });
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if (location != null) {
                    try {
                        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                        List<Address> addresses = geocoder.getFromLocation(
                                location.getLatitude(),location.getLongitude(),1
                        );
                        Lat = String.valueOf(location.getLatitude());
                        Lng = String.valueOf(location.getLongitude());
                        MediumTextView map = findViewById(R.id.tv_adOnMap);
                        map.setText("Marked on Map");
                        ImageView mark = findViewById(R.id.iv_mark);
                        mark.setImageDrawable(getResources().getDrawable(R.drawable.ic_check_circle));
                        dialog.dismiss();
                        isLocationAdded = true;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void postListing() {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}