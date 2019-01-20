package ffm.geok.com.widget.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import ffm.geok.com.R;

/**
 * 单选下拉框Picker
 *
 * @author zd
 *
 */
public class OptionsPicker<T> extends LinearLayout {
    private final Context mContext;
    /** 滑动控件 */
    private ScrollerNumberPicker optionPicker;
    /** 选择监听 */
    private OnSelectingListener onSelectingListener;
    /** 刷新界面 */
    private static final int REFRESH_VIEW = 0x001;
    /** 临时日期 */
    private List<T> listData = null;
    private T options = null;
    private String options_code_string;
    private String options_string;
    private ArrayList<String> option_list = new ArrayList<String>();

    public OptionsPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    public OptionsPicker(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        LayoutInflater.from(getContext()).inflate(R.layout.options_picker, this);
        // 获取控件引用
        optionPicker = (ScrollerNumberPicker) findViewById(R.id.bankPicker);

        optionPicker.setData(option_list);
        optionPicker.setDefault(0);
        optionPicker.setOnSelectListener(new ScrollerNumberPicker.OnSelectListener() {

            @Override
            public void endSelect(int id, String text) {
                // TODO Auto-generated method stub
                System.out.println("id-->" + id + "text----->" + text);
                if (text.equals("") || text == null)
                    return;
                Message message = new Message();
                message.what = REFRESH_VIEW;
                handler.sendMessage(message);
            }

            @Override
            public void selecting(int id, String text) {
                // TODO Auto-generated method stub
            }
        });
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case REFRESH_VIEW:
                    if (onSelectingListener != null)
                        onSelectingListener.selected(true);
                    break;
                default:
                    break;
            }
        }

    };

    public void setOnSelectingListener(OnSelectingListener onSelectingListener) {
        this.onSelectingListener = onSelectingListener;
    }

    /*public String getOptions_code_string() {
        for (int i = 0; i < listData.size(); i++) {
            options = (T) listData.get(i);
            if (options instanceof IaZAdinfoEntity) {
                IaZAdinfoEntity adinfoEntity = (IaZAdinfoEntity) options;
                if (getOptions_string().equals(adinfoEntity.getAdnm())) {
                    options_code_string = (String) adinfoEntity.getAdcd();
                }
            } else if (listData.get(i) instanceof SysEuntlangEntity) {
                SysEuntlangEntity sysEuntlangEntity = (SysEuntlangEntity) listData.get(i);
                if (getOptions_string().equals(sysEuntlangEntity.getNtlang())) {
                    options_code_string = (String) sysEuntlangEntity.getEucd();
                }
            }
        }
        return options_code_string;
    }*/

    public String getOptions_string() {
        options_string = optionPicker.getSelectedText();
        return options_string;
    }

    /*public void setOptionData(List<T> list) {
        L.d("list size = " + list.size());
        listData = new ArrayList<T>();
        option_list = new ArrayList<String>();
        for (int i = 0; i < list.size(); i++) {
            listData.add(list.get(i));
            if (list.get(i) instanceof IaZAdinfoEntity) {
                IaZAdinfoEntity adinfoEntity = (IaZAdinfoEntity) list.get(i);
                option_list.add(adinfoEntity.getAdnm());
            } else if (list.get(i) instanceof SysEuntlangEntity) {
                SysEuntlangEntity sysEuntlangEntity = (SysEuntlangEntity) list.get(i);
                option_list.add(sysEuntlangEntity.getNtlang());
            }
        }
        onFinishInflate();
    }*/

    public interface OnSelectingListener {

        public void selected(boolean selected);
    }
}
