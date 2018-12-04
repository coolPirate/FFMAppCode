package ffm.geok.com.ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;
import com.orhanobut.logger.Logger;
import com.suke.widget.SwitchButton;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import ffm.geok.com.R;
import ffm.geok.com.adapter.BaseInfoImageAdapter;
import ffm.geok.com.adapter.ProjectVertifyAdapter;
import ffm.geok.com.model.InputInfoModel;
import ffm.geok.com.model.ShowImage;
import ffm.geok.com.uitls.ConstantUtils;
import ffm.geok.com.uitls.GlideImageLoader;
import ffm.geok.com.uitls.L;
import ffm.geok.com.uitls.NavigationUtils;
import ffm.geok.com.uitls.StringUtils;
import ffm.geok.com.uitls.ToastUtils;

import static com.lzy.imagepicker.ImagePicker.REQUEST_CODE_PREVIEW;
import static ffm.geok.com.adapter.ProjectVertifyAdapter.Multi_Media_IMAGES;
import static ffm.geok.com.ui.activity.ShowImagesActivity.REQUEST_CODE_SELECT;
import static ffm.geok.com.ui.activity.ShowImagesActivity.SHOW_IMGES;
import static ffm.geok.com.ui.activity.ShowImagesActivity.maxImgCount;

public class projectVertifyActivity extends AppCompatActivity {

    @BindView(R.id.switch_button)
    SwitchButton switchButton;
    @BindView(R.id.edt_multext)
    EditText edtMultext;
    @BindView(R.id.imgs_recyclerview)
    RecyclerView imgsRecyclerview;
    @BindView(R.id.btn_upload)
    TextView btnUpload;

    private BaseInfoImageAdapter imageAdapter = null;
    private RxPermissions mRxPermissions;
    private Context mContext=getBaseContext();
    private String imageFilesName = "";//保存拍照图片名字
    private ProjectVertifyAdapter mAdapter;
    private ArrayList<InputInfoModel> sourceData = new ArrayList<InputInfoModel>(); //录入模板
    private ArrayList<ImageItem> images;
    private InputInfoModel currentInputModel;
    private final String split_flag = "___";
    private final String imagesOnSaveInstanceState = "takePhotos";    //保存拍照图片名字key


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_vertify);
        ButterKnife.bind(this);

        initData();
        initView();
        initListener();
    }

    private void initData(){

    }

    private void  initView(){
        switchButton.setChecked(true);
        switchButton.isChecked();
        switchButton.toggle();     //switch state
        switchButton.toggle(false);//switch without animation
        switchButton.setShadowEffect(true);//disable shadow effect
        switchButton.setEnabled(false);//disable button
        switchButton.setEnableEffect(false);//disable the switch animation

        imageAdapter = new BaseInfoImageAdapter(mContext);
        imgsRecyclerview.setAdapter(imageAdapter);
        imgsRecyclerview.setLayoutManager(new GridLayoutManager(mContext, 3));
        imgsRecyclerview.setHasFixedSize(true);
        /*解决滑动冲突*/
        imgsRecyclerview.setNestedScrollingEnabled(false);

        initImagePicker();


        // 初始化自定义的适配器
        mAdapter = new ProjectVertifyAdapter(this);
        // 设置数据源
        mAdapter.setDataList(sourceData);
        // 为mRecyclerView设置适配器
        imgsRecyclerview.setAdapter(mAdapter);
        //初始化ImageAdapter
        imageAdapter = new BaseInfoImageAdapter(mContext);


    }

    private void initListener(){
        switchButton.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                //TODO do your job
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
}
