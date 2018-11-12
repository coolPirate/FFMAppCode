package ffm.geok.com.uitls;

import android.content.Context;
import android.text.TextUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class StringUtils {
    /**
     * 根据资源id获取指定字符串
     * @param context 上下文
     * @param rid   资源id
     * @return
     */
    public static String getResourceString(Context context, int rid) {
        return context.getResources().getString(rid);
    }
    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobiles) {
        /*
         * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
         * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
         * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
         */
        String telRegex = "[1][34578]\\d{9}";// "[1]"代表第1位为数字1，"[3458]"代表第二位可以为3、4,5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobiles))
            return false;
        else
            return mobiles.matches(telRegex);
    }
    /**
     * 对卡号进行处理，使其只显示前六位、后四位，其余数字显示成*
     *
     * @param card_number
     * @return 处理后的卡号
     */
    public static String disposeCardNumber(String card_number) {
        if (card_number != null && !"".equals(card_number)) {
            String temp = card_number;
            String number_6index = card_number.substring(0, 6);
            String number_4lashIndex = card_number.substring(card_number
                    .length() - 4);
            temp = temp.substring(6, card_number.length() - 4);

            int temp_length = temp.length();
            temp = "";
            for (int i = 0; i < temp_length; i++) {
                temp += "*";
            }

            temp = number_6index + temp + number_4lashIndex;
            return temp;
        } else {
            return null;
        }
    }

    /**
     * double保留两位小数格式化
     * @param vaule
     * @return
     */
    public static String doubleFormat(Double vaule) {
        java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
        return df.format(vaule);
    }


    /**
     * 交易金额格式化精确到分
     * @param amount
     * @return
     */
    public static String AmountFormat(Double amount) {
        DecimalFormat df = new DecimalFormat("######0.00");
        return df.format(amount);
    }
    /**
     * 打印方法调用的队列
     *
     * @param cls
     */
    public static void printStackTrace(Class cls) {
        StackTraceElement[] elements = (new Throwable()).getStackTrace();
        StringBuffer buf = new StringBuffer();
        L.d("Stack for " + cls.getName() + ":");
        for (int i = 0; i < elements.length; i++) {
            L.d("   "
                    + elements[i].getClassName()
                    + "."
                    + elements[i].getMethodName()
                    + "("
                    + elements[i].getFileName()
                    + ":"
                    + elements[i].getLineNumber()
                    + ")");
        }
    }

    public static String isEmptyString(String data) {
        if (TextUtils.isEmpty(data)) {
            return "";
        } else {
            return data.replaceAll(" ", "");
        }
    }

    /**
     * 保留有效位
     * @param num       原数据
     * @param length    保留有效位长度
     * @return
     */
    public static String reservedDecimalPlace(Double num, int length) {
        BigDecimal bigDecimal = new BigDecimal(num);
        double doubleValue = bigDecimal.setScale(length, BigDecimal.ROUND_DOWN).doubleValue();
        return String.valueOf(doubleValue);
    }
}
