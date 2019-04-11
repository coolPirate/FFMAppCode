package ffm.geok.com.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ffm.geok.com.R;
import ffm.geok.com.model.LoginModel;
import ffm.geok.com.presenter.ILoginPresenter;
import ffm.geok.com.presenter.LoginPresenter;
import ffm.geok.com.uitls.ConstantUtils;
import ffm.geok.com.uitls.L;
import ffm.geok.com.uitls.NavigationUtils;
import ffm.geok.com.uitls.SPManager;
import ffm.geok.com.uitls.ToastUtils;
import ffm.geok.com.uitls.ToolUtils;
import ffm.geok.com.widget.editview.CancelEditText;

public class LoginActivity extends AppCompatActivity {


    @BindView(R.id.login_name)
    CancelEditText loginName;
    @BindView(R.id.login_password)
    CancelEditText loginPassword;
    @BindView(R.id.isvisiable_pw)
    ImageView isvisiablePw;
    @BindView(R.id.btn_login)
    TextView btnLogin;
    @BindView(R.id.login_form)
    ScrollView loginForm;
    private int isAutoLogin = 0;
    private ILoginPresenter loginPresenter;
    private String loginname, password;
    private Context mContext = LoginActivity.this;
    private Boolean isShow = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        Bundle bundle = getIntent().getExtras();
        initParms(bundle, savedInstanceState);

    }

    private void initParms(Bundle bundle, Bundle savedInstanceState) {
        if (bundle != null) {
            isAutoLogin = bundle.getInt(ConstantUtils.global.IS_AutoLogin);
        }

        /*String name = SPManager.getUserName();
        String password1=SPManager.getPassword();
        if(name!=null&&password1!=null&&isAutoLogin!=ConstantUtils.global.autoLoginValue){
            L.i("LOGIN",password1);
            loginName.setText(name);
            loginPassword.setText(password1);
            NavigationUtils.getInstance().jumpTo(MainActivity.class, null, true);
        }*/


        loginPresenter = new LoginPresenter(this, new ILoginPresenter.LoginListener() {
            @Override
            public void onLogin(LoginModel loginModel) {

                SharedPreferences.Editor editor = SPManager.getSharedPreferences().edit();
                editor.putString(ConstantUtils.global.LOGIN_NAME, loginname);
                editor.putString(ConstantUtils.global.LOGIN_PASSWORD, password);
                editor.putString(ConstantUtils.RequestTag.ADCD,loginModel.getAdcd());
                SPManager.setADCD(loginModel.getAdcd());
                L.i("LOGIN11",loginModel.getAdcd());
                editor.commit();
                NavigationUtils.getInstance().jumpTo(MainActivity.class, null, true);
            }

            @Override
            public void onError(String errMsg) {
                ToastUtils.showShortMsg(mContext, errMsg);
            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @OnClick({R.id.isvisiable_pw, R.id.btn_login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.isvisiable_pw:
                if (isShow) {
                    isShow = false;
                    isvisiablePw.setImageResource(R.mipmap.icon_password_hide);
                    loginPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                } else {
                    isShow = true;
                    isvisiablePw.setImageResource(R.mipmap.icon_password_show);
                    loginPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }
                break;
            case R.id.btn_login:
                //NavigationUtils.getInstance().jumpTo(MainActivity.class,null,true);
                loginname = loginName.getText().toString();
                if (TextUtils.isEmpty(loginname)) {
                    ToastUtils.showShortMsg(mContext, "请输入用户名");
                    return;
                }
                password = loginPassword.getText().toString();
                if (TextUtils.isEmpty(loginname)) {
                    ToastUtils.showShortMsg(mContext, "请输入登录密码");
                    return;
                }
                loginPresenter.login(loginname, password);
                ToolUtils.hideSoftInput((Activity) mContext);
                break;
        }
    }


}
