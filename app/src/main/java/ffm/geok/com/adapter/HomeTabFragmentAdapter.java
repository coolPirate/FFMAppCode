package ffm.geok.com.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import ffm.geok.com.ui.fragment.data.DataListFragment;
import ffm.geok.com.ui.fragment.data.PagerChildFragment;
import ffm.geok.com.ui.fragment.home.HomeTabFragment;
import ffm.geok.com.ui.fragment.home.MapFragment;

public class HomeTabFragmentAdapter extends FragmentPagerAdapter {
    String[] mTitles;

    public HomeTabFragmentAdapter(FragmentManager fm, String... titles) {
        super(fm);
        mTitles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return MapFragment.newInstance();
        } else {
            return DataListFragment.newInstance();
        }
    }

    @Override
    public int getCount() {
        return mTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }
}
