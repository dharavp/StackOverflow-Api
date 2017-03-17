package com.example.dsk221.firstapidemo.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
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
import android.widget.Toast;
import com.example.dsk221.firstapidemo.R;
import com.example.dsk221.firstapidemo.UserTabActivity;
import com.example.dsk221.firstapidemo.adapters.UserAdapter;
import com.example.dsk221.firstapidemo.dialogs.FilterDialog;
import com.example.dsk221.firstapidemo.models.ListResponse;
import com.example.dsk221.firstapidemo.models.UserItem;
import com.example.dsk221.firstapidemo.retrofit.ApiClient;
import com.example.dsk221.firstapidemo.retrofit.ApiInterface;
import com.example.dsk221.firstapidemo.utility.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserDrawerFragment extends Fragment implements FilterDialog.OnResult {
    private static final String TAG = "MainActivity";
    private ListView userListView;
    private TextView textLoading;
    private ProgressBar progressBar;
    private View footerView;
    private UserAdapter mUserAdapter;
    private int mPageCount = 1;
    private boolean isLoading = false;
    private boolean isSearch = false;
    private String filterOrder = Constants.VALUE_DESC;
    private String filterSort = Constants.VALUE_REPUTATION;
    private String filterTodate = null;
    private String filterFromdate = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_drawer, container, false);
        userListView = (ListView)view.findViewById(R.id.list_user);
        progressBar = (ProgressBar)view.findViewById(R.id.progressbar);

        textLoading = (TextView)view.findViewById(R.id.text_loading);

        footerView = ((LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.footer_layout, null, false);
        footerView.setVisibility(View.GONE);
        userListView.addFooterView(footerView);

        mUserAdapter = new UserAdapter(getActivity());
        userListView.setAdapter(mUserAdapter);
       // new GetDataFromJson().execute();
        getUserDetail();
        userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserItem userItem=mUserAdapter.getItem(position);

                Intent i = UserTabActivity.getStartIntent(getActivity(),userItem);
                startActivity(i);
            }
        });

        userListView.setOnScrollListener(new AbsListView.OnScrollListener() {
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
                        && !isSearch) {
                    mPageCount = mPageCount + 1;
                    getUserDetail();
                   // new GetDataFromJson().execute();
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
                isSearch = !newText.isEmpty();
                mUserAdapter.getFilter().filter(newText);
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.filter) {
            showCustomDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void sendData(String orderData, String sortData, String todateData, String fromdateData) {

        Toast.makeText(getActivity(),
                orderData + "" + sortData + "" + todateData + "" + "" + fromdateData,
                Toast.LENGTH_SHORT).show();
        //reset the page index
        mPageCount = 1;
        mUserAdapter.removeItems();

        filterOrder = orderData;
        filterSort = sortData;
        filterFromdate = fromdateData;
        filterTodate = todateData;

       // new GetDataFromJson().execute();
        getUserDetail();
    }
//    private class GetDataFromJson extends AsyncTask<Void, Void, String> {
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            isLoading = true;
//            showProgressBar();
//        }
//
//        @Override
//        protected String doInBackground(Void... params) {
//            String response = null;
//            URLConnection urlConn = null;
//            BufferedReader bufferedReader = null;
//            try {
//                Uri.Builder builder = Uri.parse(Constants.URL_USER_LIST).buildUpon();
//                builder.appendQueryParameter(Constants.PARAMS_PAGE, String.valueOf(mPageCount));
//                if (filterFromdate != null) {
//                    builder.appendQueryParameter(Constants.PARAMS_FROMDATE, filterFromdate);
//                }
//                if (filterTodate != null) {
//                    builder.appendQueryParameter(Constants.PARAMS_TODATE,
//                            filterTodate);
//                }
//                builder.appendQueryParameter(Constants.PARAMS_ORDER, filterOrder);
//                builder.appendQueryParameter(Constants.PARAMS_SORT, filterSort);
//                builder.appendQueryParameter(Constants.PARAMS_SITE,Constants.VALUE_STACKOVERFLOW);
//
//                String userListUrl = builder.build().toString();
//
//                URL url = new URL(userListUrl);
//                urlConn = url.openConnection();
//                bufferedReader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
//
//                StringBuffer stringBuffer = new StringBuffer();
//                String line;
//                while ((line = bufferedReader.readLine()) != null) {
//                    stringBuffer.append(line);
//                }
//                response = stringBuffer.toString();
//                Log.d(TAG, stringBuffer.toString());
//
//            } catch (Exception ex) {
//                Log.e("App", "yourDataTask", ex);
//
//            } finally {
//                if (bufferedReader != null) {
//                    try {
//                        bufferedReader.close();
//                    } catch (IOException e) {
//
//                        e.printStackTrace();
//                    }
//                }
//            }
//            return response;
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//            isLoading = false;
//            if (s != null) {
//
//                Gson gson = new Gson();
//                TypeToken<ListResponse<UserItem>> collectionType = new TypeToken<ListResponse<UserItem>>(){};
//                ListResponse<UserItem> userListResponse = gson.fromJson(s, collectionType.getType());
//                mUserAdapter.addItems(userListResponse.getItems());
//                hideProgressBar();
//            } else {
//                Utils.showToast(getActivity(),R.string.error_toast);
//                hideProgressBar();
//            }
//        }
//    }
    private void getUserDetail(){
        isLoading=true;
        showProgressBar();
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        Call<ListResponse<UserItem>> call = apiService.getUserDetail(mPageCount,
                filterFromdate,filterTodate,filterOrder,
                filterSort,Constants.VALUE_STACKOVERFLOW);

        call.enqueue(new Callback<ListResponse<UserItem>>() {
            @Override
            public void onResponse(Call<ListResponse<UserItem>> call,
                                   Response<ListResponse<UserItem>> response) {
                hideProgressBar();
                isLoading=false;
                mUserAdapter.addItems(response.body().getItems());
            }
            @Override
            public void onFailure(Call<ListResponse<UserItem>>call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
            }
        });
    }

    private void hideProgressBar() {
        if (mPageCount == 1) {
            textLoading.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
        } else {
            footerView.setVisibility(View.GONE);
        }
    }

    private void showProgressBar() {
        if (mPageCount == 1) {
            textLoading.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        } else {
            footerView.setVisibility(View.VISIBLE);
        }
    }



    public void showCustomDialog() {

        FilterDialog filterDialog = FilterDialog.newInstance("userDrawer",filterOrder, filterSort,
                filterTodate, filterFromdate);
        filterDialog.setCallbackOnResult(this);
        filterDialog.show(getActivity().getSupportFragmentManager(),
                getResources().getString(R.string.dialog_tag));
    }
}
