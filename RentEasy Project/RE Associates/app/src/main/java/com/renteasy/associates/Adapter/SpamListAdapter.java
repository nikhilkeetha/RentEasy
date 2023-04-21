package com.renteasy.associates.Adapter;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.renteasy.associates.Activities.PromoteActivity;
import com.renteasy.associates.R;
import com.renteasy.associates.Utility.Promote;
import com.renteasy.associates.Utility.Spam;
import com.renteasy.associates.support.Utils;
import com.renteasy.associates.views.MediumTextView;

import java.text.BreakIterator;
import java.util.HashMap;
import java.util.List;



public class SpamListAdapter extends RecyclerView.Adapter<SpamListAdapter.ViewHolder> {
    Activity context;
    private List<Spam> list;
    DatabaseReference deleteReport,makeSpam;

    public SpamListAdapter(List<Spam> list, Activity context) {
        this.context = context;
        this.list = list;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_spam, parent, false);
        return new SpamListAdapter.ViewHolder(view);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        String reason =list.get(position).getReason();
        String pincode = list.get(position).getPincode();
        String id = list.get(position).getId();
        String key = list.get(position).getKey();

        viewHolder.setData(reason,pincode,id,key,position);


    }

    @Override
    public int getItemCount() {
        return  list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivDelete;
        MediumTextView tvReason;
        Button view,spam;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            view=itemView.findViewById(R.id.btn_view);
            ivDelete=itemView.findViewById(R.id.ivDelete);
            spam=itemView.findViewById(R.id.btn_spam);
            tvReason=itemView.findViewById(R.id.tv_reason);
        }

        public void setData(String Reason, String pincode, String id,String Key,int position) {
            tvReason.setText("Reason : "+Reason);


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri uri= Uri.parse("https://www.renteasyindia.com/listing/"+pincode+"/"+id);
                    context.startActivity(new Intent(Intent.ACTION_VIEW,uri));
                }
            });

            spam.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    makeSpam=FirebaseDatabase.getInstance().getReference().child("listings data").child(pincode).child(id);
                    HashMap hashMap = new HashMap();
                    hashMap.put("is deleted","t");
                    makeSpam.updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                        @Override
                        public void onSuccess(Object o) {
                            deleteReport = FirebaseDatabase.getInstance().getReference().child("spam reports");
                            deleteReport.child(Key).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    list.remove(position);
                                    notifyItemRemoved(position);
                                }
                            });
                        }
                    });
                }
            });

            ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Are you sure you want to Delete?")
                            .setCancelable(false)
                            .setPositiveButton("Delete", (dialog, id) -> {
                                deleteReport = FirebaseDatabase.getInstance().getReference().child("spam reports");
                                deleteReport.child(Key).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        list.remove(position);
                                        notifyItemRemoved(position);
                                    }
                                });
                            })
                            .setNegativeButton("No", (dialog, id) -> dialog.cancel());
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            });
        }
    }
}
