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
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import io.kodular.nsmarttechnologies6.RentEasy.R;
import io.kodular.nsmarttechnologies6.RentEasy.Utility.DetailsRelated;
import io.kodular.nsmarttechnologies6.RentEasy.activity.DetailActivity;


public class DetailsRelatedAdapter extends RecyclerView.Adapter<DetailsRelatedAdapter.ViewHolder> {
    private static Activity context;
    private List<DetailsRelated> list;

    public DetailsRelatedAdapter(List<DetailsRelated> list, FragmentActivity context) {
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
        String description=list.get(position).getDescription();
        String Image=list.get(position).getImage();
        String pincode=list.get(position).getPincode();
        String id=list.get(position).getid();
        String HorS=list.get(position).getHorS();

        viewHolder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("listingId", id);
            intent.putExtra("pincode", pincode);
            context.startActivity(intent);
        });

        viewHolder.setData(Rate,description,Image,HorS);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivImage,ivLike;
        io.RentEasy.views.RegularTextView tv_description;
        io.RentEasy.views.MediumTextView tv_rate,tvPromoted;
        boolean isLiked=false;
        RelativeLayout locationLay;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.iv_image);
            ivLike = itemView.findViewById(R.id.iv_like);
            tv_rate = itemView.findViewById(R.id.tv_rate);
            tv_description = itemView.findViewById(R.id.tv_description);
            tvPromoted = itemView.findViewById(R.id.tv_promoted);
            locationLay=itemView.findViewById(R.id.location_lay);
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
        public void setData(String rate, String description, String Image,String promoted) {
            tv_rate.setText(rate);
            tv_description.setText(description);
            locationLay.setVisibility(View.GONE);
            if (promoted.equals("true")) {
                tvPromoted.setVisibility(View.VISIBLE);
            }
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
