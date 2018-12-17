package com.softmed.htmr_facility.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import com.softmed.htmr_facility.R;
import com.softmed.htmr_facility.base.AppDatabase;
import com.softmed.htmr_facility.base.BaseActivity;
import com.softmed.htmr_facility.customviews.NonSwipeableViewPager;
import com.softmed.htmr_facility.fragments.TbReferralListFragment;

import static com.softmed.htmr_facility.utils.constants.CHW_TO_FACILITY;
import static com.softmed.htmr_facility.utils.constants.INTERFACILITY;

/**
 * Created by issy on 12/6/17.
 *
 * @issyzac issyzac.iz@gmail.com
 * On Project HFReferralApp
 */

public class TbReferalListActivity extends BaseActivity {

    private TabLayout tabLayout;
    public static NonSwipeableViewPager viewPager;
    private Toolbar toolbar;
    private TextView toolbarTitle;

    AppDatabase database;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_referal_list);
        setupview();

        database = AppDatabase.getDatabase(this);

        viewPager = (NonSwipeableViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
                setupTabIcons();
            }
        });

    }

    private void setupview(){

    }

    public void setupTabIcons() {

        View homeView = getLayoutInflater().inflate(R.layout.custom_tabs, null);
        TextView homeTitle = (TextView) homeView.findViewById(R.id.title_text);
        ImageView iv    = (ImageView) homeView.findViewById(R.id.icon);
        iv.setColorFilter(getResources().getColor(R.color.white));
        Glide.with(this).load(R.mipmap.ic_referals_list).into(iv);
        homeTitle.setText("Rufaa za vituo vya afya");
        tabLayout.getTabAt(0).setCustomView(homeView);

        View newsView = getLayoutInflater().inflate(R.layout.custom_tabs, null);
        TextView newsTitle = (TextView) newsView.findViewById(R.id.title_text);
        newsTitle.setText("Rufaa za chw");
        ImageView iv2    = (ImageView) newsView.findViewById(R.id.icon);
        iv2.setColorFilter(getResources().getColor(R.color.white));
        Glide.with(this).load(R.mipmap.ic_referals_list).into(iv2);
        tabLayout.getTabAt(1).setCustomView(newsView);

    }

    public void setupViewPager(ViewPager viewPager) {
        TbReferalListActivity.ViewPagerAdapter adapter = new TbReferalListActivity.ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(TbReferralListFragment.newInstance(INTERFACILITY), "hf");
        adapter.addFragment(TbReferralListFragment.newInstance(CHW_TO_FACILITY), "chw");
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
//            return mFragmentTitleList.get(position);
            return null;
        }
    }

}
