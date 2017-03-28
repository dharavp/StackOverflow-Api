package com.example.dsk221.firstapidemo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.dsk221.firstapidemo.adapters.RecyclerViewSiteDetailAdapter;
import com.example.dsk221.firstapidemo.models.ListResponse;
import com.example.dsk221.firstapidemo.models.SiteItem;
import com.example.dsk221.firstapidemo.retrofit.ApiClient;
import com.example.dsk221.firstapidemo.retrofit.ApiInterface;
import com.example.dsk221.firstapidemo.utility.Constants;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SiteListActivity extends AppCompatActivity {

    private TextView textLoading;
    private ProgressBar progressBar;
    private String[] siteArray;
    private RecyclerView recyclerViewSite;
    private RecyclerViewSiteDetailAdapter recyclerViewAdapter;
    private RecyclerView.LayoutManager recylerViewLayoutManager;
    private int sitePageNumber = 1;

    private static final String TAG = "Site Detail";

    public static Intent startIntent(Context context) {
        Intent intent = new Intent(context, SiteListActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_site_list);

        getSupportActionBar().setTitle(R.string.siteTitle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerViewSite = (RecyclerView) findViewById(R.id.recyclerview_site);

        textLoading = (TextView) findViewById(R.id.text_loading);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);

        recylerViewLayoutManager = new LinearLayoutManager(this);

        recyclerViewSite.setLayoutManager(recylerViewLayoutManager);

        recyclerViewAdapter = new RecyclerViewSiteDetailAdapter(recyclerViewSite, this);

        recyclerViewSite.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .color(R.color.dividerColor).sizeResId(R.dimen.divider)
                .marginResId(R.dimen.leftdividermargin, R.dimen.rightdividermargin).build());

        recyclerViewSite.setAdapter(recyclerViewAdapter);

        recyclerViewAdapter.setOnLoadMoreListener(new RecyclerViewSiteDetailAdapter.OnLoadMoreListener() {
            @Override
            public void loadItems() {
                sitePageNumber = sitePageNumber + 1;
                getJsonSiteResponse();
            }
        });
        getJsonSiteResponse();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == android.R.id.home) {
            this.finish();

        }
        return super.onOptionsItemSelected(item);
    }

    private void getJsonSiteResponse() {

        showProgressBar();
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<ListResponse<SiteItem>> call = apiService.getSiteList(sitePageNumber,
                Constants.VALUE_SITE_FILTER);


        call.enqueue(new Callback<ListResponse<SiteItem>>() {
            @Override
            public void onResponse(Call<ListResponse<SiteItem>> call,
                                   Response<ListResponse<SiteItem>> response) {

                hideProgressBar();
                recyclerViewAdapter.setLoaded();
                if (response.body() != null) {
                    recyclerViewAdapter.addItems(response.body().getItems());
                }
            }

            @Override
            public void onFailure(Call<ListResponse<SiteItem>> call, Throwable t) {

                Log.e(TAG, t.toString());
                recyclerViewAdapter.setLoaded();
                hideProgressBar();
            }
        });
    }

    private void hideProgressBar() {
        if (sitePageNumber == 1) {
            textLoading.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
        } else {
            recyclerViewAdapter.removeProcessItem();
        }
    }

    private void showProgressBar() {
        if (sitePageNumber == 1) {
            textLoading.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        } else {
            recyclerViewAdapter.showProcessItem();
        }
    }
}
