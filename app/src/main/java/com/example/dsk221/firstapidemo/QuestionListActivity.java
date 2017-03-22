package com.example.dsk221.firstapidemo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.dsk221.firstapidemo.fragments.QuestionDrawerFragment;

public class QuestionListActivity extends AppCompatActivity {
    public static final String ARG_TAG_NAME = "tag_name";

    public static Intent getStartIntent(Context context, String tagName) {
        Intent i = new Intent(context, QuestionListActivity.class);
        i.putExtra(ARG_TAG_NAME, tagName);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_list);

        Intent i = getIntent();
        String tagName = i.getStringExtra(ARG_TAG_NAME);
        getSupportActionBar().setTitle(tagName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Fragment fragment = QuestionDrawerFragment.newInstance(tagName);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.frame_layout_que_list, fragment);
        ft.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                QuestionListActivity.this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
