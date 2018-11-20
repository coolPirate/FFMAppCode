package ffm.geok.com.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import ffm.geok.com.R;
import ffm.geok.com.base.MySupportActivity;
import ffm.geok.com.model.TabEntity;
import ffm.geok.com.ui.fragment.data.PagerChildFragment;

public class DataListActivity extends MySupportActivity {

    @BindView(R.id.tl_2)
    CommonTabLayout tl2;
    @BindView(R.id.vp_2)
    ViewPager vp2;

    private Context mContext = this;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private String[] mTitles = {"首页", "消息", "联系人", "更多"};
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_list);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initData();
        initViews();
        initListener();

    }

    private void initData(){
        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }
    }

}
