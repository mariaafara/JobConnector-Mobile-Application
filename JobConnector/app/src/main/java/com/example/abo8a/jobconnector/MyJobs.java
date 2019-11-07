package com.example.abo8a.jobconnector;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class MyJobs extends Fragment {
    private ViewPager pager;
    private int id;
    private MyJobsViewPagerAdapter adapter;
    public static Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_jobs, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        EmployeeSearchActivity.context=getActivity();
        id = user.id;

        pager = (ViewPager) getActivity().findViewById(R.id.viewpager);
        adapter = new MyJobsViewPagerAdapter(getChildFragmentManager());
        pager.setAdapter(adapter);
        TabLayout tabLayout = (TabLayout) getActivity().findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(pager);

    }


}
