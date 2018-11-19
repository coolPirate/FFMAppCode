package ffm.geok.com.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ffm.geok.com.R;
import ffm.geok.com.listener.OnItemClickListener;
import ffm.geok.com.model.FireDateEntity;
import ffm.geok.com.uitls.ConstantUtils;
import ffm.geok.com.uitls.RxBus;
import ffm.geok.com.model.Message;

public class ProjectListAdapter extends BaseRecyclerViewAdapter {
    private LayoutInflater mInflater;
    private static final int TYPE_ITEM = 0;  //普通Item View
    private static final int TYPE_FOOTER = 1;  //底部FootView
    //加载完毕
    public static final int LOADED_MORE = 3;
    //上拉加载更多
    public static final int PULLUP_LOAD_MORE = 2;
    //正在加载中
    public static final int LOADING_MORE = 1;
    //上拉加载更多状态-默认为0
    private int LOAD_MORE_STATUS = 0;
    private boolean isLoadMore = false;
    private OnItemClickListener onItemClickListener;

    public ProjectListAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * item显示类型
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //进行判断显示类型，来创建返回不同的View
        if (viewType == TYPE_ITEM) {
            View view = mInflater.inflate(R.layout.layout_project_itemview, parent, false);
            return new ItemViewHolder(view);
        } else if (viewType == TYPE_FOOTER) {
            View foot_view = mInflater.inflate(R.layout.layout_recyclerview_loadmore, parent, false);
            return new FootViewHolder(foot_view);
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return mDataList.size() + 1;
    }

    /**
     * 数据的绑定显示
     *
     * @param holder
     * @param position
     */
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            FireDateEntity fireDateEntity = (FireDateEntity) mDataList.get(position);
            ((ItemViewHolder) holder).tvProjectLoc.setText(fireDateEntity.getCounty());
            ((ItemViewHolder) holder).tvProjectTime.setText(fireDateEntity.getCreateTime());
            holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(position,v));
        } else if (holder instanceof FootViewHolder) {
            FootViewHolder footViewHolder = (FootViewHolder) holder;
            switch (LOAD_MORE_STATUS) {
                case PULLUP_LOAD_MORE:
                    footViewHolder.footViewItemTv.setText("上拉加载更多...");
                    if (isLoadMore) {
                        footViewHolder.layoutLoadmore.setVisibility(View.VISIBLE);
                    } else {
                        footViewHolder.layoutLoadmore.setVisibility(View.GONE);
                    }
                    break;
                case LOADING_MORE:
                    footViewHolder.footViewItemTv.setText("正在加载更多数据...");
                    if (isLoadMore) {
                        footViewHolder.layoutLoadmore.setVisibility(View.VISIBLE);
                    } else {
                        footViewHolder.layoutLoadmore.setVisibility(View.GONE);
                    }
                    break;
                case LOADED_MORE:
                    if (isLoadMore) {
                        footViewHolder.layoutLoadmore.setVisibility(View.VISIBLE);
                        footViewHolder.footViewItemTv.setText("加载完毕");
                        new Handler(Looper.getMainLooper()).postDelayed(() -> footViewHolder.layoutLoadmore.setVisibility(View.GONE), 1000);
                        new Handler(Looper.getMainLooper()).postDelayed(() -> {
                            footViewHolder.layoutLoadmore.setVisibility(View.GONE);
                            RxBus.get().post(ConstantUtils.global.RefreshDataStatus,new Message(0,"加载完毕"));
                        },1000);
                        break;
                    }
            }
        }
    }

    /**
     * 进行判断是普通Item视图还是FootView视图
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        // 最后一个item设置为footerView
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    public void setLoadMore(boolean isLoad) {
        this.isLoadMore = isLoad;
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_project_loc)
        TextView tvProjectLoc;
        @BindView(R.id.tv_project_time)
        TextView tvProjectTime;

        public ItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    /**
     * 底部FootView布局
     */
    public static class FootViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.foot_view_item_tv)
        TextView footViewItemTv;
        @BindView(R.id.layout_loadmore)
        LinearLayout layoutLoadmore;

        public FootViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }


    public void addMoreItem(List<FireDateEntity> newDatas) {
        setLoadMore(true);
        mDataList.addAll(newDatas);
        notifyDataSetChanged();
    }

    /**
     * //上拉加载更多
     * PULLUP_LOAD_MORE=0;
     * //正在加载中
     * LOADING_MORE=1;
     * //加载完成已经没有更多数据了
     * NO_MORE_DATA=2;
     *
     * @param status
     */
    public void changeMoreStatus(int status) {
        setLoadMore(true);
        LOAD_MORE_STATUS = status;
        notifyDataSetChanged();
    }
}
