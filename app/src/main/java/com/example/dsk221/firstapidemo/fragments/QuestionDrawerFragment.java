package com.example.dsk221.firstapidemo.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.example.dsk221.firstapidemo.R;
import com.example.dsk221.firstapidemo.adapters.QuestionDetailAdapter;
import com.example.dsk221.firstapidemo.models.ListResponse;
import com.example.dsk221.firstapidemo.models.QuestionDetailItem;
import com.example.dsk221.firstapidemo.retrofit.ApiClient;
import com.example.dsk221.firstapidemo.retrofit.ApiInterface;
import com.example.dsk221.firstapidemo.utility.Constants;
import java.lang.reflect.Type;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by dsk-221 on 14/3/17.
 */

public class QuestionDrawerFragment extends Fragment implements Type {
    TextView textLoading;
    ProgressBar progressBar;
    ListView listQuestionDetail;
    QuestionDetailAdapter questionDetailAdapter;
    private View footerView;
    private int mQuestionPageCount = 1;
    private boolean isQuestionLoading = false;
    private static final String TAG = "Question Detail";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_question_drawer, container, false);
        textLoading=(TextView)view.findViewById(R.id.text_loading);
        progressBar=(ProgressBar)view.findViewById(R.id.progressbar);
        listQuestionDetail=(ListView)view.findViewById(R.id.list_question_detail);

        footerView = ((LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.footer_layout, null, false);
        footerView.setVisibility(View.GONE);
        listQuestionDetail.addFooterView(footerView);

        questionDetailAdapter = new QuestionDetailAdapter(getActivity());
        listQuestionDetail.setAdapter(questionDetailAdapter);

        getJsonQuestionListResponse();
        listQuestionDetail.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount,
                                 int totalItemCount) {
                if (firstVisibleItem + visibleItemCount == totalItemCount
                        && (totalItemCount - 1) != 0
                        && !isQuestionLoading) {
                    mQuestionPageCount = mQuestionPageCount + 1;
                    getJsonQuestionListResponse();
                }
            }
        });
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search,menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }
    private void getJsonQuestionListResponse(){
        isQuestionLoading=true;
        showProgressBar();
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<ListResponse<QuestionDetailItem>> call = apiService.getQuestionDetail(1,Constants.VALUE_DESC,
                Constants.VALUE_ACTIVITY,Constants.VALUE_STACKOVERFLOW);
        call.enqueue(new Callback<ListResponse<QuestionDetailItem>>() {
            @Override
            public void onResponse(Call<ListResponse<QuestionDetailItem>> call,
                                   Response<ListResponse<QuestionDetailItem>> response) {
                hideProgressBar();
                isQuestionLoading=false;
                questionDetailAdapter.addItems(response.body().getItems());
            }
            @Override
            public void onFailure(Call<ListResponse<QuestionDetailItem>>call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
            }
        });
    }
    private void hideProgressBar() {
        if (mQuestionPageCount == 1) {
            textLoading.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
        } else {
            footerView.setVisibility(View.GONE);
        }
    }

    private void showProgressBar() {
        if (mQuestionPageCount == 1) {
            textLoading.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        } else {
            footerView.setVisibility(View.VISIBLE);
        }
    }

}
