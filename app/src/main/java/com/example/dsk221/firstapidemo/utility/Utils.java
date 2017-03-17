package com.example.dsk221.firstapidemo.utility;

import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.text.Spanned;
import android.text.format.DateUtils;
import android.widget.Toast;

import com.example.dsk221.firstapidemo.R;
import com.example.dsk221.firstapidemo.dialogs.FilterDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
            repString = r.charAt(0) + "," + r.substring(1);
        } else {
            repString = (Math.round((rep / 1000f) * 10) / 10f) + "k";
        }

        return repString;
    }
    public static String getScoreString(int rep) {
        String repString;
        String r = String.valueOf(rep);

        if (rep < 1000) {
            repString = r;
        }
        else {
            repString = (Math.round((rep/1000f) * 10) / 10f) + "k";
        }
        return repString;
    }
    public static boolean isYesterday(Calendar calendar) {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.DATE,-1);

        return now.get(Calendar.YEAR) == calendar.get(Calendar.YEAR)
                && now.get(Calendar.MONTH) == calendar.get(Calendar.MONTH)
                && now.get(Calendar.DATE) == calendar.get(Calendar.DATE);
    }
    public static String getDate(long seconds, String dateFormat)
    {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(seconds*1000);

        if(DateUtils.isToday(calendar.getTimeInMillis())){
            return (String) DateUtils.getRelativeTimeSpanString( calendar.getTimeInMillis(),
                    System.currentTimeMillis(),
                    DateUtils.SECOND_IN_MILLIS,
                    DateUtils.FORMAT_ABBREV_RELATIVE);
        }
        else if(isYesterday(calendar)){
            return "yesterday";
        }
        else{
            SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
            return (formatter.format(calendar.getTime()));
        }
    }
    public static String arrayToString(ArrayList<String> listTag){
        StringBuilder sb = new StringBuilder();
        for (int listTagPointer=0;listTagPointer<listTag.size();listTagPointer++){
            if(listTagPointer == (listTag.size()-1)){
                sb.append(listTag.get(listTagPointer));
            }
            else{
                sb.append(listTag.get(listTagPointer));
                sb.append(", ");
            }
        }
        return sb.toString();
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
