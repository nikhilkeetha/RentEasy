package io.kodular.nsmarttechnologies6.RentEasy.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.libraries.places.api.Places;

import io.kodular.nsmarttechnologies6.RentEasy.R;
import io.kodular.nsmarttechnologies6.RentEasy.adapter.TestingAdapter;
import io.kodular.nsmarttechnologies6.RentEasy.api.MainPojo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    EditText et;
    RecyclerView recyclerView;
    ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et=findViewById(R.id.et1);
        recyclerView=findViewById(R.id.recycler_view);

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl("https://maps.googleapis.com/maps/api/")
                .build();
        apiInterface=retrofit.create(ApiInterface.class);


        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                getData(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void getData(String text) {
        apiInterface.getPlace(text,getString(R.string.maps_api_key)).enqueue(new Callback<MainPojo>() {
            @Override
            public void onResponse(Call<MainPojo> call, Response<MainPojo> response) {
                if (response.isSuccessful()) {

                    TestingAdapter testingAdapter = new TestingAdapter(response.body().getPredictions());
                    recyclerView.setAdapter(testingAdapter);


                }else {
                    Toast.makeText(MainActivity.this, "No results found", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<MainPojo> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

}