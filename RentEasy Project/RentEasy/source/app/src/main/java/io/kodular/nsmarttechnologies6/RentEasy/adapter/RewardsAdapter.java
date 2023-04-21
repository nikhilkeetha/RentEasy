package io.kodular.nsmarttechnologies6.RentEasy.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import io.kodular.nsmarttechnologies6.RentEasy.R;
import io.kodular.nsmarttechnologies6.RentEasy.activity.RewardsActivity;
import io.kodular.nsmarttechnologies6.RentEasy.support.Utils;

public class RewardsAdapter extends RecyclerView.Adapter<RewardsAdapter.ViewHolder> {
    private Activity context;
    //    private List<Datum> list;

    public RewardsAdapter(Activity context/*, List<Datum> list*/) {
        this.context = context;
//        this.list = list;
    }

    @NonNull
    @Override
    public RewardsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reward, parent, false);
        return new RewardsAdapter.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RewardsAdapter.ViewHolder viewHolder, final int position) {

    }

    @Override
    public int getItemCount() {
        return 5;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivImage;


        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.iv_image);
        }
    }
}
