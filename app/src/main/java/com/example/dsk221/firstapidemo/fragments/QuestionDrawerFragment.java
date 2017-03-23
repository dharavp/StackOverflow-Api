package com.example.dsk221.firstapidemo.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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
    public static final String ARG_TAG = "tagName";
    private String tagName = null;
    private String titleName = null;
    private Timer timer;
    private Handler mHandler;
    private Runnable runnable;

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

        getJsonQuestionListResponse(null);
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
                        && hasMore) {
                    mQuestionPageCount = mQuestionPageCount + 1;
                    getJsonQuestionListResponse(titleName);
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
        mHandler=new Handler();

        return view;
    }

    /**
     * last search time in milliseconds
     */
    private long lastTime;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                // reset searched text and last time when search is completed
                titleName = null;
//                lastTime = 0;
                return false;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {

                mHandler.removeCallbacksAndMessages(null);

                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        searchList(newText);
                        //Put your call to the server here (with mQueryString)
                    }
                }, 300);
                // if it is for first time search, dont need to call for empty text
//                if (lastTime == 0) {
//                    lastTime = System.currentTimeMillis();
//                    return false;
//                }
//                // when user reset text field in search view
//                if (newText.length() == 0) {
//                    // cancel timer for last typed char
//                    if (timer != null) {
//                        timer.cancel();
//                    }
//                    searchList(newText);
//                    return false;
//                }
//
//                long currentTime = System.currentTimeMillis();
//                if (currentTime - lastTime < 2000) {
//                    // cancel timer for last typed char
//                    if (timer != null) {
//                        timer.cancel();
//                    }
//                }
//                // create timer for new char
//                timer = new Timer();
//                timer.schedule(new TimerTask() {
//                    @Override
//                    public void run() {
//                        getActivity().runOnUiThread(new TimerTask() {
//                            @Override
//                            public void run() {
//                                searchList(newText);
//                            }
//                        });
//                    }
//                }, 600);
//                lastTime = System.currentTimeMillis()
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
            getJsonQuestionListResponse(null);
//            Utils.closeKeyBoard(searchView);
        } else {
//            isQuestionSearch = !newText.isEmpty();
            titleName = newText;
            getJsonQuestionListResponse(titleName);
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

        getJsonQuestionListResponse(null);
    }

    private void getJsonQuestionListResponse(String titleName) {
        isQuestionLoading = true;
        showProgressBar();
        Call<ListResponse<QuestionDetailItem>> call = null;
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        if (titleName == null) {
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
