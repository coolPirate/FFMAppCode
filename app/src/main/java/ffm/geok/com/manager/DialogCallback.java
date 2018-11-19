package ffm.geok.com.manager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.view.Window;

import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.request.base.Request;

public abstract class DialogCallback extends StringCallback {

    private ProgressDialog dialog;
    private Activity activity;

    private void initDialog(Activity activity) {
        this.activity = activity;
        dialog = new ProgressDialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("请求网络中...");
    }

    public DialogCallback(Activity activity) {
        super();
        initDialog(activity);
    }

    @Override
    public void onStart(Request<String, ? extends Request> request) {
        super.onStart(request);
        //网络请求前显示对话框
        if (!activity.isFinishing()) {
            dialog.show();
        }
    }

    @Override
    public void onFinish() {
        super.onFinish();
        //网络请求结束后关闭对话框
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

}
