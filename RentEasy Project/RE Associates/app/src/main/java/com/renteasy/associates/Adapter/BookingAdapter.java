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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.renteasy.associates.R;
import com.renteasy.associates.Utility.Booking;
import com.renteasy.associates.views.MediumTextView;

import java.util.List;



public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.ViewHolder> {
    Activity context;
    private List<Booking> list;
    DatabaseReference getOwnerNumber;
    String collection;

    public BookingAdapter(List<Booking> list, Activity context) {
        this.context = context;
        this.list = list;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_booking, parent, false);
        return new BookingAdapter.ViewHolder(view);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        String number = list.get(position).getNumber();
        String pincode = list.get(position).getPin();
        String id = list.get(position).getId();
        String date = list.get(position).getDate();
        String requirement = list.get(position).getRequirement();
        String key = list.get(position).getKey();
        boolean isCall = list.get(position).getIsCall();

        viewHolder.setData(number,pincode,id,date,requirement,position,key,isCall);
    }

    @Override
    public int getItemCount() {
        return  list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivDelete;
        MediumTextView tvRequirements;
        TextView tvDate;
        Button call,view, callRenter;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRequirements =itemView.findViewById(R.id.tv_requiremtns);
            tvDate=itemView.findViewById(R.id.tvDate);
            call=itemView.findViewById(R.id.btn_call);
            view=itemView.findViewById(R.id.btn_open);
            ivDelete=itemView.findViewById(R.id.ivDelete);
            callRenter =itemView.findViewById(R.id.btn_call_renter);
        }

        public void setData(String number,String pincode, String id,String date,String requirement,int position,String key,boolean isCall) {
            if (!requirement.equals("null")) {
                tvRequirements.setText(requirement);
                tvRequirements.setVisibility(View.VISIBLE);
            }
            tvDate.setText(date);

            call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getOwnerNumber= FirebaseDatabase.getInstance().getReference().child("listings data").child(pincode).child(id).child("17");
                    getOwnerNumber.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String Number = snapshot.getValue().toString();
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setMessage("Choose type")
                                    .setCancelable(false)
                                    .setPositiveButton("Call", (dialog, id) -> {
                                        Intent intent = new Intent(Intent.ACTION_DIAL);
                                        intent.setData(Uri.parse("tel:"+Number));
                                        context.startActivity(intent);
                                    })
                                    .setNegativeButton("Whatsapp", (dialog, id) -> {
                                        Uri uri= Uri.parse("https://api.whatsapp.com/send?phone="+ "+91" + Number + "&text=  ");
                                        context.startActivity(new Intent(Intent.ACTION_VIEW,uri));
                                    });
                            AlertDialog alert = builder.create();
                            alert.show();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            });

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri uri= Uri.parse("https://www.renteasyindia.com/listing/"+pincode+"/"+id);
                    context.startActivity(new Intent(Intent.ACTION_VIEW,uri));
                }
            });

            callRenter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Choose type")
                            .setCancelable(false)
                            .setPositiveButton("Call", (dialog, id) -> {
                                Intent intent = new Intent(Intent.ACTION_DIAL);
                                intent.setData(Uri.parse("tel:"+number));
                                context.startActivity(intent);
                            })
                            .setNegativeButton("Whatsapp", (dialog, id) -> {
                                Uri uri= Uri.parse("https://api.whatsapp.com/send?phone="+ "+91" + number + "&text=  ");
                                context.startActivity(new Intent(Intent.ACTION_VIEW,uri));
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            });

            ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Are you sure you want to Delete?")
                            .setCancelable(false)
                            .setPositiveButton("Delete", (dialog, id) -> {

                                if (isCall) {
                                    collection="requests data";
                                }else {
                                    collection="booking applications";
                                }

                                //add deleted item in firebase firestore
                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                db.collection(collection).document(key).delete()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                list.remove(position);
                                                notifyItemRemoved(position); // will remove item from list
                                                Toast.makeText(context, "Successfully Deleted", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(context, "Failed Deleted", Toast.LENGTH_SHORT).show();
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
