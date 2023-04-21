package io.kodular.nsmarttechnologies6.RentEasy.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

import io.kodular.nsmarttechnologies6.RentEasy.R;
import io.kodular.nsmarttechnologies6.RentEasy.activity.HomeActivity;
import io.kodular.nsmarttechnologies6.RentEasy.support.Utils;
import io.RentEasy.views.RegularTextView;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class PayRentFragment extends Fragment implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {

    private AdView mAdView;
    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_OWNER = "myOwner";
    private static final String KEY_OWNER_NM = "ownerName";
    private static final String KEY_OWNER_UPI = "ownerUpi";

    Dialog dialog;

    String OwnerNm = "Srinivas Rao";
    String UPIid = "nikhilkeetha.fam@idfcbank";

    final int UPI_PAYMENT = 0;

    @SuppressLint("StaticFieldLeak")
    private static PayRentFragment fragment;
    private Activity mActivity;
    private View rootView;

    public static PayRentFragment newInstance() {
        fragment = new PayRentFragment();
        return fragment;
    }

    public static PayRentFragment getInstance() {
        if (fragment == null)
            return new PayRentFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_pay_rent, container, false);
        init();
        return rootView;
    }


    @SuppressLint("SetTextI18n")
    private void init() {
        mActivity = getActivity();
        setClickListener();
        dialog =new Dialog(getActivity());
        mAdView = rootView.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        sharedPreferences = getActivity().getSharedPreferences(SHARED_PREF_OWNER,MODE_PRIVATE);
        String ownerNm = sharedPreferences.getString(KEY_OWNER_NM,null);
        String ownerUpi = sharedPreferences.getString(KEY_OWNER_UPI,null);
        TextView indicator = rootView.findViewById(R.id.tv_owner_nm_indicator);
        TextView indicator2 = rootView.findViewById(R.id.tv_ownerUpi_indicator);
        if (ownerNm !=null) {
            OwnerNm = ownerNm;
            UPIid = ownerUpi;
            indicator.setText("Paying to " + ownerNm);
            indicator2.setText("Owner UPI Id : " + ownerUpi);
        }
    }

    private void setClickListener() {
        RelativeLayout call_lay = rootView.findViewById(R.id.call_lay);
        call_lay.setOnClickListener(this);
        RelativeLayout backLay = rootView.findViewById(R.id.back_lay);
        backLay.setOnClickListener(this);
        ImageView iv_menu = rootView.findViewById(R.id.iv_menu);
        iv_menu.setOnClickListener(this);
        io.RentEasy.views.MediumTextView tvPayRent = rootView.findViewById(R.id.tv_pay_rent);
        tvPayRent.setOnClickListener(this);
        io.RentEasy.views.MediumTextView tvSurvey = rootView.findViewById(R.id.tv_survey_pay);
        tvSurvey.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_lay:
                mActivity.onBackPressed();
                break;
            case R.id.iv_menu:
                showPopupMenu(v);
                break;
            case R.id.tv_pay_rent:
                TextInputEditText etAmount = rootView.findViewById(R.id.et_amount);
                if (etAmount.getText().toString().trim().isEmpty()) {
                    Toast.makeText(getActivity(),"Enter Rent per month", Toast.LENGTH_SHORT).show();
                }else {
                    payUsingUpi();
                }
                break;
            case R.id.tv_survey_pay:
                Uri uri= Uri.parse("https://docs.google.com/forms/d/e/1FAIpQLSew0qfWTqlv4czntKzlYu3mCgI6rBmjWV8OpUZKK5gRjtvUYg/viewform?usp=sf_link");
                startActivity(new Intent(Intent.ACTION_VIEW,uri));
                break;
        }
    }

    private void payUsingUpi() {
        TextInputEditText etAmount = rootView.findViewById(R.id.et_amount);
        String Note = "Rent payment using RentEasy";
        Uri uri = Uri.parse("upi://pay").buildUpon()
                .appendQueryParameter("pa", UPIid)
                .appendQueryParameter("pn", OwnerNm)
                .appendQueryParameter("tn",Note)
                .appendQueryParameter("am", String.valueOf(etAmount))
                .appendQueryParameter("cu","INR")
                .build();

        Intent upiPayIntent = new Intent(Intent.ACTION_VIEW);
        upiPayIntent.setData(uri);

        Intent chooser = Intent.createChooser(upiPayIntent,"Pay using");

        if (null != chooser.resolveActivity(getActivity().getPackageManager())) {
            startActivityForResult(chooser, UPI_PAYMENT);
        }else  {
            Toast.makeText(getActivity(),"No UPI apps found",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case UPI_PAYMENT:
                if ((RESULT_OK == resultCode) || (resultCode == 11)) {
                    if (data !=null) {
                        String trxt = data.getStringExtra("response");
                        Log.d("UPI","onActivityResult: " + trxt);
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add(trxt);
                        upiPaymentDataOperation(dataList);
                    }else {
                        Log.d("UPI","onActivityResult: " + "Result data is null");
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add("nothing");
                        upiPaymentDataOperation(dataList);
                    }
                }else {
                    Log.d("UPI","onActivityResult: " + "Return dta is null");
                    ArrayList<String> dataList = new ArrayList<>();
                    dataList.add("nothing");
                    upiPaymentDataOperation(dataList);
                }
                break;
        }
    }

    private void upiPaymentDataOperation(ArrayList<String> dataList) {
        String str = dataList.get(0);
        Log.d("UPIPAY","upiPaymentDataOperation: "+str);
        String paymentCancel = "";
        if (str == null) str ="discard";
        String status = "";
        String approvalRefNo = "";
        String response[] = str.split("&");
        for (int i = 0;i < response.length; i++) {
            String equalStr[] = response[i].split("=");
            if(equalStr.length>=2) {
                if (equalStr[0].toLowerCase().equals("Status".toLowerCase())) {
                    status = equalStr[1].toLowerCase();
                }
                else if (equalStr[0].toLowerCase().equals("ApprovalRefNo".toLowerCase())  || equalStr[0].toLowerCase().equals("txnRef".toLowerCase())) {
                    approvalRefNo = equalStr[1];
                }
            }
            else {
                paymentCancel = "Payment cancelled by user.";
            }
        }
        if (status.equals("success")) {
            openPaySuccessDialog();
        }
        else if ("Payment cancelled by user.".equals(paymentCancel)) {
            Toast.makeText(getActivity(),"Payment Cancelled.",Toast.LENGTH_SHORT).show();
        }
        else {
            openPayFailedDialog();
        }
    }

    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
        popupMenu.inflate(R.menu.popup_menu);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_popup_edit:
                openEditDialog();
                return true;

            default:
                return false;
        }
    }

    private void openEditDialog() {
        dialog.setContentView(R.layout.owner_details_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                Intent intent = new Intent(mActivity,HomeActivity.class);
                startActivity(intent);
            }
        });

        AppCompatImageView imageViewClose = dialog.findViewById(R.id.pay_close);
        imageViewClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        RegularTextView tvSave = dialog.findViewById(R.id.tv_save);
        TextInputEditText etOwnerNm = dialog.findViewById(R.id.et_OwnerNm);
        TextInputEditText etUpi = dialog.findViewById(R.id.et_upiId);
        String ownerNm = sharedPreferences.getString(KEY_OWNER_NM,null);
        if (ownerNm !=null) {
            etOwnerNm.setText(OwnerNm);
            etUpi.setText(UPIid);

        }else {
            imageViewClose.setVisibility(View.INVISIBLE);
        }
        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etOwnerNm.getText().toString().trim().isEmpty()) {
                    Toast.makeText(getActivity(),"Please enter owner name",Toast.LENGTH_SHORT).show();
                }else if (etUpi.getText().toString().trim().isEmpty()) {
                    Toast.makeText(getActivity(), "Please enter UPI ID", Toast.LENGTH_SHORT).show();
                } else {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(KEY_OWNER_NM,etOwnerNm.getText().toString().trim());
                    editor.putString(KEY_OWNER_UPI,etUpi.getText().toString().trim());
                    editor.apply();
                    Toast.makeText(getActivity(), "Owner details saved successful", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    init();
                }
            }
        });
        dialog.show();
    }

    private void openPaySuccessDialog() {
        dialog.setContentView(R.layout.payment_success_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);

        AppCompatTextView tvOkay = dialog.findViewById(R.id.done);

        tvOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.startActivity(mActivity, HomeActivity.class);
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    private void openPayFailedDialog() {
        dialog.setContentView(R.layout.payment_failed_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        AppCompatTextView tvRetry = dialog.findViewById(R.id.retry);
        tvRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}