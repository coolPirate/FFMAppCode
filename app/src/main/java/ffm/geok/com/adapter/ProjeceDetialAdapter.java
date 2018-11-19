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
import ffm.geok.com.uitls.ConstantUtils;
import ffm.geok.com.uitls.ImageUtils;
import ffm.geok.com.uitls.L;
import ffm.geok.com.uitls.NavigationUtils;

public class ProjeceDetialAdapter extends BaseRecyclerViewAdapter {
    private static final int INPUTINFO_TYPE_INPUT = 0;          //单输入框类型
    private static final int INPUTINFO_TYPE_Multi_Media = 4;    //多媒体文件类型
    private static final int INPUTINFO_TYPE_Multi_INPUT = 5;    //双输入框类型
    private LayoutInflater mLayoutInflater;

    public ProjeceDetialAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
        mContext = context;
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }
    @Override
    public int getItemViewType(int position) {
        int itemViewType = -1;
        if (getDataList().size() > 0) {
            InputInfoModel infomodel = (InputInfoModel) getDataList().get(position);
            switch (infomodel.getInputType()) {
                case INPUT:
                    itemViewType = INPUTINFO_TYPE_INPUT;
                    break;
                case Multi_Media:
                    itemViewType = INPUTINFO_TYPE_Multi_Media;
                    break;
                case Multi_INPUT:
                    itemViewType = INPUTINFO_TYPE_Multi_INPUT;
                    break;
            }
        }
        return itemViewType == -1 ? super.getItemViewType(position) : itemViewType;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        View view = null;
        switch (viewType) {
            case INPUTINFO_TYPE_INPUT:
                view = mLayoutInflater.inflate(R.layout.layout_detialinfo_item, parent, false);
                holder = new ViewHolder_INPUT(view);
                break;
            /*case INPUTINFO_TYPE_Multi_Media:
                view = mLayoutInflater.inflate(R.layout.layout_inputinfo_type_multidedia, parent, false);
                holder = new ViewHolder_Multi_Media(view);
                break;
            case INPUTINFO_TYPE_Multi_INPUT:
                view = mLayoutInflater.inflate(R.layout.layout_inputinfo_type_multinput, parent, false);
                holder = new ViewHolder_Multi_INPUT(view);
                break;*/
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        InputInfoModel infomodel = (InputInfoModel) mDataList.get(position);
        if (null != infomodel) {
            switch (getItemViewType(position)) {
                case INPUTINFO_TYPE_INPUT:
                    ((ViewHolder_INPUT) holder).tv_label.setText(infomodel.getLable());
                    ((ViewHolder_INPUT) holder).tv_value.setText(infomodel.getInputResultText());
                    break;
                case INPUTINFO_TYPE_Multi_Media:
                    ((ViewHolder_Multi_Media) holder).tv_label.setText(infomodel.getLable());
                    if (null != infomodel.getMultiMedia()) {
                        Bitmap bitmap = null;
                        ImageView imageViewItem = null;
                        /*每次刷新后清空子view重新添加*/
                        ((ViewHolder_Multi_Media) holder).layout.removeAllViews();
                        ArrayList<File> tempList = (ArrayList<File>) infomodel.getMultiMedia();
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ConstantUtils.global.ThumbnailSize,ConstantUtils.global.ThumbnailSize);
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
                                    showImage.setBrowse(true);
                                    Bundle bundle = new Bundle();
                                    //TODO 图片展示
                                    /*bundle.putParcelable(ShowImagesActivity.SHOW_IMGES, showImage);
                                    NavigationUtils.getInstance().jumpToForResult(ShowImagesActivity.class, bundle, Multi_Media_IMAGES);*/
                                });
                                ((ViewHolder_Multi_Media) holder).layout.addView(imageViewItem);
                            }
                        }
                    }
                    break;
                case INPUTINFO_TYPE_Multi_INPUT:
                    ((ViewHolder_Multi_INPUT) holder).tv_label.setText(infomodel.getLable());
                    ((ViewHolder_Multi_INPUT) holder).tv_longitude.setText(String.valueOf(infomodel.getLatLng().longitude));
                    ((ViewHolder_Multi_INPUT) holder).tv_latitude.setText(String.valueOf(infomodel.getLatLng().latitude));
                    break;
            }
        }
    }

    class ViewHolder_INPUT extends RecyclerView.ViewHolder {
        TextView tv_label;
        TextView tv_value;

        ViewHolder_INPUT(View itemView) {
            super(itemView);
            LinearLayout linearLayout = (LinearLayout) ((CardView) itemView).getChildAt(0);
            tv_label = (TextView) linearLayout.getChildAt(0);
            tv_value = (TextView) linearLayout.getChildAt(1);
            L.d(itemView.toString());
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
    class ViewHolder_Multi_INPUT extends RecyclerView.ViewHolder {
        TextView tv_label;
        TextView tv_longitude; //经度
        TextView tv_latitude;  //纬度

        ViewHolder_Multi_INPUT(View itemView) {
            super(itemView);
            LinearLayout linearLayout = (LinearLayout) ((CardView) itemView).getChildAt(0);
            tv_label = (TextView) linearLayout.getChildAt(0);
            LinearLayout layout = (LinearLayout) linearLayout.getChildAt(1);
            tv_longitude = (TextView) layout.getChildAt(0);
            tv_latitude = (TextView) layout.getChildAt(1);
        }
    }
}
