package ffm.geok.com.widget.dialog;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.TimePicker;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ffm.geok.com.R;

public class SelectDateWindow extends PopupWindow {

    @BindView(R.id.btn_selectdate_cancel)
    TextView btnSelectdateCancel;
    @BindView(R.id.btn_selectdate_finish)
    TextView btnSelectdateFinish;
    @BindView(R.id.datepicker)
    public DatePicker datepicker;
    @BindView(R.id.timePicker)
    public TimePicker timePicker;
    private View mMenuView;

    public SelectDateWindow(Activity context, View.OnClickListener listener, DatePicker.OnSelectingListener selectingListener,TimePicker.OnTimeChangedListener timeChangedListener) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.date_select, null);
        // 设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        ButterKnife.bind(this, mMenuView);

        // 取消按钮
        btnSelectdateCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        btnSelectdateFinish.setOnClickListener(listener);
        datepicker.setOnSelectingListener(selectingListener);
        timePicker.setOnTimeChangedListener(timeChangedListener);
        // 设置监听按钮

        timePicker.setIs24HourView(true);

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
        mMenuView.setOnTouchListener(new OnTouchListener() {

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

    @OnClick({R.id.btn_selectdate_cancel, R.id.btn_selectdate_finish})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_selectdate_cancel:
                break;
            case R.id.btn_selectdate_finish:
                break;
        }
    }
}
