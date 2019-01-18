package ffm.geok.com.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import ffm.geok.com.R;
import ffm.geok.com.model.InputInfoModel;
import ffm.geok.com.model.InputInfoModelPattern;
import ffm.geok.com.model.ShowImage;
import ffm.geok.com.ui.activity.ShowImagesActivity;
import ffm.geok.com.uitls.ConstantUtils;
import ffm.geok.com.uitls.ImageUtils;
import ffm.geok.com.uitls.L;
import ffm.geok.com.uitls.NavigationUtils;

/**
 * Created by zhanghs on 2017/9/4.
 * 工程详细信息录入适配器
 */

public class InputDetialAdapter extends BaseRecyclerViewAdapter {
    private static final int INPUTINFO_TYPE_INPUT = 0;          //单输入框类型
    private static final int INPUTINFO_TYPE_SPINNER = 1;        //下拉选择类型
    private static final int INPUTINFO_TYPE_RADIO = 2;          //单选按钮类型
    private static final int INPUTINFO_TYPE_DATE = 3;           //日期选择类型
    private static final int INPUTINFO_TYPE_Multi_Media = 4;    //多媒体文件类型
    private static final int INPUTINFO_TYPE_Multi_INPUT = 5;    //双输入框类型
    public static final int Multi_Media_IMAGES = 6;             //多媒体文件
    private static final int INPUTINFO_TYPE_BUTTON = 7;    //按钮类型
    private static final int INPUTINFO_TYPE_RELOC=8;//重定位


    private LayoutInflater mLayoutInflater;
    public InputDetialAdapter(Context context) {
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

    private OnItemOperationListener onItemOperationListener;

    public void setOnItemOperationListener(OnItemOperationListener onItemOperationListener) {
        this.onItemOperationListener = onItemOperationListener;
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
                case SPINNER:
                    itemViewType = INPUTINFO_TYPE_SPINNER;
                    break;
                case RADIO:
                    itemViewType = INPUTINFO_TYPE_RADIO;
                    break;
                case Date:
                    itemViewType = INPUTINFO_TYPE_DATE;
                    break;
                case Multi_Media:
                    itemViewType = INPUTINFO_TYPE_Multi_Media;
                    break;
                case Multi_INPUT:
                    itemViewType = INPUTINFO_TYPE_Multi_INPUT;
                    break;
                case Button:
                    itemViewType=INPUTINFO_TYPE_RELOC;
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
            case INPUTINFO_TYPE_INPUT:
                view = mLayoutInflater.inflate(R.layout.layout_inputinfo_type_input, parent, false);
                holder = new ViewHolder_INPUT(view);
                break;
            case INPUTINFO_TYPE_SPINNER:
                view = mLayoutInflater.inflate(R.layout.layout_inputinfo_type_spinner, parent, false);
                holder = new ViewHolder_SPINNER(view);
                break;
            case INPUTINFO_TYPE_RADIO:
                view = mLayoutInflater.inflate(R.layout.layout_inputinfo_type_radio, parent, false);
                holder = new ViewHolder_RADIO(view);
                break;
            case INPUTINFO_TYPE_DATE:
                view = mLayoutInflater.inflate(R.layout.layout_inputinfo_type_date, parent, false);
                holder = new ViewHolder_Date(view);
                break;
            case INPUTINFO_TYPE_Multi_Media:
                view = mLayoutInflater.inflate(R.layout.layout_inputinfo_type_multidedia, parent, false);
                holder = new ViewHolder_Multi_Media(view);
                break;
            case INPUTINFO_TYPE_Multi_INPUT:
                view = mLayoutInflater.inflate(R.layout.layout_inputinfo_type_multinput, parent, false);
                holder = new ViewHolder_Multi_INPUT(view);
                break;
            case INPUTINFO_TYPE_BUTTON:
                view = mLayoutInflater.inflate(R.layout.layout_inputinfo_type_button, parent, false);
                holder = new ViewHolder_BUTTON(view);
                break;
            case INPUTINFO_TYPE_RELOC:
                view=mLayoutInflater.inflate(R.layout.layout_inputinfo_type_reloc,parent,false);
                holder=new ViewHolder_Reloc(view);
                break;
        }
        return holder;
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);
        if (holder instanceof ViewHolder_INPUT) {
            if (((ViewHolder_INPUT) holder).ev_input.getTag() instanceof TextWatcher) {
                ((ViewHolder_INPUT) holder).ev_input.removeTextChangedListener((TextWatcher) (((ViewHolder_INPUT) holder).ev_input.getTag()));
            }
        } else if (holder instanceof ViewHolder_RADIO) {
            if (((ViewHolder_RADIO) holder).radioGroup.getTag() instanceof RadioGroup.OnCheckedChangeListener) {
                ((ViewHolder_RADIO) holder).radioGroup.setOnCheckedChangeListener(null);
                ((ViewHolder_RADIO) holder).radioGroup.clearCheck();
            }
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        InputInfoModel infomodel = (InputInfoModel) mDataList.get(position);
        switch (getItemViewType(position)) {
            case INPUTINFO_TYPE_INPUT:
                ((ViewHolder_INPUT) holder).tv_label.setText(infomodel.getLable());
                ((ViewHolder_INPUT) holder).ev_input.setText(infomodel.getInputResultText());
                ((ViewHolder_INPUT) holder).ev_input.setHint(infomodel.getDefaultHint());
                ((ViewHolder_INPUT) holder).ev_input.setText(infomodel.getInputResultText());
                if (InputInfoModelPattern.number.equals(infomodel.getInfoModelPattern())) {
                    ((ViewHolder_INPUT) holder).ev_input.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
                } else {
                    ((ViewHolder_INPUT) holder).ev_input.setInputType(InputType.TYPE_CLASS_TEXT);
                }
                TextWatcher watcher = new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        if (!TextUtils.isEmpty(editable.toString())) {
                            infomodel.setInputResultText(editable.toString());
                        }
                    }
                };
                ((ViewHolder_INPUT) holder).ev_input.addTextChangedListener(watcher);
                ((ViewHolder_INPUT) holder).ev_input.setTag(watcher);
                break;
            case INPUTINFO_TYPE_SPINNER:
                ((ViewHolder_SPINNER) holder).tv_label.setText(infomodel.getLable());
                ((ViewHolder_SPINNER) holder).spinnerText.setHint(infomodel.getDefaultHint());
                ((ViewHolder_SPINNER) holder).spinnerText.setText(infomodel.getSpinnerText());
                ((ViewHolder_SPINNER) holder).spinnerText.setOnClickListener(v -> {
                    onItemOperationListener.OnItemOperation(v, position, ((ViewHolder_SPINNER) holder).spinnerText.getText().toString());
                });
                break;
            case INPUTINFO_TYPE_RADIO:
                ((ViewHolder_RADIO) holder).tv_label.setText(infomodel.getLable());
                if (!TextUtils.isEmpty(infomodel.getRadioText()) && "是".equals(infomodel.getRadioText())) {
                    ((ViewHolder_RADIO) holder).radioButton_yes.setChecked(true);
                } else {
                    ((ViewHolder_RADIO) holder).radioButton_no.setChecked(true);
                }
                RadioGroup.OnCheckedChangeListener checkedChangeListener = (group, checkedId) -> {
                    RadioButton currentCheck = (RadioButton) group.findViewById(checkedId);
                    onItemOperationListener.OnItemOperation(currentCheck, position, currentCheck.getText().toString());
                };
                ((ViewHolder_RADIO) holder).radioGroup.setOnCheckedChangeListener(checkedChangeListener);
                ((ViewHolder_RADIO) holder).radioGroup.setTag(checkedChangeListener);
                break;
            case INPUTINFO_TYPE_DATE:
                ((ViewHolder_Date) holder).tv_label.setText(infomodel.getLable());
                ((ViewHolder_Date) holder).createTimeView.setHint(infomodel.getDefaultHint());
                ((ViewHolder_Date) holder).createTimeView.setOnClickListener(v ->{
                    onItemOperationListener.OnItemOperation(v, position, "");
                    infomodel.setResultDate(((ViewHolder_Date) holder).createTimeView.getText().toString());
                });
                break;
            case INPUTINFO_TYPE_Multi_Media:
                ((ViewHolder_Multi_Media) holder).tv_label.setText(infomodel.getLable());

                if (null != infomodel.getMultiMedia()) {
                    Bitmap bitmap = null;
                    ImageView imageViewItem = null;
                    //每次刷新后清空子view重新添加
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
                    //末位增加新增按钮
                    /*ImageView addImage = new ImageView(mContext);
                    addImage.setLayoutParams(layoutParams);
                    addImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.selector_image_add));
                    addImage.setOnClickListener(v -> {
                        onItemOperationListener.OnItemOperation(v, position, "add_multi_media");
                    });
                    ((ViewHolder_Multi_Media) holder).layout.addView(addImage);*/
                }else {
                    //末位增加新增按钮
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ConstantUtils.global.ThumbnailSize, ConstantUtils.global.ThumbnailSize);
                    layoutParams.setMargins(ConstantUtils.global.ThumbnailMargin, ConstantUtils.global.ThumbnailMargin, ConstantUtils.global.ThumbnailMargin, ConstantUtils.global.ThumbnailMargin);
                    ImageView addImage = new ImageView(mContext);
                    addImage.setLayoutParams(layoutParams);
                    addImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.selector_image_add));
                    addImage.setOnClickListener(v -> {
                        onItemOperationListener.OnItemOperation(v, position, "add_multi_media");
                    });
                    ((ViewHolder_Multi_Media) holder).layout.addView(addImage);
                    }
                break;
            case INPUTINFO_TYPE_Multi_INPUT:
                ((ViewHolder_Multi_INPUT) holder).tv_label.setText(infomodel.getLable());
                ((ViewHolder_Multi_INPUT) holder).tv_longitude.setText(String.valueOf(infomodel.getLatLng().longitude));
                ((ViewHolder_Multi_INPUT) holder).tv_latitude.setText(String.valueOf(infomodel.getLatLng().latitude));
                break;
            case INPUTINFO_TYPE_BUTTON:
                ((ViewHolder_BUTTON) holder).btn_label.setText(infomodel.getLable());
                ((ViewHolder_BUTTON) holder).btn_label.setOnClickListener(v ->{
                    onItemOperationListener.OnItemOperation(v, position, "add_FeesRecords");
                });
                break;
            case INPUTINFO_TYPE_RELOC:
                ((ViewHolder_Reloc) holder).tv_label.setText(infomodel.getLable());
                if(infomodel.getLatLng()==null){
                    ((ViewHolder_Reloc) holder).tv_longitude.setText("");
                    ((ViewHolder_Reloc) holder).tv_latitude.setText("");
                }else {
                    ((ViewHolder_Reloc) holder).tv_longitude.setText(String.valueOf(infomodel.getLatLng().longitude));
                    ((ViewHolder_Reloc) holder).tv_latitude.setText(String.valueOf(infomodel.getLatLng().latitude));
                }
                ((ViewHolder_Reloc) holder).btn_reloc.setOnClickListener(v -> {
                    onItemOperationListener.OnItemOperation(v, position, "map_relocation");
                });
                break;
        }
    }

    class ViewHolder_INPUT extends RecyclerView.ViewHolder {
        TextView tv_label;
        EditText ev_input;

        ViewHolder_INPUT(View itemView) {
            super(itemView);
            LinearLayout linearLayout = (LinearLayout) ((CardView) itemView).getChildAt(0);
            tv_label = (TextView) linearLayout.getChildAt(0);
            ev_input = (EditText) linearLayout.getChildAt(1);
            L.d(itemView.toString());
        }
    }

    class ViewHolder_SPINNER extends RecyclerView.ViewHolder {
        TextView tv_label;
        TextView spinnerText;

        ViewHolder_SPINNER(View itemView) {
            super(itemView);
            LinearLayout linearLayout = (LinearLayout) ((CardView) itemView).getChildAt(0);
            tv_label = (TextView) linearLayout.getChildAt(0);
            spinnerText = (TextView) linearLayout.getChildAt(1);
            ViewHolder_SPINNER.this.setIsRecyclable(true);
        }
    }

    class ViewHolder_RADIO extends RecyclerView.ViewHolder {
        TextView tv_label;
        RadioGroup radioGroup;
        RadioButton radioButton_yes;
        RadioButton radioButton_no;

        ViewHolder_RADIO(View itemView) {
            super(itemView);
            LinearLayout linearLayout = (LinearLayout) ((CardView) itemView).getChildAt(0);
            tv_label = (TextView) linearLayout.getChildAt(0);
            radioGroup = (RadioGroup) linearLayout.getChildAt(1);
            radioButton_yes = (RadioButton) radioGroup.getChildAt(0);
            radioButton_no = (RadioButton) radioGroup.getChildAt(2);
        }
    }

    class ViewHolder_Date extends RecyclerView.ViewHolder {
        TextView tv_label;
        TextView createTimeView;

        ViewHolder_Date(View itemView) {
            super(itemView);
            LinearLayout linearLayout = (LinearLayout) ((CardView) itemView).getChildAt(0);
            tv_label = (TextView) linearLayout.getChildAt(0);
            createTimeView = (TextView) linearLayout.getChildAt(1);
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

    class ViewHolder_BUTTON extends RecyclerView.ViewHolder {
        Button btn_label;

        ViewHolder_BUTTON(View itemView) {
            super(itemView);
            LinearLayout linearLayout = (LinearLayout) ((CardView) itemView).getChildAt(0);
            btn_label=(Button)linearLayout.getChildAt(0);
        }
    }

    class ViewHolder_Reloc extends RecyclerView.ViewHolder {
        TextView tv_label;
        TextView tv_longitude; //经度
        TextView tv_latitude;  //纬度
        AppCompatImageButton btn_reloc;//重定位按钮

        ViewHolder_Reloc(View itemView) {
            super(itemView);
            LinearLayout linearLayout = (LinearLayout) ((CardView) itemView).getChildAt(0);
            tv_label = (TextView) linearLayout.getChildAt(0);
            LinearLayout layout = (LinearLayout) linearLayout.getChildAt(1);
            tv_longitude = (TextView) layout.getChildAt(0);
            tv_latitude = (TextView) layout.getChildAt(1);
            btn_reloc=(AppCompatImageButton)linearLayout.getChildAt(2);
        }
    }
}