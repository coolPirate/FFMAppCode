package ffm.geok.com.ui.fragment.data;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ffm.geok.com.R;
import ffm.geok.com.adapter.ProjectListAdapter;
import ffm.geok.com.base.BaseMainFragment;
import ffm.geok.com.listener.OnItemClickListener;
import ffm.geok.com.listener.OnRefreshAndLoadmore;
import ffm.geok.com.model.FireDateEntity;
import ffm.geok.com.model.InputInfoModel;
import ffm.geok.com.model.InputInfoModelPattern;
import ffm.geok.com.model.InputInfoModelType;
import ffm.geok.com.model.Message;
import ffm.geok.com.model.VerificationType;
import ffm.geok.com.presenter.IProjectPresenter;
import ffm.geok.com.presenter.ProjectPresenter;
import ffm.geok.com.ui.activity.ProjectDetialActivity;
import ffm.geok.com.uitls.ConstantUtils;
import ffm.geok.com.uitls.DateUtils;
import ffm.geok.com.uitls.L;
import ffm.geok.com.uitls.NavigationUtils;
import ffm.geok.com.uitls.RxBus;
import ffm.geok.com.uitls.StringUtils;
import ffm.geok.com.uitls.ToastUtils;
import ffm.geok.com.uitls.ToolUtils;
import ffm.geok.com.widget.editview.SearchEditText;
import rx.Observable;


public class DataListFragment extends BaseMainFragment implements OnRefreshAndLoadmore, OnItemClickListener, TextView.OnEditorActionListener {

    @BindView(R.id.tv_project_name)
    SearchEditText tvProjectName;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.mSwiperefreshlayout)
    SwipeRefreshLayout mSwiperefreshlayout;
    @BindView(R.id.ll_empty)
    FrameLayout llEmpty;


    private LinearLayoutManager linearLayoutManager;

    private ProjectListAdapter projectListAdapter;
    private IProjectPresenter projectPresenter;

    protected Context mContext;

    private boolean isRefresh=true;
    private int pageNumber = 0;
    private String queryAdcd = "";
    private int pageSize = 10;
    private String queryProjectName = "";
    //private String _time;
    private int lastVisibleItem = -1;
    private String _startTime = "2018-11-10";
    private String _endTime = "2019-11-19";
    private ArrayAdapter adapter;

    private ArrayList<InputInfoModel> sourceData = new ArrayList<InputInfoModel>(); //录入模板

    private Observable<Message> observable;

    public static DataListFragment newInstance() {
        return new DataListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_data_list, container, false);
        mContext = getContext();
        ButterKnife.bind(this, view);

        initView();
        initData();
        initListener();
        onRefresh();

        return view;
    }

    private void initView() {
        projectListAdapter = new ProjectListAdapter(mContext);
        //防止数据不足一屏,默认不显示加载更多
        projectListAdapter.changeMoreStatus(ProjectListAdapter.LOADED_MORE);
        //设置布局管理器
        linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        //设置adapter
        mRecyclerView.setAdapter(projectListAdapter);
        //设置Item增加、移除动画
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //添加分割线
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        //初始化加载全部
        projectPresenter = new ProjectPresenter(getActivity(), new IProjectPresenter.ProjectCallback() {

            @Override
            public void onFiresListSuccess(List<FireDateEntity> fireDateEntityList) {
                if (isRefresh) {
                    mSwiperefreshlayout.setRefreshing(false);
                    projectListAdapter.setDataList(fireDateEntityList);
                } else {
                    projectListAdapter.addMoreItem(fireDateEntityList);
                    projectListAdapter.changeMoreStatus(ProjectListAdapter.LOADED_MORE);
                }
                projectListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFiresListFail(String error) {
                ToastUtils.showShortMsg(mContext, error);
                if (isRefresh) {
                    mSwiperefreshlayout.setRefreshing(false);
                } else {
                    projectListAdapter.changeMoreStatus(ProjectListAdapter.LOADED_MORE);
                }

            }

            @Override
            public void onSaveSampleInfoSucdess() {

            }

            @Override
            public void onSaveSampleInfoFail(String error) {

            }

            @Override
            public void onFiresListSuccess(String responseString) {

            }
        });
        /*//spinner
        //将可选内容与ArrayAdapter连接起来
        adapter=ArrayAdapter.createFromResource(mContext, R.array.timeSpinner, android.R.layout.simple_spinner_item);
        //adapter=ArrayAdapter.createFromResource(mContext, R.array.timeSpinner, android.R.layout.select_dialog_item);
        //设置下拉列表的风格
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        //将adapter 添加到spinner中
        timeSpinner.setAdapter(adapter);
        //设置默认值
        timeSpinner.setVisibility(View.VISIBLE);
        timeSpinner.setGravity(Gravity.CENTER_HORIZONTAL);*/
    }



    private void initListener() {
        mSwiperefreshlayout.setOnRefreshListener(() -> {
            onRefresh();
        });

        /*//添加事件Spinner事件监听
        //添加事件Spinner事件监听
        timeSpinner.setOnItemSelectedListener(new SpinnerXMLSelectedListener());*/


        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == projectListAdapter.getItemCount()) {
                    onLoadmore();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
            }
        });

        tvProjectName.setOnEditorActionListener(this);

        projectListAdapter.setOnItemClickListener(this);

        observable.subscribe(message -> {
            L.d("code = " + message.getMsgCode() + " content = " + message.getMsgContent());
            if (0 == projectListAdapter.getDataList().size()) {
                llEmpty.setVisibility(View.VISIBLE);
            } else {
                llEmpty.setVisibility(View.GONE);
            }
            onRefresh();
        });

    }

    private void initData() {
        //当前时间
        //_time=DateUtils.Date2String(new Date(),DateUtils.pattern_full);
        tvProjectName.clearFocus();

        observable = RxBus.get().register(ConstantUtils.global.RefreshDataStatus, Message.class);

        onRefresh();
    }


    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        switch (actionId) {
            case EditorInfo.IME_NULL:
                System.out.println("null for default_content: " + v.getText());
                break;
            case EditorInfo.IME_ACTION_SEND:
                System.out.println("action send for email_content: " + v.getText());
                break;
            case EditorInfo.IME_ACTION_DONE:
                System.out.println("action done for number_content: " + v.getText());
                break;
            case EditorInfo.IME_ACTION_SEARCH:
                queryProjectName = tvProjectName.getText().toString();
                //强制隐藏键盘
                ToolUtils.hideSoftInput(getActivity());
                onRefresh();
                break;
        }
        return false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //null.unbind();
    }

    public void onRefresh() {
        isRefresh = true;
        pageNumber = 0;
        projectPresenter.getFiresList(queryAdcd, queryProjectName, pageSize, pageNumber);
        //projectPresenter.getFiresList("2018-11-10", "2019-11-19");
    }

    @Override
    public void onLoadmore() {
        projectListAdapter.changeMoreStatus(ProjectListAdapter.LOADING_MORE);
        pageNumber++;
        isRefresh = false;
        projectPresenter.getFiresList(queryAdcd, queryProjectName, pageSize, pageNumber);
        //projectPresenter.getFiresList(_time,queryAdcd, queryProjectName, pageSize, pageNumber);
    }

    @Override
    public void onItemClick(int position, View view) {
        if (projectListAdapter.getDataList().size() >= position) {
            FireDateEntity fireDateEntity = (FireDateEntity) projectListAdapter.getDataList().get(position);
            if (null != fireDateEntity) {
                sourceData.clear();
                initInputIaCEwellsTemplate(fireDateEntity);
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList(ConstantUtils.global.ProjectDetial, sourceData);
                bundle.putString(ConstantUtils.global.ProjectEntityId, fireDateEntity.getId());
                NavigationUtils.getInstance().jumpTo(ProjectDetialActivity.class, bundle, false);
                //地图更新
                RxBus.get().post("DataSelected",new Message(1000, String.valueOf(fireDateEntity.getLat())+","+String.valueOf(fireDateEntity.getLon())));
            }
        }
    }

    private void initInputIaCEwellsTemplate(FireDateEntity fireDateEntity) {
        InputInfoModel infomodel = null;
        infomodel = new InputInfoModel(ConstantUtils.FIRES_LABELS.CREATETIME, InputInfoModelType.INPUT, "请输入开始时间", VerificationType.none, 1, Integer.MAX_VALUE, InputInfoModelPattern.normal);
        infomodel.setInputResultText(String.valueOf(fireDateEntity.getCreateTime()));
        sourceData.add(infomodel);
        infomodel = new InputInfoModel(ConstantUtils.FIRES_LABELS.FINDTIME, InputInfoModelType.INPUT, "请输入发现时间", VerificationType.none, 1, Integer.MAX_VALUE, InputInfoModelPattern.normal);
        infomodel.setInputResultText(String.valueOf(fireDateEntity.getFindTime()));
        sourceData.add(infomodel);
        infomodel = new InputInfoModel(ConstantUtils.FIRES_LABELS.UPDATETIME, InputInfoModelType.INPUT, "请输入更新时间", VerificationType.none, 1, Integer.MAX_VALUE, InputInfoModelPattern.normal);
        infomodel.setInputResultText(String.valueOf(fireDateEntity.getUpdateTime()));
        sourceData.add(infomodel);
        infomodel = new InputInfoModel(ConstantUtils.FIRES_LABELS.PROVINCE, InputInfoModelType.INPUT, "请输入省份", VerificationType.none, 1, Integer.MAX_VALUE, InputInfoModelPattern.normal);
        infomodel.setInputResultText(StringUtils.isEmptyString(fireDateEntity.getProvince()));
        sourceData.add(infomodel);
        infomodel = new InputInfoModel(ConstantUtils.FIRES_LABELS.CITY, InputInfoModelType.INPUT, "请输入城市", VerificationType.none, 1, Integer.MAX_VALUE, InputInfoModelPattern.normal);
        infomodel.setInputResultText(StringUtils.isEmptyString(fireDateEntity.getCity()));
        sourceData.add(infomodel);
        infomodel = new InputInfoModel(ConstantUtils.FIRES_LABELS.COUNTY, InputInfoModelType.INPUT, "请输入县名称", VerificationType.none, 1, Integer.MAX_VALUE, InputInfoModelPattern.normal);
        infomodel.setInputResultText(StringUtils.isEmptyString(fireDateEntity.getCounty()));
        sourceData.add(infomodel);
        infomodel = new InputInfoModel(ConstantUtils.FIRES_LABELS.LAT, InputInfoModelType.INPUT, "请输入纬度", VerificationType.none, 1, Integer.MAX_VALUE, InputInfoModelPattern.normal);
        infomodel.setInputResultText(StringUtils.isEmptyString(String.valueOf(fireDateEntity.getLat())));
        sourceData.add(infomodel);
        infomodel = new InputInfoModel(ConstantUtils.FIRES_LABELS.LON, InputInfoModelType.INPUT, "请输入经度", VerificationType.none, 1, Integer.MAX_VALUE, InputInfoModelPattern.normal);
        infomodel.setInputResultText(StringUtils.isEmptyString(String.valueOf(fireDateEntity.getLon())));
        sourceData.add(infomodel);
        infomodel = new InputInfoModel(ConstantUtils.FIRES_LABELS.SATELLITE, InputInfoModelType.INPUT, "请输入卫星名称", VerificationType.none, 1, Integer.MAX_VALUE, InputInfoModelPattern.normal);
        infomodel.setInputResultText(StringUtils.isEmptyString(fireDateEntity.getSatellite()));
        sourceData.add(infomodel);
        /*infomodel = new InputInfoModel(ConstantUtils.FIRES_LABELS.TYPE, InputInfoModelType.INPUT, "请输入类型", VerificationType.none, 1, Integer.MAX_VALUE, InputInfoModelPattern.normal);
        infomodel.setInputResultText(StringUtils.isEmptyString(fireDateEntity.getType()));
        sourceData.add(infomodel);*/
        /*infomodel = new InputInfomodel(ConstantUtils.IA_C_MEDIA_LABELS.Media, InputInfoModelType.Multi_Media, "请选择多媒体", null, null, VerificationType.request, 0, 0, InputInfoModelPattern.normal);
        infomodel.setMultiMedia(getMultiMediaByObjID(iaCEwellsEntity.getPid()));
        sourceData.add(infomodel);*/

    }

}
