package io.kodular.nsmarttechnologies6.RentEasy.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.List;

import io.kodular.nsmarttechnologies6.RentEasy.R;

import io.kodular.nsmarttechnologies6.RentEasy.Utility.MyPostings;
import io.kodular.nsmarttechnologies6.RentEasy.activity.DetailActivity;
import io.kodular.nsmarttechnologies6.RentEasy.activity.EditListingActivity;

public class MyPostingsItemAdapter extends RecyclerView.Adapter<MyPostingsItemAdapter.ViewHolder> {
    private Activity context;
    private List<MyPostings> list;
    DatabaseReference getListingInfo;
    DatabaseReference deleteListings,updateConnections;
    String getDetails = "";
    String editPincodeBucket = "";
    String editId = "";
    String Status = "",pastStatus="";
    String city;
    boolean once=true,open=false;
    FirebaseStorage storage;
    String Cloudinar = "https://res.cloudinary.com/nsmarttechnologies/image/upload/";

    public MyPostingsItemAdapter(List<MyPostings> list, Activity context) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyPostingsItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_posting, parent, false);
        return new MyPostingsItemAdapter.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyPostingsItemAdapter.ViewHolder viewHolder, final int position) {
        String Rate=list.get(position).getRate();
        String description=list.get(position).getDescription();
        String Image=list.get(position).getImage();
        String pincodeBucket=list.get(position).getPincodeBucket();
        String id=list.get(position).getid();
        editId = id;
        editPincodeBucket = pincodeBucket;

        viewHolder.setData(Rate,description,Image,pincodeBucket,id);

        //on click card view listener
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("listingId", id);
                intent.putExtra("pincode", pincodeBucket);
                context.startActivity(intent);
            }
        });

        // on click listener for user editing the listing
        viewHolder.editLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditListingActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("pincode", pincodeBucket);
                context.startActivity(intent);
            }
        });

        //on click listener for user deleting the listing
        viewHolder.deleteLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storage = FirebaseStorage.getInstance();
                viewHolder.showAlertDialogDelete(viewHolder.itemView,position,pincodeBucket,id);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder  {

        ImageView ivImage,ivStatus,ivStatistics;
        LinearLayout editLay,deleteLay;
        io.RentEasy.views.RegularTextView tvDescription,tvAddress,tvViews,tvCall,tvWhastapp,tvArea;
        io.RentEasy.views.MediumTextView tvRate,tvCategory;
        Dialog dialog;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.iv_image);
            editLay = itemView.findViewById(R.id.editLay);
            tvRate = itemView.findViewById(R.id.tv_rate);
            tvDescription = itemView.findViewById(R.id.tv_description);
            tvAddress = itemView.findViewById(R.id.tv_location);
            deleteLay = itemView.findViewById(R.id.deleteLay);
            ivStatus=itemView.findViewById(R.id.iv_info_status);
            tvViews=itemView.findViewById(R.id.tv_views);
            tvCall=itemView.findViewById(R.id.tv_calll);
            tvWhastapp=itemView.findViewById(R.id.tv_whatsapp);
            tvArea=itemView.findViewById(R.id.tv_location);

            tvCategory = itemView.findViewById(R.id.tv_category_myposting);
            ivStatistics=itemView.findViewById(R.id.iv_statistics);
            dialog =new Dialog(context);
        }

        public void setData(String rate, String description, String Image, String pincodeBucket, String id) {
            tvRate.setText(rate);
            tvDescription.setText(description);
            /*
            GeoLocation geoLocation = new GeoLocation();
            geoLocation.getAddress(pincodeBucket,context,new GeoHandler());

             */
            String noImageUrl = "https://res.cloudinary.com/nsmarttechnologies/image/upload/v1642161658/no_image_pokwhn.png";
            if ( Image.equals("No")) {
                Glide
                        .with(context)
                        .load(noImageUrl)
                        .into(ivImage);
            }else {
                if (Image.contains("https://res.cloudinary.com/nsmarttechnologies/image/upload/")) {
                    Glide
                            .with(context)
                            .load(Image)
                            .centerCrop()
                            .into(ivImage);
                }else {
                    Glide
                            .with(context)
                            .load("https://firebasestorage.googleapis.com/v0/b/renteasy-f31ba.appspot.com/o/"+Image)
                            .centerCrop()
                            .into(ivImage);
                }
            }
            //getting total views
            getListingInfo = FirebaseDatabase.getInstance().getReference().child("listings data").child(pincodeBucket).child(id);
            getListingInfo.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild("12")) {
                            tvViews.setText("Views : " + dataSnapshot.child("12").getValue().toString());
                        }
                        if (dataSnapshot.hasChild("19")) {
                            String raw = dataSnapshot.child("19").getValue().toString();
                            String[] split = raw.split(",");
                            tvCall.setText(split[0]);
                            tvWhastapp.setText(split[1]);
                        }
                        String cityId = dataSnapshot.child("20").getValue().toString();
                        String areaId = dataSnapshot.child("21").getValue().toString();
                        if (cityId.equals("1")) {
                            city = "Hyderabad";
                        }
                        if (cityId.equals("2")) {
                            city = "Visakhapatnam";
                        }
                        if (cityId.equals("3")) {
                            city = "Delhi";
                        }
                        if (cityId.equals("4")) {
                            city = "Bengaluru";
                        }
                        if (cityId.equals("5")) {
                            city = "Mumbai";
                        }

                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        DocumentReference docRef = db.collection(city).document(areaId);
                        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        tvArea.setText(document.get("n").toString());
                                    } else {
                                        Toast.makeText(context, "failed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        public void showAlertDialogDelete(View view, int position, String pincode , String listingId ) {
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            builder.setMessage("Are you sure you want to Delete your Posting?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", (DialogInterface dialog, int id) -> {
                        //when pressed yes to delete the listing
                        HashMap hashMap = new HashMap();
                        hashMap.put("is deleted","t");
                        deleteListings = FirebaseDatabase.getInstance().getReference().child("listings data").child(pincode).child(listingId);
                        deleteListings.addValueEventListener(new ValueEventListener() {
                            @RequiresApi(api = Build.VERSION_CODES.N)
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.hasChild("0")) {
                                    String raw = dataSnapshot.child("0").getValue().toString();
                                    String url = "https://firebasestorage.googleapis.com/v0/b/renteasy-f31ba.appspot.com/o/"+raw;
                                    if (!raw.contains(Cloudinar)) {
                                        StorageReference imageRef = storage.getReferenceFromUrl(url);
                                        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(@NonNull Void aVoid) {
                                                Toast.makeText(context, "image1 deleted", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                }
                                if (dataSnapshot.hasChild("1")) {
                                    String raw = dataSnapshot.child("1").getValue().toString();
                                    String url = "https://firebasestorage.googleapis.com/v0/b/renteasy-f31ba.appspot.com/o/"+raw;
                                    if (!raw.contains(Cloudinar)) {
                                        StorageReference imageRef = storage.getReferenceFromUrl(url);
                                        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(@NonNull Void aVoid) {
                                                Toast.makeText(context, "image2 deleted", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                }
                                if (dataSnapshot.hasChild("2")) {
                                    String raw = dataSnapshot.child("2").getValue().toString();
                                    String url = "https://firebasestorage.googleapis.com/v0/b/renteasy-f31ba.appspot.com/o/"+raw;
                                    if (!raw.contains(Cloudinar)) {
                                        StorageReference imageRef = storage.getReferenceFromUrl(url);
                                        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(@NonNull Void aVoid) {
                                                Toast.makeText(context, "image3 deleted", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                }
                                if (dataSnapshot.hasChild("3")) {
                                    String raw = dataSnapshot.child("3").getValue().toString();
                                    String url = "https://firebasestorage.googleapis.com/v0/b/renteasy-f31ba.appspot.com/o/"+raw;
                                    if (!raw.contains(Cloudinar)) {
                                        StorageReference imageRef = storage.getReferenceFromUrl(url);
                                        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(@NonNull Void aVoid) {
                                                Toast.makeText(context, "image4 deleted", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                }
                                if (dataSnapshot.hasChild("4")) {
                                    String raw = dataSnapshot.child("4").getValue().toString();
                                    String url = "https://firebasestorage.googleapis.com/v0/b/renteasy-f31ba.appspot.com/o/"+raw;
                                    if (!raw.contains(Cloudinar)) {
                                        StorageReference imageRef = storage.getReferenceFromUrl(url);
                                        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(@NonNull Void aVoid) {
                                                Toast.makeText(context, "image5 deleted", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                }
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                String key = ds.getKey().toString();
                                boolean isNumeric = key.chars().allMatch(Character::isDigit);
                                if (isNumeric==true) {
                                    deleteListings.child(key).removeValue();
                                }
                            }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        deleteListings.updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                            @Override
                            public void onSuccess(@NonNull Object o) {
                                list.remove(position);
                                notifyItemRemoved(position);
                                Toast.makeText(context,"Deleted Successfully.",Toast.LENGTH_LONG).show();
                            }
                        });

                    })
                    .setNegativeButton("No", (dialog, id) -> {
                        dialog.cancel();
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }
}
