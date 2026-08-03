package io.kodular.nsmarttechnologies6.RentEasy.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import io.kodular.nsmarttechnologies6.RentEasy.R;
import io.kodular.nsmarttechnologies6.RentEasy.Utility.NetworkChangeListener;
import io.kodular.nsmarttechnologies6.RentEasy.support.Utils;

public class EditListingActivity extends AppCompatActivity {

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    String Token = "cHTc0aPgSdacQCtBi7VNRr:APA91bH1ImfzYeRUKZ4r1RbmW7Zk1t8_ObxCQ1etT3FMqfkvE2Rs4n7PfVkdCjZtzRQJ8f71DlO0K2XTjYwtsHgAQjZ_ttBvLW2Ou2uP_T4XSUfodDjreFJ04-Zp3kebcCCbi-9mjAOM";
    io.RentEasy.views.MediumTextView tvAmountIndicator,tvPromote;
    TextInputEditText etTitle,etDes,etAmount,etDeposit;
    LinearLayout save;
    Spinner Cycle;
    String Status = "",pincode, ID,pastStatus,selectedCycle,Phno;
    DatabaseReference getListingInfo,updateConnections,setPromoted,notificationId;
    RelativeLayout backLay,securityLay;
    Dialog dialog;
    boolean once=true,promote=true,isDeposit;
    ImageView iv1,iv2,iv3,iv4,iv5;
    ImageView ivE1,ivE2,ivE3,ivE4,ivE5;
    ImageView ivR1,ivR2,ivR3,ivR4,ivR5;
    int SELECT_PHOTO = 1;
    int Id = 0;
    Uri uri1,uri2,uri3,uri4,uri5;
    boolean i1,i2,i3,i4,i5;
    boolean e1=false,e2=false,e3=false,e4=false,e5=false;
    boolean r1=false,r2=false,r3=false,r4=false,r5=false;
    boolean load=true;
    int totalUpdatesPics = 0;
    FirebaseStorage storage,storage2,storage3,storage4,storage5;
    StorageReference storageReference,storageReference2,storageReference3,storageReference4,storageReference5;
    String I1url,I2url,I3url,I4url,I5url;
    String cloudinary = "https://res.cloudinary.com/nsmarttechnologies/image/upload/";
    Switch activeStatus;
    io.RentEasy.views.RegularTextView tvAvialable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_listing);

        dialog =new Dialog(this);

        openLoadingDialog();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        storage2 =FirebaseStorage.getInstance();
        storageReference2=storage2.getReference();

        storage3=FirebaseStorage.getInstance();
        storageReference3 =storage3.getReference();

        storage4=FirebaseStorage.getInstance();
        storageReference4=storage4.getReference();

        storage5=FirebaseStorage.getInstance();
        storageReference5=storage5.getReference();

        FirebaseMessaging.getInstance().subscribeToTopic("all");

        //assigning variables
        tvPromote = findViewById(R.id.tv_promote);
        save = findViewById(R.id.saveLay);
        Cycle = findViewById(R.id.SpinnerRentCycle);
        etTitle = findViewById(R.id.et_title);
        etDes = findViewById(R.id.et_description);
        etAmount = findViewById(R.id.et_rent);
        tvAmountIndicator = findViewById(R.id.tv_amount_indicator);
        backLay = findViewById(R.id.back_lay);
        iv1 = findViewById(R.id.image1);
        iv2 =findViewById(R.id.image2);
        iv3 = findViewById(R.id.image3);
        iv4 = findViewById(R.id.image4);
        iv5 = findViewById(R.id.image5);
        ivE1 = findViewById(R.id.iv_edit1);
        ivE2 = findViewById(R.id.iv_edit2);
        ivE3 = findViewById(R.id.iv_edit3);
        ivE4 = findViewById(R.id.iv_edit4);
        ivE5 = findViewById(R.id.iv_edit5);
        ivR1 = findViewById(R.id.ivR1);
        ivR2 = findViewById(R.id.ivR2);
        ivR3 = findViewById(R.id.ivR3);
        ivR4 = findViewById(R.id.ivR4);
        ivR5 = findViewById(R.id.ivR5);
        tvAvialable=findViewById(R.id.tv_avialable_label);
        activeStatus=findViewById(R.id.active_status_switch);
        securityLay=findViewById(R.id.security_lay);
        etDeposit=findViewById(R.id.input_deposit);

        //getting intents
        pincode = getIntent().getStringExtra("pincode");
        ID = getIntent().getStringExtra("id");

        //getting listing info
        getListingInfo = FirebaseDatabase.getInstance().getReference().child("listings data").child(pincode).child(ID);
        getListingInfo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (load==true) {
                    load=false;
                    if (dataSnapshot.hasChild("0")) {
                        String raw = dataSnapshot.child("0").getValue().toString();
                        if (raw.contains(cloudinary)) {
                            I1url = raw;
                        }else {
                            I1url = "https://firebasestorage.googleapis.com/v0/b/renteasy-f31ba.appspot.com/o/" + raw;
                        }
                        Glide
                                .with(EditListingActivity.this)
                                .load(I1url)
                                .into(iv1);
                        i1=true;
                    }else {ivE1.setVisibility(View.VISIBLE);iv1.setVisibility(View.GONE);ivR1.setVisibility(View.GONE);i1=false;}
                    if (dataSnapshot.hasChild("1")) {
                        String raw = dataSnapshot.child("1").getValue().toString();
                        if (raw.contains(cloudinary)) {
                            I2url = raw;
                        } else {
                            I2url = "https://firebasestorage.googleapis.com/v0/b/renteasy-f31ba.appspot.com/o/" + raw;
                        }
                        Glide
                                .with(EditListingActivity.this)
                                .load(I2url)
                                .into(iv2);
                        i2=true;
                    }else {ivE2.setVisibility(View.VISIBLE);iv2.setVisibility(View.GONE);ivR2.setVisibility(View.GONE);i2=false;}
                    if (dataSnapshot.hasChild("2")) {
                        String raw = dataSnapshot.child("2").getValue().toString();
                        if (raw.contains(cloudinary)) {
                            I3url=raw;
                        }else {
                            I3url = "https://firebasestorage.googleapis.com/v0/b/renteasy-f31ba.appspot.com/o/"+raw;
                        }
                        Glide
                                .with(EditListingActivity.this)
                                .load(I3url)
                                .into(iv3);
                        i3=true;
                    }else {ivE3.setVisibility(View.VISIBLE);iv3.setVisibility(View.GONE);ivR3.setVisibility(View.GONE);i3=false;}
                    if (dataSnapshot.hasChild("3")) {
                        String raw = dataSnapshot.child("3").getValue().toString();
                        if (raw.contains(cloudinary)) {
                            I4url=raw;
                        }else {
                            I4url = "https://firebasestorage.googleapis.com/v0/b/renteasy-f31ba.appspot.com/o/" + raw;
                        }
                        Glide
                                .with(EditListingActivity.this)
                                .load(I4url)
                                .into(iv4);
                        i4=true;
                    }else {ivE4.setVisibility(View.VISIBLE);iv4.setVisibility(View.GONE);ivR4.setVisibility(View.GONE);i4=false;}
                    if (dataSnapshot.hasChild("4")) {
                        String raw = dataSnapshot.child("4").getValue().toString();
                        if (raw.contains(cloudinary)) {
                            I5url=raw;
                        }else {
                            I5url = "https://firebasestorage.googleapis.com/v0/b/renteasy-f31ba.appspot.com/o/" + raw;
                        }
                        Glide
                                .with(EditListingActivity.this)
                                .load(I5url)
                                .into(iv5);
                        i5=true;
                    }else {ivE5.setVisibility(View.VISIBLE);iv5.setVisibility(View.GONE);ivR5.setVisibility(View.GONE);i5=false;}

                    String rate = dataSnapshot.child("5").getValue().toString();
                    String[] split = rate.split(",");
                    String Rate = split[0];
                    etAmount.setText(Rate);
                    String cycle = split[1];
                    if (cycle.equals("m")) {
                        tvAmountIndicator.setText("Rent per Month");
                        Cycle.setSelection(0);
                    } else if (cycle.equals("d")) {
                        tvAmountIndicator.setText("Rent per Day");
                        Cycle.setSelection(1);
                    } else {
                        tvAmountIndicator.setText("Rent per Hour");
                        Cycle.setSelection(2);
                    }
                    etTitle.setText(dataSnapshot.child("6").getValue().toString());
                    etDes.setText(dataSnapshot.child("7").getValue().toString());
                    Status = dataSnapshot.child("9").getValue().toString();
                    pastStatus=Status;
                    if (Status.equals("N")) {
                        activeStatus.setChecked(false);
                        tvAvialable.setText("Not-Available");
                    }else {
                        activeStatus.setChecked(true);
                        tvAvialable.setText("Available");
                    }
                    Phno = dataSnapshot.child("17").getValue().toString();

                    if (dataSnapshot.hasChild("22")) {
                        isDeposit=true;
                        etDeposit.setText(dataSnapshot.child("22").getValue().toString());
                    }else {
                        isDeposit=false;
                        securityLay.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //on user change rent cycle
        Cycle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (Cycle.getSelectedItem().equals("Month")) {
                    selectedCycle="m";
                    tvAmountIndicator.setText("Rent per Month");
                }else if (Cycle.getSelectedItem().equals("Day")) {
                    selectedCycle="d";
                    tvAmountIndicator.setText("Rent per Day");
                }else {
                    selectedCycle="h";
                    tvAmountIndicator.setText("Rent per Hour");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //on user change status
        activeStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked==true) {
                    Status="A";
                    tvAvialable.setText("Available");
                }else {
                    Status="N";
                    tvAvialable.setText("Not-Available");
                }
            }
        });


        //on user click save details
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EditListingActivity.this);
                builder.setMessage("Are you sure you want to save?")
                        .setCancelable(false)
                        .setPositiveButton("Save", (dialog, id) -> {
                            if (etTitle.getText().toString().isEmpty()) {
                                Toast.makeText(EditListingActivity.this, "Please enter title.", Toast.LENGTH_LONG).show();
                            }else {
                                if ( etDes.getText().toString().isEmpty()) {
                                    Toast.makeText(EditListingActivity.this, "Please enter description.", Toast.LENGTH_LONG).show();
                                }else {
                                    if ( etAmount.getText().toString().isEmpty()) {
                                        Toast.makeText(EditListingActivity.this, "Please enter rent amount.", Toast.LENGTH_LONG).show();
                                    }else {
                                        if (isDeposit) {
                                            if (etDeposit.getText().toString().isEmpty()) {
                                                Toast.makeText(EditListingActivity.this, "Please enter security deposit amount.", Toast.LENGTH_LONG).show();
                                            }
                                            else {
                                                checkstatus();
                                            }
                                        }else {
                                            checkstatus();
                                        }
                                    }
                                }
                            }
                        })
                        .setNegativeButton("No", (dialog, id) -> dialog.cancel());
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        backLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //setting on click listeners for images
        ivE1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Id=1;
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,SELECT_PHOTO);
            }
        });

        ivE2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Id=2;
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,SELECT_PHOTO);
            }
        });

        ivE3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Id=3;
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,SELECT_PHOTO);
            }
        });

        ivE4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Id=4;
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,SELECT_PHOTO);
            }
        });

        ivE5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Id=5;
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,SELECT_PHOTO);
            }
        });

        ivR1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv1.setVisibility(View.GONE);
                ivR1.setVisibility(View.GONE);
                ivE1.setVisibility(View.VISIBLE);
                r1=true;
            }
        });

        ivR2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv2.setVisibility(View.GONE);
                ivR2.setVisibility(View.GONE);
                ivE2.setVisibility(View.VISIBLE);
                r2=true;
            }
        });

        ivR3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv3.setVisibility(View.GONE);
                ivR3.setVisibility(View.GONE);
                ivE3.setVisibility(View.VISIBLE);
                r3=true;
            }
        });

        ivR4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv4.setVisibility(View.GONE);
                ivR4.setVisibility(View.GONE);
                ivE4.setVisibility(View.VISIBLE);
                r4=true;
            }
        });

        ivR5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv5.setVisibility(View.GONE);
                ivR5.setVisibility(View.GONE);
                ivE5.setVisibility(View.VISIBLE);
                r5=true;
            }
        });

        tvPromote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //getting my notification id
                notificationId = FirebaseDatabase.getInstance().getReference().child("Server");
                notificationId.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Token = dataSnapshot.child("notification token").getValue().toString();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                AlertDialog.Builder builder = new AlertDialog.Builder(EditListingActivity.this);
                builder.setMessage("Are you sure you want to apply for promotion?")
                        .setCancelable(false)
                        .setPositiveButton("Apply", (dialog, id) -> {
                            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy.hh.mm.ss");
                            String currentDateandTime = sdf.format(new Date());
                            setPromoted = FirebaseDatabase.getInstance().getReference().child("promoted");
                            setPromoted.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.hasChild(Phno)) {
                                        for (DataSnapshot ds : dataSnapshot.child(Phno).getChildren()) {
                                            String raw = ds.getValue().toString();
                                            String[] split = raw.split(",");
                                            if (pincode.equals(split[0])) {
                                                if (ID.equals(split[1])) {
                                                    Toast.makeText(EditListingActivity.this, "Successfully applied for promotion. We will call or whatsapp you as soon as possible.", Toast.LENGTH_LONG).show();
                                                }else {
                                                    if (promote==true) {
                                                        promote=false;
                                                        setPromoted.child(Phno).push().setValue(pincode+","+ID+","+currentDateandTime);
                                                    }
                                                }
                                            }else {
                                                if (promote==true) {
                                                    promote=false;
                                                    setPromoted.child(Phno).push().setValue(pincode+","+ID+","+currentDateandTime);
                                                }
                                            }
                                        }
                                    }else {
                                        if (promote==true) {
                                            promote=false;
                                            setPromoted.child(Phno).push().setValue(pincode+","+ID+","+currentDateandTime);
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        })
                        .setNegativeButton("No", (dialog, id) -> dialog.cancel());
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        dialog.dismiss();

    }

    private void checkstatus() {
        if (Status.equals("N")){
            if (!pastStatus.equals("N")) {
                showStatusSurveyDialog();
            }else {
                openLoadingDialog();
                try {
                    saveDetails();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else {
            openLoadingDialog();
            try {
                saveDetails();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_PHOTO && resultCode == RESULT_OK && data !=null && data.getData() !=null) {
            if (Id==1) {
                uri1 = data.getData();
                iv1.setVisibility(View.VISIBLE);
                ivE1.setVisibility(View.GONE);
                iv1.setImageURI(uri1);
                e1=true;
            }else if (Id==2) {
                uri2 = data.getData();
                iv2.setVisibility(View.VISIBLE);
                ivE2.setVisibility(View.GONE);
                iv2.setImageURI(uri2);
                e2=true;
            } else if (Id==3) {
                uri3 = data.getData();
                iv3.setVisibility(View.VISIBLE);
                ivE3.setVisibility(View.GONE);
                iv3.setImageURI(uri3);
                e3=true;
            } else if (Id==4) {
                uri4 = data.getData();
                iv4.setVisibility(View.VISIBLE);
                ivE4.setVisibility(View.GONE);
                iv4.setImageURI(uri4);
                e4=true;
            } else if (Id==5) {
                uri5 = data.getData();
                iv5.setVisibility(View.VISIBLE);
                ivE5.setVisibility(View.GONE);
                iv5.setImageURI(uri5);
                e5=true;
            }
          }
    }

    private void saveDetails() throws IOException {

        HashMap hashMap = new HashMap();
        hashMap.put("6",etTitle.getText().toString());
        hashMap.put("7",etDes.getText().toString());
        hashMap.put("5",etAmount.getText().toString()+","+selectedCycle);
        hashMap.put("9",Status);
        if (isDeposit) {
            hashMap.put("22",etDeposit.getText().toString());
        }
        getListingInfo.updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(@NonNull Object o) {
                check1();
            }
        });
    }

    private void check1() {
        if (e1==true) {
            if (r1==true) {
                //when the user deleted the old image
                //also delete the image
                if (!I1url.contains(cloudinary)) {
                    StorageReference imageRef = storage.getReferenceFromUrl(I1url);
                    imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(@NonNull Void aVoid) {
                            getListingInfo.child("0").removeValue();
                            try {
                                uploadPicture1(uri1,"0");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            getListingInfo.child("0").removeValue();
                            try {
                                uploadPicture1(uri1,"0");
                            } catch (IOException ioException) {
                                ioException.printStackTrace();
                            }
                        }
                    });
                }else {
                    try {
                        uploadPicture1(uri1,"0");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }else {
                //when the user has added the image for first time.
                try {
                    uploadPicture1(uri1,"0");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else {
            if (r1==true) {
                //when the user deleted the old image
                if (!I1url.contains(cloudinary)) {
                    StorageReference imageRef = storage.getReferenceFromUrl(I1url);
                    imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(@NonNull Void aVoid) {
                            getListingInfo.child("0").removeValue();
                            check2();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            getListingInfo.child("0").removeValue();
                            check2();
                        }
                    });
                } else {check2();}
            }else {check2();}
        }
    }

    private void check2() {
        if (e2==true) {
            if (r2==true) {
                //when the user deleted the old image
                //also delete the image
                if (!I2url.contains(cloudinary)) {
                    StorageReference imageRef = storage2.getReferenceFromUrl(I2url);
                    imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(@NonNull Void aVoid) {
                            getListingInfo.child("1").removeValue();
                            try {
                                uploadPicture1(uri2, "1");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            getListingInfo.child("1").removeValue();
                            try {
                                uploadPicture1(uri2, "1");
                            } catch (IOException ioException) {
                                ioException.printStackTrace();
                            }
                        }
                    });
                }else {
                    try {
                        uploadPicture1(uri2, "1");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }else {
                //when the user has added the image for first time.
                try {
                    uploadPicture1(uri2,"1");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else {
            if (r2==true) {
                //when the user deleted the old image
                if (!I2url.contains(cloudinary)) {
                    StorageReference imageRef = storage2.getReferenceFromUrl(I2url);
                    imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(@NonNull Void aVoid) {
                            getListingInfo.child("1").removeValue();
                            check3();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            getListingInfo.child("1").removeValue();
                            check3();
                        }
                    });
                } else {check3();}
            }else {check3();}
        }
    }

    private void check3() {
        if (e3==true) {
            if (r3==true) {
                //when the user deleted the old image
                //also delete the image
                if (!I3url.contains(cloudinary)) {
                    StorageReference imageRef = storage3.getReferenceFromUrl(I3url);
                    imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(@NonNull Void aVoid) {
                            getListingInfo.child("2").removeValue();
                            try {
                                uploadPicture1(uri3, "2");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            getListingInfo.child("2").removeValue();
                            try {
                                uploadPicture1(uri3, "2");
                            } catch (IOException ioException) {
                                ioException.printStackTrace();
                            }
                        }
                    });
                }else {
                    try {
                        uploadPicture1(uri3, "2");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }else {
                //when the user has added the image for first time.
                try {
                    uploadPicture1(uri3,"2");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else {
            if (r3==true) {
                //when the user deleted the old image
                if (!I3url.contains(cloudinary)) {
                    StorageReference imageRef = storage3.getReferenceFromUrl(I3url);
                    imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(@NonNull Void aVoid) {
                            getListingInfo.child("2").removeValue();
                            check4();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            getListingInfo.child("2").removeValue();
                            check4();
                        }
                    });
                }else {check4();}
            }else {check4();}
        }
    }

    private void check4() {
        if (e4==true) {
            if (r4==true) {
                //when the user deleted the old image
                //also delete the image
                if (!I4url.contains(cloudinary)) {
                    StorageReference imageRef = storage4.getReferenceFromUrl(I4url);
                    imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(@NonNull Void aVoid) {
                            getListingInfo.child("3").removeValue();
                            try {
                                uploadPicture1(uri4, "3");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            getListingInfo.child("3").removeValue();
                            try {
                                uploadPicture1(uri4, "3");
                            } catch (IOException ioException) {
                                ioException.printStackTrace();
                            }
                        }
                    });
                }else {
                    try {
                        uploadPicture1(uri4, "3");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }else {
                //when the user has added the image for first time.
                try {
                    uploadPicture1(uri4,"3");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else {
            if (r4==true) {
                //when the user deleted the old image
                if (!I4url.contains(cloudinary)) {
                    StorageReference imageRef = storage4.getReferenceFromUrl(I4url);
                    imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(@NonNull Void aVoid) {
                            getListingInfo.child("3").removeValue();
                            check5();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            getListingInfo.child("3").removeValue();
                            check5();
                        }
                    });
                }else {check5();}
            }else {check5();}
        }
    }

    private void check5() {
        if (e5==true) {
            if (r5==true) {
                //when the user deleted the old image
                //also delete the image
                if (!I5url.contains(cloudinary)) {
                    StorageReference imageRef = storage5.getReferenceFromUrl(I5url);
                    imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(@NonNull Void aVoid) {
                            getListingInfo.child("4").removeValue();
                            try {
                                uploadPicture1(uri5, "4");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            getListingInfo.child("4").removeValue();
                            try {
                                uploadPicture1(uri5, "4");
                            } catch (IOException ioException) {
                                ioException.printStackTrace();
                            }
                        }
                    });
                }else {
                    try {
                        uploadPicture1(uri5, "4");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }else {
                //when the user has added the image for first time.
                try {
                    uploadPicture1(uri5,"4");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else {
            if (r5==true) {
                //when the user deleted the old image
                if (!I5url.contains(cloudinary)) {
                    StorageReference imageRef = storage5.getReferenceFromUrl(I5url);
                    imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(@NonNull Void aVoid) {
                            getListingInfo.child("4").removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(@NonNull Void aVoid) {
                                    dialog.dismiss();
                                    Utils.startActivity(EditListingActivity.this, HomeActivity.class);
                                    Toast.makeText(getApplicationContext(), "Saved Successfully.", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            getListingInfo.child("4").removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(@NonNull Void aVoid) {
                                    dialog.dismiss();
                                    Utils.startActivity(EditListingActivity.this, HomeActivity.class);
                                    Toast.makeText(getApplicationContext(), "Saved Successfully.", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    });
                }else {
                    dialog.dismiss();
                    Utils.startActivity(EditListingActivity.this, HomeActivity.class);
                    Toast.makeText(getApplicationContext(),"Saved Successfully.",Toast.LENGTH_LONG).show();
                }
            }else {
                dialog.dismiss();
                Utils.startActivity(EditListingActivity.this, HomeActivity.class);
                Toast.makeText(getApplicationContext(),"Saved Successfully.",Toast.LENGTH_LONG).show();
            }
        }
    }

    private void uploadPicture1(Uri uri,String id) throws IOException {
        Bitmap bmp = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
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
                                String url = uri.toString().replace("https://firebasestorage.googleapis.com/v0/b/renteasy-f31ba.appspot.com/o/","");
                                HashMap hashMap = new HashMap();
                                hashMap.put(id,url);
                                getListingInfo.updateChildren(hashMap);
                                if (id.equals("0")) {
                                    check2();
                                } else if (id.equals("1")) {
                                    check3();
                                } else if (id.equals("2")) {
                                    check4();
                                } else if (id.equals("3")) {
                                    check5();
                                } else {
                                    dialog.dismiss();
                                    Utils.startActivity(EditListingActivity.this, HomeActivity.class);
                                    Toast.makeText(getApplicationContext(),"Saved Successfully.",Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditListingActivity.this,"An error occurred while uploading image.",Toast.LENGTH_SHORT).show();
                    }
               });
    }

    private void showStatusSurveyDialog() {
        dialog.setContentView(R.layout.status_off_survey_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);

        Spinner spinner = dialog.findViewById(R.id.Spinner);

        io.RentEasy.views.MediumTextView tvClose = dialog.findViewById(R.id.tv_close);
        tvClose.setVisibility(View.VISIBLE);
        tvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spinner.getSelectedItem().equals("Select a reason")) {
                    Toast.makeText(getApplicationContext(), "We are trying our best to connect owners and renter. Please help us by selecting a reason.", Toast.LENGTH_LONG).show();
                }
            }
        });

        io.RentEasy.views.MediumTextView offStatus = dialog.findViewById(R.id.tv_ok);
        offStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spinner.getSelectedItem().equals("Select a reason")) {
                    Toast.makeText(getApplicationContext(), "Please select a reason.", Toast.LENGTH_LONG).show();
                }
                else {
                    updateConnections = FirebaseDatabase.getInstance().getReference().child("Server");
                    updateConnections.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (spinner.getSelectedItem().equals("I found renter using RentEasy.")) {
                                String total = dataSnapshot.child("total connected").getValue().toString();
                                int sum = Integer.parseInt(total) + 1;
                                HashMap hashMap = new HashMap();
                                hashMap.put("total connected",sum);
                                if (once==true) {
                                    once=false;
                                    updateConnections.updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                                        @Override
                                        public void onSuccess(@NonNull Object o) {
                                            dialog.dismiss();
                                            Toast.makeText(getApplicationContext(), "Your Listings Status is Turned off. Thank you for helping us.", Toast.LENGTH_LONG).show();
                                            try {
                                                saveDetails();
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                                }
                            }else if (spinner.getSelectedItem().equals("I found renter offline.")){
                                String total = dataSnapshot.child("total connected offline").getValue().toString();
                                int sum = Integer.parseInt(total) + 1;
                                HashMap hashMap = new HashMap();
                                hashMap.put("total connected offline",sum);
                                if (once==true) {
                                    once=false;
                                    updateConnections.updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                                        @Override
                                        public void onSuccess(@NonNull Object o) {
                                            dialog.dismiss();
                                            Toast.makeText(getApplicationContext(), "Your Listings Status is Turned off. Thank you for helping us.", Toast.LENGTH_LONG).show();
                                            try {
                                                saveDetails();
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                                }
                            }else {
                                dialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Your Listings Status is Turned off. Thank you for helping us.", Toast.LENGTH_LONG).show();
                                try {
                                    saveDetails();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });

        dialog.show();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(EditListingActivity.this);
        builder.setMessage("Are you sure you want to go back with out saving?")
                .setCancelable(false)
                .setPositiveButton("Go Back", (dialog, id) -> {
                    super.onBackPressed();
                    Utils.hideKeyboard(this);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                })
                .setNegativeButton("No", (dialog, id) -> dialog.cancel());
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void openLoadingDialog() {
        dialog.setContentView(R.layout.uploading_dilalog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        io.RentEasy.views.MediumTextView uploding = dialog.findViewById(R.id.uploading);
        uploding.setText("Updating..");
        dialog.show();
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