package ffm.geok.com.helper;

import ffm.geok.com.javagen.Book;
import ffm.geok.com.model.FireDateEntity;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface RetrofitService {
    @GET("/getfire?")
    Observable<FireDateEntity> getSearchFires(@Query("st") String name,
                                              @Query("et") String tag);
}
