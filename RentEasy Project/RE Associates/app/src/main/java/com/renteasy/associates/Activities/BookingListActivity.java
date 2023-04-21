package com.renteasy.associates.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.renteasy.associates.Adapter.BookingAdapter;
import com.renteasy.associates.Adapter.PromoteAdapter;
import com.renteasy.associates.R;
import com.renteasy.associates.Utility.Booking;
import com.renteasy.associates.views.BoldTextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BookingListActivity extends AppCompatActivity {

    FirebaseFirestore db;
    ProgressBar pb;
    RecyclerView rv;
    List<Booking> list;
    BookingAdapter bookingAdapter;
    String r = "null";
    String bORc;
    boolean isCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_list);

        bORc=getIntent().getStringExtra("bORc");

        BoldTextView title = findViewById(R.id.title);

        if (bORc.equals("b")) {
            title.setText("booking applications");
            isCall=false;
        }else {
            title.setText("requests data");
            isCall=true;
        }

        db = FirebaseFirestore.getInstance();
        pb=findViewById(R.id.progressBar);
        rv=findViewById(R.id.recylcer_view);

        list = new ArrayList<>();

        CollectionReference collectionReference = db.collection(title.getText().toString());
        collectionReference.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for (DocumentSnapshot q1 : value.getDocuments()) {

                    if (q1.contains("r")) {
                        r=String.valueOf(q1.get("r"));
                    }else {
                        r="null";
                    }
                    list.add(new Booking(String.valueOf(q1.get("n")),String.valueOf(q1.get("p")),String.valueOf(q1.get("i")),String.valueOf(q1.get("d")),r,String.valueOf(q1.getId()),isCall));
                }
                pb.setVisibility(View.GONE);
                rv.setVisibility(View.VISIBLE);
                setAdapter();
            }
        });
    }

    private void setAdapter() {
        rv.setLayoutManager(new LinearLayoutManager(BookingListActivity.this, RecyclerView.VERTICAL, false));
        bookingAdapter=new BookingAdapter(list, BookingListActivity.this);
        rv.setAdapter(bookingAdapter);
        bookingAdapter.notifyDataSetChanged();
    }
}