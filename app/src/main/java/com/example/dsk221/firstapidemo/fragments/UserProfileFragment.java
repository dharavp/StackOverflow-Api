package com.example.dsk221.firstapidemo.fragments;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.dsk221.firstapidemo.R;
import com.example.dsk221.firstapidemo.models.ListResponse;
import com.example.dsk221.firstapidemo.models.UserItem;
import com.example.dsk221.firstapidemo.utility.Constants;
import com.example.dsk221.firstapidemo.utility.HtmlImageGetter;
import com.example.dsk221.firstapidemo.utility.SessionManager;
import com.example.dsk221.firstapidemo.utility.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

public class UserProfileFragment extends Fragment {
    private TextView textAboutUser, textAnswerCounts, textQuestionCounts,
            textViewCounts, textLocation, textWebsiteUrl;
    private TextView textLoading;
    private ProgressBar progressBar;
    public static final String ARG_USER_ID = "user_id";
    private int userId;
    private LinearLayout linearLayout;


    public UserProfileFragment() {
    }

    public static UserProfileFragment newInstance(int userId) {
        UserProfileFragment userProfileFragment = new UserProfileFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_USER_ID, userId);
        userProfileFragment.setArguments(bundle);
        return userProfileFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);

        textAboutUser = (TextView) view.findViewById(R.id.text_about_user);
        textAnswerCounts = (TextView) view.findViewById(R.id.text_answer_count);
        textQuestionCounts = (TextView) view.findViewById(R.id.text_question_count);
        textViewCounts = (TextView) view.findViewById(R.id.text_view_counts);
        textLocation = (TextView) view.findViewById(R.id.text_location);
        textWebsiteUrl = (TextView) view.findViewById(R.id.text_website_url);

        progressBar = (ProgressBar) view.findViewById(R.id.progressbar);

        linearLayout = (LinearLayout) view.findViewById(R.id.linearLayout);

        textLoading = (TextView) view.findViewById(R.id.text_loading);

        Bundle bundle = getArguments();
        userId = bundle.getInt(ARG_USER_ID);
        new GetUserDetailFromJson().execute();

        return view;
    }


    private class GetUserDetailFromJson extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
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
                builder.appendQueryParameter(Constants.PARAMS_ORDER, Constants.VALUE_DESC);
                builder.appendQueryParameter(Constants.PARAMS_SORT, Constants.VALUE_REPUTATION);
                builder.appendQueryParameter(Constants.PARAMS_SITE,
                        SessionManager.getInstance(getActivity()).getApiSiteParameter());
                builder.appendQueryParameter(Constants.PARAMS_FILTER,
                        Constants.VALUE_USER_PROFILE_FILTER);

                String userDetailUrl = builder.build().toString();

                URL url = new URL(userDetailUrl);
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
            if (s != null) {
             Gson gson = new Gson();
                TypeToken<ListResponse<UserItem>> collectionType = new TypeToken<ListResponse<UserItem>>() {
                };
                ListResponse<UserItem> listResponse = gson.fromJson(s, collectionType.getType());
                List<UserItem> userItemList = listResponse.getItems();
                UserItem aboutUser = userItemList.get(0);

                hideProgressBar();
                linearLayout.setVisibility(View.VISIBLE);

                showUserDetail(aboutUser);

            } else {
                hideProgressBar();
                Utils.showToast(getActivity(), R.string.error_toast);
            }
        }
    }

    private void showProgressBar() {
        textLoading.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        textLoading.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
    }
    private void showUserDetail(UserItem aboutUser){
        String userDetail = aboutUser.getAboutMe();

        if ((userDetail==null) || (userDetail.isEmpty())) {
            textAboutUser.setVisibility(View.GONE);
        } else {
            HtmlImageGetter imageGetter = new HtmlImageGetter(getActivity(),
                    R.drawable.image_html_response_background) {
                @Override
                public void onTextUpdate() {
                    CharSequence sequence = textAboutUser.getText();
                    textAboutUser.setText(sequence);
                }
            };
            Spanned spannedBody = Utils.convertHtmlInTxt(userDetail, imageGetter);
            textAboutUser.setText(spannedBody);

        }
        textAboutUser.setMovementMethod(LinkMovementMethod.getInstance());
        textAnswerCounts.setText((aboutUser.getAnswerCount()) + "");
        textQuestionCounts.setText((aboutUser.getQuestionCount()) + "");
        textViewCounts.setText((aboutUser.getViewCount()) + "");

        String location = aboutUser.getLocation();
        if ((location==null) || (location.isEmpty())) {
            textLocation.setVisibility(View.GONE);
        } else {
            textLocation.setText(location);
        }
        String url = aboutUser.getWebsiteUrl();
        if ((url==null) || (url.isEmpty())) {
            textWebsiteUrl.setVisibility(View.GONE);
        } else {
            textWebsiteUrl.setText(url);
        }
    }
}
