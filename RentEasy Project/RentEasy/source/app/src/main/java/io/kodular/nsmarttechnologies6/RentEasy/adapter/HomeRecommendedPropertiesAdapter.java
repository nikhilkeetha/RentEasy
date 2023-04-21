package io.kodular.nsmarttechnologies6.RentEasy.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import io.kodular.nsmarttechnologies6.RentEasy.R;
import io.kodular.nsmarttechnologies6.RentEasy.Utility.HomeProperty;
import io.kodular.nsmarttechnologies6.RentEasy.activity.RecommendedPropertyDetailActivity;
import io.kodular.nsmarttechnologies6.RentEasy.support.Utils;


public class HomeRecommendedPropertiesAdapter extends RecyclerView.Adapter<HomeRecommendedPropertiesAdapter.ViewHolder> {
    private static Activity context;
    private List<HomeProperty> list;

    public HomeRecommendedPropertiesAdapter(List<HomeProperty> list, FragmentActivity context) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_recommended_properties, parent, false);
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
            Intent intent = new Intent(context, RecommendedPropertyDetailActivity.class);
            intent.putExtra("propertyId", id);
            intent.putExtra("pincode", pincode);
            intent.putExtra("H_or_S", HorS);
            context.startActivity(intent);
        });

        viewHolder.setData(Rate,description,Image);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivImage;
        io.RentEasy.views.RegularTextView tv_rate, tv_description;


        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.iv_image);
            tv_rate = itemView.findViewById(R.id.tv_rate);
            tv_description = itemView.findViewById(R.id.tv_description);
        }
        public void setData(String rate, String description, String Image) {
            tv_rate.setText(rate);
            tv_description.setText(description);
            String noImageUrl = "https://res.cloudinary.com/nsmarttechnologies/image/upload/v1607406483/S_7_omb4tr.jpg";
            if ( Image.equals("No")) {
                Glide
                        .with(context)
                        .load(noImageUrl)
                        .centerCrop()
                        .into(ivImage);
            }else {
                Glide
                        .with(context)
                        .load(Image)
                        .centerCrop()
                        .into(ivImage);
            }
        }
    }
}
