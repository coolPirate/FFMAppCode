package ffm.geok.com.presenter;

import android.app.Activity;
import android.app.Fragment;

import com.amap.api.location.AMapLocation;


public interface IMapLocationPresenter {
    /*初始化地图*/
    void initAMap(Activity activity);
    /*开始定位*/
    void startLocation();
    /*销毁*/
    void onDestroy();

    interface LocationCallBack {
        void onInitFinish();
        /*定位成功回调*/
        void onLocationSuccess(AMapLocation location);
        /*定位异常回调*/
        void onError(String returncon);
    }
}