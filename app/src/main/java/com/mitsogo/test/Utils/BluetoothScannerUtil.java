package com.mitsogo.test.Utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;


import com.mitsogo.test.R;

import java.util.List;


/**
 * Created by viswajith on 7/6/17.
 */

public class BluetoothScannerUtil {

    private static ProgressDialog progressDialog;

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static boolean isPermissionGranted(Context context, String permission) {
        boolean isGranted = false;
        if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
            isGranted = true;
        }
        return isGranted;
    }

    public static void showSnackbar(View view, Object message, boolean isActionEnabled) {
        String msg = null;
        Context context = view.getContext();
        if (message instanceof Integer) {
            msg = context.getString((int) message);
        } else {
            msg = (String) message;
        }

        final Snackbar snackbar = Snackbar.make(view, msg, Snackbar.LENGTH_SHORT);
        customizeSnackBar(view.getContext(), snackbar);

        if (isActionEnabled) {
            snackbar.setDuration(Snackbar.LENGTH_INDEFINITE);
            snackbar.setActionTextColor(ContextCompat.getColor(view.getContext(), R.color.colorPrimary));
            snackbar.setAction(R.string.ok, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    snackbar.dismiss();
                }
            });
        }
        snackbar.show();
    }

    public static void showSnackbar(View view, Object message) {
        String msg = null;
        Context context = view.getContext();
        if (message instanceof Integer) {
            int resId = (int) message;
            msg = context.getString(resId);
        } else {
            msg = (String) message;
        }

        Snackbar snackbar = Snackbar.make(view, msg, Snackbar.LENGTH_SHORT);
        customizeSnackBar(view.getContext(), snackbar);
        snackbar.show();
    }


    private static void customizeSnackBar(Context context, Snackbar snackbar) {
        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();
        TextView textView = (TextView) layout.findViewById(android.support.design.R.id.snackbar_text);
        if (textView != null) {
            try {
                Typeface typeface = Typeface.createFromAsset(context.getAssets(), "OpenSans-Regular.ttf");
                textView.setTypeface(typeface);
                textView.setTextSize(12);
                textView.setMaxLines(3);
            } catch (Exception e) {
                Log.e("error", "Could not get typeface: " + e.getMessage());
            }
        }
    }





}
