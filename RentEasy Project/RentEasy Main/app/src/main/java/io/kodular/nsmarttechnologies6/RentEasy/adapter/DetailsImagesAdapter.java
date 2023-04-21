package io.kodular.nsmarttechnologies6.RentEasy.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.List;

import io.kodular.nsmarttechnologies6.RentEasy.R;
import io.kodular.nsmarttechnologies6.RentEasy.Utility.DetailsImages;
import io.kodular.nsmarttechnologies6.RentEasy.Utility.Review;
import io.kodular.nsmarttechnologies6.RentEasy.activity.DetailActivity;
import io.kodular.nsmarttechnologies6.RentEasy.activity.ImageDetailViewerActivity;


public class DetailsImagesAdapter extends SliderViewAdapter<DetailsImagesAdapter.Holder> {
    private Activity context;
    private List<DetailsImages> list;


    public DetailsImagesAdapter(List<DetailsImages> list, Activity context) {
        this.context = context;
        this.list = list;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_details_images_slider,parent,false);

        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder viewHolder, int position) {
        String imageUrl = list.get(position).getImageUrl();
        viewHolder.setData(imageUrl);
        viewHolder.ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!imageUrl.equals("No")) {
                    if (imageUrl.contains("https://res.cloudinary.com/nsmarttechnologies/image/upload/")) {
                        openImage(imageUrl);
                    }else {
                        openImage("https://firebasestorage.googleapis.com/v0/b/renteasy-f31ba.appspot.com/o/" + imageUrl);
                    }
                }else {
                    Toast.makeText(context, "Owner has not uploaded the Image.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public int getCount() {
        return list.size();
    }

    public class Holder extends  SliderViewAdapter.ViewHolder{

        ImageView ivImage;
        String cloudinary = "https://res.cloudinary.com/nsmarttechnologies/image/upload/";
        String noImageUrl = "https://res.cloudinary.com/nsmarttechnologies/image/upload/v1642161658/no_image_pokwhn.png";

        public Holder(View itemView){
            super(itemView);
            ivImage = itemView.findViewById(R.id.iv_image);
        }
        public void setData(String url) {
           // glide to load images
            if (!url.equals("No")) {
                if (url.contains(cloudinary)) {
                    Glide
                            .with(context)
                            .load(url)
                            .into(ivImage);
                }else {
                    Glide
                            .with(context)
                            .load("https://firebasestorage.googleapis.com/v0/b/renteasy-f31ba.appspot.com/o/" + url)
                            .into(ivImage);
                }
            }else {
                Glide
                        .with(context)
                        .load(noImageUrl)
                        .into(ivImage);
            }
        }
    }
    private void openImage(String url) {
        Intent intent = new Intent(context.getApplicationContext(), ImageDetailViewerActivity.class);
        intent.putExtra("image url", url);
        context.startActivity(intent);
    }
}