package ffm.geok.com.presenter;

import android.app.Activity;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import ffm.geok.com.manager.DialogCallback;
import ffm.geok.com.model.AddressAttributes;
import ffm.geok.com.model.AddressModel;
import ffm.geok.com.model.ResponseModelAddress;
import ffm.geok.com.uitls.Convert;
import ffm.geok.com.uitls.L;
import ffm.geok.com.uitls.ServerUrl;

public class MapDemPre implements IMapDemPre {
    private Activity mActivity;
    private DemCallback mDemCallback;

    public MapDemPre(Activity activity,DemCallback demCallback){
        this.mActivity=activity;
        this.mDemCallback=demCallback;
    }
    @Override
    public void getDem(Double lat, Double lon) {
        String geometry="{{\"x\":"+lat+", \"y\":"+lon+"}}";
        String mapExtent=String.valueOf(lat-0.005)+","
                +String.valueOf(lon-0.005)+","
                +String.valueOf(lat+0.005)+","
                +String.valueOf(lon+0.005);
        L.i("GEOMETRYDEM",geometry);
        OkGo.<String>get(ServerUrl.Dem)
                .tag(this)
                .params("geometry", geometry)
                .params("mapExtent", mapExtent)
                .params("geometryType", "esriGeometryPoint")
                .params("tolerance", "3")
                .params("layers", "all")
                .params("returnGeometry", "false")
                .params("sr", "4326")
                .params("tdsourcetag", "s_pcqq_aiomsg")
                .params("imageDisplay", "1434,366,96")
                .params("returnDistinctValues", "False")
                .params("f", "json")
                .execute(new DialogCallback(mActivity) {
                    @Override
                    public void onSuccess(Response<String> response) {
                        String responseString = response.body();
                        L.d("testDEM", "请求成功：" + responseString);
                        ResponseModelAddress responseModel = Convert.fromJson(responseString,ResponseModelAddress.class);
                        if (null != responseModel){
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        L.e("testDEM", "请求失败：" + response.message());
                    }
                });

    }
}
