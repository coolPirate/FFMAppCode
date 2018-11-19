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

    /*public static boolean isLoadSPData() {
        return getSharedPreferences().getBoolean(ConstantUtils.global.isLoadSPData, false);
    }

    public static void setLoadSPData(boolean loadSPData) {
        getSharedPreferences().edit().putBoolean(ConstantUtils.global.isLoadSPData, loadSPData).commit();
    }*/
}
