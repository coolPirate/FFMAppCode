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
import android.view.Menu;
import android.view.MenuItem;
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
    /*@BindView(R.id.btn_vertify)
    TextView btnVertify;*/

    private ArrayList<InputInfoModel> sourceData = null; //录入模板
    private String entityId;
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

        initData();
        initViews();
        initListener();

    }

    @Override
    protected void OnDestory() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 这条表示加载菜单文件，第一个参数表示通过那个资源文件来创建菜单
        // 第二个表示将菜单传入那个对象中。这里我们用Menu传入menu
        // 这条语句一般系统帮我们创建好
        getMenuInflater().inflate(R.menu.check, menu);
        return true;
    }

    // 菜单的监听方法
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_address:
                Bundle bundle = new Bundle();
                bundle.putString(ConstantUtils.global.ProjectEntityId, entityId);
                NavigationUtils.getInstance().jumpTo(projectVertifyActivity.class,bundle,false);
                break;
            default:
                break;
        }
        return true;

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
        toolbar.setTitle("火点详情");

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

        /*btnVertify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString(ConstantUtils.global.ProjectEntityId, entityId);
                NavigationUtils.getInstance().jumpTo(projectVertifyActivity.class,bundle,false);
            }
        });*/

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
