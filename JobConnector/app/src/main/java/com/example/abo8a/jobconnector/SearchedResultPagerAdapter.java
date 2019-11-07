package com.example.abo8a.jobconnector;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by lenovo on 3/31/2018.
 */

public class SearchedResultPagerAdapter extends FragmentPagerAdapter {

        public SearchedResultPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override // Returns the fragment to display for that page
        public Fragment getItem(int arg0) {
            switch (arg0) {
                case 0:
                    return new SearchedJobsFragment();
                case 1:
                    return new SearchedTasksFragment();
                default:
                    return null;
            }
        }

        @Override    // Returns total number of pages
        public int getCount() {
            // TODO Auto-generated method stub
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
//            return mFragmentTitleList.get(position);
            switch (position) {
                case 0:
                    return "Jobs";
                case 1:
                    return "Tasks";

            }

            return null;
        }
    }
