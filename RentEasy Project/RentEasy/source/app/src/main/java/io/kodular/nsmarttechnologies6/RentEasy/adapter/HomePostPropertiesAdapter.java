package io.kodular.nsmarttechnologies6.RentEasy.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import io.kodular.nsmarttechnologies6.RentEasy.R;


public class HomePostPropertiesAdapter extends RecyclerView.Adapter<HomePostPropertiesAdapter.ViewHolder> {
    private Activity context;
//    private List<Datum> list;

    public HomePostPropertiesAdapter(Activity context/*, List<Datum> list*/) {
        this.context = context;
//        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_post_properties, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {

    }

    @Override
    public int getItemCount() {
        return 3;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivImage;


        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.iv_image);
        }
    }
}
