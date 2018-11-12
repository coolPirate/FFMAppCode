package ffm.geok.com.ui.fragment.data;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ffm.geok.com.R;
import ffm.geok.com.adapter.DataFragmentAdapter;
import ffm.geok.com.base.BaseMainFragment;


public class DataFragment extends BaseMainFragment {

    public static DataFragment newInstance() {
        return new DataFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_data, container, false);

        initView(view);

        return view;
    }

    private void initView(View view) {
        Toolbar mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        TabLayout mTabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        ViewPager mViewPager = (ViewPager) view.findViewById(R.id.viewPager);

        mToolbar.setTitle(R.string.titl_data);
        initToolbarNav(mToolbar);

        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.time_3h));
        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.time_6h));
        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.time_1d));
        mViewPager.setAdapter(new DataFragmentAdapter(getChildFragmentManager(), getString(R.string.time_3h), getString(R.string.time_6h), getString(R.string.time_1d)));
        mTabLayout.setupWithViewPager(mViewPager);
    }
}
