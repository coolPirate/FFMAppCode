package ffm.geok.com.presenter;

import java.util.List;

import ffm.geok.com.model.FireDateEntity;
import ffm.geok.com.model.InputInfoModel;

public interface IProjectPresenter {
    void getFiresList(String adcd);
    void getFiresList(String startTime,String endTime);
    void getFiresList(String adcd, String projectNmae, int pageSize, int pageNumber);
    void saveSampleProjectInfo(InputInfoModel infomodel);

    interface ProjectCallback {
        /*加载本地回调*/
        void onFiresListSuccess(List<FireDateEntity> fireDateEntityList);

        void onFiresListFail(String error);

        void onSaveSampleInfoSucdess();

        void onSaveSampleInfoFail(String error);
        /*加载本地回调*/
        void onFiresListSuccess(String responseString);
    }
}
