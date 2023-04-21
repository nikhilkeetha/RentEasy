package io.kodular.nsmarttechnologies6.RentEasy.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.io.InputStream;
import java.security.AccessController;
import java.util.List;

import io.kodular.nsmarttechnologies6.RentEasy.R;
import io.kodular.nsmarttechnologies6.RentEasy.Utility.Property;
import io.kodular.nsmarttechnologies6.RentEasy.activity.OtpActivity;
import io.kodular.nsmarttechnologies6.RentEasy.activity.RecommendedPropertyActivity;
import io.kodular.nsmarttechnologies6.RentEasy.activity.RecommendedPropertyDetailActivity;
import io.kodular.nsmarttechnologies6.RentEasy.support.Utils;

import static io.kodular.nsmarttechnologies6.RentEasy.support.Utils.startActivity;


public class RecommendedPropertyAdapter extends RecyclerView.Adapter<RecommendedPropertyAdapter.ViewHolder> {
    Activity context;
    private List<Property> list;
    private View.OnClickListener btnListener;

    public RecommendedPropertyAdapter(List<Property> list, Activity context) {
        this.context = context;
        this.list = list;
        this.btnListener = btnListener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recommended_property, parent, false);
        return new ViewHolder(view, btnListener);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {

        String Rate=list.get(position).getRate();
        String description=list.get(position).getDescription();
        String ownerNm=list.get(position).getOwnerNm();
        String Image=list.get(position).getImage();
        String pincode=list.get(position).getPincode();
        String HorS=list.get(position).getH_or_S();
        String id=list.get(position).getid();

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RecommendedPropertyDetailActivity.class);
                intent.putExtra("propertyId", id);
                intent.putExtra("pincode", pincode);
                intent.putExtra("H_or_S", HorS);
                context.startActivity(intent);
            }
        });

        viewHolder.setData(Rate,description,ownerNm,Image);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivImage;
        io.RentEasy.views.RegularTextView tv_rate, tv_description, tv_ownerNm;
        RelativeLayout callLay;

        ViewHolder(@NonNull View itemView, View.OnClickListener btnListener) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.iv_image);
            tv_rate = itemView.findViewById(R.id.tv_rate);
            tv_description = itemView.findViewById(R.id.tv_description);
            tv_ownerNm = itemView.findViewById(R.id.tv_owner_name);
            callLay = itemView.findViewById(R.id.call_lay);
            callLay.setOnClickListener(btnListener);
        }

        public void setData(String rate, String description, String ownerNm, String Image) {
            tv_rate.setText("₹" + rate + " per Month");
            tv_description.setText(description);
            tv_ownerNm.setText(ownerNm);
            String noImageUrl = "https://res.cloudinary.com/nsmarttechnologies/image/upload/v1607406483/S_7_omb4tr.jpg";
            if ( Image.equals("No")) {
                Glide
                        .with(context)
                        .load(noImageUrl)
                        .into(ivImage);
            }else {
                Glide
                        .with(context)
                        .load(Image)
                        .into(ivImage);
            }
        }
    }
}
