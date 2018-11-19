package ffm.geok.com.presenter;

public interface IDataSynchronizationPresenter {
    void dataSynchronization();

    interface DataSynchronizationCallback {
        /*无需同步*/
        void onNotNecessarySyn();
        /*同步成功*/
        void onSynchronizationSuccess();
        /*同步失败*/
        void onSynchronizationFail(String errorMsg);
    }
}
