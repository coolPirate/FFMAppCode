package ffm.geok.com.widget.dialog;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ffm.geok.com.R;

/**
 * Created by zhanghs on 2017/2/28.
 */

public class SelectOptionsWindow extends PopupWindow {
    @BindView(R.id.btn_optionselect_cancel)
    TextView btnOptionselectCancel;
    @BindView(R.id.btn_optionselect_finish)
    TextView btnOptionselectFinish;
    @BindView(R.id.optionpicker)
    public OptionsPicker optionpicker;
    @BindView(R.id.tv_selectoption_title)
    TextView tvSelectoptionTitle;
    private View mMenuView;

    public SelectOptionsWindow(Activity context, View.OnClickListener listener, OptionsPicker.OnSelectingListener selectingListener) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.options_select_layout, null);
        // 设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        ButterKnife.bind(this, mMenuView);

        // 取消按钮
        btnOptionselectCancel.setOnClickListener(v -> dismiss());
        // 设置监听按钮
        btnOptionselectFinish.setOnClickListener(listener);
        optionpicker.setOnSelectingListener(selectingListener);


        // 设置SelectPicPopupWindow弹出窗体的高和宽
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);

        // 设置SelectPicPopupWindow的弹出窗体可被点击
        this.setFocusable(true);

        // 设置SelectPicPopupWindow的弹出窗体的动画效果
        this.setAnimationStyle(R.style.popwin_anim_style);

        // 实例化一个ColorDrawable的颜色为半透明
        ColorDrawable dw = new ColorDrawable(Color.TRANSPARENT);
        // 设置SelectPicPopupWindow的弹出窗体的背景
        this.setBackgroundDrawable(dw);
        // mMenuView添加OnTouchListener的监听器判断获取触屏位置如果在选择框外面就销毁弹出框
        mMenuView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int height = mMenuView.findViewById(R.id.pop_layout1).getTop();
                // 得到触摸位置的距离底部的高
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });
    }

    public void setSelectOptionTitle(String title) {
        tvSelectoptionTitle.setText("选择" + title);
    }
}
