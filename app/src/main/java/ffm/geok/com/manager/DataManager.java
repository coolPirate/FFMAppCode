package ffm.geok.com.manager;

import android.content.Context;

import ffm.geok.com.helper.RetrofitHelper;
import ffm.geok.com.helper.RetrofitService;
import ffm.geok.com.javagen.Book;
import ffm.geok.com.model.FireDateEntity;
import rx.Observable;

public class DataManager {
    private RetrofitService mRetrofitService;
    public DataManager(Context context){
        this.mRetrofitService = RetrofitHelper.getInstance(context).getServer();
    }
    public Observable<FireDateEntity> getSearchBooks(String startTime, String endTime){
        return mRetrofitService.getSearchFires(startTime,endTime);
    }
}
