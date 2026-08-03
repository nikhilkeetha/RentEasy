package io.kodular.nsmarttechnologies6.RentEasy.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import io.kodular.nsmarttechnologies6.RentEasy.R;
import io.kodular.nsmarttechnologies6.RentEasy.Utility.NetworkChangeListener;
import io.kodular.nsmarttechnologies6.RentEasy.adapter.NotificationAdapter;
import io.kodular.nsmarttechnologies6.RentEasy.support.Utils;

public class NotificationsActivity extends AppCompatActivity implements View.OnClickListener{

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NOTIFICATIONS = "myNotification";
    private static final String KEY_TOTAL = "total";
    Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        init();
        ImageView noNotification = findViewById(R.id.iv_noNotifications);
        io.RentEasy.views.MediumTextView no = findViewById(R.id.tv_no);
        RecyclerView rv = findViewById(R.id.recycler_view);
        noNotification.setVisibility(View.GONE);
        no.setVisibility(View.GONE);
        sharedPreferences = getSharedPreferences(SHARED_PREF_NOTIFICATIONS,MODE_PRIVATE);
        String Total = sharedPreferences.getString(KEY_TOTAL,null);
        if (Total == null) {
            rv.setVisibility(View.GONE);
            no.setVisibility(View.VISIBLE);
            noNotification.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Utils.hideKeyboard(mActivity);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void init() {
        mActivity = this;

        RelativeLayout backLay = findViewById(R.id.back_lay);
        backLay.setOnClickListener(this);

        getNotification();
    }

    private void getNotification() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity, RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(new NotificationAdapter(mActivity));
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.back_lay)
            onBackPressed();
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