package com.example.dsk221.firstapidemo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.dsk221.firstapidemo.adapters.SiteAdapter;
import com.example.dsk221.firstapidemo.fragments.QuestionDrawerFragment;
import com.example.dsk221.firstapidemo.fragments.TagDrawerFragment;
import com.example.dsk221.firstapidemo.fragments.UserDrawerFragment;
import com.example.dsk221.firstapidemo.models.SiteItem;
import com.example.dsk221.firstapidemo.utility.SessionManager;
import com.example.dsk221.firstapidemo.utility.Utils;
import com.squareup.picasso.Picasso;

import org.afinal.simplecache.ACache;

import java.util.HashMap;
import java.util.List;

public class UserQuestionDrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private LinearLayout headerRootView, siteListLayout;
    private ImageView imageArrow, imageNavigationIcon, imageSite;
    private ListView listSite;
    private SiteAdapter siteAdapter;
    private TextView textNavigationDescription, textNavigationSiteName, textSite;
    private View footerView;
    ACache mCache;
    private List<SiteItem> siteItems;

    public static Intent getStartIntent(Context context) {
        Intent i = new Intent(context, UserQuestionDrawerActivity.class);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_question_drawer);
        toolbar = (Toolbar) findViewById(R.id.toolbar_navigation_drawer);
        setSupportActionBar(toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        listSite = (ListView) findViewById(R.id.list_site);

        footerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.item_site, listSite, false);
        imageSite = (ImageView) footerView.findViewById(R.id.image_site);
        textSite = (TextView) footerView.findViewById(R.id.text_site_name);
        imageSite.setImageResource(R.drawable.ic_more_horiz_black);
        textSite.setText(R.string.siteTitle);
        listSite.addFooterView(footerView);

        siteAdapter = new SiteAdapter(UserQuestionDrawerActivity.this);
        listSite.setAdapter(siteAdapter);


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                Utils.closeKeyBoard(getCurrentFocus());
                if (siteListLayout.getVisibility() == View.VISIBLE) {
                    siteListLayout.setVisibility(View.GONE);
                    imageArrow.setImageResource(R.drawable.ic_arrow_drop_down_black);
                }
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
        imageNavigationIcon = (ImageView) headerView.findViewById(R.id.image_navigation_icon);
        textNavigationDescription = (TextView) headerView.findViewById(R.id.text_navigation_description);
        textNavigationSiteName = (TextView) headerView.findViewById(R.id.text_navigation_site_name);
        siteListLayout = (LinearLayout) findViewById(R.id.siteListLayout);

        navigationView.setNavigationItemSelectedListener(this);

        navigationView.setCheckedItem(R.id.nav_user);
        Fragment fragment = UserDrawerFragment.newInstance();
        showFragment(R.string.nav_user_detail_title, fragment);

        mCache = ACache.get(this);
        siteItems = mCache.getAsObjectList(SplashActivity.KEY_CACHE, SiteItem.class);

        showSharedPreferenceDetail();
        headerRootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (siteListLayout.getVisibility() == View.VISIBLE) {
                    siteListLayout.setVisibility(View.GONE);
                    imageArrow.setImageResource(R.drawable.ic_arrow_drop_down_black);
                } else {
                    siteListLayout.setVisibility(View.VISIBLE);
                    footerView.setVisibility(View.VISIBLE);
                    imageArrow.setImageResource(R.drawable.ic_arrow_drop_up_black);
                }
            }
        });

        footerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = SiteListActivity.startIntent(getApplicationContext());
                startActivityForResult(intent, 222);
            }
        });

        listSite.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SessionManager.getInstance(UserQuestionDrawerActivity.this)
                        .addSiteDetail(siteAdapter.getItem(position));
                showSharedPreferenceDetail();
                changeSite();
            }
        });
    }

    public void showCustomListView(String selectedSite) {

        siteAdapter.addItems(siteItems, selectedSite);
        //   changeSite();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 222 && resultCode == 333) {
            showSharedPreferenceDetail();
            changeSite();
        }
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
        openSelectedFragment(item);
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

    public void showSharedPreferenceDetail() {

        HashMap<String, String> siteDetail = SessionManager.getInstance(this).getSiteDetail();
        String name = siteDetail.get(SessionManager.KEY_SITE_NAME);
        String image = siteDetail.get(SessionManager.KEY_SITE_IMAGE);
        final String audience = siteDetail.get(SessionManager.KEY_SITE_AUDIENCE);
        textNavigationSiteName.setText(name);
        textNavigationDescription.setText(audience);
        Picasso.with(getApplicationContext())
                .load(image)
                .into(imageNavigationIcon);
        showCustomListView(siteDetail.get(SessionManager.KEY_SITE_PARAMETER));
    }

    public void changeSite() {
        Menu menu = navigationView.getMenu();
        MenuItem selectedMenu = null;
        for (int i = 0; i < menu.size(); i++) {
            MenuItem menuItem = menu.getItem(i);
            if (menuItem.isChecked()) {
                selectedMenu = menuItem;
                break;
            }
        }
        if (selectedMenu != null) {
            openSelectedFragment(selectedMenu);
        }
    }

    public void openSelectedFragment(MenuItem menuItem) {
        int id = menuItem.getItemId();
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
    }
}
