package io.kodular.nsmarttechnologies6.RentEasy.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import io.kodular.nsmarttechnologies6.RentEasy.R;

import io.kodular.nsmarttechnologies6.RentEasy.Utility.MyProperty;
import io.kodular.nsmarttechnologies6.RentEasy.Utility.Property;
import io.kodular.nsmarttechnologies6.RentEasy.activity.MyPropertyActivity;
import io.kodular.nsmarttechnologies6.RentEasy.support.Utils;

public class MyPropertyAdapter extends RecyclerView.Adapter<MyPropertyAdapter.ViewHolder> {
    private Activity context;
    private List<MyProperty> list;
    DatabaseReference getPropertyDetails;
    DatabaseReference UpdatePropertyDetails;
    String getDetails = "";
    String editPincodeBucket = "";
    String editId = "";
    String Status = "";

    public MyPropertyAdapter(List<MyProperty> list, Activity context) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyPropertyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_property, parent, false);
        return new MyPropertyAdapter.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyPropertyAdapter.ViewHolder viewHolder, final int position) {
        String Rate=list.get(position).getRate();
        String description=list.get(position).getDescription();
        String Image=list.get(position).getImage();
        String pincodeBucket=list.get(position).getPincodeBucket();
        String id=list.get(position).getid();
        editId = id;
        editPincodeBucket = pincodeBucket;

        viewHolder.setData(Rate,description,Image,pincodeBucket,id);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {

        ImageView ivImage,ivMenu;
        RelativeLayout editLay;
        io.RentEasy.views.RegularTextView tvRate,tvDescription;
        private Dialog dialog;


        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.iv_image);
            ivMenu = itemView.findViewById(R.id.iv_menu);
            editLay = itemView.findViewById(R.id.edit_lay);
            tvRate = itemView.findViewById(R.id.tv_rate);
            tvDescription = itemView.findViewById(R.id.tv_description);
            ivMenu.setOnClickListener(this);
        }

        public void setData(String rate, String description, String Image, String pincodeBucket, String id) {
            tvRate.setText("₹" + rate + " per Month");
            tvDescription.setText(description);
            String noImageUrl = "https://res.cloudinary.com/nsmarttechnologies/image/upload/v1607406483/S_7_omb4tr.jpg";
            if ( Image.equals("No")) {
                Glide
                        .with(context)
                        .load(noImageUrl)
                        .into(ivImage);
            }else {
                Glide
                        .with(context)
                        .load(Image)
                        .into(ivImage);
            }
            editLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getPropertyDetails = FirebaseDatabase.getInstance().getReference().child(pincodeBucket);
                    getPropertyDetails.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String datalist = dataSnapshot.child(id).getValue().toString();
                            String removeLeftChar = datalist.replace("[","");
                            String removeRightChar = removeLeftChar.replace("]","");
                            String value = removeRightChar.replace("\"","");
                            getDetails = value;
                            String[] split = value.split(",");
                            String Rate = split[15];
                            Status = split[8];
                            TextInputEditText etRate = dialog.findViewById(R.id.et_rate);
                            etRate.setText(Rate);
                            TextView tvStatus = dialog.findViewById(R.id.tv_avialable);
                            if (Status.equals("N")) {
                                tvStatus.setText("No");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    showEditDialog(itemView);
                }
            });

        }

        @Override
        public void onClick(View v) {
            showPopupMenu(v);
        }

        private void showPopupMenu(View view) {
            PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
            popupMenu.inflate(R.menu.popup_menu_property);
            popupMenu.setOnMenuItemClickListener(this);
            popupMenu.show();
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_popup_edit_property:
                    showEditDialog(itemView);
                    return true;

                case R.id.action_popup_promote:

                    return true;

                case R.id.action_popup_delete:
                    showAlertDialogLogout(itemView);
                    return true;

                default:
                    return false;
            }
        }
        private void showEditDialog(View view) {
            dialog =new Dialog(view.getContext());
            dialog.setContentView(R.layout.edit_property_dialog);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCancelable(false);
            TextInputEditText etRate = dialog.findViewById(R.id.et_rate);
            ProgressBar progressBar = dialog.findViewById(R.id.loading);
            AppCompatImageView imageViewClose = dialog.findViewById(R.id.close_edit);
            imageViewClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            AppCompatTextView tvStatus = dialog.findViewById(R.id.tv_avialable);
            tvStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Status.equals("N")) {
                        tvStatus.setText("Yes");
                        Status = "A";
                    }else {
                        tvStatus.setText("No");
                        Status = "N";
                    }
                }
            });
            AppCompatTextView tvSave = dialog.findViewById(R.id.tv_save);
            tvSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tvSave.setVisibility(View.INVISIBLE);
                    progressBar.setVisibility(View.VISIBLE);
                    UpdatePropertyDetails = FirebaseDatabase.getInstance().getReference().child(editPincodeBucket);
                    String[] split = getDetails.split(",");
                    String I1Url = split[0];
                    String I2Url = split[1];
                    String I3Url = split[2];
                    String I4Url = split[3];
                    String I5Url = split[4];
                    String propertyType = split[5];
                    String NoRooms = split[6];
                    String facing = split[7];
                    String floor = split[9];
                    String parking = split[10];
                    String address = split[11];
                    String pincode = split[12];
                    String lat = split[13];
                    String lng =split[14];
                    String Nm =split[17];
                    String PhnNo =split[18];
                    String isPromoted =split[19]; //finish it after purchase
                    UpdatePropertyDetails.child(editId).setValue(I1Url + "," + I2Url + "," + I3Url + "," + I4Url + "," + I5Url + "," + propertyType + "," + NoRooms + "," + facing + "," + Status + "," + floor + "," + parking + "," + address + "," + pincode + "," + lat + "," + lng + "," + etRate.getText().toString().trim() + "," + editId + "," + Nm + "," + PhnNo + "," + isPromoted);
                    dialog.dismiss();
                }
            });
            dialog.show();
        }

        public void showAlertDialogLogout(View view) {
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            builder.setMessage("Are you sure you want to Delete Property?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", (dialog, id) -> {

                    })
                    .setNegativeButton("No", (dialog, id) -> dialog.cancel());
            AlertDialog alert = builder.create();
            alert.show();
        }


    }
}
