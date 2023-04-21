package io.kodular.nsmarttechnologies6.RentEasy.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import io.kodular.nsmarttechnologies6.RentEasy.R;
import io.kodular.nsmarttechnologies6.RentEasy.Utility.Sugession;
import io.kodular.nsmarttechnologies6.RentEasy.activity.RecommendedPropertyActivity;
import io.kodular.nsmarttechnologies6.RentEasy.adapter.SearchSugessionAdapter;


public class SearchFragment extends Fragment implements View.OnClickListener {

    private AdView mAdView;

    String H_or_S = "H";
    String Cb = "n";
    List<Sugession> list;
    SearchSugessionAdapter adapter;
    TextInputEditText pincode;

    @SuppressLint("StaticFieldLeak")
    private static SearchFragment fragment;
    private Activity mActivity;
    private View rootView;

    public static SearchFragment newInstance() {
        fragment = new SearchFragment();
        return fragment;
    }

    public static SearchFragment getInstance() {
        if (fragment == null)
            return new SearchFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_search, container, false);
        init();
        return rootView;
    }

    @SuppressLint("SetTextI18n")
    private void init() {
        mActivity = getActivity();
        mAdView = rootView.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        setUpSearchSugessions();
        pincode = rootView.findViewById(R.id.pincode);
        /*pincode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                ArrayList<Sugession> resultList = new ArrayList<>();
                for (Sugession item :list) {
                    if (item.getName().toLowerCase().contains(s.toString().toLowerCase())) {
                        resultList.add(item);
                    }
                }
                adapter.resultList(resultList);
            }
        });
         */
        setClickListener();
    }

    private void setUpSearchSugessions() {
        list = new ArrayList<>();
        list.add(new Sugession("Huzurnagar","508204","Telanagana"));
        list.add(new Sugession("Test2","508245","Telanagana"));
        list.add(new Sugession("test","508263","Telanagana"));
        RecyclerView recyclerView = rootView.findViewById(R.id.recycler_sugession);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity, RecyclerView.VERTICAL, false));
        adapter=new SearchSugessionAdapter(list, getActivity());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void setClickListener() {
        RelativeLayout backLay = rootView.findViewById(R.id.back_lay);
        backLay.setOnClickListener(this);
        CardView cvHouse = rootView.findViewById(R.id.cv_house);
        cvHouse.setOnClickListener(this);
        CardView cvShop = rootView.findViewById(R.id.cv_shop);
        cvShop.setOnClickListener(this);
        TextView tvShiftProperty = rootView.findViewById(R.id.tv_shift_property);
        tvShiftProperty.setOnClickListener(this);
    }

    public void resetAll(String type) {
        CardView cvHouse = rootView.findViewById(R.id.cv_house);
        CardView cvShop = rootView.findViewById(R.id.cv_shop);

        cvHouse.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
        cvShop.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));

        switch (type) {
            case "House":
                H_or_S = "H";
                cvHouse.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
                break;
            case "Shop":
                H_or_S = "S";
                cvShop.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_lay:
                mActivity.onBackPressed();
                break;
            case R.id.cv_house:
                resetAll("House");
                break;
            case R.id.cv_shop:
                resetAll("Shop");
                break;
            case R.id.tv_shift_property:
                CheckBox cb = rootView.findViewById(R.id.cb);
                if (cb.isChecked()) {
                    Cb = "y";
                }
                if (pincode.getText().toString().trim().isEmpty()) {
                    Toast toast = Toast.makeText(getActivity(), "please enter pincode", Toast.LENGTH_SHORT);
                    toast.show();
                }else if (pincode.getText().toString().trim().length() < 6) {
                    Toast toast = Toast.makeText(getActivity(), "please enter a valid pincode" ,Toast.LENGTH_SHORT);
                    toast.show();
                }else  {
                    Intent intent = new Intent(getActivity(), RecommendedPropertyActivity.class);
                    intent.putExtra("pincode", pincode.getText().toString().trim());
                    intent.putExtra("H_or_S", H_or_S);
                    intent.putExtra("is_Avialable", Cb);
                    getActivity().startActivity(intent);
                }
                break;
        }
    }
}