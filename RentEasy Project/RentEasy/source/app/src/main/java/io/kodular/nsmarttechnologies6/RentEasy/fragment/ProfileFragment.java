package io.kodular.nsmarttechnologies6.RentEasy.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.auth.FirebaseAuth;
import com.razorpay.Checkout;

import org.json.JSONException;
import org.json.JSONObject;

import io.kodular.nsmarttechnologies6.RentEasy.R;

import io.kodular.nsmarttechnologies6.RentEasy.activity.LoginActivity;
import io.kodular.nsmarttechnologies6.RentEasy.activity.MyPropertyActivity;
import io.kodular.nsmarttechnologies6.RentEasy.support.Utils;
import io.RentEasy.views.RegularTextView;

import static android.content.Context.MODE_PRIVATE;

public class ProfileFragment extends Fragment implements View.OnClickListener {

    private AdView mAdView;
    SharedPreferences sharedPreferences; // to get user name and phone number
    private static final String SHARED_PREF_NM = "myPref";
    private static final String KEY_NM = "name";
    private static final String KEY_PHONENO = "number";
    SharedPreferences sharedPreferences2; // to get is user a pro user
    private static final String SHARED_PREF_PAID = "myPaid";
    private static final String KEY_IS_PAID = "isPaid";
    @SuppressLint("StaticFieldLeak")
    private static ProfileFragment fragment;
    private Activity mActivity;
    private View rootView;


    public static ProfileFragment newInstance() {
        fragment = new ProfileFragment();
        return fragment;
    }

    public static ProfileFragment getInstance() {
        if (fragment == null)
            return new ProfileFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        init();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        requireView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                mActivity.onBackPressed();
                return true;
            }
            return false;
        });
    }

    @SuppressLint("SetTextI18n")
    private void init() {
        mActivity = getActivity();
        setClickListener();
        mAdView = rootView.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        sharedPreferences = getActivity().getSharedPreferences(SHARED_PREF_NM,MODE_PRIVATE);
        String Nm = sharedPreferences.getString(KEY_NM,null);
        String PhnNm = sharedPreferences.getString(KEY_PHONENO,null);
        TextView UserNm = rootView.findViewById(R.id.tv_name);
        TextView UserPhnNm = rootView.findViewById(R.id.tv_phone);
        if (Nm !=null){
            UserNm.setText(Nm);
        }
        if (PhnNm !=null) {
            UserPhnNm.setText("+91 " + PhnNm);
        }
        sharedPreferences2 = getActivity().getSharedPreferences(SHARED_PREF_PAID,MODE_PRIVATE);
        String isPro = sharedPreferences2.getString(KEY_IS_PAID,null);
        RegularTextView goProBtn = rootView.findViewById(R.id.tv_goPro);
        RegularTextView description = rootView.findViewById(R.id.tv_proDescription);
        io.RentEasy.views.MediumTextView pro = rootView.findViewById(R.id.tv_pro);
        ImageView ivPro = rootView.findViewById(R.id.iv_pro);
        LinearLayout indicator = rootView.findViewById(R.id.ads_blocked);

        if (isPro != null) {
            //if user is pro then
            goProBtn.setVisibility(View.GONE);
            description.setText("Your are now a pro user. Find properties for rent ad-free. Note you need to re-buy the pro plan if you uninstall the app.");
            pro.setText("Pro User");
            ivPro.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
            indicator.setVisibility(View.VISIBLE);
            mAdView.setVisibility(View.GONE);
        }
    }

        private void setClickListener() {
        RelativeLayout backLay = rootView.findViewById(R.id.back_lay);

        CardView cvMyProperty = rootView.findViewById(R.id.cv_my_property);
        CardView cvShare = rootView.findViewById(R.id.cv_share);
        CardView cvContactUs = rootView.findViewById(R.id.cv_contact_us);
        CardView cvLogout = rootView.findViewById(R.id.cv_logout);
        CardView cvPrivacy = rootView.findViewById(R.id.cv_privacy);
        CardView cvHelp = rootView.findViewById(R.id.cv_help);
        CardView cvRateUs = rootView.findViewById(R.id.cv_rate_us);
        ImageView ivFacebook = rootView.findViewById(R.id.iv_facebook);
        ImageView ivInstagram = rootView.findViewById(R.id.iv_instagram);
        ImageView ivTwitter = rootView.findViewById(R.id.iv_twitter);
        RegularTextView ProBtn = rootView.findViewById(R.id.tv_goPro);

        backLay.setOnClickListener(this);
        cvMyProperty.setOnClickListener(this);
        cvShare.setOnClickListener(this);
        cvContactUs.setOnClickListener(this);
        cvLogout.setOnClickListener(this);
        cvPrivacy.setOnClickListener(this);
        cvHelp.setOnClickListener(this);
        cvRateUs.setOnClickListener(this);
        ivFacebook.setOnClickListener(this);
        ivInstagram.setOnClickListener(this);
        ivTwitter.setOnClickListener(this);
        ProBtn.setOnClickListener(this);
    }

    private void shareIntent() {
        String appLink = "https://play.google.com/store/apps/details?id=" + mActivity.getPackageName();

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, appLink);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    private void openEmail() {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + "nsmarttechnologies6@gmail.com"));
            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
            intent.putExtra(Intent.EXTRA_TEXT, "Hi,");
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void showAlertDialogLogout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setMessage("Are you sure you want to logout?")
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog, id) -> {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear();
                    editor.commit();
                    FirebaseAuth.getInstance().signOut();
                    mActivity.finishAffinity();
                    Utils.startActivity(mActivity, LoginActivity.class);

                })
                .setNegativeButton("No", (dialog, id) -> dialog.cancel());
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_lay:
                mActivity.onBackPressed();
                break;
            case R.id.cv_my_property:
                Utils.startActivity(mActivity, MyPropertyActivity.class);
                break;
            case R.id.cv_share:
                shareIntent();
                break;
            case R.id.cv_contact_us:
                openEmail();
                break;
            case R.id.cv_logout:
                showAlertDialogLogout();
                break;
            case R.id.iv_facebook:
                 gotoUrl("https://www.facebook.com/renteasyindia");
                break;
            case R.id.iv_instagram:
                gotoUrl("https://www.instagram.com/renteasyindia/");
                break;
            case R.id.iv_twitter:
                gotoUrl("https://twitter.com/renteasyindia");
                break;
            case R.id.cv_privacy:
                gotoUrl("https://www.renteasyindia.com/privacy-terms");
                break;
            case R.id.cv_help:
                gotoUrl("https://www.youtube.com/channel/UCSIeDfzuLDlxGbquvIAK1TA?view_as=subscriber");
                break;
            case R.id.cv_rate_us:
                gotoUrl("https://play.google.com/store/apps/details?id=io.kodular.nsmarttechnologies6.RentEasy");
                break;
            case R.id.tv_goPro:
                String price = "50";
                int amount = Math.round(Float.parseFloat(price)*100);
                Checkout checkout = new Checkout();
                checkout.setKeyID("rzp_live_g7i7onGCVMJrUN");
                JSONObject object = new JSONObject();
                try {
                    object.put("name","RentEasy Pro");
                    object.put("description","Go Ad-Free with just Rs.50.00");
                    object.put("currency","INR");
                    object.put("amount",amount);
                    checkout.open(getActivity(),object);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            default:
        }
    }

    private void gotoUrl(String s) {
        Uri uri= Uri.parse(s);
        startActivity(new Intent(Intent.ACTION_VIEW,uri));
    }
}
