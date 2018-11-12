package ffm.geok.com.widget.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import ffm.geok.com.R;

public class DialogUtils {
    private static ProgressDialog progressDialog;

    public interface DialogConfirmCallback {
        void onConfirm(DialogInterface dialog);
    }

    public interface DialogCancelCallback {
        void onCancel(DialogInterface dialog);
    }

    public static void ShowConfirmDialog(Context context, String msg, final DialogConfirmCallback cb) {
        new AlertDialog.Builder(context)
                .setMessage(msg)
                .setPositiveButton(context.getResources().getString(R.string.dialog_confirm),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if (cb != null) {
                                    cb.onConfirm(dialog);
                                }
                            }
                        })
                .setCancelable(false).show();
    }
    public static void ShowTitleConfirmDialog(Context context,String title, String msg, final DialogConfirmCallback cb) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton(context.getResources().getString(R.string.dialog_confirm),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if (cb != null) {
                                    cb.onConfirm(dialog);
                                }
                            }
                        })
                .setCancelable(false).show();
    }
    public static void ShowConfirmCancelDialog(Context context, String title, String msg, String btnLeft_msg, String btnRight_msg, final DialogConfirmCallback confirmCB,
                                               final DialogCancelCallback cancelCB) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton(btnLeft_msg,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if (confirmCB != null) {
                                    confirmCB.onConfirm(dialog);
                                }

                            }
                        })
                .setNegativeButton(btnRight_msg, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (cancelCB != null) {
                            cancelCB.onCancel(dialog);
                        }
                    }
                }).setCancelable(false).show();

    }

    public static void showProgressDlg(Context context, String msg) {
//        context = NavigationController.getInstance().getCurrentActivity();
        if (TextUtils.isEmpty(msg)) {
            msg = context.getResources().getString(R.string.waiting);
        }
        try {
            if(progressDialog != null)
            {
                progressDialog.cancel();
                progressDialog = null;
            }
            progressDialog = new ProgressDialog(context);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMax(100);
            progressDialog.setMessage(msg);
            progressDialog.setCancelable(false);
            progressDialog.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void closeProgressDialog() {
        try {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.cancel();
                progressDialog = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static ProgressDialog getProgressDialog(Context context, String msg) {
        ProgressDialog dlg = null;
        if (msg == null) {
            msg = context.getString(R.string.waiting);
        }
        try {
            dlg = new ProgressDialog(context);
            dlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dlg.setMax(100);
            dlg.setMessage(msg);
            dlg.setCancelable(false);
            dlg.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dlg;
    }
    public static void closeProgressDialogObject(ProgressDialog progressDialog) {
        try {
            if (progressDialog != null) {
                progressDialog.cancel();
                progressDialog = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static Dialog getDialog(Activity activity, int layoutId) {
        Dialog secondsTypeDialog = new Dialog(activity, R.style.transparentFrameWindowStyle);
        secondsTypeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        secondsTypeDialog.setContentView(layoutId);
        Window dialogWindow = secondsTypeDialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER);
        Display d = dialogWindow.getWindowManager().getDefaultDisplay(); // 为获取屏幕宽、高
        lp.width = (int) (d.getWidth() * 0.8); // 高度设置为屏幕的0.8
//        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT; // 高度设置
        lp.height = (int) (d.getHeight() * 0.4); // 高度设置
        dialogWindow.setAttributes(lp); // 设置生效
        if (!activity.isFinishing()) {
            secondsTypeDialog.show();
        }
        secondsTypeDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                dialog.dismiss();
            }
        });
        return secondsTypeDialog;
    }
}
