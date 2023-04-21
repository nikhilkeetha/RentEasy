package com.renteasy.associates.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.renteasy.associates.Adapter.PromoteAdapter;
import com.renteasy.associates.Adapter.SpamListAdapter;
import com.renteasy.associates.R;
import com.renteasy.associates.Utility.Spam;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SpamListActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ProgressBar pb;
    List<Spam> list;
    SpamListAdapter adapter;
    int var=0,total=0;
    DatabaseReference getReports;
    String reason="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spam_list);
        recyclerView=findViewById(R.id.recylcer_view);
        pb=findViewById(R.id.progressBar);

        list = new ArrayList<>();

        getReports = FirebaseDatabase.getInstance().getReference();
        getReports.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
            if (snapshot.hasChild("spam reports")) {
                total = (int) snapshot.child("spam reports").getChildrenCount();
                for (DataSnapshot ds : snapshot.child("spam reports").getChildren()) {
                    var++;
                    String raw = ds.getValue().toString();
                    String[] split = raw.split(",");
                    String pincode = split[0];
                    String id = split[1];
                    String reasonCode = split[2];
                    if (reasonCode.equals("1")) {
                        reason="Fake Posting";
                    }else if (reasonCode.equals("2")) {
                        reason="Sexual";
                    }else if (reasonCode.equals("3")) {
                        reason="Misleading";
                    }
                    String key = ds.getKey();
                    list.add(new Spam(reason,pincode,id,key));
                    if (total==var) {
                        setDataToAdapter();
                    }
                }
            }else {
                Toast.makeText(SpamListActivity.this, "No Reports", Toast.LENGTH_LONG).show();
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
        recyclerView.setLayoutManager(new LinearLayoutManager(SpamListActivity.this, RecyclerView.VERTICAL, false));
        Collections.reverse(list);
        adapter=new SpamListAdapter(list, SpamListActivity.this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

}
