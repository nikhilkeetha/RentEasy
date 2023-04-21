package io.kodular.nsmarttechnologies6.RentEasy.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import io.kodular.nsmarttechnologies6.RentEasy.R;
import io.kodular.nsmarttechnologies6.RentEasy.Utility.NetworkChangeListener;

public class CityChooserActivity extends AppCompatActivity {

    RelativeLayout Lay1,Lay2,Lay3,Lay4,Lay5,backLay;
    String isSearch;
    String id;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_chooser);

        if (getIntent().getStringExtra("search").equals("true")) {
            isSearch = getIntent().getStringExtra("search");
            id = getIntent().getStringExtra("id");
        }else {
            isSearch=getIntent().getStringExtra("search");
        }


        //assianing variables
        Lay1=findViewById(R.id.Lay_1);
        Lay2=findViewById(R.id.Lay_2);
        Lay3=findViewById(R.id.Lay_3);
        Lay4=findViewById(R.id.Lay_4);
        Lay5=findViewById(R.id.Lay_5);

        backLay=findViewById(R.id.back_lay);

        backLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Lay1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                citySelected("Hyderabad");
            }
        });

        Lay2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                citySelected("Visakhapatnam");
            }
        });

        Lay3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                citySelected("Delhi");
            }
        });

        Lay4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                citySelected("Bengaluru");
            }
        });

        Lay5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                citySelected("Mumbai");
            }
        });
    }

    private void citySelected(String city) {
        if (isSearch.equals("true")) {
            Intent i = new Intent(CityChooserActivity.this,PlacesSearcherActivity.class);
            i.putExtra("city",city);
            i.putExtra("id",id);
            i.putExtra("post","false");
            startActivity(i);
        }else {
            Intent i = new Intent();
            i.putExtra("city",city);
            setResult(RESULT_FIRST_USER,i);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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