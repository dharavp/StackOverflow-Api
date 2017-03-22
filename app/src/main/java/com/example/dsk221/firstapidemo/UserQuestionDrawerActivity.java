package com.example.dsk221.firstapidemo;

import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import com.example.dsk221.firstapidemo.fragments.QuestionDrawerFragment;
import com.example.dsk221.firstapidemo.fragments.TagDrawerFragment;
import com.example.dsk221.firstapidemo.fragments.UserDrawerFragment;
import com.example.dsk221.firstapidemo.utility.Utils;

public class UserQuestionDrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawer;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_question_drawer);
        toolbar = (Toolbar) findViewById(R.id.toolbar_navigation_drawer);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.nav_home_title);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                Utils.closeKeyBoard(getCurrentFocus());

            }
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
               Utils.closeKeyBoard(getCurrentFocus());
            }
        };

        drawer.addDrawerListener(actionBarDrawerToggle);

        actionBarDrawerToggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);

        }
        showDialog();
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        Fragment fragment = null;

        switch (id) {
            case R.id.nav_user:
                fragment = UserDrawerFragment.newInstance();
                getSupportActionBar().setTitle(R.string.nav_user_detail_title);
                break;
            case R.id.nav_question:
                fragment = QuestionDrawerFragment.newInstance(null);
                getSupportActionBar().setTitle(R.string.nav_question_detail_title);
                break;
            case R.id.nav_tag:
                fragment = TagDrawerFragment.newInstance();
                getSupportActionBar().setTitle(R.string.nav_tag_title);
                break;
        }
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void showDialog() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(UserQuestionDrawerActivity.this);
        builder1.setMessage(R.string.back_dialog_text);
        builder1.setCancelable(true);
        builder1.setPositiveButton(
                R.string.back_dialog_positive_btn,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        UserQuestionDrawerActivity.this.finish();
                    }
                });
        builder1.setNegativeButton(
                R.string.back_dialog_negative_btn,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

}
