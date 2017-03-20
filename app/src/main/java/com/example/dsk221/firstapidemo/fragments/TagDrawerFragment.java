package com.example.dsk221.firstapidemo.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import com.example.dsk221.firstapidemo.R;
import com.example.dsk221.firstapidemo.adapters.TagAdapter;
import com.example.dsk221.firstapidemo.models.ListResponse;
import com.example.dsk221.firstapidemo.models.TagItem;
import com.example.dsk221.firstapidemo.retrofit.ApiClient;
import com.example.dsk221.firstapidemo.retrofit.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by dsk-221 on 17/3/17.
 */

public class TagDrawerFragment extends Fragment {
    private Spinner spinnerTagSearch;
    private EditText editTagSearch;
    private ListView listTag;
    private TextView textLoading;
    private ProgressBar progressBar;
    private View footerView;
    TagAdapter tagAdapter;
    private int mTagPageCount=1;
    private static final String TAG = "Tag Detail";
    private boolean isTagLoading=false;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tag_drawer, container, false);
        spinnerTagSearch=(Spinner)view.findViewById(R.id.spinner_tag_search);
        editTagSearch=(EditText)view.findViewById(R.id.edit_tag_search);
        listTag=(ListView)view.findViewById(R.id.list_tag);
        textLoading=(TextView)view.findViewById(R.id.text_loading);
        progressBar=(ProgressBar)view.findViewById(R.id.progressbar);
        footerView = ((LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.footer_layout, null, false);
        footerView.setVisibility(View.GONE);
        listTag.addFooterView(footerView);

        tagAdapter = new TagAdapter(getActivity());
        listTag.setAdapter(tagAdapter);

        ArrayAdapter<String> tagSpinnerAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item);
        tagSpinnerAdapter.addAll(getResources().getStringArray(R.array.tag_spinner_array));
        tagSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTagSearch.setAdapter(tagSpinnerAdapter);

        getJsonTagResponse();
        listTag.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount,
                                 int totalItemCount) {
                if (firstVisibleItem + visibleItemCount == totalItemCount
                        && (totalItemCount - 1) != 0
                        && !isTagLoading) {
                    mTagPageCount = mTagPageCount + 1;
                    getJsonTagResponse();
                }
            }
        });
        return view;
    }
    private void getJsonTagResponse(){
        isTagLoading=true;
        showProgressBar();
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<ListResponse<TagItem>> call = apiService.getTagDetail(mTagPageCount,
                "desc","popular",
                "stackoverflow");


        call.enqueue(new Callback<ListResponse<TagItem>>() {
            @Override
            public void onResponse(Call<ListResponse<TagItem>> call,
                                   Response<ListResponse<TagItem>> response) {
                hideProgressBar();
                isTagLoading=false;
                tagAdapter.addItems(response.body().getItems());
            }
            @Override
            public void onFailure(Call<ListResponse<TagItem>>call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
            }
        });
    }
    private void hideProgressBar() {
        if (mTagPageCount == 1) {
            textLoading.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
        } else {
            footerView.setVisibility(View.GONE);
        }
    }

    private void showProgressBar() {
        if (mTagPageCount == 1) {
            textLoading.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        } else {
            footerView.setVisibility(View.VISIBLE);
        }
    }
}
