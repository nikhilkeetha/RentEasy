package io.kodular.nsmarttechnologies6.RentEasy.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.List;

import io.kodular.nsmarttechnologies6.RentEasy.R;
import io.kodular.nsmarttechnologies6.RentEasy.Utility.AutoSliders;


public class HomeAutoSliderAdapter extends SliderViewAdapter<HomeAutoSliderAdapter.Holder> {
    private static Activity context;
    private List<AutoSliders> listAuto;


    public HomeAutoSliderAdapter(List<AutoSliders> listAuto, FragmentActivity context) {
        this.context = context;
        this.listAuto = listAuto;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_home_auto_scroll,parent,false);

        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder viewHolder, int position) {
        Glide
                .with(context)
                .load(listAuto.get(position).getUrl())
                .centerCrop()
                .into(viewHolder.imageView);
    }

    @Override
    public int getCount() { return listAuto.size();
    }

    public class Holder extends  SliderViewAdapter.ViewHolder{

        ImageView imageView;

        public Holder(View itemView){
            super(itemView);

            imageView = itemView.findViewById(R.id.iv_image);

        }
    }
}