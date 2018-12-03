package ffm.geok.com.presenter;

import ffm.geok.com.model.LoginModel;

public interface ILoginPresenter {
    void login(String loginname, String password);

    interface LoginListener {
        void onLogin(LoginModel loginModel);

        void onError(String errMsg);
    }
}
