package io.kodular.nsmarttechnologies6.RentEasy.activity;

import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;


import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import io.kodular.nsmarttechnologies6.RentEasy.R;
import io.kodular.nsmarttechnologies6.RentEasy.Utility.NetworkChangeListener;
import io.kodular.nsmarttechnologies6.RentEasy.support.Preference;
import io.kodular.nsmarttechnologies6.RentEasy.support.Utils;

public class OtpActivity extends AppCompatActivity {

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    Activity mActivity;
    private String verificationId,Name;
    String PhoneNo = "8367530592";

    SharedPreferences sharedPreferences;

    private static final String SHARED_PREF_NM = "myPref";
    private static final String KEY_NM = "name";
    private static final String KEY_PHONENO = "number";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        mActivity = this;
        TextInputEditText otp = findViewById(R.id.input_otp);
        TextView indicator = findViewById(R.id.tv_indicator);

        final ProgressBar progressBar = findViewById(R.id.progressBar);
        final CardView cvVerify = findViewById(R.id.verify);

        verificationId = getIntent().getStringExtra("verificationId");
        Name = getIntent().getStringExtra("Nm");
        PhoneNo = getIntent().getStringExtra("Ph");
        indicator.setText("Enter the OTP sent to +91-"+PhoneNo);

        sharedPreferences = getSharedPreferences(SHARED_PREF_NM,MODE_PRIVATE);


        cvVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(otp.getText().toString().trim().isEmpty()) {
                    Toast.makeText(OtpActivity.this, "Please enter OTP", Toast.LENGTH_SHORT).show();
                    return;
                }
                String code = otp.getText().toString().trim();

                if (verificationId != null) {
                    progressBar.setVisibility(View.VISIBLE);
                    cvVerify.setVisibility(View.INVISIBLE);
                    PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(
                            verificationId,
                            code
                    );
                    FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressBar.setVisibility(View.GONE);
                                    cvVerify.setVisibility(View.VISIBLE);
                                    if (task.isSuccessful()) {
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString(KEY_NM,Name);
                                        editor.putString(KEY_PHONENO,PhoneNo);
                                        editor.apply();
                                        Utils.startActivity(mActivity, HomeActivity.class);
                                    } else {
                                        Toast.makeText(OtpActivity.this, "Please enter a valid OTP", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
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