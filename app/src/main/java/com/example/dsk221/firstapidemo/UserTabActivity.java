package com.example.dsk221.firstapidemo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dsk221.firstapidemo.adapters.UserAdapter;
import com.example.dsk221.firstapidemo.fragments.UserActivityFragment;
import com.example.dsk221.firstapidemo.fragments.UserProfileFragment;
import com.example.dsk221.firstapidemo.models.BuzzItem;
import com.example.dsk221.firstapidemo.models.UserItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class UserTabActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "position";
    private static final String TAG = "bronze detail";
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TextView textName, textReputation, textSilver, textBronze, textGold;
    private ImageView imageUser;
    UserItem user;

    public static Intent getStartIntent(Context context, UserItem user) {
        Intent i = new Intent(context, UserTabActivity.class);
        i.putExtra(EXTRA_POSITION, user);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_tab);

        Intent i = getIntent();
        user = i.getParcelableExtra(EXTRA_POSITION);

        textName = (TextView) findViewById(R.id.text_name);
        textReputation = (TextView) findViewById(R.id.text_reputation);
        textBronze = (TextView) findViewById(R.id.text_bronze);
        textGold = (TextView) findViewById(R.id.text_gold);
        textSilver = (TextView) findViewById(R.id.text_silver);

        imageUser = (ImageView) findViewById(R.id.image_user);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.setTabTextColors(ContextCompat.getColor(UserTabActivity.this, R.color.colorCountText),
                ContextCompat.getColor(UserTabActivity.this, R.color.colorTabTitle));

        getSupportActionBar().setTitle("User Detail");
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(ContextCompat.getColor(UserTabActivity.this, R.color.colorTabTitle));

        BuzzItem buzzDetail = user.getBadgeCounts();

        textName.setTextColor(ContextCompat.getColor(UserTabActivity.this, R.color.colorTabTitle));
        textReputation.setTextColor(ContextCompat.getColor(UserTabActivity.this, R.color.colorTabTitle));

        textName.setText(user.getDisplayName());
        textReputation.setText(UserAdapter.getRepString(user.getReputation()));
        textBronze.setText(String.valueOf(buzzDetail.getBronze()));
        textSilver.setText(String.valueOf(buzzDetail.getSilver()));
        textGold.setText(String.valueOf(buzzDetail.getGold()));

        Picasso.with(UserTabActivity.this)
                .load(user.getProfileImage())
                .placeholder(R.drawable.image_background)
                .into(imageUser);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                UserTabActivity.this.finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new UserProfileFragment(), "Profile");
        adapter.addFragment(new UserActivityFragment(), "Activity");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    public int getUserId() {
        return (user.getUserId());
    }
}
