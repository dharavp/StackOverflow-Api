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
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {

    private ProgressBar progressbarLoading;
    private static final String TAG = "SplashActivity";
    public static final String KEY_CASE = "caseKey";
    private TextView textError;
    private Button buttonTryAgain;
    private int pageNo = 1;
    private List<SiteItem> listSiteDetail = new ArrayList<>();
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        progressbarLoading = (ProgressBar) findViewById(R.id.progressbar_loading);
        textError = (TextView) findViewById(R.id.text_error);
        buttonTryAgain = (Button) findViewById(R.id.button_try_again);
        progressbarLoading.setVisibility(View.VISIBLE);
        session = new SessionManager(getApplicationContext());
        getAppList();
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
                listSiteDetail = response.body().getItems();

                saveDataInCase();
                saveDataInSharedPreference();
                openActivity();

                Utils.showToast(SplashActivity.this, "get list Successfully..");

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

    private void saveDataInSharedPreference() {
        SiteItem siteItem = null;
        for (SiteItem item : listSiteDetail) {

            if (item.getApiSiteParameter() != null &&
                    item.getApiSiteParameter()
                            .equalsIgnoreCase("stackoverflow")) {
                siteItem = item;
                break;

            }
        }
        if (siteItem == null) {
            listSiteDetail.get(0);
        }
        session.addSiteDetail(siteItem);
    }

    private void openActivity() {
        Intent i = new Intent(SplashActivity.this, UserQuestionDrawerActivity.class);
        startActivity(i);
        finish();
    }
    private void saveDataInCase(){
        ACache mCache = ACache.get(this);
        mCache.put(KEY_CASE, listSiteDetail);
    }
}
