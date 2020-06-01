package com.example.sau22rane.goals;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.sau22rane.goals.R;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    String goal;
    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2, R.string.tab_text_3};
    private final Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm, String par) {
        super(fm);
        goal = par;
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0:
                Rituals tab1 = new Rituals(goal);
                return tab1;
            case 1:
                Expectations tab2 = new Expectations(goal);
                return tab2;
            case 2:
                Work_Done tab3 = new Work_Done(goal);
                return tab3;
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return 3;
    }
}