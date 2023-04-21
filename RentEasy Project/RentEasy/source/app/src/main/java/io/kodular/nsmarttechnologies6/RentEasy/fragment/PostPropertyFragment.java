package io.kodular.nsmarttechnologies6.RentEasy.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.content.Context;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import io.kodular.nsmarttechnologies6.RentEasy.R;

import io.kodular.nsmarttechnologies6.RentEasy.activity.HomeActivity;

import io.kodular.nsmarttechnologies6.RentEasy.support.Utils;
import io.RentEasy.views.RegularTextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static io.kodular.nsmarttechnologies6.RentEasy.R.drawable.ic_check_circle;


public class PostPropertyFragment extends Fragment implements View.OnClickListener {

    private AdView mAdView;
    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NM = "myPref";
    private static final String KEY_NM = "name";
    private static final String KEY_PHONENO = "number";

    Dialog dialog;
    int SELECT_PHOTO = 1;
    Uri uri1;
    Uri uri2;
    Uri uri3;
    Uri uri4;
    Uri uri5;
    int PLACE_PICKER_REQUEST = 1;
    int var = 0; // to check how many images added

    boolean isStarted = false; // to check either user started filling details or not so,if user press back we can show alert dialog

    // Is Image 1 added or not so, can upload only added images else set as "No"
    boolean I1Added = false;
    boolean I2Added = false;
    boolean I3Added = false;
    boolean I4Added = false;
    boolean I5Added = false;
    boolean isValueAddedH1 = true;
    boolean isValueAddedHn = true;
    boolean isValueAddedS1 = true;
    boolean isValueAddedSn = true;
    boolean once = true;
    boolean isLoactionAdded = false;

    String I1Url = "No";
    String I2Url = "No";
    String I3Url = "No";
    String I4Url = "No";
    String I5Url = "No";
    String address = "";
    String pincode = "";
    String rent = "";
    String Status = "A";
    String Cb2 = "n";
    String Cb4 = "n";
    String NoRooms = "";
    String facing = "";
    String propertyType = "";
    String floor = "";
    String status = "";
    String parking = "";
    String UserNm = "";
    String UserPhnNm = "";
    String Lat = "20.5937";
    String Lng = "78.9629";

    // temporary variables
    String sumH = "";
    String sumS = "";
    String test = "1";
    String num = "";
    String num2 = "";
    String totalPostings = "0";
    String totalProperties = "";
    
    @SuppressLint("StaticFieldLeak")
    private static PostPropertyFragment fragment;
    private Activity mActivity;
    private View rootView;
    private  Context context;
    DatabaseReference propertyDetails;
    DatabaseReference getNoOfProperties;
    DatabaseReference updateNoOfProperties;
    DatabaseReference getNoOfPostings;
    DatabaseReference getTotalProperties;
    DatabaseReference updateTotalProperties;
    DatabaseReference updateNoOfPostings;
    DatabaseReference updatePostings;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    public static PostPropertyFragment newInstance() {
        return fragment;
    }

    public static PostPropertyFragment getInstance() {
        if (fragment == null)
            return new PostPropertyFragment();
        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_post_property, container, false);
        init();
        return rootView;
    }

    @SuppressLint("SetTextI18n")
    private void init() {
        mActivity = getActivity();
        setClickListener();
        dialog =new Dialog(getActivity());
        mAdView = rootView.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        sharedPreferences = getActivity().getSharedPreferences(SHARED_PREF_NM,MODE_PRIVATE);
        UserNm = sharedPreferences.getString(KEY_NM,null);
        UserPhnNm = sharedPreferences.getString(KEY_PHONENO,null);
        getNoOfPostings = FirebaseDatabase.getInstance().getReference().child("Phone Numbers"); // gets the total posting by user from firebase database
        getNoOfPostings.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(UserPhnNm)) {
                    totalPostings = dataSnapshot.child(UserPhnNm).getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        getTotalProperties = FirebaseDatabase.getInstance().getReference().child("Server"); // gets the total properties in servers from firebase database
        getTotalProperties.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("Total Properties Registered")) {
                    totalProperties = dataSnapshot.child("Total Properties Registered").getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void setClickListener() {
        RelativeLayout backLay = rootView.findViewById(R.id.back_lay);
        backLay.setOnClickListener(this);
        RegularTextView tvPostProperty = rootView.findViewById(R.id.tv_post_property);
        tvPostProperty.setOnClickListener(this);
        RelativeLayout imageLay = rootView.findViewById(R.id.imageLay);
        imageLay.setOnClickListener(this);
        CardView cvMap =rootView.findViewById(R.id.cv_map);
        cvMap.setOnClickListener(this);
        RegularTextView status = rootView.findViewById(R.id.tv_avialable);
        status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Status.equals("N")) {
                    Status = "A";
                    status.setText("Yes");
                }else {
                    Status  = "N";
                    status.setText("No");
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_lay:
                if (isStarted == true) {
                    showAlertDialogExit();
                }else {
                    mActivity.onBackPressed();
                }
                break;
            case R.id.tv_post_property:
                CheckBox cb2 = rootView.findViewById(R.id.cb_2);
                CheckBox cb4 = rootView.findViewById(R.id.cb_4);
                if (cb2.isChecked()) {
                    Cb2 = "y";
                }
                if (cb4.isChecked()) {
                    Cb4 = "y";
                }
                storage = FirebaseStorage.getInstance();
                storageReference = storage.getReference();
                TextInputEditText etAddress = rootView.findViewById(R.id.et_address);
                address = etAddress.getText().toString().trim();
                TextInputEditText etPincode = rootView.findViewById(R.id.et_pincode);
                pincode = etPincode.getText().toString().trim();
                TextInputEditText etRent = rootView.findViewById(R.id.et_rent);
                rent = etRent.getText().toString().trim();
                if (etAddress.getText().toString().trim().isEmpty()) {
                    Toast toast = Toast.makeText(getActivity(), "enter address" ,Toast.LENGTH_SHORT);
                    toast.show();
                }else if (etAddress.getText().toString().trim().contains(",")) {
                Toast toast = Toast.makeText(getActivity(), "address should not contain ',' " ,Toast.LENGTH_SHORT);
                toast.show();}
                else if (etPincode.getText().toString().trim().isEmpty()) {
                    Toast toast = Toast.makeText(getActivity(), "please enter pincode" ,Toast.LENGTH_SHORT);
                    toast.show();}
                else if (etPincode.getText().toString().trim().length() < 6) {
                Toast toast = Toast.makeText(getActivity(), "please enter a valid pincode" ,Toast.LENGTH_SHORT);
                toast.show();}
                else if (etRent.getText().toString().trim().isEmpty()) {
                    Toast toast = Toast.makeText(getActivity(), "please enter rent per month" ,Toast.LENGTH_SHORT);
                    toast.show();}
                else if (isLoactionAdded == false) {
                    Toast toast = Toast.makeText(getActivity(), "please mark location on map" ,Toast.LENGTH_SHORT);
                    toast.show();
                }
                else {
                    openLoadingDialog();
                    if ( I1Added == true ) {
                        try {
                            uploadPicture1();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else {
                        //go to direct step after uploading images
                        getNoOfProperties();
                    }

                }
                break;
            case R.id.imageLay:
                if (var == 5) {
                Toast toast = Toast.makeText(getActivity(), "You can add only 5 images." ,Toast.LENGTH_SHORT);
                toast.show();}
                else{
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent,SELECT_PHOTO);
                }
                break;
            case R.id.cv_map:
                mapPickerDialog();
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_PHOTO && resultCode == RESULT_OK && data !=null && data.getData() !=null) {
            try {

                if (var == 0) {
                    uri1 = data.getData();
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),uri1);
                    ImageView imageView = rootView.findViewById(R.id.image1);
                    imageView.setImageBitmap(bitmap);
                    var++;
                    isStarted = true;
                    I1Added = true;
                }
                else if (var == 1) {
                    uri2 = data.getData();
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),uri1);
                    ImageView imageView2 = rootView.findViewById(R.id.image2);
                    imageView2.setImageBitmap(bitmap);
                    var++;
                    I2Added = true;
                }
                else if (var == 2) {
                    uri3 = data.getData();
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),uri1);
                    ImageView imageView3 = rootView.findViewById(R.id.image3);
                    imageView3.setImageBitmap(bitmap);
                    var++;
                    I3Added = true;
                }
                else if (var == 3) {
                    uri4 = data.getData();
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),uri1);
                    ImageView imageView4 = rootView.findViewById(R.id.image4);
                    imageView4.setImageBitmap(bitmap);
                    var++;
                    I4Added = true;
                }
                else if (var == 4) {
                    uri5 = data.getData();
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),uri1);
                    ImageView imageView5 = rootView.findViewById(R.id.image5);
                    imageView5.setImageBitmap(bitmap);
                    var++;
                    I5Added = true;
                }
                else if (var == 5) {
                    Toast toast = Toast.makeText(getActivity(), "You can add only 5 images." ,Toast.LENGTH_SHORT);
                    toast.show();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadPicture1() throws IOException {
        Bitmap bmp = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri1);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 5, baos);
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
                                   getNoOfProperties();
                                   Toast.makeText(mActivity, randomkey, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(mActivity,"upload failed",Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void uploadPicture2() throws IOException {
        Bitmap bmp = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri2);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 5, baos);
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
                                    getNoOfProperties();
                                }
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    private void uploadPicture3() throws IOException {
        Bitmap bmp = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri3);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 5, baos);
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
                                    getNoOfProperties();
                                }
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    private void uploadPicture4() throws IOException {
        Bitmap bmp = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri4);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 5, baos);
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
                                    getNoOfProperties();
                                }
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    private void uploadPicture5() throws IOException {
        Bitmap bmp = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri5);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 5, baos);
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
                                getNoOfProperties();
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

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
            LatLng posisiabsen = new LatLng(20.5937,  78.9629); ////your lat lng
            googleMap.addMarker(new MarkerOptions().position(posisiabsen).title("property"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(posisiabsen));
            googleMap.getUiSettings().setZoomControlsEnabled(true);
            isLoactionAdded=true;
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

        AppCompatTextView addBtn = dialog.findViewById(R.id.tv_add);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                io.RentEasy.views.MediumTextView map = rootView.findViewById(R.id.tv_adOnMap);
                map.setText("Marked on Map");
                ImageView mark = rootView.findViewById(R.id.iv_mark);
                mark.setImageDrawable(getResources().getDrawable(ic_check_circle));
                dialog.dismiss();
                isLoactionAdded = true;
            }
        });
    }
    

    private void showAlertDialogExit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Are you sure you want to Discard?")
                .setCancelable(false)
                .setPositiveButton("Discard", (dialog, id) -> {
                    mActivity.onBackPressed();
                })
                .setNegativeButton("No", (dialog, id) -> dialog.cancel());
        AlertDialog alert = builder.create();
        alert.show();
    }
    private void openLoadingDialog() {
        dialog.setContentView(R.layout.progress_bar);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.show();
    }

    private void getNoOfProperties() {
        Spinner rooms = rootView.findViewById(R.id.SpinnerRooms);
        NoRooms = rooms.getSelectedItem().toString();
        Spinner aFacing = rootView.findViewById(R.id.aSpinner4);
        facing = aFacing.getSelectedItem().toString();
        Spinner aPropertyType = rootView.findViewById(R.id.aSpinner3);
        propertyType = aPropertyType.getSelectedItem().toString();
        Spinner aFloor = rootView.findViewById(R.id.aSpinner5);
        floor = aFloor.getSelectedItem().toString();
        status = Status;
        parking = Cb2 + Cb4;

        if (rooms.getSelectedItem().toString() == "Shop"){
            getNoOfProperties = FirebaseDatabase.getInstance().getReference().child(pincode + "S");
            getNoOfProperties.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild("no of S Registered")) {
                        num2 = dataSnapshot.child("no of S Registered").getValue().toString();
                        Sn();
                    }else {
                        S1();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }else {
            getNoOfProperties = FirebaseDatabase.getInstance().getReference().child(pincode + "H");
            getNoOfProperties.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild("no of H Registered")){
                        num = dataSnapshot.child("no of H Registered").getValue().toString();
                        Hn();
                    }else {
                        H1();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
    
    private void H1(){
        if (isValueAddedH1 == true) {
            isValueAddedHn = false;
            isValueAddedH1 = false;
            updateNoOfProperties = FirebaseDatabase.getInstance().getReference().child(pincode + "H");
            updateNoOfProperties.child("no of H Registered").setValue(test);
            propertyDetails = FirebaseDatabase.getInstance().getReference().child(pincode + "H");
            propertyDetails.child("1").setValue(I1Url + "," + I2Url + "," + I3Url + "," + I4Url + "," + I5Url + "," + propertyType + "," + NoRooms + "," + facing + "," + status + "," + floor + "," + parking + "," + address + "," + pincode + "," + Lat + "," + Lng + "," + rent + "," + "1" + "," + UserNm + "," + UserPhnNm + "," + "N");
            String id = "1";
            String HorS = "H";
            addPosting(id,HorS);
        }
    }

    private void Hn() {
        if (isValueAddedHn == true) {
            isValueAddedHn = false;
            String rawNo = num;
            String noOfH = rawNo.replace("\"", "");
            String temp1 = "1";
            int sum = Integer.parseInt(noOfH) + Integer.parseInt(temp1);
            sumH = String.valueOf(sum);
            propertyDetails = FirebaseDatabase.getInstance().getReference().child(pincode + "H");
            propertyDetails.child(String.valueOf(sum)).setValue(I1Url + "," + I2Url + "," + I3Url + "," + I4Url + "," + I5Url + "," + propertyType + "," + NoRooms + "," + facing + "," + status + "," + floor + "," + parking + "," + address + "," + pincode + "," + Lat + "," + Lng + "," + rent + "," + sum + "," + UserNm + "," + UserPhnNm + "," + "N");
            HashMap hashMap = new HashMap();
            hashMap.put("no of H Registered", sumH);
            updateNoOfProperties = FirebaseDatabase.getInstance().getReference().child(pincode + "H");
            updateNoOfProperties.updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                }
            });
            String id = sumH;
            String HorS = "H";
            addPosting(id,HorS);
        }
    }

    private void S1() {
        if (isValueAddedS1 == true) {
            isValueAddedSn = false;
            isValueAddedS1 = false;
            updateNoOfProperties = FirebaseDatabase.getInstance().getReference().child(pincode + "S");
            updateNoOfProperties.child("no of S Registered").setValue(test);
            propertyDetails = FirebaseDatabase.getInstance().getReference().child(pincode + "S");
            propertyDetails.child("1").setValue(I1Url + "," + I2Url + "," + I3Url + "," + I4Url + "," + I5Url + "," + propertyType + "," + NoRooms + "," + facing + "," + status + "," + floor + "," + parking + "," + address + "," + pincode + "," + Lat + "," + Lng + "," + rent + "," + "1" + "," + UserNm + "," + UserPhnNm + "," + "N");
            String id = "1";
            String HorS = "S";
            addPosting(id,HorS);
        }

    }

    private void Sn() {
        if (isValueAddedSn == true) {
            isValueAddedSn = false;
            String rawNo = num2;
            String noOfS = rawNo.replace("\"", "");
            String temp1 = "1";
            int sum = Integer.parseInt(noOfS) + Integer.parseInt(temp1);
            sumS = String.valueOf(sum);
            propertyDetails = FirebaseDatabase.getInstance().getReference().child(pincode + "S");
            propertyDetails.child(String.valueOf(sumS)).setValue(I1Url + "," + I2Url + "," + I3Url + "," + I4Url + "," + I5Url + "," + propertyType + "," + NoRooms + "," + facing + "," + status + "," + floor + "," + parking + "," + address + "," + pincode + "," + Lat + "," + Lng + "," + rent + "," + sum + "," + UserNm + "," + UserPhnNm + "," + "N");
            HashMap hashMap = new HashMap();
            hashMap.put("no of S Registered", sumS);
            updateNoOfProperties = FirebaseDatabase.getInstance().getReference().child(pincode + "S");
            updateNoOfProperties.updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                }
            });
            String id = sumS;
            String HorS = "S";
            addPosting(id,HorS);
        }
    }
    private void addPosting(String id, String HorS) {
        if (once == true) {
            once = false;
            if (totalPostings.equals("0")) {
                String temp1 = "1";
                int addProperties = Integer.parseInt(totalProperties) + Integer.parseInt(temp1);
                updateTotalProperties = FirebaseDatabase.getInstance().getReference().child("Server");
                updateTotalProperties.child("Total Properties Registered").setValue(addProperties);

                updatePostings = FirebaseDatabase.getInstance().getReference().child(UserPhnNm);
                updatePostings.child("1").setValue(id + "," + pincode+HorS);

                updateNoOfPostings = FirebaseDatabase.getInstance().getReference().child("Phone Numbers");
                updateNoOfPostings.child(UserPhnNm).setValue("1");

                dialog.dismiss();
                openSuccessDialog();
            }else {
                String raw = totalPostings;
                String total = raw.replace("\"", "");
                String temp1 = "1";
                int add = Integer.parseInt(total) + Integer.parseInt(temp1);
                int addProperties = Integer.parseInt(totalProperties) + Integer.parseInt(temp1);
                updateTotalProperties = FirebaseDatabase.getInstance().getReference().child("Server");
                updateTotalProperties.child("Total Properties Registered").setValue(addProperties);

                updatePostings = FirebaseDatabase.getInstance().getReference().child(UserPhnNm);
                updatePostings.child(String.valueOf(add)).setValue(id + "," + pincode+HorS);

                updateNoOfPostings = FirebaseDatabase.getInstance().getReference().child("Phone Numbers");
                updateNoOfPostings.child(UserPhnNm).setValue(add);

                dialog.dismiss();
                openSuccessDialog();
            }
        }
    }
}