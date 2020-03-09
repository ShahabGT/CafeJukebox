package ir.cafejukebox.app.classes;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.inputmethod.InputMethodManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class Utils {

    public static boolean checkInternet(Context context) {
        try {
            ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();
            return activeInfo != null && activeInfo.isConnected();

        } catch (NullPointerException e) {
            return false;
        }

    }

    public static void hideKeyboard(Activity activity) {
        try {
            InputMethodManager inputManager = (InputMethodManager)
                    activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
        }
    }

    public static String moneySeparator(String text) {

        int len = text.length();
        if(len>3) {
            String res="";
            while(len>3) {
                res = ","+text.substring(len-3, len) + res;

                len-=3;
            }

            res = text.substring(0,len)+res;
            return res;
        }else {
            return text;
        }
    }

    public static String convertToTimeFormat(long millisecond){
        Date date = new Date(millisecond);
        DateFormat formatter = new SimpleDateFormat("mm:ss", Locale.ENGLISH);
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        return formatter.format(date);
    }
}
