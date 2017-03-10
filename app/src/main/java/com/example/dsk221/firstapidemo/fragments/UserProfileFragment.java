package com.example.dsk221.firstapidemo.fragments;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.dsk221.firstapidemo.R;
import com.example.dsk221.firstapidemo.UserTabActivity;
import com.example.dsk221.firstapidemo.models.UserItem;
import com.example.dsk221.firstapidemo.models.ListResponse;
import com.example.dsk221.firstapidemo.utility.Constants;
import com.example.dsk221.firstapidemo.utility.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class UserProfileFragment extends Fragment{
    private TextView textAboutUser,textAnswerCounts,textQuestionCounts,
            textViewCounts,textLocation,textWebsiteUrl;
    private TextView textLoading;
    private ProgressBar progressBar;
    private ListResponse<UserItem> listResponse;
    private int userid;
    private String userDetailUrl,userdetail;
    LinearLayout linearLayout;
    List<UserItem> userDetail=new ArrayList<>();

    public UserProfileFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);

        textAboutUser=(TextView)view.findViewById(R.id.text_about_user);
        textAnswerCounts=(TextView)view.findViewById(R.id.text_answer_count);
        textQuestionCounts=(TextView)view.findViewById(R.id.text_question_count);
        textViewCounts=(TextView)view.findViewById(R.id.text_view_counts);
        textLocation=(TextView)view.findViewById(R.id.text_location);
        textWebsiteUrl=(TextView)view.findViewById(R.id.text_website_url);

        progressBar = (ProgressBar)view.findViewById(R.id.progressbar);

        linearLayout=(LinearLayout)view.findViewById(R.id.linearLayout);

        textLoading = (TextView)view.findViewById(R.id.text_loading);
        UserTabActivity userTabActivity=(UserTabActivity)getActivity();
        userid= userTabActivity.getUserId();

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
                builder.appendPath(String.valueOf(userid));
                builder.appendQueryParameter(Constants.PARAMS_ORDER, "desc");
                builder.appendQueryParameter(Constants.PARAMS_SORT, "reputation");
                builder.appendQueryParameter(Constants.PARAMS_SITE, "stackoverflow");
                builder.appendQueryParameter(Constants.PARAMS_FILTER,"!Ln3laa1vT(0iqDP9Y6uLh_");

                userDetailUrl = builder.build().toString();

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
                TypeToken<ListResponse<UserItem>> collectionType = new TypeToken<ListResponse<UserItem>>(){};
                listResponse = gson.fromJson(s, collectionType.getType());
                userDetail= listResponse.getItems();
                UserItem aboutUser = userDetail.get(0);

                hideProgressBar();
                linearLayout.setVisibility(View.VISIBLE);

                userdetail=aboutUser.getAboutMe();

                textAboutUser.setText(Utils.convertHtmlInTxt(userdetail));
                textAboutUser.setMovementMethod(LinkMovementMethod.getInstance());
                textAnswerCounts.setText((aboutUser.getAnswerCount())+"");
                textQuestionCounts.setText((aboutUser.getQuestionCount())+"");
                textViewCounts.setText((aboutUser.getViewCount())+"");
                textLocation.setText(aboutUser.getLocation());
                textWebsiteUrl.setText(aboutUser.getWebsiteUrl());

            } else {

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
}
