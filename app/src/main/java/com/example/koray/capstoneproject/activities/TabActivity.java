package com.example.koray.capstoneproject.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.koray.capstoneproject.Adapters.ViewPagerAdapter;
import com.example.koray.capstoneproject.R;
import com.example.koray.capstoneproject.Utils.SetViewPager;

public class TabActivity extends AppCompatActivity {
    public Toolbar toolbar;
    public TabLayout tabLayout;
    private ViewPager viewPager;
    private SetViewPager setViewPager = new SetViewPager();
    private ViewPagerAdapter viewPagerAdapter;

    private void init() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setViewPager.setPager(viewPager, viewPagerAdapter);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);
        init();
    }
}
