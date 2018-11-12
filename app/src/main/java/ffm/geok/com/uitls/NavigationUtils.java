package ffm.geok.com.uitls;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.orhanobut.logger.Logger;

import java.util.ArrayList;

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class NavigationUtils implements Application.ActivityLifecycleCallbacks {
    private static final ArrayList<Activity> activities = new ArrayList<>();

    private static class SingletonHolder {
        private static final NavigationUtils INSTANCE = new NavigationUtils();
    }

    private NavigationUtils() {
    }

    public static NavigationUtils getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public Activity getCurrentActivity() {
        Activity activity = null;
        if (!activities.isEmpty()) {
            activity = activities.get(activities.size() - 1);
        }
        return activity;
    }

    public void jumpTo(Class activityClass, Bundle bundle, boolean isFinish) {
        Activity currentActivity = getCurrentActivity();
        if (currentActivity != null) {
            Intent intent = new Intent(currentActivity, activityClass);
            if (bundle != null) {
                intent.putExtras(bundle);
            }
            currentActivity.startActivity(intent);
            if (isFinish) {
                currentActivity.finish();
            }
        }
    }

    public void jumpToForResult(Class activityClass, Bundle bundle, int requestCode) {
        Activity currentActivity = getCurrentActivity();
        if (currentActivity != null) {
            Intent intent = new Intent(currentActivity, activityClass);
            if (bundle != null) {
                intent.putExtras(bundle);
            }
            currentActivity.startActivityForResult(intent,requestCode);
        }
    }

    public void clear() {
        Logger.d("[JK_Bubble] Activity Size: " + activities.size());
        for (int i = activities.size() - 1; i >= 0; i--) {
            Logger.d("[JK_Bubble] Activity Name: " + activities.get(i).getComponentName());
            activities.get(i).finish();
            activities.remove(i);
        }
    }
    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        Logger.i("onActivityCreated");
        activities.add(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {
        Logger.i("onActivityStarted");
    }

    @Override
    public void onActivityResumed(Activity activity) {
        Logger.i("onActivityResumed");
    }

    @Override
    public void onActivityPaused(Activity activity) {
        Logger.i("onActivityPaused");

    }

    @Override
    public void onActivityStopped(Activity activity) {
        Logger.i("onActivityStopped");

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        Logger.i("onActivitySaveInstanceState");
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        Logger.i("onActivityDestroyed");
        activities.remove(activity);
    }
}
