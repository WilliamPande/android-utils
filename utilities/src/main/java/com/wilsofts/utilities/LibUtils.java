package com.wilsofts.utilities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

public class LibUtils {
    private static final String TAG = "LIB UTILS";
    public static boolean SHOW_LOG = true;

    public static String URL_LINK = "";
    public static String AUTHORIZATION_BEARER = "";

    public static void setUrlLink(String link) {
        LibUtils.URL_LINK = link;
    }

    public static boolean noInternetConnection(@NonNull Context context, CoordinatorLayout coordinatorLayout) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = null;
        if (connectivityManager != null) {
            activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        }

        if (activeNetworkInfo == null) {
            LibUtils.showError(coordinatorLayout, "No network connection found");
        }
        return activeNetworkInfo == null;
    }

    public static void showError(CoordinatorLayout coordinatorLayout, String error) {
        Snackbar snackbar = Snackbar.make(coordinatorLayout, error, Snackbar.LENGTH_LONG);

        View view = snackbar.getView();
        view.setBackgroundColor(Color.RED);

        TextView textView = view.findViewById(R.id.snackbar_text);
        textView.setGravity(Gravity.CENTER_HORIZONTAL);
        textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        textView.setTextColor(Color.WHITE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        } else {
            textView.setGravity(Gravity.CENTER_HORIZONTAL);
        }

        snackbar.show();
    }

    public static void logE(String s) {
        if (LibUtils.SHOW_LOG) {
            Log.e(LibUtils.TAG, s);
        }
    }

    public static void logE(Throwable throwable) {
        if (LibUtils.SHOW_LOG) {
            Log.e(LibUtils.TAG, throwable.getMessage(), throwable);
        }
    }

    public static void restart(@NonNull Intent intent, @NonNull Context context) {
        // Closing all the Activities from stack
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // Add new Flag to start new Activity
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // Staring Login Activity
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        context.startActivity(intent);
    }

    public static boolean invalidEmail(@NonNull String email_address) {
        return email_address.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email_address).matches();
    }

    public static boolean invalidPassword(@NonNull String password) {
        String regex = "^[a-zA-Z0-9@\\\\#$%&*()_+\\]\\[';:?.,!^-]{6,}$";
        return !password.matches(regex);
    }

    public static boolean invalidName(@NonNull String name) {
        String expression = "^[a-zA-Z\\s]+";
        return !name.matches(expression);
    }

    public static boolean invalidPhoneNumber(@NonNull String number) {
        //256703683125
        return number.length() < 12 || !android.util.Patterns.PHONE.matcher(number).matches();
    }

    public static boolean invalidDouble(String number) {
        String DOUBLE_PATTERN = "^[0-9]+(\\.)?[0-9]*";
        String INTEGER_PATTERN = "\\d+";

        if (number.isEmpty()) {
            return true;
        }

        if (Pattern.matches(INTEGER_PATTERN, number)) {
            return false;
        } else {
            return !Pattern.matches(DOUBLE_PATTERN, number);
        }
    }

    public static void dialogWindow(Window window) {
        if (window != null) {
            Point point = new Point();
            Display display = window.getWindowManager().getDefaultDisplay();
            display.getSize(point);
            window.setLayout((int) (point.x * 0.9), ViewGroup.LayoutParams.WRAP_CONTENT);
            window.setGravity(Gravity.CENTER);
        }
    }

    public static String getDate(long timeStamp) {
        try {
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat format = new SimpleDateFormat("dd MMMM, yyyy");
            Date netDate = (new Date(timeStamp * 1000));
            return format.format(netDate);
        } catch (Exception ex) {
            return "";
        }
    }

    @NotNull
    public static String shortDateTime(long timestamp) {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat format = new SimpleDateFormat("dd EEE MMM yyyy hh:mm:ss a");
        Date date = new Date(timestamp * 1000);
        return format.format(date);
    }

    @NotNull
    public static String shortDate(long timestamp) {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat format = new SimpleDateFormat("dd EEE MMM yyyy");
        Date date = new Date(timestamp * 1000);
        return format.format(date);
    }

    public static String formatDouble(double number) {
        return NumberFormat.getNumberInstance(Locale.getDefault()).format(number);
    }

    public static String formatNumber(double number) {
        @SuppressLint("DefaultLocale")
        String formatted = String.format("%,.2f", number);
        if (formatted.endsWith(".00"))
            return formatted.substring(0, formatted.length() - 3);
        else if (formatted.contains(".") && formatted.endsWith("0"))
            return formatted.substring(0, formatted.length() - 2);
        return formatted;
    }
}
