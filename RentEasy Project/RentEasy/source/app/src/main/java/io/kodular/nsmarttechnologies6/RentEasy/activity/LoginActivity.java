package io.kodular.nsmarttechnologies6.RentEasy.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import io.kodular.nsmarttechnologies6.RentEasy.R;
import io.kodular.nsmarttechnologies6.RentEasy.Utility.NetworkChangeListener;
import io.kodular.nsmarttechnologies6.RentEasy.support.Utils;


public class LoginActivity extends AppCompatActivity {

    Activity mActivity;

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    SharedPreferences sharedPreferences;

    private static final String SHARED_PREF_NM = "myPref";
    private static final String KEY_NM = "name";
    private static final String KEY_PHONENO = "number";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        sharedPreferences = getSharedPreferences(SHARED_PREF_NM,MODE_PRIVATE);
        CardView signIn = findViewById(R.id.signIn);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final TextInputEditText textInputEditTextNm = findViewById(R.id.input_nm);
                final TextInputEditText textInputEditTextPhnNo = findViewById(R.id.input_phn_no);
                final ProgressBar progressBar = findViewById(R.id.progressBar);
                final CardView cvSignIn = findViewById(R.id.signIn);
                if(textInputEditTextNm.getText().toString().trim().isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please Enter your Name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(textInputEditTextNm.getText().toString().trim().contains(",")) {
                    Toast.makeText(LoginActivity.this, "No , should be in your name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(textInputEditTextPhnNo.getText().toString().trim().isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please Enter your phone number", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(textInputEditTextPhnNo.getText().toString().trim().length() < 10) {
                    Toast.makeText(LoginActivity.this, "Please Enter a valid phone number", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                cvSignIn.setVisibility(View.INVISIBLE);

                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        "+91" + textInputEditTextPhnNo.getText().toString().trim(),
                        60,
                        TimeUnit.SECONDS,
                        LoginActivity.this,
                        new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {  // if verified with out OTP then opens home activity & saves preferences
                                progressBar.setVisibility(View.GONE);
                                cvSignIn.setVisibility(View.VISIBLE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString(KEY_NM,textInputEditTextNm.getText().toString().trim());
                                editor.putString(KEY_PHONENO,textInputEditTextPhnNo.getText().toString().trim());
                                editor.apply();
                                Utils.startActivity(mActivity, HomeActivity.class);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                progressBar.setVisibility(View.GONE);
                                cvSignIn.setVisibility(View.VISIBLE);
                                Toast.makeText(LoginActivity.this, "verification failed", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                progressBar.setVisibility(View.GONE);
                                cvSignIn.setVisibility(View.VISIBLE);
                                Intent intent = new Intent(getApplicationContext(),OtpActivity.class);
                                intent.putExtra("verificationId",verificationId);
                                intent.putExtra("Nm",textInputEditTextNm.getText().toString().trim());
                                intent.putExtra("Ph",textInputEditTextPhnNo.getText().toString().trim());
                                startActivity(intent);
                            }
                        }
                );
            }
        });
    }

    private void init(){
        mActivity = this;
        spannableText();
    }

    private void spannableText(){  // open privacy policy (text view)
        TextView tvTermsCondition = findViewById(R.id.tv_terms_condition);
        SpannableString span = new SpannableString("By proceeding, you agree to our Terms & Conditions.");
        ClickableSpan clickSpan = new ClickableSpan() {
            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setUnderlineText(true);
            }

            @Override
            public void onClick(View textView) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.renteasyindia.com/privacy-terms"));
                startActivity(browserIntent);
            }
        };

        span.setSpan(clickSpan, 0, span.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvTermsCondition.setText(span);
        tvTermsCondition.setMovementMethod(LinkMovementMethod.getInstance());
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