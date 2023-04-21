package io.kodular.nsmarttechnologies6.RentEasy.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import io.kodular.nsmarttechnologies6.RentEasy.R;
import io.kodular.nsmarttechnologies6.RentEasy.Utility.SearchList;
import io.kodular.nsmarttechnologies6.RentEasy.activity.DetailActivity;


public class SearchListItemAdapter extends RecyclerView.Adapter<SearchListItemAdapter.ViewHolder> {
    Activity context;
    private List<SearchList> list;
    private View.OnClickListener btnListener;
    String city;

    public SearchListItemAdapter(List<SearchList> list, Activity context) {
        this.context = context;
        this.list = list;
        this.btnListener = btnListener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_list, parent, false);
        return new ViewHolder(view, btnListener);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {

        String Rate=list.get(position).getRate();
        String description=list.get(position).getDescription();
        String Image=list.get(position).getImage();
        String pincode=list.get(position).getPincode();
        String id=list.get(position).getid();
        boolean Verified = list.get(position).getIsVerified();
        boolean isAll = list.get(position).getIsAll();

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("listingId", id);
                intent.putExtra("pincode", pincode);
                context.startActivity(intent);
            }
        });

        viewHolder.shareLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.renteasyindia.com/ad/"+pincode+"/"+id;
                shareIntent(url);
            }
        });

        viewHolder.setData(Rate,description,Image,Verified,pincode,id,isAll);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivImage,ivLike;
        io.RentEasy.views.RegularTextView tv_description,tvLocation;
        io.RentEasy.views.MediumTextView tv_rate;
        RelativeLayout callLay,shareLay,locationLay;
        boolean isLiked=false;
        LinearLayout verfiedLay;

        ViewHolder(@NonNull View itemView, View.OnClickListener btnListener) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.iv_image);
            tv_rate = itemView.findViewById(R.id.tv_rate);
            tv_description = itemView.findViewById(R.id.tv_description);
            tvLocation=itemView.findViewById(R.id.tv_location);
            verfiedLay=itemView.findViewById(R.id.verified_lay);
            shareLay = itemView.findViewById(R.id.shareLay);
            locationLay=itemView.findViewById(R.id.location_lay);
            callLay = itemView.findViewById(R.id.call_lay);
            callLay.setOnClickListener(btnListener);
            ivLike=itemView.findViewById(R.id.iv_like);
            ivLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isLiked ==false) {
                        ivLike.setBackgroundResource(R.drawable.ic_fill_heart);
                        ivLike.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF0000")));
                        isLiked=true;
                        //add to liked list
                    } else if (isLiked == true) {
                        ivLike.setBackgroundResource(R.drawable.ic_empty_heart);
                        ivLike.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4c4b4b")));
                        isLiked=false;
                        //remove from liked list
                    }
                }
            });
        }

        public void setData(String rate, String description, String Image, boolean verified,String pincode,String Id,boolean isAll) {
            tv_rate.setText(rate);
            tv_description.setText(description);
            if (isAll) {

                DatabaseReference getData = FirebaseDatabase.getInstance().getReference().child("listings data").child(pincode).child(Id);
                getData.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String cityId = dataSnapshot.child("20").getValue().toString();
                        String area = dataSnapshot.child("21").getValue().toString();

                        if (cityId.equals("1")) {
                            city="Hyderabad";
                        }else if (cityId.equals("2")){
                            city="Visakhapatnam";
                        }else if (cityId.equals("3")){
                            city="New Delhi";
                        }else if (cityId.equals("4")) {
                            city="Bengaluru";
                        }else if (cityId.equals("5")) {
                            city="Mumbai";
                        }

                        FirebaseFirestore db = FirebaseFirestore.getInstance();

                        DocumentReference docRef = db.collection(city).document(area);
                        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        tvLocation.setText(document.get("n").toString());
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

                locationLay.setVisibility(View.VISIBLE);

            }
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
            if (verified==true) {
               verfiedLay.setVisibility(View.VISIBLE);
            }
        }
    }
    private void shareIntent(String url) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Hey, I've found something for rent. Click the below link to see \n" + url + "\nor if you not yet installed the app click below \nhttps://play.google.com/store/apps/details?id=io.kodular.nsmarttechnologies6.RentEasy");
        sendIntent.setType("text/plain");
        context.startActivity(sendIntent);
    }
}
