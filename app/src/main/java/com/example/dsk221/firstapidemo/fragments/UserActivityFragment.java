package com.example.dsk221.firstapidemo.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.dsk221.firstapidemo.R;
import com.example.dsk221.firstapidemo.adapters.QuestionAdapter;
import com.example.dsk221.firstapidemo.models.ListResponse;
import com.example.dsk221.firstapidemo.models.QuestionItem;
import com.example.dsk221.firstapidemo.utility.Constants;
import com.example.dsk221.firstapidemo.utility.SessionManager;
import com.example.dsk221.firstapidemo.utility.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class UserActivityFragment extends Fragment {
    private ListView listQuestion;
    private ProgressBar progressBar;
    private TextView textLoading,textNoDetail;
    private QuestionAdapter mQuestionAdapter;
    private View footerView;
    private int userId;
    private int mQuestionPageCount = 1;
    private boolean isLoading = false;
    public static final String  ARG_USER_ID="userId";
    private boolean hasMoreUserActivity = true;

    public UserActivityFragment() {
    }
    public static UserActivityFragment newInstance(int userId) {
        UserActivityFragment userActivityFragment=new UserActivityFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_USER_ID, userId);
        userActivityFragment.setArguments(bundle);
        return userActivityFragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_activity, container, false);

        listQuestion = (ListView) view.findViewById(R.id.list_question);
        progressBar = (ProgressBar) view.findViewById(R.id.progressbar);
        textLoading = (TextView) view.findViewById(R.id.text_loading);
        textNoDetail = (TextView) view.findViewById(R.id.text_no_detail);
        footerView = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.footer_layout, null, false);
        footerView.setVisibility(View.GONE);
        listQuestion.addFooterView(footerView);

        Bundle bundle = getArguments();
        userId = bundle.getInt(ARG_USER_ID);
        mQuestionAdapter = new QuestionAdapter(getActivity());
        listQuestion.setAdapter(mQuestionAdapter);

        new GetQuestionDetailFromJson().execute();

        listQuestion.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                QuestionItem item = mQuestionAdapter.getItem(position);
                String openLink = item.getLink();
                Intent intent = new Intent(Intent.ACTION_VIEW)
                        .setData(Uri.parse(openLink));
                startActivity(intent);

            }
        });
        listQuestion.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount,
                                 int totalItemCount) {
                if (firstVisibleItem + visibleItemCount == totalItemCount
                        && (totalItemCount - 1) != 0
                        && !isLoading
                        && hasMoreUserActivity) {
                    mQuestionPageCount = mQuestionPageCount + 1;
                    new GetQuestionDetailFromJson().execute();
                }
            }
        });
        return view;
    }

    private class GetQuestionDetailFromJson extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            isLoading = true;
            showProgressBar();
        }

        @Override
        protected String doInBackground(Void... params) {
            String response = null;
            URLConnection urlConn = null;
            BufferedReader bufferedReader = null;
            try {
                Uri.Builder builder = Uri.parse(Constants.URL_USER_LIST).buildUpon();
                builder.appendPath(String.valueOf(userId));
                builder.appendPath(Constants.URL_PATH_POSTS);
                builder.appendQueryParameter(Constants.PARAMS_PAGE,
                        String.valueOf(mQuestionPageCount));
                builder.appendQueryParameter(Constants.PARAMS_ORDER, Constants.VALUE_DESC);
                builder.appendQueryParameter(Constants.PARAMS_SORT, Constants.VALUE_ACTIVITY);
                builder.appendQueryParameter(Constants.PARAMS_SITE, Constants.VALUE_STACKOVER_FLOW);
                builder.appendQueryParameter(Constants.PARAMS_FILTER,
                        SessionManager.getInstance(getActivity()).getApiSiteParameter());

                String questionListUrl = builder.build().toString();

                URL url = new URL(questionListUrl);
                urlConn = url.openConnection();
                bufferedReader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));

                StringBuffer stringBuffer = new StringBuffer();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuffer.append(line);
                }
                response = stringBuffer.toString();

            } catch (Exception ex) {
                // Log.e("App", "yourDataTask", ex);

            } finally {
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {

                        e.printStackTrace();
                    }
                }
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            isLoading = false;
            if (s != null) {
                Gson gson = new Gson();
                TypeToken<ListResponse<QuestionItem>> collectionType1 = new TypeToken<ListResponse<QuestionItem>>() {
                };
                ListResponse<QuestionItem> questionResponse = gson.fromJson(s, collectionType1.getType());
                hasMoreUserActivity=questionResponse.isHasMore();
                mQuestionAdapter.addItems(questionResponse.getItems());
                if(mQuestionAdapter.getCount()==0){
                    textNoDetail.setVisibility(View.VISIBLE);
                    listQuestion.setVisibility(View.GONE);
                }
                hideProgressBar();
            } else {
                Utils.showToast(getActivity(),R.string.error_toast);
                hideProgressBar();
            }
        }
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
