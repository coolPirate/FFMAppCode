package ffm.geok.com.widget.editview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

import ffm.geok.com.R;

import static android.support.v4.content.ContextCompat.getDrawable;

@SuppressLint("AppCompatCustomView")
public class CancelEditText extends EditText implements TextWatcher {
    private Drawable drawable_clear;

    public CancelEditText(Context context) {
        super(context);
        init();
    }

    public CancelEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CancelEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        setClearDrawable();
    }

    @Override
    public void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        setClearDrawable();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_UP) {
            if (getCompoundDrawables()[2] != null) {
                boolean touchable = event.getX() > (getWidth() - getTotalPaddingRight())
                        && (event.getX() < ((getWidth() - getPaddingRight())));

                if (touchable) {
                    this.setText("");
                }
            }
            this.setFocusable(true);
            this.setFocusableInTouchMode(true);
            this.requestFocus();
        }
        return super.onTouchEvent(event);
    }

    private void init() {
        drawable_clear = getCompoundDrawables()[2];
        drawable_clear = getDrawable(getContext(), R.mipmap.icon_clear);
        drawable_clear.setBounds(0, 0,
                (int) (drawable_clear.getIntrinsicWidth() * 0.6),
                (int) (drawable_clear.getIntrinsicHeight() * 0.6));
        setClearDrawable();
        addTextChangedListener(this);
    }

    /**
     * 设置删除图片的显示
     * length(),是TextView自带的方法,判断内容有无
     */
    private void setClearDrawable() {
        if (length() < 1)
            setClearIconVisible(false);
        else
            setClearIconVisible(true);
    }

    /**
     * 设置清除图标的显示与隐藏，调用setCompoundDrawables为EditText绘制上去
     */
    protected void setClearIconVisible(boolean visible) {
        setCompoundDrawables(getCompoundDrawables()[0],
                getCompoundDrawables()[1], visible ? drawable_clear : null,
                getCompoundDrawables()[3]);
    }
}
