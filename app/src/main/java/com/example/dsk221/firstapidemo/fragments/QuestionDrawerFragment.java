package com.example.dsk221.firstapidemo.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.dsk221.firstapidemo.R;
import com.example.dsk221.firstapidemo.adapters.QuestionDetailAdapter;
import com.example.dsk221.firstapidemo.dialogs.FilterDialog;
import com.example.dsk221.firstapidemo.models.ListResponse;
import com.example.dsk221.firstapidemo.models.QuestionDetailItem;
import com.example.dsk221.firstapidemo.retrofit.ApiClient;
import com.example.dsk221.firstapidemo.retrofit.ApiInterface;
import com.example.dsk221.firstapidemo.utility.Constants;

import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by dsk-221 on 14/3/17.
 */

public class QuestionDrawerFragment extends Fragment implements FilterDialog.OnResult {
    private TextView textLoading;
    private ProgressBar progressBar;
    private ListView listQuestionDetail;
    private QuestionDetailAdapter questionDetailAdapter;
    private View footerView;
    private int mQuestionPageCount = 1;
    private boolean isQuestionLoading = false;
    private static final String TAG = "QuestionDrawerFragment";
    private String filterQuestionOrder = Constants.VALUE_DESC;
    private String filterQuestionSort = Constants.VALUE_VOTES;
    private String filterQuestionTodate = null;
    private String filterQuestionFromdate = null;
    private boolean hasMore = true;
    private boolean isQuestionSearch = false;
    public static final String ARG_TAG = "tagName";
    private String tagName = null;
    private String titleName = null;
    private Timer timer;
    private Handler mHandler;
    private Runnable runnable;

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (timer != null) {
                timer.cancel();
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    // do your actual work here
                }
            }, 600);
        }
    };

    public static QuestionDrawerFragment newInstance(String tag) {
        QuestionDrawerFragment questionDrawerFragment = new QuestionDrawerFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_TAG, tag);
        questionDrawerFragment.setArguments(bundle);
        return questionDrawerFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_question_drawer, container, false);
        textLoading = (TextView) view.findViewById(R.id.text_loading);
        progressBar = (ProgressBar) view.findViewById(R.id.progressbar);
        listQuestionDetail = (ListView) view.findViewById(R.id.list_question_detail);

        footerView = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.footer_layout, null, false);
        footerView.setVisibility(View.GONE);
        listQuestionDetail.addFooterView(footerView);

        Bundle bundle = getArguments();
        tagName = bundle.getString(ARG_TAG);

        questionDetailAdapter = new QuestionDetailAdapter(getActivity());
        listQuestionDetail.setAdapter(questionDetailAdapter);

        getJsonQuestionListResponse("no_search");
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
                        && !isQuestionLoading
                        && hasMore
                        && isQuestionSearch) {
                    mQuestionPageCount = mQuestionPageCount + 1;
                    getJsonQuestionListResponse("search");
                } else if (firstVisibleItem + visibleItemCount == totalItemCount
                        && (totalItemCount - 1) != 0
                        && !isQuestionLoading
                        && hasMore
                        && !isQuestionSearch) {
                    mQuestionPageCount = mQuestionPageCount + 1;
                    getJsonQuestionListResponse("no_search");
                } else {

                }
            }
        });

        listQuestionDetail.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                QuestionDetailItem questionDetail = questionDetailAdapter.getItem(position);
                String openLink = questionDetail.getLink();
                Intent intent = new Intent(Intent.ACTION_VIEW)
                        .setData(Uri.parse(openLink));
                startActivity(intent);
            }
        });

        return view;
    }

    private long lastTime;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastTime = System.currentTimeMillis();
                Log.d(TAG, "onClick: " + lastTime);
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {
                if (newText.length() == 0) {
                    if (timer != null) {
                        timer.cancel();
                    }
                    searchList(newText);
                    return false;
                }
                long currentTime = System.currentTimeMillis();
                if (currentTime - lastTime < 2000) {
                    if (timer != null) {
                        timer.cancel();
                    }
                }
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        getActivity().runOnUiThread(new TimerTask() {
                            @Override
                            public void run() {
                                searchList(newText);
                            }
                        });
                    }
                }, 600);
                lastTime = System.currentTimeMillis();
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void searchList(String newText) {
        Log.d(TAG, "searchList() called with: newText = [" + newText + "]");
        mQuestionPageCount = 1;
        questionDetailAdapter.removeItems();
        if (newText.isEmpty()) {
            getJsonQuestionListResponse("no_search");
//            Utils.closeKeyBoard(searchView);
        } else {
            isQuestionSearch = !newText.isEmpty();
            titleName = newText;
            getJsonQuestionListResponse("search");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.filter) {
            showFilterDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void sendData(String orderData, String sortData, String todateData, String fromdateData) {
        mQuestionPageCount = 1;
        questionDetailAdapter.removeItems();

        filterQuestionOrder = orderData;
        filterQuestionSort = sortData;
        filterQuestionFromdate = fromdateData;
        filterQuestionTodate = todateData;

        getJsonQuestionListResponse("no_search");
    }

    private void getJsonQuestionListResponse(String type) {
        isQuestionLoading = true;
        showProgressBar();
        Call<ListResponse<QuestionDetailItem>> call = null;
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        if (type.equalsIgnoreCase("no_search")) {
            call = apiService.getQuestionList(
                    mQuestionPageCount,
                    filterQuestionFromdate,
                    filterQuestionTodate,
                    filterQuestionOrder,
                    filterQuestionSort,
                    tagName,
                    Constants.VALUE_STACKOVER_FLOW);
        } else {
            call = apiService.getSearchQuestionList(mQuestionPageCount,
                    filterQuestionFromdate,
                    filterQuestionTodate,
                    filterQuestionOrder,
                    filterQuestionSort,
                    titleName,
                    Constants.VALUE_STACKOVER_FLOW);

        }
        call.enqueue(new Callback<ListResponse<QuestionDetailItem>>() {
            @Override
            public void onResponse(Call<ListResponse<QuestionDetailItem>> call,
                                   Response<ListResponse<QuestionDetailItem>> response) {

                isQuestionLoading = false;
                hideProgressBar();
                if (response.body() != null) {
                    hasMore = response.body().isHasMore();
                    questionDetailAdapter.addItems(response.body().getItems());
                }

            }

            @Override
            public void onFailure(Call<ListResponse<QuestionDetailItem>> call, Throwable t) {
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

    public void showFilterDialog() {
        FilterDialog filterDialog = FilterDialog.newInstance(
                getResources().getString(R.string.open_dialog_from_question_drawer),
                filterQuestionOrder,
                filterQuestionSort,
                filterQuestionTodate,
                filterQuestionFromdate);
        filterDialog.setCallbackOnResult(this);
        filterDialog.show(getActivity().getSupportFragmentManager(),
                getResources().getString(R.string.dialog_tag));
    }
}
