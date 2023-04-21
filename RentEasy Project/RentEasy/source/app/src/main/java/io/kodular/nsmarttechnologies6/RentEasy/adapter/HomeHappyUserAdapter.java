package io.kodular.nsmarttechnologies6.RentEasy.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.concurrent.Callable;

import io.kodular.nsmarttechnologies6.RentEasy.R;
import io.kodular.nsmarttechnologies6.RentEasy.Utility.Review;


public class HomeHappyUserAdapter extends RecyclerView.Adapter<HomeHappyUserAdapter.ViewHolder> {
    private Activity context;
    private List<Review> list;
    int[] images;

    public HomeHappyUserAdapter(List<Review> list, FragmentActivity context,int[] images) {
        this.context = context;
        this.list = list;
        this.images = images;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_happy_user, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        String des = list.get(position).getDes();
        String Nm = list.get(position).getNm();
        String city = list.get(position).getCity();
        viewHolder.setData(des,Nm,city);
        viewHolder.ivImage.setImageResource(images[position]);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivImage;
        TextView Des,City,Nm;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.iv_image);
            Des = itemView.findViewById(R.id.tv_description);
            City = itemView.findViewById(R.id.tv_city);
            Nm = itemView.findViewById(R.id.tv_name);
        }

        public void setData(String des, String nm, String city) {
            Des.setText(des);
            Nm.setText(nm);
            City.setText(city);
        }
    }
}
