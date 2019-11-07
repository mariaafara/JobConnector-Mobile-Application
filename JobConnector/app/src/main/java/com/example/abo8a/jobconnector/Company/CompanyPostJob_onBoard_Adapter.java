package com.example.abo8a.jobconnector.Company;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by lenovo on 3/20/2018.
 */

public class CompanyPostJob_onBoard_Adapter extends FragmentPagerAdapter {

    public CompanyPostJob_onBoard_Adapter(FragmentManager fm) {
        super(fm);
    }

    @Override // Returns the fragment to display for that page
    public Fragment getItem(int arg0) {
        switch (arg0) {
            case 0:
                return new CompanyJobCategoryFragment();
            case 1:
                return new CompanyJobDescriptionFragment();
            case 2:
                return new CompanyJobLocationFragment();
            case 3:
                return new CompanyJobExperienceFragment();
            case 4:
                return new CompanyJobLanguagesFragment();
            case 5:
                return new CompanyJobGenderFragment();
            default:
                return null;
        }
    }
//    @Override
//    public void destroyItem(ViewGroup container, int position, Object object) {
//        // TODO Auto-generated method stub
//      //  super.destroyItem(container,position,object);
//    }
    @Override    // Returns total number of pages
    public int getCount() {
        // TODO Auto-generated method stub
        return 6;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "Tab " + (position + 1);
    }



}
