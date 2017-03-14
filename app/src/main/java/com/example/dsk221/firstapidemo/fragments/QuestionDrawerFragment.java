package com.example.dsk221.firstapidemo.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.example.dsk221.firstapidemo.models.QuestionDetailItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dsk-221 on 14/3/17.
 */

public class QuestionDrawerFragment extends Fragment {
    TextView textLoading;
    ProgressBar progressBar;
    ListView listQuestionDetail;
    QuestionDetailAdapter questionDetailAdapter;
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
        return view;
    }
}
