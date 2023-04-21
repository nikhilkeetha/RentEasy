package com.renteasy.associates.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.renteasy.associates.R;
import com.renteasy.associates.Utility.Promote;
import com.renteasy.associates.views.MediumTextView;

import java.util.List;



public class PromoteAdapter extends RecyclerView.Adapter<PromoteAdapter.ViewHolder> {
    Activity context;
    private List<Promote> list;
    DatabaseReference deleteApplication;

    public PromoteAdapter(List<Promote> list, Activity context) {
        this.context = context;
        this.list = list;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_promote, parent, false);
        return new PromoteAdapter.ViewHolder(view);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        String Name =list.get(position).getName();
        String PhoneNo = list.get(position).getPhoneNo();
        String pincode = list.get(position).getPincode();
        String id = list.get(position).getId();
        String date = list.get(position).getDate();
        String key = list.get(position).getKey();

        viewHolder.setData(Name,PhoneNo,pincode,id,date,key,position);


    }

    @Override
    public int getItemCount() {
        return  list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivDelete;
        MediumTextView tvName,tvLocation;
        TextView tvDate;
        Button call,view,whatspp;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName=itemView.findViewById(R.id.tv_requiremtns);
            tvLocation=itemView.findViewById(R.id.tv_location);
            tvDate=itemView.findViewById(R.id.tvDate);
            call=itemView.findViewById(R.id.btn_call);
            view=itemView.findViewById(R.id.btn_open);
            ivDelete=itemView.findViewById(R.id.ivDelete);
            whatspp=itemView.findViewById(R.id.btn_whatsapp);
        }

        public void setData(String name, String number, String pincode, String id,String date,String Key,int position) {
            tvName.setText("Owner Name : "+name);
            tvLocation.setText("Location : "+pincode);
            tvDate.setText(date);

            call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:"+number));
                    context.startActivity(intent);
                }
            });

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri uri= Uri.parse("https://www.renteasyindia.com/listing/"+pincode+"/"+id);
                    context.startActivity(new Intent(Intent.ACTION_VIEW,uri));
                }
            });

            whatspp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri uri= Uri.parse("https://api.whatsapp.com/send?phone="+ "+91" + number + "&text=  ");
                    context.startActivity(new Intent(Intent.ACTION_VIEW,uri));
                }
            });

            ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Are you sure you want to Delete?")
                            .setCancelable(false)
                            .setPositiveButton("Delete", (dialog, id) -> {
                              deleteApplication = FirebaseDatabase.getInstance().getReference().child("promoted");
                              deleteApplication.child(number).child(Key).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
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
