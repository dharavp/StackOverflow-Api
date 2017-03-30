package com.example.dsk221.firstapidemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import com.example.dsk221.firstapidemo.utility.SessionManager;
import com.example.dsk221.firstapidemo.utility.Utils;
import org.afinal.simplecache.ACache;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {

    private ProgressBar progressbarLoading;
    private static final String TAG = "SplashActivity";
    public static final String KEY_CACHE = "caseKey";
    private TextView textError;
    private Button buttonTryAgain;
    private int pageNo = 1;
    private List<SiteItem> listSiteDetail = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        progressbarLoading = (ProgressBar) findViewById(R.id.progressbar_loading);
        textError = (TextView) findViewById(R.id.text_error);
        buttonTryAgain = (Button) findViewById(R.id.button_try_again);
        progressbarLoading.setVisibility(View.VISIBLE);
        getAppList();

        buttonTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonTryAgain.setVisibility(View.GONE);
                textError.setVisibility(View.GONE);
                progressbarLoading.setVisibility(View.VISIBLE);
                getAppList();
            }
        });
    }

    public void getAppList() {

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<ListResponse<SiteItem>> call = apiService.getSiteList(pageNo,
                Constants.VALUE_SITE_FILTER);

        call.enqueue(new Callback<ListResponse<SiteItem>>() {
            @Override
            public void onResponse(Call<ListResponse<SiteItem>> call,
                                   Response<ListResponse<SiteItem>> response) {
                progressbarLoading.setVisibility(View.GONE);
                Log.d(TAG, "onResponse: " + response.body());
                if(response.body()!=null){
                    listSiteDetail = response.body().getItems();
                    saveDataInSharedPreference();
                    saveDataInCache();
                    openActivity();
                    Utils.showToast(SplashActivity.this, "get list Successfully..");
                }
                else{
                    textError.setVisibility(View.VISIBLE);
                    buttonTryAgain.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onFailure(Call<ListResponse<SiteItem>> call, Throwable t) {
                textError.setVisibility(View.VISIBLE);
                buttonTryAgain.setVisibility(View.VISIBLE);
                progressbarLoading.setVisibility(View.GONE);
                Log.e(TAG, t.toString());
            }
        });
    }
    private void saveDataInSharedPreference() {
        HashMap<String, String> siteDetail = SessionManager.getInstance(this).getSiteDetail();
        final String audience = siteDetail.get(SessionManager.KEY_SITE_PARAMETER);
        if(audience == null){
            SiteItem siteItem = null;
            for (SiteItem item : listSiteDetail) {

                if (item.getApiSiteParameter() != null &&
                        item.getApiSiteParameter()
                                .equalsIgnoreCase(getResources().getString(R.string.siteCompare))) {
                    siteItem = item;
                    break;
                }
            }
            if (siteItem == null) {
                listSiteDetail.get(0);
            }
            SessionManager.getInstance(this).addSiteDetail(siteItem);
        }
    }
    private void openActivity() {
        Intent intent=UserQuestionDrawerActivity.getStartIntent(SplashActivity.this);
        startActivity(intent);
        finish();
    }
    private void saveDataInCache(){
        ACache mCache = ACache.get(this);
        mCache.put(KEY_CACHE, listSiteDetail);
    }
}
