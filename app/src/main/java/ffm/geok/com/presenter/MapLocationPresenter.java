package ffm.geok.com.presenter;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.orhanobut.logger.Logger;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

import ffm.geok.com.R;
import ffm.geok.com.global.XApplication;
import ffm.geok.com.uitls.ConstantUtils;
import ffm.geok.com.uitls.SPManager;
import ffm.geok.com.uitls.StringUtils;
import ffm.geok.com.uitls.ToastUtils;
import ffm.geok.com.widget.dialog.DialogUtils;

public final class MapLocationPresenter implements IMapLocationPresenter {
    private static String TAG = MapLocationPresenter.class.getSimpleName();
    private Context mContext;
    private LocationCallBack mCallBack;
    private AMapLocationClient mLocationClient;         //定位发起端
    private AMapLocationClientOption mLocationOption;   //定位参数
    private RxPermissions mRxPermissions;

    public MapLocationPresenter(Context context, LocationCallBack locationCallBack) {
        this.mContext = context;
        this.mCallBack = locationCallBack;
    }


    @Override
    public void initAMap(Activity activity) {
        mRxPermissions = new RxPermissions(activity);
        // 初始化定位
        mLocationClient = new AMapLocationClient(XApplication.getContext());
        // 设置定位回调监听，这里要实现AMapLocationListener接口，AMapLocationListener接口只有onLocationChanged方法可以实现，用于接收异步返回的定位结果，参数是AMapLocation类型。
        mLocationClient.setLocationListener(mAMapLocationListener);

        // 初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        // 设置定位模式为Hight_Accuracy高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        // 设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        // 设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(true);
        // 设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(1000);
        // 设置是否开启缓存
        mLocationOption.setLocationCacheEnable(true);

        // 给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        /*初始化结束,回调页面开始定位*/
        mCallBack.onInitFinish();
    }

    @Override
    public void startLocation() {
        DialogUtils.showProgressDlg(mContext, StringUtils.getResourceString(mContext, R.string.location_ing));
        mRxPermissions
                .request(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(granted -> {
                    if (granted) { // Always true pre-M
                        DialogUtils.showProgressDlg(mContext, StringUtils.getResourceString(mContext, R.string.operation_ing));
                        mLocationClient.startLocation();
                    } else {
                        Logger.d("未授权");
                        ToastUtils.showShortMsg(mContext, StringUtils.getResourceString(mContext, R.string.location_Permissions_tip));
                    }
                });
    }

    @Override
    public void onDestroy() {
        if(null != mLocationClient){
            mLocationClient.onDestroy();
        }
    }

    // 以下为后者的举例：
    AMapLocationListener mAMapLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation location) {
            DialogUtils.closeProgressDialog();
            Log.i(TAG, "onLocationChanged");
            Log.d(TAG, "定位完成!");
            StringBuffer sb = new StringBuffer();
            // errCode等于0代表定位成功，其他的为定位失败，具体的可以参照官网定位错误码说明
            if (location.getErrorCode() == 0) {
                mLocationClient.stopLocation();
//                sb.append("定位成功" + "\n");
                sb.append("定位类型: " + location.getLocationType() + "\n");
                sb.append("经    度    : " + location.getLongitude() + "\n");
                sb.append("纬    度    : " + location.getLatitude() + "\n");

                sb.append("省            : " + location.getProvince() + "\n");
                sb.append("市            : " + location.getCity() + "\n");
                sb.append("区            : " + location.getDistrict() + "\n");
                sb.append("街道    : " + location.getStreet() + "\n");
                sb.append("地    址    : " + location.getAddress() + "\n");

                String tradeAddress = location.getProvince() + "," + location.getCity() + "," + location.getDistrict() + "," + location.getStreet();
                /**记录最后一次省、市、县或区三级定位成功的地址，以备定位获取地址失败时用*/
                if (TextUtils.isEmpty(location.getProvince()) && TextUtils.isEmpty(location.getCity()) && TextUtils.isEmpty(location.getDistrict())) {
                    SPManager.getSharedPreferences().edit().putString("tradeAddress", tradeAddress).commit();
                }
                XApplication.getInstance().put(ConstantUtils.mapLocation.TRADEADDRESS, tradeAddress);
                XApplication.getInstance().put(ConstantUtils.mapLocation.LOCALCITY, location.getCity());
                XApplication.getInstance().put(ConstantUtils.mapLocation.LATITUDE, String.valueOf(location.getLatitude()));
                XApplication.getInstance().put(ConstantUtils.mapLocation.LONTITUDE, String.valueOf(location.getLongitude()));
                mCallBack.onLocationSuccess(location);
            } else {
                DialogUtils.closeProgressDialog();
                // 定位失败
                sb.append("定位失败" + "\n");
                sb.append("错误码:" + location.getErrorCode() + "\n");
                sb.append("错误信息:" + location.getErrorInfo() + "\n");
                sb.append("错误描述:" + location.getLocationDetail() + "\n");
                mCallBack.onError(sb.toString());
                Logger.e(sb.toString());
            }
        }

    };

    public static String sHA1(Context context) {
        try {
            String result = "";
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), PackageManager.GET_SIGNATURES);
            byte[] cert = info.signatures[0].toByteArray();
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] publicKey = md.digest(cert);
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < publicKey.length; i++) {
                String appendString = Integer.toHexString(0xFF & publicKey[i])
                        .toUpperCase(Locale.US);
                if (appendString.length() == 1)
                    hexString.append("0");
                hexString.append(appendString);
                hexString.append(":");
            }
            result = hexString.toString();
            return result.substring(0, result.length() - 1);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
