package com.example.dsk221.firstapidemo;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.example.dsk221.firstapidemo.fragments.QuestionDrawerFragment;
import com.example.dsk221.firstapidemo.fragments.TagDrawerFragment;
import com.example.dsk221.firstapidemo.fragments.UserDrawerFragment;
import com.example.dsk221.firstapidemo.utility.Utils;

public class UserQuestionDrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private LinearLayout headerRootView, siteListLayout;
    private ImageView imageArrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_question_drawer);
        toolbar = (Toolbar) findViewById(R.id.toolbar_navigation_drawer);
        setSupportActionBar(toolbar);

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
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        headerRootView = (LinearLayout) headerView.findViewById(R.id.headerRootView);
        imageArrow = (ImageView) headerView.findViewById(R.id.image_arrow);
        siteListLayout = (LinearLayout) findViewById(R.id.siteListLayout);

        navigationView.setNavigationItemSelectedListener(this);

        navigationView.setCheckedItem(R.id.nav_user);
        Fragment fragment = UserDrawerFragment.newInstance();
        showFragment(R.string.nav_user_detail_title, fragment);

        headerRootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (siteListLayout.getVisibility() == View.VISIBLE)
                {
                    siteListLayout.setVisibility(View.GONE);
                    imageArrow.setImageResource(R.drawable.ic_arrow_drop_down_black);
                }
                else {
                    siteListLayout.setVisibility(View.VISIBLE);
                    imageArrow.setImageResource(R.drawable.ic_arrow_drop_up_black);
                }
            }
        });
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            showDialog();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        navigationView.setCheckedItem(id);
        Fragment fragment;
        switch (id) {
            case R.id.nav_user:
                fragment = UserDrawerFragment.newInstance();
                showFragment(R.string.nav_user_detail_title, fragment);
                break;
            case R.id.nav_question:
                fragment = QuestionDrawerFragment.newInstance(null);
                showFragment(R.string.nav_question_detail_title, fragment);
                break;
            case R.id.nav_tag:
                fragment = TagDrawerFragment.newInstance();
                showFragment(R.string.nav_tag_title, fragment);
                break;
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

    public void showFragment(int fragmentNameRes, Fragment fragment) {
        if (fragment != null) {
            if (getSupportActionBar() != null)
                getSupportActionBar().setTitle(fragmentNameRes);

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }
    }

}
