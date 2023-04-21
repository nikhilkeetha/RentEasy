package com.renteasy.associates.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.renteasy.associates.Adapter.PromoteAdapter;
import com.renteasy.associates.R;
import com.renteasy.associates.Utility.Promote;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PromoteActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    List<Promote> list;
    PromoteAdapter adapter;
    DatabaseReference getPromotions,getOwnerDetails;
    int var=0;
    int total=0;
    ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promote);
        pb=findViewById(R.id.progressBar);
        recyclerView=findViewById(R.id.recylcer_view);

        list = new ArrayList<>();

        getPromotions = FirebaseDatabase.getInstance().getReference();
        getPromotions.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild("promoted")) {
                    total = (int) snapshot.child("promoted").getChildrenCount();
                    for (DataSnapshot ds : snapshot.child("promoted").getChildren()) {
                        var++;
                        int temp = (int) ds.getChildrenCount();
                        total = total + temp;
                        for (DataSnapshot ds2 : ds.getChildren()) {
                            var++;
                            String key = ds2.getKey();
                            String raw = ds2.getValue().toString();
                            String[] split = raw.split(",");
                            String pincode = split[0];
                            String id = split[1];
                            String date = split[2];
                            getOwnerDetails = FirebaseDatabase.getInstance().getReference().child("listings data").child(pincode).child(id);
                            getOwnerDetails.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (!snapshot.hasChild("is deleted")) {
                                        String name = snapshot.child("16").getValue().toString();
                                        String number = snapshot.child("17").getValue().toString();
                                        list.add(new Promote(name, pincode, id, number, date, key));
                                        if (total == var) {
                                            setDataToAdapter();
                                        }
                                    } else {
                                        if (total == var) {
                                            setDataToAdapter();
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    }
                } else {
                    pb.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
    private void setDataToAdapter() {
        pb.setVisibility(View.GONE);
        recyclerView.setLayoutManager(new LinearLayoutManager(PromoteActivity.this, RecyclerView.VERTICAL, false));
        Collections.reverse(list);
        adapter=new PromoteAdapter(list, PromoteActivity.this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}