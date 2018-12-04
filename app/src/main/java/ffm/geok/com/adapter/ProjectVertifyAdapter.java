package ffm.geok.com.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import ffm.geok.com.R;
import ffm.geok.com.model.InputInfoModel;
import ffm.geok.com.model.ShowImage;
import ffm.geok.com.ui.activity.ShowImagesActivity;
import ffm.geok.com.uitls.ConstantUtils;
import ffm.geok.com.uitls.ImageUtils;
import ffm.geok.com.uitls.NavigationUtils;

import static ffm.geok.com.model.InputInfoModelType.Date;
import static ffm.geok.com.model.InputInfoModelType.INPUT;
import static ffm.geok.com.model.InputInfoModelType.Multi_INPUT;
import static ffm.geok.com.model.InputInfoModelType.Multi_Media;
import static ffm.geok.com.model.InputInfoModelType.RADIO;
import static ffm.geok.com.model.InputInfoModelType.SPINNER;

public class ProjectVertifyAdapter extends BaseRecyclerViewAdapter{
    public static final int Multi_Media_IMAGES = 1;             //多媒体文件
    private static final int INPUTINFO_TYPE_Multi_Media = 2;    //多媒体文件类型
    private LayoutInflater mLayoutInflater;
    public ProjectVertifyAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
        mContext = context;
    }

    // 操作事件
    public interface OnItemOperationListener {
        /**
         * item操作回调
         *
         * @param v
         * @param position
         * @param input
         */
        void OnItemOperation(View v, int position, String input);
    }

    private ProjectVertifyAdapter.OnItemOperationListener onItemOperationListener;
    public void setOnItemOperationListener(ProjectVertifyAdapter.OnItemOperationListener onItemOperationListener) {
        this.onItemOperationListener = onItemOperationListener;
    }

    @Override
    public int getItemViewType(int position) {
        int itemViewType = -1;
        if (getDataList().size() > 0) {
            InputInfoModel infomodel = (InputInfoModel) getDataList().get(position);
            switch (infomodel.getInputType()) {
                case Multi_Media:
                    itemViewType = INPUTINFO_TYPE_Multi_Media;
                    break;
            }
        }
        return itemViewType == -1 ? super.getItemViewType(position) : itemViewType;
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        View view = null;
        switch (viewType) {
            case INPUTINFO_TYPE_Multi_Media:
                view = mLayoutInflater.inflate(R.layout.layout_inputinfo_type_multimedia, parent, false);
                holder = new ViewHolder_Multi_Media(view);
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        InputInfoModel infomodel = (InputInfoModel) mDataList.get(position);
        switch (getItemViewType(position)) {
            case INPUTINFO_TYPE_Multi_Media:
                ((ViewHolder_Multi_Media) holder).tv_label.setText(infomodel.getLable());
                if (null != infomodel.getMultiMedia()) {
                    Bitmap bitmap = null;
                    ImageView imageViewItem = null;
                    /*每次刷新后清空子view重新添加*/
                    ((ViewHolder_Multi_Media) holder).layout.removeAllViews();
                    ArrayList<File> tempList = (ArrayList<File>) infomodel.getMultiMedia();
                    /**
                     * 多媒体操作
                     * 1、有图标点击图片去预览
                     * 2、没有图片点击新增按钮直接去选择或拍照*/
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ConstantUtils.global.ThumbnailSize, ConstantUtils.global.ThumbnailSize);
                    layoutParams.setMargins(ConstantUtils.global.ThumbnailMargin, ConstantUtils.global.ThumbnailMargin, ConstantUtils.global.ThumbnailMargin, ConstantUtils.global.ThumbnailMargin);
                    if (null != tempList && tempList.size() > 0) {
                        for (int i = 0; i < infomodel.getMultiMedia().size(); i++) {
                            bitmap = ImageUtils.getThumbnail(infomodel.getMultiMedia().get(i).getAbsolutePath(), layoutParams.width, layoutParams.height);
                            imageViewItem = new ImageView(mContext);
                            imageViewItem.setTag(i);
                            imageViewItem.setLayoutParams(layoutParams);
                            imageViewItem.setImageBitmap(bitmap);
                            imageViewItem.setOnClickListener(v -> {
                                ShowImage showImage = new ShowImage();
                                showImage.setCurrentIndex(position);
                                showImage.setSelectIndex((Integer) v.getTag());
                                showImage.setFiles(tempList);
                                showImage.setBrowse(false);
                                Bundle bundle = new Bundle();
                                bundle.putParcelable(ShowImagesActivity.SHOW_IMGES, showImage);
                                NavigationUtils.getInstance().jumpToForResult(ShowImagesActivity.class, bundle, Multi_Media_IMAGES);
                            });
                            ((ViewHolder_Multi_Media) holder).layout.addView(imageViewItem);
                        }
                    }
                    /*末位增加新增按钮*/
                    ImageView addImage = new ImageView(mContext);
                    addImage.setLayoutParams(layoutParams);
                    addImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.selector_image_add));
                    addImage.setOnClickListener(v -> {
                        onItemOperationListener.OnItemOperation(v, position, "add_multi_media");
                    });
                    ((ViewHolder_Multi_Media) holder).layout.addView(addImage);
                }
                break;
        }
    }

    class ViewHolder_Multi_Media extends RecyclerView.ViewHolder {
        TextView tv_label;
        LinearLayout layout;
        ViewHolder_Multi_Media(View itemView) {
            super(itemView);
            LinearLayout linearLayout = (LinearLayout) ((CardView) itemView).getChildAt(0);
            tv_label = (TextView) linearLayout.getChildAt(0);
            layout = (LinearLayout) linearLayout.findViewById(R.id.id_gallery);
        }
    }
}
