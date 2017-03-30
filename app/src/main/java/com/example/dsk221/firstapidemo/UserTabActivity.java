package com.example.dsk221.firstapidemo;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dsk221.firstapidemo.fragments.UserActivityFragment;
import com.example.dsk221.firstapidemo.fragments.UserProfileFragment;
import com.example.dsk221.firstapidemo.models.BuzzItem;
import com.example.dsk221.firstapidemo.models.UserItem;
import com.example.dsk221.firstapidemo.utility.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class UserTabActivity extends AppCompatActivity {

    public static final String EXTRA_USER = "user";
    public static final String EXTRA_LINK = "link";
    private static final String TAG = "bronze detail";
    private String link;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TextView textName, textReputation, textSilver, textBronze, textGold;
    private ImageView imageUser;
    private UserItem user;

    public static Intent getStartIntent(Context context, UserItem user) {
        Intent i = new Intent(context, UserTabActivity.class);
        i.putExtra(EXTRA_USER, user);
        return i;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_tab);

        Intent i = getIntent();
        user = i.getParcelableExtra(EXTRA_USER);

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

        getSupportActionBar().setTitle(getResources().getString(R.string.tab_activity_title));
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(ContextCompat.getColor(UserTabActivity.this, R.color.colorTabTitle));

        link = user.getLink();
        BuzzItem buzzDetail = user.getBadgeCounts();

        textName.setTextColor(ContextCompat.getColor(UserTabActivity.this, R.color.colorTabTitle));
        textReputation.setTextColor(ContextCompat.getColor(UserTabActivity.this, R.color.colorTabTitle));

        textName.setText(user.getDisplayName());
        textReputation.setText(Utils.getRepString(user.getReputation()));
        textBronze.setText(String.valueOf(buzzDetail.getBronze()));
        textSilver.setText(String.valueOf(buzzDetail.getSilver()));
        textGold.setText(String.valueOf(buzzDetail.getGold()));

        Picasso.with(UserTabActivity.this)
                .load(user.getProfileImage())
                .placeholder(R.drawable.image_background)
                .into(imageUser);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_user_tab, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                UserTabActivity.this.finish();
                break;

            case R.id.open_in_browser:
                Intent intent = new Intent(Intent.ACTION_VIEW)
                        .setData(Uri.parse(link));
                startActivity(intent);
                break;

            case R.id.copy_to_clipboard:
                ClipboardManager clipboard = (ClipboardManager)
                        getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText(EXTRA_LINK, link);
                clipboard.setPrimaryClip(clip);
                ClipData abc = clipboard.getPrimaryClip();
                ClipData.Item item1 = abc.getItemAt(0);
                String text = item1.getText().toString();

                Utils.showToast(getApplicationContext(), text);
                break;

            case R.id.share:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, link);
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent,
                        getResources().getString(R.string.share_dialog_title)));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(UserProfileFragment.newInstance(user.getUserId()),
                getResources().getString(R.string.tab_title_profile));
        adapter.addFragment(UserActivityFragment.newInstance(user.getUserId()),
                getResources().getString(R.string.tab_title_activity));
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
}
