package ffm.geok.com.uitls;

import android.content.Context;
import android.content.SharedPreferences;

import ffm.geok.com.global.XApplication;

public class SPManager {
    public static SharedPreferences getSharedPreferences() {
        SharedPreferences sharedPreferences = XApplication.getInstance().getSharedPreferences(XApplication.getInstance().getPackageName(), Context.MODE_PRIVATE);
        return sharedPreferences;
    }

    public static String getADCD() {
        return getSharedPreferences().getString(ConstantUtils.RequestTag.ADCD, "");
    }

    public static void setADCD(String ADCD) {
        getSharedPreferences().edit().putString(ConstantUtils.RequestTag.ADCD, ADCD).commit();
    }

    public static String getUserName(){
        return getSharedPreferences().getString(ConstantUtils.global.LOGIN_NAME, "");
    }

    public static void setUserName(String userName) {
        getSharedPreferences().edit().putString(ConstantUtils.global.LOGIN_NAME, userName).commit();
    }

    public static String getPassword(){
        return getSharedPreferences().getString(ConstantUtils.global.LOGIN_PASSWORD, "");
    }

    public static void setPassword(String password) {
        getSharedPreferences().edit().putString(ConstantUtils.global.LOGIN_PASSWORD, password).commit();
    }
}
