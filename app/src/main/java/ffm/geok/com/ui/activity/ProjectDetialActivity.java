package ffm.geok.com.ui.activity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import ffm.geok.com.R;
import ffm.geok.com.adapter.ProjeceDetialAdapter;
import ffm.geok.com.base.MySupportActivity;
import ffm.geok.com.model.InputInfoModel;
import ffm.geok.com.uitls.ConstantUtils;

public class ProjectDetialActivity extends MySupportActivity {
    protected Context mContext;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;

    private ArrayList<InputInfoModel> sourceData = null; //录入模板
    private ProjeceDetialAdapter mProjeceListAdapter;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_detial);
        ButterKnife.bind(this);
        mContext=this;
        steepStatusBar();

        Bundle bundle = getIntent().getExtras();
        sourceData = bundle.getParcelableArrayList(ConstantUtils.global.ProjectDetial);

        initData();
        initViews();
        initListener();

    }

    @Override
    protected void OnDestory() {

    }

    /**
     * 初始化参数
     *
     * @return
     */
    private void initParms() {


    }

    /**
     * 初始化数据
     *
     * @return
     */
    private void initData() {
        toolbar.setTitle("工程详情");

        mProjeceListAdapter = new ProjeceDetialAdapter(mContext);
        //设置布局管理器
        linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        if (null != sourceData && !sourceData.isEmpty()) {
            mProjeceListAdapter.setDataList(sourceData);
        }
        mRecyclerView.setLayoutManager(linearLayoutManager);
        //设置adapter
        mRecyclerView.setAdapter(mProjeceListAdapter);
        //设置Item增加、移除动画
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //添加分割线
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));

    }

    /**
     * 初始化视图
     *
     * @return
     */
    private void initViews() {
        //布局渲染完了之后，才能setSupportActionBar
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    /**
     * 初始化视图监听
     *
     * @return
     */
    private void initListener() {

    }

    /**
     * 沉浸状态栏
     */
    private void steepStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

}
