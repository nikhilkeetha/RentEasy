package com.renteasy.associates.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.renteasy.associates.R;
import com.renteasy.associates.Utility.NetworkChangeListener;
import com.renteasy.associates.support.Utils;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {

    TextInputEditText etName;
    CardView cvLogin;
    ProgressBar loading;
    DatabaseReference getUser;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NM = "myPref";
    private static final String KEY_NM = "name";
    private static final String KEY_PHONENO = "number";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //assigning components
        etName = findViewById(R.id.input_phn_no);
        cvLogin = findViewById(R.id.login);
        loading = findViewById(R.id.progressBar);
        sharedPreferences = getSharedPreferences(SHARED_PREF_NM,MODE_PRIVATE);

        cvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etName.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(),"Please enter phone number",Toast.LENGTH_SHORT).show();
                }
                if (etName.getText().length() < 10) {
                    Toast.makeText(getApplicationContext(),"Enter a valid phone number",Toast.LENGTH_SHORT).show();
                }
                getUser = FirebaseDatabase.getInstance().getReference().child("associates data");
                getUser.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChild(etName.getText().toString())) {
                            String userNm = snapshot.child(etName.getText().toString()).child("0").getValue().toString();

                            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                                    "+91" + etName.getText().toString().trim(),
                                    60,
                                    TimeUnit.SECONDS,
                                    LoginActivity.this,
                                    new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                                        @Override
                                        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {  // if verified with out OTP then opens home activity & saves preferences
                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                            editor.putString(KEY_NM,userNm);
                                            editor.putString(KEY_PHONENO,etName.getText().toString().trim());
                                            editor.apply();
                                            Utils.startActivity(LoginActivity.this, HomeActivity.class);
                                            loading.setVisibility(View.GONE);
                                            cvLogin.setVisibility(View.VISIBLE);
                                        }

                                        @Override
                                        public void onVerificationFailed(@NonNull FirebaseException e) {
                                            Toast.makeText(LoginActivity.this, "verification failed", Toast.LENGTH_SHORT).show();
                                            loading.setVisibility(View.GONE);
                                            cvLogin.setVisibility(View.VISIBLE);
                                        }

                                        @Override
                                        public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {

                                            Intent intent = new Intent(getApplicationContext(),OtpActivity.class);
                                            intent.putExtra("verificationId",verificationId);
                                            intent.putExtra("Nm",userNm);
                                            intent.putExtra("Ph",etName.getText().toString().trim());
                                            startActivity(intent);
                                            loading.setVisibility(View.GONE);
                                            cvLogin.setVisibility(View.VISIBLE);
                                        }
                                    }
                            );

                        } else {
                            Toast.makeText(getApplicationContext(),"You are not a registered associate. To become associate visit https://renteasyindia.com/forAssociates ",Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
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