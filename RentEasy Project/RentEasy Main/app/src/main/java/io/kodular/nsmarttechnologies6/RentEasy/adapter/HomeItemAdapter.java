package io.kodular.nsmarttechnologies6.RentEasy.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
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
import io.kodular.nsmarttechnologies6.RentEasy.Utility.HomeItems;
import io.kodular.nsmarttechnologies6.RentEasy.activity.DetailActivity;

import static android.content.ContentValues.TAG;


public class HomeItemAdapter extends RecyclerView.Adapter<HomeItemAdapter.ViewHolder> {
    private static Activity context;
    private List<HomeItems> list;
    boolean once=true;

    public HomeItemAdapter(List<HomeItems> list, FragmentActivity context) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_recommended, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        String Rate=list.get(position).getRate();
        String title=list.get(position).getTitle();
        String Image=list.get(position).getImage();
        String pincode=list.get(position).getPincode();
        String id=list.get(position).getid();
        String area = list.get(position).getArea();
        String city = list.get(position).getCity();

        viewHolder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("listingId", id);
            intent.putExtra("pincode", pincode);
            context.startActivity(intent);
        });

        viewHolder.setData(Rate,title,Image,area,city,pincode,id);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivImage,ivLike;
        io.RentEasy.views.RegularTextView tv_description,tv_area;
        io.RentEasy.views.MediumTextView tv_rate;
        boolean isLiked=false;
        RelativeLayout verfiedLay;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.iv_image);
            tv_rate = itemView.findViewById(R.id.tv_rate);
            tv_description = itemView.findViewById(R.id.tv_description);
            tv_area =itemView.findViewById(R.id.tv_location);
            verfiedLay=itemView.findViewById(R.id.verfied_layout);
            ivLike=itemView.findViewById(R.id.iv_like);
            ivLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isLiked ==false) {
                        ivLike.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF0000")));
                        ivLike.setBackgroundResource(R.drawable.ic_fill_heart);
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
        public void setData(String rate, String description, String Image,String area,String city,String pin,String id) {
            tv_rate.setText(rate);
            tv_description.setText(description);

            FirebaseFirestore db = FirebaseFirestore.getInstance();

            DocumentReference docRef = db.collection(city).document(area);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            tv_area.setText(document.get("n").toString());
                        } else {
                            Toast.makeText(context, "failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });

            
            DatabaseReference getData = FirebaseDatabase.getInstance().getReference().child("listings data").child(pin).child(id);
            getData.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.hasChild("24")) {
                        verfiedLay.setVisibility(View.VISIBLE);
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            String noImageUrl = "https://res.cloudinary.com/nsmarttechnologies/image/upload/v1642161658/no_image_pokwhn.png";
            if ( Image.equals("No")) {
                Glide
                        .with(context)
                        .load(noImageUrl)
                        .centerCrop()
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
        }
    }
}
