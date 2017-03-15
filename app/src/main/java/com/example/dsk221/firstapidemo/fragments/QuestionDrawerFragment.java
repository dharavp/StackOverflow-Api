package com.example.dsk221.firstapidemo.fragments;

import android.graphics.Movie;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.util.SortedList;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.dsk221.firstapidemo.MainActivity;
import com.example.dsk221.firstapidemo.R;
import com.example.dsk221.firstapidemo.adapters.QuestionDetailAdapter;
import com.example.dsk221.firstapidemo.adapters.UserAdapter;
import com.example.dsk221.firstapidemo.models.ListResponse;
import com.example.dsk221.firstapidemo.models.QuestionDetailItem;
import com.example.dsk221.firstapidemo.retrofit.ApiClient;
import com.example.dsk221.firstapidemo.retrofit.ApiInterface;
import com.example.dsk221.firstapidemo.utility.Constants;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

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

        questionDetailAdapter = new QuestionDetailAdapter(getActivity());
        listQuestionDetail.setAdapter(questionDetailAdapter);

        progressBar.setVisibility(View.VISIBLE);
        textLoading.setVisibility(View.VISIBLE);
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<ListResponse<QuestionDetailItem>> call = apiService.getQuestionDetail("desc",
                "votes","stackoverflow");
        call.enqueue(new Callback<ListResponse<QuestionDetailItem>>() {
            @Override
            public void onResponse(Call<ListResponse<QuestionDetailItem>> call,
                                   Response<ListResponse<QuestionDetailItem>> response) {
                progressBar.setVisibility(View.GONE);
                textLoading.setVisibility(View.GONE);
                questionDetailAdapter.addItems(response.body().getItems());
                // Log.d(TAG, "title name: " + questionDetailItems.get(1).getTitle());
            }

            @Override
            public void onFailure(Call<ListResponse<QuestionDetailItem>>call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
            }
        });
        return view;
    }
}
