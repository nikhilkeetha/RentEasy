package io.kodular.nsmarttechnologies6.RentEasy.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.kodular.nsmarttechnologies6.RentEasy.R;
import io.kodular.nsmarttechnologies6.RentEasy.Utility.NetworkChangeListener;

public class PlacesSearcherActivity extends AppCompatActivity {

    FirebaseFirestore db,db2;
    ListView listView;
    EditText etSearch;
    ArrayAdapter<String> adapter;
    ArrayList<String> mylist;
    String cityId,id,isPost;
    ProgressBar pbLoading;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places_searcher);

        //assaining variables
        listView = findViewById(R.id.listView_areas);
        pbLoading=findViewById(R.id.loading2);
        etSearch=findViewById(R.id.et_search_area);
        db=FirebaseFirestore.getInstance();
        db2=FirebaseFirestore.getInstance();
        mylist = new ArrayList<>();
        //getting city intent
        isPost=getIntent().getStringExtra("post");
        if (isPost.equals("false")) {
            id=getIntent().getStringExtra("id");
        }
        cityId=getIntent().getStringExtra("city");
        etSearch.setHint("Search Area in "+cityId);

        CollectionReference collectionReference = db.collection(cityId);
        collectionReference.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for (DocumentSnapshot q1 : value.getDocuments()) {
                    mylist.add(String.valueOf(q1.get("n")));
                }
                pbLoading.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
                Collections.sort(mylist);
                if (isPost.equals("false")) {
                    mylist.add(0,"All in "+cityId);
                }
            }
        });

        // Set adapter to ListView
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mylist);
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String s = listView.getItemAtPosition(position).toString();
                check(s);
            }
        });

        etSearch.addTextChangedListener(new TextWatcher() {
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
        CollectionReference collectionReference = db2.collection(cityId);
        collectionReference.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for (DocumentSnapshot q1 : value.getDocuments()) {
                    String tes = String.valueOf(q1.get("n"));
                    if (tes.equals(text)) {
                        if (isPost.equals("true")) {
                            Intent i = new Intent();
                            i.putExtra("area",text);
                            i.putExtra("areaId",q1.getId());
                            i.putExtra("pincode", String.valueOf(q1.get("p")));
                            setResult(10,i);
                            finish();
                        }else {
                            Intent i = new Intent(PlacesSearcherActivity.this, SearchListActivity.class);
                            i.putExtra("isLiked", "null");
                            i.putExtra("category-id", id);
                            i.putExtra("city", cityId);
                            i.putExtra("area", q1.getId());
                            i.putExtra("pincode", String.valueOf(q1.get("p")));
                            startActivity(i);
                        }
                        // Toast.makeText(PlacesSearcherActivity.this,  q1.getId()+q1.get("p"), Toast.LENGTH_SHORT).show();
                    }else {
                        if (text.equals("All in "+cityId)) {
                            Intent i = new Intent(PlacesSearcherActivity.this, SearchListActivity.class);
                            i.putExtra("isLiked", "null");
                            i.putExtra("category-id", id);
                            i.putExtra("city", cityId);
                            i.putExtra("area", "all");
                            i.putExtra("pincode", String.valueOf(q1.get("p")));
                            startActivity(i);
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent();
        i.putExtra("area","Select area");
        i.putExtra("areaId","0");
        i.putExtra("pincode", "");
        setResult(10,i);
        finish();
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