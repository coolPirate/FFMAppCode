package ffm.geok.com.ui.fragment.home;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.next.easynavigation.view.EasyNavigationBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ffm.geok.com.R;
import ffm.geok.com.base.BaseMainFragment;
import ffm.geok.com.ui.activity.FireAddActivity;
import ffm.geok.com.ui.activity.FireAddActivity2;
import ffm.geok.com.ui.activity.LoginActivity;
import ffm.geok.com.ui.fragment.data.DataListFragment;
import ffm.geok.com.ui.view.CustomViewPager;
import ffm.geok.com.uitls.ConstantUtils;
import ffm.geok.com.uitls.NavigationUtils;
import ffm.geok.com.uitls.SPManager;

import static com.next.easynavigation.view.EasyNavigationBar.*;


public class HomeTabFragment extends BaseMainFragment implements Toolbar.OnMenuItemClickListener {


    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    /*@BindView(R.id.tab_layout)
    TabLayout tabLayout;*/
    @BindView(R.id.cViewPager)
    CustomViewPager cViewPager;
    @BindView(R.id.tab_navigationBar)
    EasyNavigationBar tabNavigationBar;

    private String[] tabText = {"地图", "上报", "数据"};
    private List<Fragment> fragments = new ArrayList<>();
    //未选中icon
    private int[] normalIcon = {R.mipmap.icon_map2, R.mipmap.icon_add, R.mipmap.icon_data2};
    //选中时icon
    private int[] selectIcon = {R.mipmap.icon_map, R.mipmap.icon_add, R.mipmap.icon_data};


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
        mToolbar.inflateMenu(R.menu.main);
        mToolbar.setOnMenuItemClickListener(this);

        fragments.add(MapFragment.newInstance());
        fragments.add(DataListFragment.newInstance());

        tabNavigationBar = view.findViewById(R.id.tab_navigationBar);
        tabNavigationBar.titleItems(tabText)
                .normalIconItems(normalIcon)
                .selectIconItems(selectIcon)
                .fragmentList(fragments)
                .mode(MODE_ADD)
                .fragmentManager(getChildFragmentManager())
                .onTabClickListener(new EasyNavigationBar.OnTabClickListener() {
                    @Override
                    public boolean onTabClickEvent(View view, int position) {
                        if (position == 1) {
                        //return true则拦截事件、不进行页面切换
                            NavigationUtils.getInstance().jumpTo(FireAddActivity2.class,null,false);
                            return true;
                        }
                    return false;
                    }
                })
                .build();






        /*TabLayout mTabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        CustomViewPager cViewPager=(CustomViewPager)view.findViewById(R.id.cViewPager);
        cViewPager.setScanScroll(false);

        mTabLayout.addTab(mTabLayout.newTab());
        mTabLayout.addTab(mTabLayout.newTab());

        cViewPager.setAdapter(new HomeTabFragmentAdapter(getChildFragmentManager(), getString(R.string.title_map), getString(R.string.title_details)));
        mTabLayout.setupWithViewPager(cViewPager);*/

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //null.unbind();

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {


            Dialog alertDialog = new AlertDialog.Builder(getContext()).
                    setTitle("提示").
                    setMessage("您确定注销用户吗？").
                    setPositiveButton("确定", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            Bundle bundle = new Bundle();
                            bundle.putInt(ConstantUtils.global.IS_AutoLogin, ConstantUtils.global.autoLoginValue);
                            NavigationUtils.getInstance().jumpTo(LoginActivity.class, bundle, false);
                            SharedPreferences.Editor editor = SPManager.getSharedPreferences().edit();
                            editor.clear().commit();
                            NavigationUtils.getInstance().jumpTo(LoginActivity.class, bundle, false);
                        }
                    }).
                    setNegativeButton("取消", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            return;
                        }
                    }).
                    create();
            alertDialog.show();
            return true;
        }

        return false;
    }


}
