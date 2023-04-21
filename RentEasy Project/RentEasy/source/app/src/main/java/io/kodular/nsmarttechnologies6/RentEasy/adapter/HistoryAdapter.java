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
import io.kodular.nsmarttechnologies6.RentEasy.activity.HistoryActivity;
import io.kodular.nsmarttechnologies6.RentEasy.support.Utils;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    private Activity context;
    //    private List<Datum> list;

    public HistoryAdapter(Activity context/*, List<Datum> list*/) {
        this.context = context;
//        this.list = list;
    }

    @NonNull
    @Override
    public HistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false);
        return new HistoryAdapter.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.ViewHolder viewHolder, final int position) {

        viewHolder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, HistoryActivity.class);
            context.startActivity(intent);
            Utils.startActivityFromAdapter(context);
        });
    }

    @Override
    public int getItemCount() {
        return 15;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivImage;


        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.iv_image);
        }
    }
}
