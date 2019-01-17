package ffm.geok.com.ui.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;
import com.orhanobut.logger.Logger;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ffm.geok.com.R;
import ffm.geok.com.adapter.BaseInfoImageAdapter;
import ffm.geok.com.adapter.InputDetialAdapter;
import ffm.geok.com.adapter.ProjectVertifyAdapter;
import ffm.geok.com.base.MySupportActivity;
import ffm.geok.com.model.FireDateEntity;
import ffm.geok.com.model.FireMediaEntity;
import ffm.geok.com.model.InputInfoModel;
import ffm.geok.com.model.InputInfoModelPattern;
import ffm.geok.com.model.InputInfoModelType;
import ffm.geok.com.model.InputInfoType;
import ffm.geok.com.model.ShowImage;
import ffm.geok.com.model.VerificationType;
import ffm.geok.com.presenter.DataSynchronizationPresenter;
import ffm.geok.com.presenter.IDataSynchronizationPresenter;
import ffm.geok.com.uitls.ConstantUtils;
import ffm.geok.com.uitls.DBUtils;
//import ffm.geok.com.uitls.GlideImageLoader;
import ffm.geok.com.uitls.GlideImageLoader;
import ffm.geok.com.uitls.L;
import ffm.geok.com.uitls.NavigationUtils;
import ffm.geok.com.uitls.SnackbarUtil;
import ffm.geok.com.uitls.StringUtils;
import ffm.geok.com.uitls.ToastUtils;
import ffm.geok.com.uitls.ToolUtils;
import ffm.geok.com.widget.dialog.DialogUtils;


import static ffm.geok.com.adapter.InputDetialAdapter.Multi_Media_IMAGES;
import static ffm.geok.com.ui.activity.ShowImagesActivity.REQUEST_CODE_SELECT;
import static ffm.geok.com.ui.activity.ShowImagesActivity.SHOW_IMGES;
import static ffm.geok.com.ui.activity.ShowImagesActivity.maxImgCount;

public class FireAddActivity extends MySupportActivity implements InputDetialAdapter.OnItemOperationListener,View.OnClickListener{

    @BindView(R.id.toolbar)
    Toolbar mtoolBar;
    @BindView(R.id.input_list)
    RecyclerView inputList;
    @BindView(R.id.btn_submit)
    TextView btnSubmit;
    @BindView(R.id.root_ll)
    LinearLayout rootLl;

    private InputDetialAdapter mAdapter;
    private ArrayList<InputInfoModel> sourceData = new ArrayList<InputInfoModel>(); //录入模板
    private Context mContext;
    private ArrayList<ImageItem> images;
    private BaseInfoImageAdapter imageAdapter=null;
    private String imageFilesName = "";//保存拍照图片名字
    private final String split_flag = "___";
    private final String imagesOnSaveInstanceState = "takePhotos";    //保存拍照图片名字key
    private InputInfoModel currentInfomodel;
    private RxPermissions mRxPermissions;

    private ProgressDialog mProgressDialog = null;
    private IDataSynchronizationPresenter dataSynchronizationPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fire_add);
        ButterKnife.bind(this);
        mContext=this;
        mRxPermissions = new RxPermissions(this);

        initViews();
        initData();


    }

    protected void initViews(){
        initInputIaCEwellsTemplate();

        //布局渲染完了之后，才能setSupportActionBar
        setSupportActionBar(mtoolBar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        mtoolBar.setNavigationOnClickListener(v -> finish());
        mtoolBar.setTitle("火点信息录入");

        // 设置LinearLayoutManager
        inputList.setLayoutManager(new LinearLayoutManager(this));
        // 设置ItemAnimator
        inputList.setItemAnimator(new DefaultItemAnimator());
        // 设置固定大小
        inputList.setHasFixedSize(true);
        // 初始化自定义的适配器
        mAdapter = new InputDetialAdapter(this);
        // 设置数据源
        mAdapter.setDataList(sourceData);
        // 为mRecyclerView设置适配器
        inputList.setAdapter(mAdapter);
        //初始化ImageAdapter
        imageAdapter = new BaseInfoImageAdapter(mContext);

    }

    protected void initData(){
        mAdapter.setOnItemOperationListener(this);
        btnSubmit.setOnClickListener(this);

        initImagePicker();

        dataSynchronizationPresenter = new DataSynchronizationPresenter(this, new IDataSynchronizationPresenter.DataSynchronizationCallback() {
            @Override
            public void onNotNecessarySyn() {
                DialogUtils.closeProgressDialogObject(mProgressDialog);
                ToastUtils.showShortMsg(mContext, "数据无需同步");
            }

            @Override
            public void onSynchronizationSuccess() {
                DialogUtils.closeProgressDialogObject(mProgressDialog);
                ToastUtils.showShortMsg(mContext, "数据同步成功");
                finish();
            }

            @Override
            public void onSynchronizationFail(String errorMsg) {
                DialogUtils.closeProgressDialogObject(mProgressDialog);
                ToastUtils.showShortMsg(mContext, "数据同步失败:" + errorMsg);
                L.e(errorMsg);
            }
        });


    }

    @Override
    protected void OnDestory() {

    }

    private void initInputIaCEwellsTemplate() {
        InputInfoModel infomodel = null;
        infomodel = new InputInfoModel(ConstantUtils.FIRES_LABELS.FINDTIME, InputInfoModelType.INPUT, "请输入发现时间", VerificationType.none, 1, Integer.MAX_VALUE, InputInfoModelPattern.normal);
        sourceData.add(infomodel);
        infomodel = new InputInfoModel(ConstantUtils.FIRES_LABELS.PROVINCE, InputInfoModelType.INPUT, "请输入省份", VerificationType.none, 1, Integer.MAX_VALUE, InputInfoModelPattern.normal);
        sourceData.add(infomodel);
        infomodel = new InputInfoModel(ConstantUtils.FIRES_LABELS.CITY, InputInfoModelType.INPUT, "请输入城市", VerificationType.none, 1, Integer.MAX_VALUE, InputInfoModelPattern.normal);
        sourceData.add(infomodel);
        infomodel = new InputInfoModel(ConstantUtils.FIRES_LABELS.COUNTY, InputInfoModelType.INPUT, "请输入县名称", VerificationType.none, 1, Integer.MAX_VALUE, InputInfoModelPattern.normal);
        sourceData.add(infomodel);
        infomodel = new InputInfoModel(ConstantUtils.FIRES_LABELS.LAT, InputInfoModelType.INPUT, "请输入纬度", VerificationType.none, 1, Integer.MAX_VALUE, InputInfoModelPattern.normal);
        sourceData.add(infomodel);
        infomodel = new InputInfoModel(ConstantUtils.FIRES_LABELS.LON, InputInfoModelType.INPUT, "请输入经度", VerificationType.none, 1, Integer.MAX_VALUE, InputInfoModelPattern.normal);
        sourceData.add(infomodel);
        infomodel = new InputInfoModel(ConstantUtils.FIRES_LABELS.USERNAME, InputInfoModelType.INPUT, "请输入上报人", VerificationType.none, 1, Integer.MAX_VALUE, InputInfoModelPattern.normal);
        sourceData.add(infomodel);
        infomodel = new InputInfoModel(ConstantUtils.FIRES_LABELS.MEDIA, InputInfoModelType.Multi_Media, "请选择多媒体",  VerificationType.request, 0, 0, InputInfoModelPattern.normal);
        sourceData.add(infomodel);
        /*infomodel = new InputInfoModel(ConstantUtils.FIRES_LABELS.TYPE, InputInfoModelType.INPUT, "请输入类型", VerificationType.none, 1, Integer.MAX_VALUE, InputInfoModelPattern.normal);
        infomodel.setInputResultText(StringUtils.isEmptyString(fireDateEntity.getType()));
        sourceData.add(infomodel);*/
        /*infomodel = new InputInfomodel(ConstantUtils.IA_C_MEDIA_LABELS.Media, InputInfoModelType.Multi_Media, "请选择多媒体", null, null, VerificationType.request, 0, 0, InputInfoModelPattern.normal);
        infomodel.setMultiMedia(getMultiMediaByObjID(iaCEwellsEntity.getPid()));
        sourceData.add(infomodel);*/

    }

    @OnClick({R.id.btn_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            /*case R.id.btn_selectdate_finish:
                selectDateWindow.dismiss();
                selectDateString = selectDateWindow.datepicker.getDateString();
                ((TextView) currentSelectView).setText(selectDateString);
                L.d("selectDateString = " + DateUtils.dateStringFormat(selectDateString));
                currentInfomodel.setResultDate(DateUtils.dateStringFormat(selectDateString));
                break;
            case R.id.btn_optionselect_finish:
                selectOptionsWindow.dismiss();
                selectOptionsString = selectOptionsWindow.optionpicker.getOptions_string();
                selectOptionsCode = selectOptionsWindow.optionpicker.getOptions_code_string();
                L.d("selectDateString = " + selectOptionsString + " selectOptionsCode = " + selectOptionsCode);
                currentInfomodel.setSpinnerCode(selectOptionsCode);
                currentInfomodel.setSpinnerText(selectOptionsString);
                mAdapter.notifyDataSetChanged();
                if (isTownship) {
                    SelectTownshipCode = selectOptionsCode;
                    L.d("selectDateString = " + selectOptionsString + " selectOptionsCode = " + selectOptionsCode);
                }
                ((TextView) currentSelectView).setText(selectOptionsString);
                break;*/
            case R.id.btn_submit:
                if (checkVaule()) {
                    ToastUtils.showShortMsg(mContext, "校验输入值");
                    SnackbarUtil.show(view, "校验输入值");
                    assembleProgectData();
                    //上传服务器
                    mProgressDialog = DialogUtils.getProgressDialog(mContext, "数据同步中...");
                    dataSynchronizationPresenter.dataSynchronization();
                    ToastUtils.showShortMsg(this, "上报完成");
                    L.i("Upload", "上报完成");
                }
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        if (null != imageAdapter && imageAdapter.getDataList().size() > 0) {
            String fileName = "";
            for (File file : imageAdapter.getDataList()) {
                fileName = file.getName();
                /*当前文件名不在文件列表名录中则记录，已存在则不做处理*/
                if (imageFilesName.indexOf(fileName) == -1) {
                    imageFilesName += fileName + split_flag;
                    outState.putString(imageFilesName, file.getAbsolutePath());
                    L.d("onSaveInstanceState = " + imageFilesName);
                    outState.putString(imagesOnSaveInstanceState, imageFilesName);
                }
            }
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        try {
            if (savedInstanceState != null) {
                imageFilesName = savedInstanceState.getString(imagesOnSaveInstanceState,"");
                L.d("onRestoreInstanceState = " + imageFilesName);
                if (null == imageAdapter) {
                    imageAdapter = new BaseInfoImageAdapter(mContext);
                } else {
                    imageAdapter.getDataList().clear();
                }
                if (!TextUtils.isEmpty(imageFilesName)) {
                    File imgFile = null;
                    for (String imageName : imageFilesName.split(split_flag)) {
                        L.d("imageName = " + imageName);
                        imgFile = new File(imageName);
                        if (imgFile.exists()) {
                            imageAdapter.getDataList().add(imgFile);
                        }
                    }
                    imageAdapter.notifyDataSetChanged();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 验证录入信息
     *
     * @return
     */
    private boolean checkVaule() {
        if (null != sourceData && sourceData.size() > 0) {
            for (InputInfoModel infomodel : sourceData) {
                switch (infomodel.getInputType()) {
                    case INPUT:
                        /*非空校验*/
                        if (VerificationType.none.equals(infomodel.getVerification())) {
                            return true;
                        } else if (VerificationType.Length.equals(infomodel.getVerification())) {
                            if (infomodel.getInputResultText().length() < infomodel.getMinLength()) {
                                ToastUtils.showShortMsg(mContext, infomodel.getLable() + getString(R.string.error_input_length));
                                return false;
                            }
                        } else if (VerificationType.request.equals(infomodel.getVerification())) {
                            if (TextUtils.isEmpty(infomodel.getInputResultText())) {
                                ToastUtils.showShortMsg(mContext, infomodel.getDefaultHint());
                                return false;
                            }
                        }
                        break;
                    case SPINNER:
                        /*是否必选项判断*/
                        if (TextUtils.isEmpty(infomodel.getSpinnerText()) && VerificationType.request.equals(infomodel.getVerification())) {
                            ToastUtils.showShortMsg(mContext, infomodel.getDefaultHint());
                            return false;
                        }
                        break;
                    case RADIO:
                        if (TextUtils.isEmpty(infomodel.getRadioText()) || "否".equals(infomodel.getRadioText())) {
                            //默认是1
                            infomodel.setRadioText("1");
                        } else {
                            infomodel.setRadioText("0");
                        }
                        break;
                    case Date:
                        /*是否必选项判断*/
                        if (null == infomodel.getResultDate() && VerificationType.request.equals(infomodel.getVerification())) {
                            ToastUtils.showShortMsg(mContext, infomodel.getDefaultHint());
                            return false;
                        }
                        break;
                    case Multi_Media:
                        if (null == infomodel.getMultiMedia() && VerificationType.request.equals(infomodel.getVerification())) {
                            ToastUtils.showShortMsg(mContext, infomodel.getDefaultHint());
                            return false;
                        }
                        break;
                    case Multi_INPUT:
                        break;
                }
            }
        }
        return true;
    }

    private void assembleProgectData() {
        FireDateEntity fireDateEntity = new FireDateEntity();
        String projectPid = ToolUtils.generateUUID();

        if (null != sourceData && sourceData.size() > 0) {
            for (InputInfoModel infomodel : sourceData) {

                switch (infomodel.getLable()) {
                    case ConstantUtils.FIRES_LABELS.FINDTIME:
                        fireDateEntity.setCreateTime(infomodel.getInputResultText());
                        fireDateEntity.setCreateTime(infomodel.getInputResultText());
                        fireDateEntity.setUpdateTime(infomodel.getInputResultText());
                        break;
                    case ConstantUtils.FIRES_LABELS.PROVINCE:
                        fireDateEntity.setProvince(infomodel.getInputResultText());
                        break;
                    case ConstantUtils.FIRES_LABELS.CITY:
                        fireDateEntity.setCity(infomodel.getInputResultText());
                        break;
                    case ConstantUtils.FIRES_LABELS.COUNTY:
                        fireDateEntity.setCounty(infomodel.getInputResultText());
                        break;
                    case ConstantUtils.FIRES_LABELS.USERNAME:
                        fireDateEntity.setUsername(infomodel.getInputResultText());
                        break;
                    case ConstantUtils.FIRES_LABELS.MEDIA:
                        if (null != infomodel.getMultiMedia()) {
                            //保存多媒体
                            FireMediaEntity fireMediaEntity = new FireMediaEntity();
                            File imageFile = null;
                            for (ImageItem imageItem : images) {
                                imageFile = new File(imageItem.path);
                                fireMediaEntity = new FireMediaEntity();
                                fireMediaEntity.setFireid(projectPid);
                                fireMediaEntity.setAdcd("");
                                fireMediaEntity.setObjtp("");
                                fireMediaEntity.setModitime(ToolUtils.getSystemDate());
                                fireMediaEntity.setFname(imageFile.getName());
                                fireMediaEntity.setFpath(imageFile.getAbsolutePath());
                                String takePicTime = imageFile.getName().substring(imageFile.getName().lastIndexOf("_") + 1, imageFile.getName().lastIndexOf("."));
                                fireMediaEntity.setPtime(takePicTime);
                                fireMediaEntity.setPid(ToolUtils.generateUUID());
                                fireMediaEntity.setMultitype(ConstantUtils.global.MultiType_JPG);
                                /*新增本地多媒体记录*/
                                try {
                                    DBUtils.getInstance().getmDaoSession().insertOrReplace(fireMediaEntity);
                                } catch (Exception e) {
                                    L.e(e.toString());
                                }
                                L.d("新增一条多媒体记录成功");
                            }
                        }
                        break;
                }
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == Multi_Media_IMAGES) {
            /*预览图片回调*/
            ShowImage showImage = (ShowImage) data.getExtras().get(SHOW_IMGES);
            L.d("showImage = " + showImage);
            if (null != showImage && showImage.getCurrentIndex() < mAdapter.getDataList().size()) {
                InputInfoModel infomodel = (InputInfoModel) mAdapter.getDataList().get(showImage.getCurrentIndex());
                infomodel.setMultiMedia(showImage.getFiles());
                mAdapter.notifyDataSetChanged();
            }
        } else if (resultCode == ImagePicker.RESULT_CODE_ITEMS && requestCode == REQUEST_CODE_SELECT &&  null != data ) {
            /*选择图片回调*/
            images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
            ArrayList<File> fileArrayList=new ArrayList<File>();
            if (images != null && images.size() > 0) {
                File imageFile = null;
                for (ImageItem imageItem : images) {
                    imageFile = new File(imageItem.path);
                    fileArrayList.add(imageFile);
                    if(null==currentInfomodel.getMultiMedia()){
                        currentInfomodel.setMultiMedia(fileArrayList);
                    }else {
                        currentInfomodel.getMultiMedia().add(imageFile);
                    }
                }
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void OnItemOperation(View v, int position, String input) {

        if (position < sourceData.size()) {
            currentInfomodel = sourceData.get(position);
            L.i("curr",currentInfomodel.getLable());
            switch (currentInfomodel.getInputType()) {
                case INPUT:
                    currentInfomodel = sourceData.get(position);
                    currentInfomodel.setInputResultText(input);
                    break;
                case Multi_Media:
                    currentInfomodel = sourceData.get(position);
                    if ("add_multi_media".equals(input)) {
                        if (null != imageAdapter && position < imageAdapter.getDataList().size()) {
                            /*图片预览*/
                            ShowImage showImage = new ShowImage();
                            showImage.setCurrentIndex(position);
                            showImage.setSelectIndex(position);
                            showImage.setFiles((ArrayList<File>) imageAdapter.getDataList());
                            showImage.setBrowse(false);
                            Bundle bundle = new Bundle();
                            bundle.putParcelable(SHOW_IMGES, showImage);
                            NavigationUtils.getInstance().jumpToForResult(ShowImagesActivity.class, bundle, ProjectVertifyAdapter.Multi_Media_IMAGES);
                            return;
                        }
                        mRxPermissions
                                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                                .subscribe(granted -> {
                                    if (granted) {
                                        Intent intent1 = new Intent(mContext, ImageGridActivity.class);
                                        startActivityForResult(intent1, REQUEST_CODE_SELECT);
                                    } else {
                                        Logger.d("未授权");
                                        ToastUtils.showShortMsg(mContext, StringUtils.getResourceString(mContext, R.string.camera_Permissions_tip));
                                    }
                                });
                    }
                    break;
                case Multi_INPUT:
                    break;
                case Button:
                    currentInfomodel = sourceData.get(position);
                    if("add_FeesRecords".equals(input)){

                    }
                    break;
            }
            mAdapter.notifyDataSetChanged();
        }
    }

    private void initImagePicker() {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());   //设置图片加载器
        imagePicker.setShowCamera(true);                      //显示拍照按钮
        imagePicker.setCrop(true);                           //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(true);                   //是否按矩形区域保存
        imagePicker.setSelectLimit(maxImgCount);              //选中数量限制
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
        imagePicker.setFocusWidth(800);                       //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800);                      //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(1000);                         //保存文件的宽度。单位像素
        imagePicker.setOutPutY(1000);                         //保存文件的高度。单位像素
    }
}
