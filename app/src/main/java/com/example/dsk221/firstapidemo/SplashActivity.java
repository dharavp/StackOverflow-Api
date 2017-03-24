package com.example.dsk221.firstapidemo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.example.dsk221.firstapidemo.models.ListResponse;
import com.example.dsk221.firstapidemo.models.SiteItem;
import com.example.dsk221.firstapidemo.retrofit.ApiClient;
import com.example.dsk221.firstapidemo.retrofit.ApiInterface;
import com.example.dsk221.firstapidemo.utility.Constants;
import com.example.dsk221.firstapidemo.utility.Utils;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {

    private ProgressBar progressbarLoading;
    private static final String TAG = "SplashActivity";
    private TextView textError;
    private Button buttonTryAgain;
    private int pageNo =1;
    public static ArrayList<SiteItem> listSiteDetail=new ArrayList<>();
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String SiteName = "nameKey";
    public static final String SiteImage = "phoneKey";
    public static String txtSite;
    public static String imgSite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        progressbarLoading = (ProgressBar) findViewById(R.id.progressbar_loading);
        textError = (TextView) findViewById(R.id.text_error);
        buttonTryAgain = (Button) findViewById(R.id.button_try_again);
        progressbarLoading.setVisibility(View.VISIBLE);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        txtSite = sharedpreferences.getString(SiteName, "");
        imgSite = sharedpreferences.getString(SiteImage,"");

        getAppList();
    }
    public void getAppList(){

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<ListResponse<SiteItem>> call = apiService.getSiteList(pageNo,
                Constants.VALUE_SITE_FILTER);

        call.enqueue(new Callback<ListResponse<SiteItem>>() {
            @Override
            public void onResponse(Call<ListResponse<SiteItem>> call,
                                   Response<ListResponse<SiteItem>> response) {
                progressbarLoading.setVisibility(View.GONE);
                Log.d(TAG, "onResponse: "+response.body());
                listSiteDetail = response.body().getItems();

                for(int i=0;i<listSiteDetail.size();i++){

                    if((listSiteDetail.get(i).getName()).equalsIgnoreCase("Stack Overflow")){
                        Log.d(TAG, "onResponse: "+listSiteDetail.get(i).getName());
                        SharedPreferences.Editor editor = sharedpreferences.edit();

                        editor.putString(SiteName, listSiteDetail.get(i).getName());
                        editor.putString(SiteImage, listSiteDetail.get(i).getIconUrl());
                        editor.apply();
                    }
                }

                Utils.showToast(SplashActivity.this,"get list Successfully..");
                Intent i = new Intent(SplashActivity.this, UserQuestionDrawerActivity.class);
                startActivity(i);
                finish();
            }
            @Override
            public void onFailure(Call<ListResponse<SiteItem>> call, Throwable t) {
                // Log error here since request failed
                textError.setVisibility(View.VISIBLE);
                buttonTryAgain.setVisibility(View.VISIBLE);
                getAppList();
                Log.e(TAG, t.toString());
            }
        });
    }


        }
