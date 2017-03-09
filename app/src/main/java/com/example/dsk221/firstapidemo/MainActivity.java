package com.example.dsk221.firstapidemo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dsk221.firstapidemo.adapters.UserAdapter;
import com.example.dsk221.firstapidemo.dialogs.FilterDialog;
import com.example.dsk221.firstapidemo.models.BuzzItem;
import com.example.dsk221.firstapidemo.models.UserItem;
import com.example.dsk221.firstapidemo.models.UserListResponse;
import com.example.dsk221.firstapidemo.utility.Constants;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity implements FilterDialog.OnResult {
    private static final String TAG = "MainActivity";
    private ListView userListView;
    private TextView textLoading;
    private ProgressBar progressBar;
    private View footerView;
    private UserAdapter mUserAdapter;
    private String userListUrl;
    private int mPageCount = 1;
    public boolean isLoading = false;
    public boolean isSearch = false;
    UserListResponse userlistResponse;

    private String filterOrder = "desc";
    private String filterSort = "reputation";
    private String filterTodate = null;
    private String filterFromdate = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userListView = (ListView) findViewById(R.id.list_user);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);

        textLoading = (TextView) findViewById(R.id.text_loading);

        footerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.footer_layout, null, false);
        footerView.setVisibility(View.GONE);
        userListView.addFooterView(footerView);

        mUserAdapter = new UserAdapter(MainActivity.this);
        userListView.setAdapter(mUserAdapter);
        new GetDataFromJson().execute();

        userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserItem userItem=mUserAdapter.getItem(position);

                Intent i = UserTabActivity.getStartIntent(MainActivity.this,userItem);
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
                    new GetDataFromJson().execute();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        //  super.onBackPressed();
        showDialog();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);

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
        return super.onCreateOptionsMenu(menu);
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
        Toast.makeText(MainActivity.this,
                orderData + "" + sortData + "" + todateData + "" + "" + fromdateData,
                Toast.LENGTH_SHORT).show();
        //reset the page index
        mPageCount = 1;
        mUserAdapter.removeItems();

        filterOrder = orderData;
        filterSort = sortData;
        filterFromdate = fromdateData;
        filterTodate = todateData;

        new GetDataFromJson().execute();
    }

    private class GetDataFromJson extends AsyncTask<Void, Void, String> {
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
                builder.appendQueryParameter(Constants.PARAMS_PAGE, String.valueOf(mPageCount));
                if (filterFromdate != null) {
                    builder.appendQueryParameter(Constants.PARAMS_FROMDATE, filterFromdate);
                }
                if (filterTodate != null) {
                    builder.appendQueryParameter(Constants.PARAMS_TODATE,
                            filterTodate);
                }
                builder.appendQueryParameter(Constants.PARAMS_ORDER, filterOrder);
                builder.appendQueryParameter(Constants.PARAMS_SORT, filterSort);
                builder.appendQueryParameter(Constants.PARAMS_SITE, "stackoverflow");

                userListUrl = builder.build().toString();

                URL url = new URL(userListUrl);
                urlConn = url.openConnection();
                bufferedReader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));

                StringBuffer stringBuffer = new StringBuffer();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuffer.append(line);
                }
                response = stringBuffer.toString();
                Log.d(TAG, stringBuffer.toString());

            } catch (Exception ex) {
                Log.e("App", "yourDataTask", ex);

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
//                ObjectMapper mapper =new ObjectMapper();
//                try {
//                    UserItem userItem=mapper.readValue(s,UserItem.class);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }

//                try {
//                    JSONObject responseObj = new JSONObject(s);
//                    JSONArray items = responseObj.getJSONArray("items");
//
//
//                    for (int i = 0; i < items.length(); i++) {
////                               UserItem userItem = new UserItem();
////                               BuzzItem buzzItem = new BuzzItem();
//
//
//                        JSONObject userJsonObj = items.getJSONObject(i);
//
////                               userItem.setDisplayName(c.getString("display_name"));
////                               userItem.setReputation(c.getInt("reputation"));
////                               userItem.setProfileImage(c.getString("profile_image"));
////
////                               JSONObject badge_count = c.getJSONObject("badge_counts");
////                               buzzItem.setSilver(badge_count.getInt("silver"));
////                               buzzItem.setGold(badge_count.getInt("gold"));
////                               buzzItem.setBronze(badge_count.getInt("bronze"));
////
////                               userItem.setBadgeCounts(buzzItem);
//                        Gson gson = new Gson();
//                        UserItem userItem = gson.fromJson(userJsonObj.toString(), UserItem.class);
//                        userArrayList.add(userItem);
//
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
                //    Log.d(TAG, "onPostExecute: "+userItem.getDisplayName());
                Gson gson = new Gson();
                userlistResponse = gson.fromJson(s, UserListResponse.class);
                //     userArrayList.add(userItem);

                mUserAdapter.addItems(userlistResponse.getItems());
                hideProgressBar();
            } else {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_toast),
                        Toast.LENGTH_SHORT).show();
                hideProgressBar();
            }
        }
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

    public void showDialog() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
        builder1.setMessage(R.string.backdialog_text);
        builder1.setCancelable(true);
        builder1.setPositiveButton(
                R.string.backdialog_positive_btn,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        MainActivity.this.finish();
                    }
                });
        builder1.setNegativeButton(
                R.string.backdialog_negative_btn,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    public void showCustomDialog() {

        FilterDialog filterDialog = FilterDialog.newInstance(filterOrder, filterSort,
                filterTodate, filterFromdate);
        filterDialog.show((MainActivity.this).getSupportFragmentManager(), "Dialog");


    }
}
