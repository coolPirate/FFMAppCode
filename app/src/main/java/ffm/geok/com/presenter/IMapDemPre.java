package ffm.geok.com.presenter;

import ffm.geok.com.model.AddressModel;

public interface IMapDemPre {
    void getDem(Double lat,Double lon);

    interface DemCallback {
        /*加载本地回调*/
        void onDemSuccess(String dem);

        void onError(String errMsg);
    }
}
