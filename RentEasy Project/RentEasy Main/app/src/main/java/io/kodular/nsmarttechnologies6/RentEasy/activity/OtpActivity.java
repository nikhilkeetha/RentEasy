package io.kodular.nsmarttechnologies6.RentEasy.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import android.app.Activity;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import io.kodular.nsmarttechnologies6.RentEasy.R;
import io.kodular.nsmarttechnologies6.RentEasy.Utility.NetworkChangeListener;
import io.kodular.nsmarttechnologies6.RentEasy.support.Utils;

public class OtpActivity extends AppCompatActivity {

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    Activity mActivity;
    private String verificationId,Name,O_R,survey;
    String PhoneNo = "8367530592";
    SharedPreferences sharedPreferences,sp2;
    DatabaseReference userInfo;
    String isPro;
    String isNewUser;

    private static final String SHARED_PREF_NM = "myPref";
    private static final String KEY_NM = "name";
    private static final String KEY_PHONENO = "number";
    private static final String SHARED_PREF_PAID = "myPaid";
    private static final String KEY_IS_PAID = "isPaid";

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
        isNewUser=getIntent().getStringExtra("isNewUser");

        indicator.setText("Enter the OTP sent to +91-"+PhoneNo);

        sharedPreferences = getSharedPreferences(SHARED_PREF_NM,MODE_PRIVATE);
        sp2 = getSharedPreferences(SHARED_PREF_PAID,MODE_PRIVATE);


        cvVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(otp.getText().toString().trim().isEmpty()) {
                    Toast.makeText(OtpActivity.this, "Please enter OTP", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (otp.getText().toString().length()!=6) {
                    Toast.makeText(OtpActivity.this, "Please enter a valid OTP.", Toast.LENGTH_SHORT).show();
                    return;
                }
                String code = otp.getText().toString().trim();

                if (verificationId != null) {
                    progressBar.setVisibility(View.VISIBLE);
                    cvVerify.setVisibility(View.GONE);
                    PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(
                            verificationId,
                            code
                    );
                    FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        if (isNewUser.equals("no")) {
                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                            editor.putString(KEY_NM, Name);
                                            editor.putString(KEY_PHONENO, PhoneNo);
                                            editor.apply();
                                            Utils.startActivity(mActivity, HomeActivity.class);
                                            progressBar.setVisibility(View.GONE);
                                            cvVerify.setVisibility(View.VISIBLE);
                                        }else {
                                            saveNewUserSignInData();
                                        }
                                    } else {
                                        progressBar.setVisibility(View.GONE);
                                        cvVerify.setVisibility(View.VISIBLE);
                                        Toast.makeText(OtpActivity.this, "Please enter a valid OTP", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                private void saveNewUserSignInData() {
                                    userInfo = FirebaseDatabase.getInstance().getReference().child("users data").child(PhoneNo);
                                    userInfo.child("0").setValue(Name).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                                editor.putString(KEY_NM, Name);
                                                editor.putString(KEY_PHONENO, PhoneNo);
                                                editor.apply();
                                                Utils.startActivity(mActivity, HomeActivity.class);
                                                progressBar.setVisibility(View.GONE);
                                                cvVerify.setVisibility(View.VISIBLE);
                                            }
                                        }
                                    });
                                }

                            });
                }
            }
        });

        TextView tvResend = findViewById(R.id.tv_resend_otp);
        tvResend.setOnClickListener(new View.OnClickListener() { // on user click to resend otp.
            @Override
            public void onClick(View v) {

            }
        });

        RelativeLayout backLay = findViewById(R.id.back_lay);
        backLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


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
