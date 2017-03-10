package com.example.dsk221.firstapidemo.utility;

import android.net.Uri;
import android.os.Build;
import android.text.Html;
import android.text.Spanned;

import java.util.HashMap;

/**
 * Created by dsk-221 on 28/2/17.
 */

public class Utils {
    public static Spanned convertHtmlInTxt(String body){
        Spanned spanned;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            spanned = Html.fromHtml(body, Html.FROM_HTML_MODE_LEGACY);
        } else {
            spanned = Html.fromHtml(body,null, null);
        }
        return spanned;
    }
}
