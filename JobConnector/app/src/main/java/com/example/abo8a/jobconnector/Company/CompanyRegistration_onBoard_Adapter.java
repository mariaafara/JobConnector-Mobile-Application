package com.example.abo8a.jobconnector.Company;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by lenovo on 3/20/2018.
 */

public class CompanyRegistration_onBoard_Adapter extends FragmentPagerAdapter {

    public CompanyRegistration_onBoard_Adapter(FragmentManager fm) {
        super(fm);
    }

    @Override // Returns the fragment to display for that page
    public Fragment getItem(int arg0) {
        switch (arg0) {
            case 0:
                return new CompanyRegisterFragment1();
            case 1:
                return new CompanyRegisterFragment2();
            case 2:
                return new CompanyRegisterFragment3();
            default:
                return null;
        }
    }

    @Override    // Returns total number of pages
    public int getCount() {
        // TODO Auto-generated method stub
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "Tab " + (position + 1);
    }
}
