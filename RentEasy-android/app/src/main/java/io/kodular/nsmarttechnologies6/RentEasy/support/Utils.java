package io.kodular.nsmarttechnologies6.RentEasy.support;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.google.gson.Gson;
import io.kodular.nsmarttechnologies6.RentEasy.R;
import com.loopj.android.http.BuildConfig;


import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by admin on 12-Sep-17.
 */

public class Utils {

    Activity activity;

    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


   /* public static void saveLoginUser(Activity context, LoginResponse loginResponse) {
        Gson gson = new Gson();
        Preference preference = Preference.getInstance(context);
        preference.putString("loginUser", gson.toJson(loginResponse));
    }

    public static LoginResponse getSaveLoginUser(Activity context) {
        Preference preference = Preference.getInstance(context);
        String resposne = preference.getString("loginUser");
        Gson gson = new Gson();
        return (LoginResponse) getObject(resposne, LoginResponse.class);
    }*/


    @SuppressLint("HardwareIds")
    public static String getDeviceId(Context context) {

        return Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }


    public static void openKeyboard(Activity context, EditText editText) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void hideSoftKeyboard(Activity context) {
        InputMethodManager inputManager = (InputMethodManager)
                context.getSystemService(Context.INPUT_METHOD_SERVICE);
        Objects.requireNonNull(inputManager).hideSoftInputFromWindow(Objects.requireNonNull(context.getCurrentFocus()).getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = null;
        if (connectivityManager != null) {
            activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        }
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void startActivity(Activity activity, Class<?> aClass) {
        activity.startActivity(new Intent(activity, aClass));
        activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public static void startActivityFromAdapter(Activity activity) {
        activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public static void startActivityFinish(Activity activity, Class<?> aClass) {
        activity.startActivity(new Intent(activity, aClass));
        activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        activity.finish();
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void showProgressBar(Activity activity) {
        try {
            if (dialog != null && dialog.isShowing()) {
                return;
            }
            dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.uploading_dilalog);
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            //dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
            dialog.setCancelable(false);
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void hideProgressBar() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }


    public static String getCurrentDate() {
        //"2020-02-04T00:00:00"
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static String getCurrentTime() {
        //"2020-02-04T00:00:00"
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("hh:mm");
        Date currentTime = Calendar.getInstance().getTime();
        return dateFormat.format(currentTime);
    }


    private static Dialog dialog, mDialog;

    public static void showToastPopup(Context context, String message) {
        try {
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.loading_dialog);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            dialog.setCanceledOnTouchOutside(false);

            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isValidEmail(String email) {

        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static Object getObject(String name, Class<?> aClass) {
        Gson gson = new Gson();
        return gson.fromJson(name, aClass);
    }


    public static String getUserId(Activity context) {
        Preference preference = Preference.getInstance(context);
        try {
            return preference.getString("user_id");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


    public static int convertDpToPx(int dp, Context context) {
        return Math.round(dp * (context.getResources().getDisplayMetrics().xdpi / DisplayMetrics.DENSITY_DEFAULT));

    }

    public static int convertPxToDp(int px, Context context) {
        return Math.round(px / (Resources.getSystem().getDisplayMetrics().xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }


    public static String imageConvertIntoString(Bitmap image) {
        ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 70, stream1);
        byte[] imageBytes1 = stream1.toByteArray();
        return Base64.encodeToString(imageBytes1, Base64.DEFAULT);

    }


    public static void printLog(String key, String message) {
        if (BuildConfig.DEBUG) {
            Log.e(key, message);
        }
    }

}


