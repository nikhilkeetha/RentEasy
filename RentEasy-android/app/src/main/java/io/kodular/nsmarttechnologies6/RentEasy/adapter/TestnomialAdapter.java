package io.kodular.nsmarttechnologies6.RentEasy.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.List;

import io.kodular.nsmarttechnologies6.RentEasy.R;
import io.kodular.nsmarttechnologies6.RentEasy.Utility.DetailsImages;
import io.kodular.nsmarttechnologies6.RentEasy.Utility.Review;


public class TestnomialAdapter extends SliderViewAdapter<TestnomialAdapter.Holder> {
    private static Activity context;
    private List<Review> list;


    public TestnomialAdapter(List<Review> list, FragmentActivity context) {
        this.context = context;
        this.list = list;
    }


    @Override
    public Holder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_testnomial,parent,false);

        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder viewHolder, int position) {
        String des = list.get(position).getDes();
        String Nm = list.get(position).getNm();
        String city = list.get(position).getCity();
        String url = list.get(position).getUrl();
        viewHolder.setData(des,Nm,city,url);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    public class Holder extends  SliderViewAdapter.ViewHolder{

        ImageView ivImage;
        TextView Des,City,Nm;

        public Holder(View itemView){
            super(itemView);
            ivImage = itemView.findViewById(R.id.iv_image);
            Des = itemView.findViewById(R.id.tv_description);
            City = itemView.findViewById(R.id.tv_city);
            Nm = itemView.findViewById(R.id.tv_name);
        }
        public void setData(String des, String nm, String city,String url) {
            Des.setText(des);
            Nm.setText(nm);
            City.setText(city);
            Glide
                    .with(context)
                    .load(url)
                    .centerCrop()
                    .into(ivImage);
        }

    }
}