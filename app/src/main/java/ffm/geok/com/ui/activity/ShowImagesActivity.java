package ffm.geok.com.ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.orhanobut.logger.Logger;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ffm.geok.com.R;
import ffm.geok.com.adapter.ImageViewPagerAdapter;
import ffm.geok.com.model.ShowImage;
import ffm.geok.com.uitls.L;
import ffm.geok.com.uitls.StringUtils;
import ffm.geok.com.uitls.ToastUtils;

public class ShowImagesActivity extends AppCompatActivity implements View.OnClickListener{
    public static final String SHOW_IMGES = "SHOW_IMGES";

    @BindView(R.id.tv_indicator)
    TextView tvIndicator;
    @BindView(R.id.tv_add)
    TextView tvAdd;
    @BindView(R.id.tv_delete)
    TextView tvDelete;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.vp_image)
    ViewPager vpImage;

    private Context mContext;
    private ShowImage showImage;
    private int currIndex = -1;
    private ImageViewPagerAdapter imageViewPagerAdapter;
    private RxPermissions mRxPermissions;
    public static final int REQUEST_CODE_SELECT = 100;
    public static final int maxImgCount = 9;
    private ArrayList<ImageItem> images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_images);
        ButterKnife.bind(this);
        this.mContext = this;

        Bundle bundle = getIntent().getExtras();
        initParms(bundle,savedInstanceState);
        initData();
        initViews();
        initListener();
    }

    private void initParms(Bundle bundle, Bundle savedInstanceState) {
        if (null != bundle) {
            showImage = (ShowImage) bundle.get(SHOW_IMGES);
            L.d("showImage = " + showImage.getFiles().size());
        }
    }

    private void initData() {
        if (null != showImage && null != showImage.getFiles()) {
            imageViewPagerAdapter = new ImageViewPagerAdapter(mContext, showImage.getFiles());
            currIndex = showImage.getSelectIndex();
            tvIndicator.setText(String.valueOf(showImage.getSelectIndex()) + "/" + showImage.getFiles().size());
            vpImage.setAdapter(imageViewPagerAdapter);
            vpImage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    currIndex = position;
                    tvIndicator.setText(String.valueOf(position + 1) + "/" + showImage.getFiles().size());//<span style="white-space: pre;">在当前页面滑动至其他页面后，获取position值</span>
                }

                @Override
                public void onPageSelected(int position) {
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
            vpImage.setCurrentItem(currIndex);
            mRxPermissions = new RxPermissions(this);
        }
    }

    private void initViews() {
        //布局渲染完了之后，才能setSupportActionBar
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        if (null != showImage && showImage.isBrowse()) {
            tvDelete.setVisibility(View.GONE);
            tvAdd.setVisibility(View.GONE);
        }
    }

    private void initListener() {
        mToolbar.setNavigationOnClickListener(v -> {
            returnBack();
        });
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        returnBack();
    }

    private void returnBack() {
        Intent result = new Intent();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ShowImagesActivity.SHOW_IMGES, showImage);
        result.putExtras(bundle);
        setResult(RESULT_OK, result);
        finish();
    }

    @OnClick({R.id.tv_delete, R.id.tv_add})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_delete:
                showImage.getFiles().remove(currIndex);
                imageViewPagerAdapter = new ImageViewPagerAdapter(mContext, showImage.getFiles());
                vpImage.setAdapter(imageViewPagerAdapter);
                vpImage.setCurrentItem(currIndex);
                if (0 == showImage.getFiles().size()) {
                    returnBack();
                }
                break;
            case R.id.tv_add:
                mRxPermissions
                        .request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                        .subscribe(granted -> {
                            if (granted) {
                                ImagePicker.getInstance().setSelectLimit(maxImgCount - showImage.getFiles().size());
                                Intent intent1 = new Intent(mContext, ImageGridActivity.class);
                                startActivityForResult(intent1, REQUEST_CODE_SELECT);
                            } else {
                                Logger.d("未授权");
                                ToastUtils.showShortMsg(mContext, StringUtils.getResourceString(mContext, R.string.camera_Permissions_tip));
                            }
                        });
                break;
        }
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
                        showImage.getFiles().add(imageFile);
                    }
                    imageViewPagerAdapter.notifyDataSetChanged();
                }
            }
        }
    }
}
