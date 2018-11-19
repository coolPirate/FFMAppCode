package ffm.geok.com.ui.fragment.data;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.lang.reflect.Field;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import ffm.geok.com.R;
import ffm.geok.com.adapter.DataFragmentAdapter;
import ffm.geok.com.base.BaseMainFragment;
import q.rorbin.badgeview.QBadgeView;


public class DataFragment extends BaseMainFragment {

    protected Context mContext ;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    Unbinder unbinder;


    public static DataFragment newInstance() {
        return new DataFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_data, container, false);
        mContext= getContext();

        initView(view);

        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    private void initView(View view) {
        Toolbar mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        TabLayout mTabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        ViewPager mViewPager = (ViewPager) view.findViewById(R.id.viewPager);

        mToolbar.setTitle(R.string.title_data);
        initToolbarNav(mToolbar);

        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.time_3h));
        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.time_6h));
        mTabLayout.addTab(mTabLayout.newTab().setText(R.string.time_1d));
        mViewPager.setAdapter(new DataFragmentAdapter(getChildFragmentManager(), getString(R.string.time_3h), getString(R.string.time_6h), getString(R.string.time_1d)));
        mTabLayout.setupWithViewPager(mViewPager);

        QBadgeView badgeView = new QBadgeView(getContext());
        badgeView.bindTarget(getTabView(0));
        badgeView.setBadgeNumber(10);

    }

    public View getTabView(int index) {
        View tabView = null;
        TabLayout.Tab tab = mTabLayout.getTabAt(index);
        Field view = null;
        try {
            view = TabLayout.Tab.class.getDeclaredField("mView");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        view.setAccessible(true);
        try {
            tabView = (View) view.get(tab);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return tabView;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
