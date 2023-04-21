package io.kodular.nsmarttechnologies6.RentEasy.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.truecaller.android.sdk.ITrueCallback;
import com.truecaller.android.sdk.TrueError;
import com.truecaller.android.sdk.TrueProfile;
import com.truecaller.android.sdk.TruecallerSDK;
import com.truecaller.android.sdk.TruecallerSdkScope;

import java.util.concurrent.TimeUnit;

import io.kodular.nsmarttechnologies6.RentEasy.R;
import io.kodular.nsmarttechnologies6.RentEasy.Utility.NetworkChangeListener;
import io.kodular.nsmarttechnologies6.RentEasy.support.Utils;

public class LoginActivity extends AppCompatActivity {

    Activity mActivity;

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    SharedPreferences sharedPreferences,sp2;
    DatabaseReference getUserInfo,userInfo;



    private static final String SHARED_PREF_NM = "myPref";
    private static final String KEY_NM = "name";
    private static final String KEY_PHONENO = "number";

    private static final String SHARED_PREF_PAID = "myPaid";
    private static final String KEY_IS_PAID = "isPaid";
    String userNm,userPhn;
    TextInputEditText PhnNo,etName;
    ProgressBar progressBar;
    CardView signIn;
    boolean isPro=false,once=true,isNewUser=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        PhnNo = findViewById(R.id.input_phn_no);
        etName=findViewById(R.id.input_nm);
        progressBar = findViewById(R.id.progressBar);
        signIn = findViewById(R.id.signIn);

        init();

        //True caller SDK
        TruecallerSdkScope truecallerSdkScope = new TruecallerSdkScope.Builder(this,callBack)
                .consentMode(TruecallerSdkScope.CONSENT_MODE_BOTTOMSHEET)
                .buttonColor(Color.RED)
                .buttonTextColor(Color.WHITE)
                .loginTextPrefix(TruecallerSdkScope.LOGIN_TEXT_PREFIX_TO_GET_STARTED)
                .loginTextSuffix(TruecallerSdkScope.LOGIN_TEXT_SUFFIX_PLEASE_VERIFY_MOBILE_NO)
                .ctaTextPrefix(TruecallerSdkScope.CTA_TEXT_PREFIX_USE)
                .buttonShapeOptions(TruecallerSdkScope.BUTTON_SHAPE_ROUNDED)
                .privacyPolicyUrl("https://www.truecaller.com")
                .termsOfServiceUrl("https://www.truecaller.com")
                .footerType(TruecallerSdkScope.FOOTER_TYPE_NONE)
                .consentTitleOption(TruecallerSdkScope.SDK_CONSENT_TITLE_LOG_IN)
                .sdkOptions(TruecallerSdkScope.SDK_OPTION_WITH_OTP)
                .build();
        TruecallerSDK.init(truecallerSdkScope);


        //checking is true caller app installed in device
        if (TruecallerSDK.getInstance().isUsable()) {
            TruecallerSDK.getInstance().getUserProfile(this);
        }

        sharedPreferences = getSharedPreferences(SHARED_PREF_NM,MODE_PRIVATE);
        sp2 = getSharedPreferences(SHARED_PREF_PAID,MODE_PRIVATE);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(PhnNo.getText().toString().isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please enter your phone number", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(PhnNo.getText().toString().length() < 10) {
                    Toast.makeText(LoginActivity.this, "Please enter a valid phone number", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (etName.getText().toString().isEmpty()) {
                    Toast.makeText(mActivity, "Please enter your name", Toast.LENGTH_SHORT).show();
                }
                userNm = etName.getText().toString();
                userPhn = PhnNo.getText().toString();
                progressBar.setVisibility(View.VISIBLE);
                signIn.setVisibility(View.GONE);


                if (isNewUser==true) { //new user save data and send otp
                    sendOTP();
                }else  { //old user save/edit name and send otp
                    userInfo = FirebaseDatabase.getInstance().getReference().child("users data").child(userPhn);
                    userInfo.child("0").setValue(userNm).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            sendOTP();
                        }
                    });
                }

            }
        });

        PhnNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length()==10) {
                    userPhn=s.toString();
                    checkServer("manual");
                }
            }
        });

    }

    ITrueCallback callBack = new ITrueCallback() {
        @Override
        public void onSuccessProfileShared(@NonNull TrueProfile trueProfile) {


            userNm=trueProfile.firstName + " " +trueProfile.lastName;
            String rawNo = trueProfile.phoneNumber;
            userPhn = rawNo.replace("+91","");
            checkServer("truecaller");
        }

        @Override
        public void onFailureProfileShared(@NonNull TrueError trueError) {
            Toast.makeText(mActivity, "Sorry an error ouccerd", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onVerificationRequired(TrueError trueError) {
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        TruecallerSDK.getInstance().onActivityResultObtained(LoginActivity.this,requestCode,resultCode,data);
    }

    private void checkServer(String type) {
        getUserInfo = FirebaseDatabase.getInstance().getReference().child("users data").child(userPhn);
        getUserInfo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (once==true) {
                    once=false;
                    if (dataSnapshot.hasChild("0")) { //if the user is already registered then will send otp.
                        if (type.equals("truecaller")) {
                            loginSuccessful();
                        }else {
                            userNm = dataSnapshot.child("0").getValue().toString();
                            etName.setText(userNm);
                            Toast.makeText(mActivity, "Welcome back " + userNm, Toast.LENGTH_SHORT).show();
                            isNewUser = false;
                        }
                    }
                    else {
                        if (type.equals("truecaller")) {
                            userInfo = FirebaseDatabase.getInstance().getReference().child("users data").child(userPhn);
                            userInfo.child("0").setValue(userNm).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                       loginSuccessful();
                                    }
                                }
                            });
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendOTP() {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + PhnNo.getText().toString().trim(),
                60,
                TimeUnit.SECONDS,
                LoginActivity.this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {  // if verified with out OTP then opens home activity & saves preferences
                        loginSuccessful();
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        progressBar.setVisibility(View.GONE);
                        signIn.setVisibility(View.VISIBLE);
                        Toast.makeText(LoginActivity.this, "verification failed", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        Intent intent = new Intent(getApplicationContext(),OtpActivity.class);
                        intent.putExtra("verificationId",verificationId);
                        intent.putExtra("Nm",userNm);
                        intent.putExtra("Ph", userPhn);
                        if (isNewUser) {
                            intent.putExtra("isNewUser","yes");
                        }else {
                            intent.putExtra("isNewUser","no");
                        }
                        startActivity(intent);
                        progressBar.setVisibility(View.GONE);
                        signIn.setVisibility(View.VISIBLE);
                    }
                }
        );
    }

    private void loginSuccessful() {
        Toast.makeText(mActivity, "Login successful", Toast.LENGTH_LONG).show();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_NM,userNm);
        editor.putString(KEY_PHONENO,userPhn);
        editor.apply();
        Utils.startActivity(mActivity, HomeActivity.class);
        progressBar.setVisibility(View.GONE);
        signIn.setVisibility(View.VISIBLE);
    }

    private void init(){
        mActivity = this;
        TextView tvTermsCondition = findViewById(R.id.tv_terms_condition);
        tvTermsCondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.renteasyindia.com/privacy-terms"));
                startActivity(browserIntent);
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