package ffm.geok.com.presenter;

import android.app.Activity;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import ffm.geok.com.manager.DialogCallback;
import ffm.geok.com.model.AddressAttributes;
import ffm.geok.com.model.AddressModel;
import ffm.geok.com.model.ResponseModelAddress;
import ffm.geok.com.ui.activity.MapLocationActivity;
import ffm.geok.com.uitls.Convert;
import ffm.geok.com.uitls.L;
import ffm.geok.com.uitls.ServerUrl;

public final class MapAddressPre implements IMapAddressPre {
    private Activity mActivity;
    private AddressCallback mAddressCallback;

    public MapAddressPre(Activity activity, AddressCallback addressCallback){
        this.mActivity=activity;
        this.mAddressCallback=addressCallback;
    }
    @Override
    public void getAddress(Double lat, Double lon) {
        String geometry="{{\"x\":"+lat+", \"y\":"+lon+"}}";
        L.i("GEOMETRY",geometry);
        OkGo.<String>get(ServerUrl.Address)
                .tag(this)
                .params("returnGeometry", "false")
                .params("f", "json")
                .params("imageDisplay", "1434,366,96")
                .params("sr", "4326")
                .params("geometry", geometry)
                .params("geometryType", "esriGeometryPoint")
                .params("mapExtent", "111,26,113,24")
                .params("tolerance", "3")
                .params("layers", "all")
                .params("tdsourcetag", "s_pcqq_aiomsg")
                .execute(new DialogCallback(mActivity) {
                    @Override
                    public void onSuccess(Response<String> response) {
                        String responseString = response.body();
                        L.d("test11", "请求成功：" + responseString);
                        ResponseModelAddress responseModel = Convert.fromJson(responseString,ResponseModelAddress.class);
                        if (null != responseModel){
                            AddressAttributes addressAttributes=responseModel.getResults().get(0).getAttributes();
                            AddressModel address=new AddressModel();
                            address.setAdcd(addressAttributes.getAdcd());
                            address.setCity(addressAttributes.getCity());
                            address.setCounty(addressAttributes.getCounty());
                            address.setProviencd(addressAttributes.getProvince());
                            L.e("Address",addressAttributes.getCity());
                            L.e("Address111",address.getCity());
                            mAddressCallback.onAddressSuccess(address);
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        L.e("test", "请求失败：" + response.message());
                    }
                });

    }
}
