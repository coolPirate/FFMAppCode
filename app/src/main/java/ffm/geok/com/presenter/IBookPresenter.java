package ffm.geok.com.presenter;

import android.content.Intent;
import android.view.View;

public interface IBookPresenter {
    void onCreate();

    void onStart();

    void onStop();

    void pause();

    void attachView(View view);

    void attachIncomingIntent(Intent intent);
}
