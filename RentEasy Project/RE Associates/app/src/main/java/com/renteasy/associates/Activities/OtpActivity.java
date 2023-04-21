package com.renteasy.associates.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.renteasy.associates.R;
import com.renteasy.associates.Utility.NetworkChangeListener;
import com.renteasy.associates.support.Utils;

public class OtpActivity extends AppCompatActivity {

    TextInputEditText textInputEditText;
    CardView cvVerify;
    ProgressBar progressBar;
    String verificationId,PhoneNo;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        verificationId = getIntent().getStringExtra("verificationId");
        PhoneNo = getIntent().getStringExtra("Ph");



        //Initilize components
        textInputEditText = findViewById(R.id.input_otp);
        cvVerify = findViewById(R.id.verify);
        progressBar = findViewById(R.id.progressBar2);

        //on user clicked verify
        cvVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textInputEditText.getText().toString().trim().isEmpty()) {
                    Toast.makeText(OtpActivity.this,"Please enter OTP!",Toast.LENGTH_LONG).show();
                }
                String code = textInputEditText.getText().toString().trim();

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
                                    if (task.isSuccessful()) {
                                        Utils.startActivity(OtpActivity.this, HomeActivity.class);
                                    } else {
                                        Toast.makeText(OtpActivity.this, "Please enter a valid OTP", Toast.LENGTH_SHORT).show();
                                    }
                                    progressBar.setVisibility(View.GONE);
                                    cvVerify.setVisibility(View.VISIBLE);
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