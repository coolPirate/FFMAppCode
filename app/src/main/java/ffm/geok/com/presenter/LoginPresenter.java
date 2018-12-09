package ffm.geok.com.presenter;

import android.app.Activity;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.lzy.okgo.OkGo;

import java.lang.reflect.Type;

import ffm.geok.com.R;
import ffm.geok.com.global.XApplication;
import ffm.geok.com.manager.DialogCallback;
import ffm.geok.com.model.LoginModel;
import ffm.geok.com.model.ResponseModel;
import ffm.geok.com.model.ResponseModelLogin;
import ffm.geok.com.uitls.ConstantUtils;
import ffm.geok.com.uitls.Convert;
import ffm.geok.com.uitls.L;
import ffm.geok.com.uitls.ServerUrl;

public final class LoginPresenter implements ILoginPresenter {
    private Activity mActivity;
    private LoginListener mListener;

    public LoginPresenter(Activity loginActivity, LoginListener loginCallBack) {
        this.mActivity = loginActivity;
        this.mListener = loginCallBack;
    }

    @Override
    public void login(String loginname, String password) {
        try {
            OkGo.<String>get(ServerUrl.Login)
                    .tag(this)
                    .params(ConstantUtils.global.LOGIN_TURE, "true")
                    .params(ConstantUtils.global.LOGIN_JAX, "json")
                    .params(ConstantUtils.global.LOGIN_NAME, loginname)
                    .params(ConstantUtils.global.LOGIN_PASSWORD, password)
                    .execute(new DialogCallback(mActivity) {

                        @Override
                        public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                            String responseString = response.body();
                            L.d("登录请求成功：" + responseString);
                            try {
                                ResponseModelLogin responseModel = Convert.fromJson(responseString,ResponseModelLogin.class);
                                L.d("登录请求22：" + responseModel.getResult());
                                if (null != responseModel&&responseModel.getResult().equals("true" )){
                                    String adcd=responseModel.getUser().getRefObj().getOffice().getOfficeCode();
                                    LoginModel loginModel = new LoginModel(adcd);
                                    L.d("登录请求22：" + loginModel.getAdcd());
                                    XApplication.getInstance().put(ConstantUtils.global.LoginModel,loginModel);
                                    mListener.onLogin(loginModel);
                                }
                                /*if (null != responseModel && responseModel.isStatus()) {
                                    String responseData = responseModel.getData();
                                    L.d("登录请求成功下发数据：" + responseData);
                                    LoginModel loginModel = Convert.fromJson(responseData, LoginModel.class);

                                    XApplication.getInstance().put(ConstantUtils.global.LoginModel,loginModel);
                                    if(loginModel.getResult()=="true"){
                                        mListener.onLogin(loginModel);
                                    }
                                }*/ else {
                                    mListener.onError("登录失败"+responseModel.getMessage());
                                }
                            } catch (JsonIOException e) {
                                e.printStackTrace();
                                mListener.onError("登录失败"+mActivity.getString(R.string.parsedata_error));
                            } catch (JsonSyntaxException e) {
                                e.printStackTrace();
                                mListener.onError("登录失败"+mActivity.getString(R.string.parsedata_error));
                            }
                        }

                        @Override
                        public void onError(com.lzy.okgo.model.Response<String> response) {
                            super.onError(response);
                            mListener.onError("登录失败");
                        }
                    });

        } catch (Exception e) {
        }
    }
}