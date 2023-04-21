package io.kodular.nsmarttechnologies6.RentEasy.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import io.kodular.nsmarttechnologies6.RentEasy.R;
import io.kodular.nsmarttechnologies6.RentEasy.api.Listclass;

public class TestingAdapter extends RecyclerView.Adapter<TestingAdapter.ViewHolder> {

    ArrayList<Listclass> list;

    public TestingAdapter(ArrayList<Listclass> list) {
        this.list = list;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sugession,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tv_des.setText(list.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        io.RentEasy.views.MediumTextView tv_des;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_des=itemView.findViewById(R.id.tv_des);
        }
    }
}
