package com.example.dsk221.firstapidemo.utility;

import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.text.Spanned;
import android.widget.Toast;

import com.example.dsk221.firstapidemo.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utils {
    public static Spanned convertHtmlInTxt(String body) {
        Spanned spanned;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            spanned = Html.fromHtml(body, Html.FROM_HTML_MODE_LEGACY);
        } else {
            spanned = Html.fromHtml(body, null, null);
        }
        return spanned;
    }

    public static String getRepString(int rep) {
        String repString;
        String r = String.valueOf(rep);

        if (rep < 1000) {
            repString = r;
        } else if (rep < 10000) {
            repString = r.charAt(0) + ',' + r.substring(1);
        } else {
            repString = (Math.round((rep / 1000) * 10) / 10) + "k";
        }

        return repString;
    }

    public static String returnFormattedDate(long timeInMilliSec) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT,
                Locale.getDefault());
        String formatedDate = dateFormat.format(new Date(timeInMilliSec));
        return formatedDate;
    }

    public static void showToast(Context context, int stringRes) {

        Toast.makeText(context, stringRes, Toast.LENGTH_SHORT).show();

    }

    public static void showToast(Context context,String stringData){
        Toast.makeText(context,stringData,Toast.LENGTH_SHORT).show();
    }
}
