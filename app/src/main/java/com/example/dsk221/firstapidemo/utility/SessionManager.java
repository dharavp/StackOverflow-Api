package com.example.dsk221.firstapidemo.utility;

import android.content.Context;
import android.content.SharedPreferences;
import com.example.dsk221.firstapidemo.models.SiteItem;
import java.util.HashMap;

/**
 * Created by dsk-221 on 27/3/17.
 */

public  class SessionManager {
    private SharedPreferences sharedpreferences;
    private String MyPREFERENCES = "MyPrefs";
    public static final String KEY_SITE_NAME = "nameKey";
    public static final String KEY_SITE_IMAGE = "imageKey";
    public static final String KEY_SITE_PARAMETER = "parameterKey";
    public static final String KEY_SITE_AUDIENCE = "audienceKey";
    private static SessionManager sessionManager;
    private  SharedPreferences.Editor editor;

    private SessionManager(Context context) {
        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
    }

    public static SessionManager getInstance(Context context) {
        if (null == sessionManager) {
            sessionManager = new SessionManager(context);
        }
        return sessionManager;
    }
    public  void addSiteDetail(SiteItem siteItem) {
        editor.putString(KEY_SITE_NAME, siteItem.getName());
        editor.putString(KEY_SITE_IMAGE, siteItem.getIconUrl());
        editor.putString(KEY_SITE_PARAMETER, siteItem.getApiSiteParameter());
        editor.putString(KEY_SITE_AUDIENCE,siteItem.getAudience());
        editor.commit();

    }
    public String getApiSiteParameter(){
        return sharedpreferences.getString(KEY_SITE_PARAMETER,Constants.VALUE_STACKOVER_FLOW);
    }
    public HashMap<String, String> getSiteDetail() {
        HashMap<String, String> site = new HashMap<>();
        site.put(KEY_SITE_NAME, sharedpreferences.getString(KEY_SITE_NAME, null));
        site.put(KEY_SITE_IMAGE, sharedpreferences.getString(KEY_SITE_IMAGE, null));
        site.put(KEY_SITE_PARAMETER, sharedpreferences.getString(KEY_SITE_PARAMETER, null));
        site.put(KEY_SITE_AUDIENCE, sharedpreferences.getString(KEY_SITE_AUDIENCE, null));
        return site;
    }
}
