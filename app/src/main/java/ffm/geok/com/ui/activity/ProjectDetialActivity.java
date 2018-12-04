package ffm.geok.com.ui.activity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import ffm.geok.com.R;
import ffm.geok.com.adapter.ProjeceDetialAdapter;
import ffm.geok.com.base.MySupportActivity;
import ffm.geok.com.javagen.FireDateEntityDao;
import ffm.geok.com.manager.DataManager;
import ffm.geok.com.model.FireDateEntity;
import ffm.geok.com.model.InputInfoModel;
import ffm.geok.com.uitls.ConstantUtils;
import ffm.geok.com.uitls.DBUtils;
import ffm.geok.com.uitls.NavigationUtils;

public class ProjectDetialActivity extends MySupportActivity {
    protected Context mContext;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.btn_vertify)
    TextView btnVertify;

    private ArrayList<InputInfoModel> sourceData = null; //录入模板
    private String entityId;
    private FireDateEntity fireDateEntity;
    private ProjeceDetialAdapter mProjeceListAdapter;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_detial);
        ButterKnife.bind(this);
        mContext = this;
        steepStatusBar();

        Bundle bundle = getIntent().getExtras();
        sourceData = bundle.getParcelableArrayList(ConstantUtils.global.ProjectDetial);
        entityId=bundle.getString(ConstantUtils.global.ProjectEntityId);
        fireDateEntity= (FireDateEntity) DBUtils.getInstance().queryAllBySingleWhereConditions(FireDateEntity.class,FireDateEntityDao.Properties.Id.eq(entityId)).get(0);

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

        btnVertify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList(ConstantUtils.global.ProjectDetial, sourceData);
                bundle.putParcelableArrayList(ConstantUtils.global.ProjectVertify, sourceData);
                NavigationUtils.getInstance().jumpTo(projectVertifyActivity.class,bundle,false);
            }
        });

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
