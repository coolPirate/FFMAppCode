package ffm.geok.com.ui.fragment.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import ffm.geok.com.R;
import ffm.geok.com.adapter.HomeTabFragmentAdapter;
import ffm.geok.com.base.BaseMainFragment;
import ffm.geok.com.ui.view.CustomViewPager;


public class HomeTabFragment extends BaseMainFragment implements Toolbar.OnMenuItemClickListener {


    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.cViewPager)
    CustomViewPager cViewPager;

    public static HomeTabFragment newInstance() {
        return new HomeTabFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_tab, container, false);

        initView(view);

        ButterKnife.bind(this, view);
        return view;
    }

    private void initView(View view) {
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        mToolbar.setTitle(R.string.homeName);
        initToolbarNav(mToolbar, true);
        mToolbar.inflateMenu(R.menu.home);
        mToolbar.setOnMenuItemClickListener(this);



        TabLayout mTabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        CustomViewPager cViewPager=(CustomViewPager)view.findViewById(R.id.cViewPager);
        cViewPager.setScanScroll(false);

        mTabLayout.addTab(mTabLayout.newTab());
        mTabLayout.addTab(mTabLayout.newTab());

        cViewPager.setAdapter(new HomeTabFragmentAdapter(getChildFragmentManager(), getString(R.string.title_map), getString(R.string.title_details)));
        mTabLayout.setupWithViewPager(cViewPager);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }
}
