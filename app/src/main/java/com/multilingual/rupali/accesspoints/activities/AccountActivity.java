package com.multilingual.rupali.accesspoints.activities;

import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;

import android.widget.ProgressBar;
import android.widget.TextView;

import com.multilingual.rupali.accesspoints.R;
import com.multilingual.rupali.accesspoints.fragments.SignInFragment;
import com.multilingual.rupali.accesspoints.fragments.SignUpFragment;

public class AccountActivity extends AppCompatActivity implements SignUpFragment.ProgressListener, SignInFragment.ProgressListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    ProgressBar progressBar;
    TextView toolbarTextView;
    TabLayout tabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbarTextView=findViewById(R.id.acc_act_toolbar);
        setSupportActionBar(toolbar);
        toolbarTextView.setText("Account");
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        progressBar=findViewById(R.id.acc_act_progress_bar);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
        tabLayout.setVisibility(View.INVISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.INVISIBLE);
        tabLayout.setVisibility(View.VISIBLE);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    SignInFragment signInFragment=new SignInFragment();
                    return signInFragment;
                case 1:
                    SignUpFragment signUpFragment=new SignUpFragment();
                    return signUpFragment;
                default:
                    return  null;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }


        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                case 0:
                    return "SIGN IN";
                case 1:
                    return "SIGN UP";
                default:
                    return  null;
            }
        }
    }
}
