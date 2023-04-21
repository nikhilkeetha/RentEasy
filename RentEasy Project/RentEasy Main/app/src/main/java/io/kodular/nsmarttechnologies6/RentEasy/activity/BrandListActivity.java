package io.kodular.nsmarttechnologies6.RentEasy.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.core.OrderBy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import io.kodular.nsmarttechnologies6.RentEasy.R;
import io.kodular.nsmarttechnologies6.RentEasy.Utility.Category;
import io.kodular.nsmarttechnologies6.RentEasy.Utility.NetworkChangeListener;

public class BrandListActivity extends AppCompatActivity {

    ListView listView;
    ProgressBar pbLoading;
    ArrayAdapter<String> adapter;
    ArrayList<String> mylist;
    EditText editText;
    String bOrC;
    FirebaseFirestore db;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brand_list);

        listView = findViewById(R.id.listView);
        editText=findViewById(R.id.et_search);
        db=FirebaseFirestore.getInstance();
        pbLoading=findViewById(R.id.loading2);

        bOrC=getIntent().getStringExtra("bOrC");

        // Add items to Array List
        mylist = new ArrayList<>();
        if (bOrC.equals("bike")) {
            editText.setHint("Search bike brand");
        }else {
            editText.setHint("Search car brand");
        }

        CollectionReference collectionReference = db.collection(bOrC);
        collectionReference.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for (DocumentSnapshot q1 : value.getDocuments()) {
                    mylist.add(String.valueOf(q1.get("n")));
                }
                pbLoading.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
                Collections.sort(mylist);
            }
        });

        // Set adapter to ListView
        adapter
                = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                mylist);
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String s = listView.getItemAtPosition(position).toString();
                check(s);
                /*
                String name = String.valueOf(parent.getItemAtPosition(position));
                Intent i = new Intent();
                i.putExtra("brand",name);
                setResult(RESULT_CANCELED,i);
                finish();
                */
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }
    private void check(String text) {
        CollectionReference collectionReference = db.collection(bOrC);
        collectionReference.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for (DocumentSnapshot q1 : value.getDocuments()) {
                    String tes = String.valueOf(q1.get("n"));
                    if (tes.equals(text)) {
                        Intent i = new Intent();
                        i.putExtra("brand",text);
                        i.putExtra("id",q1.getId());
                        setResult(RESULT_CANCELED,i);
                        finish();

                        //i.putExtra("area",q1.getId());
                        //Toast.makeText(PlacesSearcherActivity.this,  q1.getId()+q1.get("pin"), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("You cannot go back with out selecting a brand.")
                .setCancelable(false)
                .setPositiveButton("Okay", (dialog, id) -> dialog.cancel());
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    protected void onStart() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener, filter);
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }

}