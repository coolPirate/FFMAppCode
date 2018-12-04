package ffm.geok.com.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;

import ffm.geok.com.R;

public class BaseInfoImageAdapter extends BaseRecyclerViewAdapter<File> {
    private LayoutInflater mLayoutInflater;
    private LinearLayout.LayoutParams photoLayoutParams;


    // 图片的点击事件
    public interface OnPicClickListener {
        void onPicClick(View v, int position);
    }

    private OnPicClickListener mOnPicClickListener;

    public void setOnPicClickListener(OnPicClickListener mOnPicClickListener) {
        this.mOnPicClickListener = mOnPicClickListener;
    }

    public BaseInfoImageAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
        mContext = context;
    }

    @Override
    public int getItemCount() {
        return mDataList.size() + 1;//比集合里的数据多一条，显示自定义的添加图片按钮
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mLayoutInflater.inflate(R.layout.item_image_seclect, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final ViewHolder viewHolder = (ViewHolder) holder;
        final int mPosition = viewHolder.getAdapterPosition();
        if (mPosition == getItemCount() - 1) {//数据的最后一条（显示的添加图片的那个按钮）
            viewHolder.ivPic.setImageDrawable(mContext.getResources().getDrawable(R.drawable.selector_image_add));
            if (mOnPicClickListener != null) {
                setItemListener(viewHolder, mPosition);
            }
        } else {
            File fileImage = mDataList.get(mPosition);
            viewHolder.ivPic.setLayoutParams(photoLayoutParams);
            Glide.with(mContext).load(fileImage).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(viewHolder.ivPic);
            if (mOnPicClickListener != null) {
                setItemListener(viewHolder, mPosition);
            }
        }
    }

    private void setItemListener(ViewHolder viewHolder, int mPosition) {
        viewHolder.ivPic.setOnClickListener(v -> {
            if (null == photoLayoutParams) {
                photoLayoutParams = new LinearLayout.LayoutParams(viewHolder.itemView.getWidth(), viewHolder.itemView.getHeight());
            }
            mOnPicClickListener.onPicClick(v,mPosition);
        });
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivPic;

        ViewHolder(View itemView) {
            super(itemView);
            ivPic = (ImageView) itemView.findViewById(R.id.img_imageSelect_item);
        }
    }
}
