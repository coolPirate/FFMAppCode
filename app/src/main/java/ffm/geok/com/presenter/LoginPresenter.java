package ffm.geok.com.presenter;

import android.app.Activity;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.lzy.okgo.OkGo;

import ffm.geok.com.R;
import ffm.geok.com.global.XApplication;
import ffm.geok.com.manager.DialogCallback;
import ffm.geok.com.model.LoginModel;
import ffm.geok.com.model.ResponseModelLogin2;
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
                                ResponseModelLogin2 responseModel = Convert.fromJson(responseString,ResponseModelLogin2.class);
                                if (null != responseModel&&responseModel.getResult().equals("true" )){
                                    String adcd=responseModel.getUser().getRefObj().getOffice().getOfficeCode();
                                    LoginModel loginModel = new LoginModel();
                                    loginModel.setAdcd(adcd);
                                    L.d("登录请求22：" + adcd);
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