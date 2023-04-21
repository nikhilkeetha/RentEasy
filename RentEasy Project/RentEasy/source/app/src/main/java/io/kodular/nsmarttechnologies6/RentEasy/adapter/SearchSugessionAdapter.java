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
import java.util.ArrayList;
import java.util.List;

import io.kodular.nsmarttechnologies6.RentEasy.R;
import io.kodular.nsmarttechnologies6.RentEasy.Utility.HomeProperty;
import io.kodular.nsmarttechnologies6.RentEasy.Utility.Sugession;
import io.kodular.nsmarttechnologies6.RentEasy.activity.RecommendedPropertyDetailActivity;
import io.kodular.nsmarttechnologies6.RentEasy.support.Utils;


public class SearchSugessionAdapter extends RecyclerView.Adapter<SearchSugessionAdapter.ViewHolder> {
    private static Activity context;
    private List<Sugession> list;

    public SearchSugessionAdapter(List<Sugession> list, FragmentActivity context) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sugession, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        String name=list.get(position).getName();
        String zipcode=list.get(position).getZipcode();
        String state=list.get(position).getState();

        viewHolder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, RecommendedPropertyDetailActivity.class);
            intent.putExtra("propertyId", name);
            intent.putExtra("pincode", zipcode);
            intent.putExtra("H_or_S", state);
            context.startActivity(intent);
        });

        viewHolder.setData(name,zipcode,state);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void resultList(ArrayList<Sugession> resultList) {
        list = resultList;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        io.RentEasy.views.MediumTextView tv_name;
        io.RentEasy.views.RegularTextView tv_zipcode, tv_state;


        ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
        public void setData(String nm, String code, String state) {
        }
    }
}
