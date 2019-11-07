package com.example.abo8a.jobconnector;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by lenovo on 3/20/2018.
 */

public class Registration_onBoard_Adapter extends FragmentPagerAdapter {

    public Registration_onBoard_Adapter(FragmentManager fm) {
        super(fm);
    }

    @Override // Returns the fragment to display for that page
    public Fragment getItem(int arg0) {
        switch (arg0) {
            case 0:
                return new RegisterFragment1();
            case 1:
                return new RegisterFragment2();
            case 2:
                return new RegisterFragment3();
            //case 3:
               // return new RegisterFragment4();
            default:
                return null;
        }
    }

    @Override    // Returns total number of pages
    public int getCount() {
        // TODO Auto-generated method stub
        return 3;//4
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "Tab " + (position + 1);
    }
}
