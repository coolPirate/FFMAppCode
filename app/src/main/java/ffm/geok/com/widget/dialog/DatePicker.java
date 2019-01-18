package ffm.geok.com.widget.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Calendar;

import ffm.geok.com.R;

/**
 * 日期Picker
 *
 * @author zd
 *
 */
public class DatePicker extends LinearLayout {
    /** 滑动控件 */
    private ScrollerNumberPicker yearPicker;
    private ScrollerNumberPicker monthPicker;
    private ScrollerNumberPicker dayPicker;
    /** 选择监听 */
    private OnSelectingListener onSelectingListener;
    /** 刷新界面 */
    private static final int REFRESH_VIEW = 0x001;
    /** 临时日期 */
    private int tempYearIndex = -1;
    private int tempMonthIndex = -1;
    private int temDayIndex = -1;
    private Context context;
    private ArrayList<String> year_list = new ArrayList<String>();
    private ArrayList<String> month_list = new ArrayList<String>();
    private ArrayList<String> day_list = new ArrayList<String>();
    /*最后返回的时间串*/
    private String date_string;
    private int currYear = 0;
    private int currYearIndex = 0;
    private int currMonth = 0;
    private int currMonthIndex = 0;
    private int currDay = 0;
    private int currDayIndex = 0;

    public DatePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initDate();
    }

    public DatePicker(Context context) {
        super(context);
        this.context = context;
        initDate();
        // TODO Auto-generated constructor stub
    }

    // 初始化时间控件
    private void initDate() {
        Calendar calendar = Calendar.getInstance();
        currYear = calendar.get(Calendar.YEAR); 	//获取当前年份
        currMonth = calendar.get(Calendar.MONTH); 	//获取当前月份
        currDay = calendar.get(Calendar.DATE); 		//获取当前日期
        int yearCount = 0;
        for (int i = 2000; i < 2099; i++) {
            if (currYear == i) {
                currYearIndex = yearCount;
            }
            yearCount++;
            year_list.add(String.valueOf(i)+"年");
        }
        int monthCount = 0;
        for (int j = 1; j < 13; j++) {
            /*月份为0-11，先++*/
            monthCount++;
            if (currMonth == j) {
                currMonthIndex = monthCount;
            }
            month_list.add(String.valueOf(j)+"月");
        }
        int dayCount = 0;
        for (int k = 1; k < 32; k++) {
            if (currDay == k) {
                currDayIndex = dayCount;
            }
            dayCount++;
            day_list.add(String.valueOf(k)+"日");
        }
        System.out.println("year-month-day = " + year_list.get(currYearIndex) +"-"+ month_list.get(currMonthIndex) +"-"+ day_list.get(currDayIndex));
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        LayoutInflater.from(getContext()).inflate(R.layout.date_picker, this);
        // 获取控件引用
        yearPicker = (ScrollerNumberPicker) findViewById(R.id.year);
        yearPicker.setData(year_list);
        yearPicker.setDefault(currYearIndex);
        monthPicker = (ScrollerNumberPicker) findViewById(R.id.month);
        monthPicker.setData(month_list);
        monthPicker.setDefault(currMonthIndex);
        dayPicker = (ScrollerNumberPicker) findViewById(R.id.day);
        dayPicker.setData(day_list);
        dayPicker.setDefault(currDayIndex);
        yearPicker.setOnSelectListener(new ScrollerNumberPicker.OnSelectListener() {

            @Override
            public void endSelect(int id, String text) {
                // TODO Auto-generated method stub
                System.out.println("id-->" + id + "text----->" + text);
                if (text.equals("") || text == null)
                    return;
                tempYearIndex = id;
                Message message = new Message();
                message.what = REFRESH_VIEW;
                handler.sendMessage(message);
            }

            @Override
            public void selecting(int id, String text) {
                // TODO Auto-generated method stub
            }
        });
        monthPicker.setOnSelectListener(new ScrollerNumberPicker.OnSelectListener() {

            @Override
            public void endSelect(int id, String text) {
                // TODO Auto-generated method stub
                System.out.println("id-->" + id + "text----->" + text);
                if (text.equals("") || text == null)
                    return;
                tempMonthIndex = id;
                Message message = new Message();
                message.what = REFRESH_VIEW;
                handler.sendMessage(message);
            }

            @Override
            public void selecting(int id, String text) {
                // TODO Auto-generated method stub
            }
        });
        yearPicker.setOnSelectListener(new ScrollerNumberPicker.OnSelectListener() {

            @Override
            public void endSelect(int id, String text) {
                // TODO Auto-generated method stub
                System.out.println("id-->" + id + "text----->" + text);
                if (text.equals("") || text == null)
                    return;
                tempYearIndex = id;
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


    public String getDateString() {
        return getYearString() + getMonthString() + getDayString();
    }
    public String getYearString() {
        return yearPicker.getSelectedText();
    }
    public String getMonthString() {
        return monthPicker.getSelectedText();
    }
    public String getDayString() {
        return dayPicker.getSelectedText();
    }

    public interface OnSelectingListener {

        public void selected(boolean selected);
    }
}
