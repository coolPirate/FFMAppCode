package ffm.geok.com.manager;

import android.content.Context;

import ffm.geok.com.helper.RetrofitHelper;
import ffm.geok.com.helper.RetrofitService;
import ffm.geok.com.javagen.Book;
import rx.Observable;

public class DataManager {
    private RetrofitService mRetrofitService;
    public DataManager(Context context){
        this.mRetrofitService = RetrofitHelper.getInstance(context).getServer();
    }
    public Observable<Book> getSearchBooks(String name, String tag, int start, int count){
        return mRetrofitService.getSearchBooks(name,tag,start,count);
    }
}
