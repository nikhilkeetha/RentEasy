package io.kodular.nsmarttechnologies6.RentEasy.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.List;

import io.kodular.nsmarttechnologies6.RentEasy.R;


public class HomePropertySliderAdapter extends SliderViewAdapter<HomePropertySliderAdapter.Holder> {
    Context context;//useless
    List<String> list;//useless

    int[] images;


    public HomePropertySliderAdapter(int[] images) {
        this.context = context;//useless
        this.list = list;//useless
        this.images = images;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_home_auto_scroll,parent,false);

        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder viewHolder, int position) {
        viewHolder.imageView.setImageResource(images[position]);
    }

    @Override
    public int getCount() { return images.length;
    }

    public class Holder extends  SliderViewAdapter.ViewHolder{

        ImageView imageView;

        public Holder(View itemView){
            super(itemView);

            imageView = itemView.findViewById(R.id.iv_image);

        }
    }
}