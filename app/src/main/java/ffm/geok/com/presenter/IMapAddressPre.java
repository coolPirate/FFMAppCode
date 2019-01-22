package ffm.geok.com.presenter;

import java.util.List;

import ffm.geok.com.model.AddressModel;
import ffm.geok.com.model.LatLngEntity;

public interface IMapAddressPre {
    void getAddress(Double lat,Double lon);

    interface AddressCallback {
        /*加载本地回调*/
        void onAddressSuccess(AddressModel addressModel);

        void onError(String errMsg);
    }
}

