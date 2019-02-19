package ffm.geok.com.ui.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
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
import ffm.geok.com.R;
import ffm.geok.com.adapter.BaseInfoImageAdapter;
import ffm.geok.com.javagen.FireCheckEntityDao;
import ffm.geok.com.javagen.FireMediaEntityDao;
import ffm.geok.com.model.FireCheckEntity;
import ffm.geok.com.model.FireMediaEntity;
import ffm.geok.com.model.ShowImage;
import ffm.geok.com.presenter.DataSynchronizationPresenter;
import ffm.geok.com.presenter.IDataSynchronizationPresenter;
import ffm.geok.com.uitls.ConstantUtils;
import ffm.geok.com.uitls.DBUtils;
import ffm.geok.com.uitls.GlideImageLoader;
import ffm.geok.com.uitls.L;
import ffm.geok.com.uitls.NavigationUtils;
import ffm.geok.com.uitls.StringUtils;
import ffm.geok.com.uitls.ToastUtils;
import ffm.geok.com.uitls.ToolUtils;
import ffm.geok.com.widget.dialog.DialogUtils;

import static com.lzy.imagepicker.ImagePicker.REQUEST_CODE_PREVIEW;
import static ffm.geok.com.adapter.ProjectVertifyAdapter.Multi_Media_IMAGES;
import static ffm.geok.com.ui.activity.ShowImagesActivity.REQUEST_CODE_SELECT;
import static ffm.geok.com.ui.activity.ShowImagesActivity.SHOW_IMGES;
import static ffm.geok.com.ui.activity.ShowImagesActivity.maxImgCount;

public class projectVertifyActivity extends AppCompatActivity {

    @BindView(R.id.edt_multext)
    EditText edtMultext;
    @BindView(R.id.imgs_recyclerview)
    RecyclerView imgsRecyclerview;
    /*@BindView(R.id.btn_upload)
    TextView btnUpload;*/
    @BindView(R.id.confirmor)
    EditText confirmor;
    @BindView(R.id.switch_button)
    Switch switchButton;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private BaseInfoImageAdapter imageAdapter = null;
    private RxPermissions mRxPermissions;
    private Context mContext = projectVertifyActivity.this;
    private String imageFilesName = "";//保存拍照图片名字
    private ArrayList<ImageItem> images;
    private final String split_flag = "___";
    private final String imagesOnSaveInstanceState = "takePhotos";    //保存拍照图片名字key
    private IDataSynchronizationPresenter dataSynchronizationPresenter;
    private ProgressDialog mProgressDialog = null;

    private FireCheckEntity fireCheckEntity = new FireCheckEntity();

    private String entityId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_project_vertify);
        ButterKnife.bind(this);

        Bundle bundle = getIntent().getExtras();
        entityId = bundle.getString(ConstantUtils.global.ProjectEntityId);

        initView();
        initData();
        initListener();
    }

    private void initData() {
        //默认是火点
        fireCheckEntity.setIsfire("是");
        List<FireCheckEntity> fireCheckEntityList = DBUtils.getInstance().queryAllBySingleWhereConditions(FireCheckEntity.class, FireCheckEntityDao.Properties.Fireid.eq(entityId));
        if (fireCheckEntityList.size() > 0) {
            /*FireCheckEntity fireCheckEntityOld=fireCheckEntityList.get(0);
            if(fireCheckEntityOld.getIsfire()=="是"){
                switchButton.setChecked(false);
            }else {
                switchButton.setChecked(true);
            }
            if(null!=fireCheckEntityOld.getConfirmor()){
                confirmor.setText(fireCheckEntityOld.getConfirmor());
            }
            if(null!=fireCheckEntityOld.getRemark()){
                edtMultext.setText(fireCheckEntityOld.getRemark());
            }*/
        }

        List<FireMediaEntity> fireMediaEntityList = DBUtils.getInstance().queryAllBySingleWhereConditions(FireMediaEntity.class, FireMediaEntityDao.Properties.Fireid.eq(entityId));
        if (fireMediaEntityList.size() > 0) {
            /*File itemFile = null;
            List<File> mediaFiles = new ArrayList<File>();
            for (FireMediaEntity entity:fireMediaEntityList) {
                itemFile = new File(entity.getFpath());
                if (itemFile.exists()) {
                    mediaFiles.add(itemFile);
                }
            }*/

        }
    }

    private void initView() {
        toolbar.setTitle("火点信息核查");
        //布局渲染完了之后，才能setSupportActionBar
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        imageAdapter = new BaseInfoImageAdapter(mContext);
        imgsRecyclerview.setAdapter(imageAdapter);
        imgsRecyclerview.setLayoutManager(new GridLayoutManager(mContext, 3));
        imgsRecyclerview.setHasFixedSize(true);
        /*解决滑动冲突*/
        imgsRecyclerview.setNestedScrollingEnabled(false);


        mRxPermissions = new RxPermissions(this);

        initImagePicker();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 这条表示加载菜单文件，第一个参数表示通过那个资源文件来创建菜单
        // 第二个表示将菜单传入那个对象中。这里我们用Menu传入menu
        // 这条语句一般系统帮我们创建好
        getMenuInflater().inflate(R.menu.save, menu);
        return true;
    }

    // 菜单的监听方法
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_save:
                //保存本地
                assembleProgectData();

                //上传服务器
                mProgressDialog = DialogUtils.getProgressDialog(mContext, "数据同步中...");
                dataSynchronizationPresenter.dataSynchronization();
                ToastUtils.showShortMsg(projectVertifyActivity.this, "上报完成");
                L.i("Upload", "上报完成");

                break;
            default:
                break;
        }
        return true;

    }

    private void initListener() {
        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    fireCheckEntity.setIsfire("否");
                    switchButton.setSwitchTextAppearance(mContext, R.style.s_true);
                } else {
                    fireCheckEntity.setIsfire("是");
                    switchButton.setSwitchTextAppearance(mContext, R.style.s_false);
                }
            }
        });

        imageAdapter.setOnPicClickListener(new BaseInfoImageAdapter.OnPicClickListener() {
            @Override
            public void onPicClick(View v, int position) {
                if (null != imageAdapter && position < imageAdapter.getDataList().size()) {
                    /*图片预览*/
                    ShowImage showImage = new ShowImage();
                    showImage.setCurrentIndex(position);
                    showImage.setSelectIndex(position);
                    showImage.setFiles((ArrayList<File>) imageAdapter.getDataList());
                    showImage.setBrowse(false);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(SHOW_IMGES, showImage);
                    NavigationUtils.getInstance().jumpToForResult(ShowImagesActivity.class, bundle, Multi_Media_IMAGES);
                    return;
                }
                mRxPermissions
                        .request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                        .subscribe(granted -> {
                            if (granted) {
                                ImagePicker.getInstance().setSelectLimit(maxImgCount - imageAdapter.getDataList().size());
                                Intent intent1 = new Intent(mContext, ImageGridActivity.class);
                                /* 如果需要进入选择的时候显示已经选中的图片，
                                 * 详情请查看ImagePickerActivity
                                 * */
//                                        intent1.putExtra(ImageGridActivity.EXTRAS_IMAGES,images);
                                startActivityForResult(intent1, REQUEST_CODE_SELECT);
                            } else {
                                Logger.d("未授权");
                                ToastUtils.showShortMsg(mContext, StringUtils.getResourceString(mContext, R.string.camera_Permissions_tip));
                            }
                        });
            }
        });

        confirmor.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                fireCheckEntity.setConfirmor(String.valueOf(confirmor.getText()));
            }
        });

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

        /*btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //保存本地
                assembleProgectData();

                //上传服务器
                mProgressDialog = DialogUtils.getProgressDialog(mContext, "数据同步中...");
                dataSynchronizationPresenter.dataSynchronization();
                ToastUtils.showShortMsg(projectVertifyActivity.this, "上报完成");
                L.i("Upload", "上报完成");

            }
        });*/

    }

    /**
     * 初始化图片选择器
     */
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

    private void assembleProgectData() {
        String projectPid = ToolUtils.generateUUID();
        fireCheckEntity.setId(projectPid);
        fireCheckEntity.setFireid(entityId);
        fireCheckEntity.setConfirmor(confirmor.getText().toString());
        fireCheckEntity.setModitime(ToolUtils.getSystemDate());
        fireCheckEntity.setRemark(String.valueOf(edtMultext.getText()));
        if (null != fireCheckEntity) {
            DBUtils.getInstance().getmDaoSession().insertOrReplace(fireCheckEntity);
        }

        //保存多媒体
        FireMediaEntity fireMediaEntity = new FireMediaEntity();
        File imageFile = null;
        for (ImageItem imageItem : images) {
            imageFile = new File(imageItem.path);
            fireMediaEntity = new FireMediaEntity();
            fireMediaEntity.setAdcd("");
            fireMediaEntity.setFireid(entityId);
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

    private void upLoadData() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            //添加图片返回
            if (data != null && requestCode == REQUEST_CODE_SELECT) {
                images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                if (images != null && images.size() > 0) {
                    File imageFile = null;
                    for (ImageItem imageItem : images) {
                        imageFile = new File(imageItem.path);
                        imageAdapter.getDataList().add(imageFile);
                    }
                    imageAdapter.notifyDataSetChanged();
                }
            }
        } else if (resultCode == ImagePicker.RESULT_CODE_BACK) {
            //预览图片返回
            if (data != null && requestCode == REQUEST_CODE_PREVIEW) {
                images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_IMAGE_ITEMS);
                if (images != null) {
//                    selImageList.clear();
//                    selImageList.addAll(images);
//                    adapter.setImages(selImageList);
                }
            }
        }
        if (Multi_Media_IMAGES == requestCode) {
            ShowImage showImage = (ShowImage) data.getExtras().get(SHOW_IMGES);
            L.d("showImage = " + showImage);
            if (null != showImage) {
                imageAdapter.setDataList(showImage.getFiles());
                imageAdapter.notifyDataSetChanged();
            }
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
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
                imageFilesName = savedInstanceState.getString(imagesOnSaveInstanceState, "");
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

}
