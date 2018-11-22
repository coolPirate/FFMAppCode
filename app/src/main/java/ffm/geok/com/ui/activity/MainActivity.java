package ffm.geok.com.ui.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ffm.geok.com.R;
import ffm.geok.com.base.BaseMainFragment;
import ffm.geok.com.base.MySupportActivity;
import ffm.geok.com.base.MySupportFragment;
import ffm.geok.com.javagen.BaseDao;
import ffm.geok.com.model.FireDateEntity;
import ffm.geok.com.presenter.IProjectPresenter;
import ffm.geok.com.presenter.ProjectPresenter;
import ffm.geok.com.ui.fragment.account.LoginFragment;
import ffm.geok.com.ui.fragment.data.DataFragment;
import ffm.geok.com.ui.fragment.home.HomeFragment;
import ffm.geok.com.ui.fragment.home.HomeTabFragment;
import ffm.geok.com.uitls.ConstantUtils;
import ffm.geok.com.uitls.DBUtils;
import ffm.geok.com.uitls.DateUtils;
import ffm.geok.com.uitls.L;
import ffm.geok.com.uitls.NavigationUtils;
import ffm.geok.com.uitls.SPManager;
import me.yokeyword.fragmentation.ISupportFragment;
import me.yokeyword.fragmentation.SupportFragment;

public class MainActivity extends MySupportActivity
        implements NavigationView.OnNavigationItemSelectedListener, BaseMainFragment.OnFragmentOpenDrawerListener
        , LoginFragment.OnLoginSuccessListener {

    public static final String TAG = MainActivity.class.getSimpleName();

    // 再点一次退出程序时间设置
    private static final long WAIT_TIME = 2000L;
    private long TOUCH_TIME = 0;

    private DrawerLayout mDrawer;
    private NavigationView mNavigationView;
    private TextView mTvName;   // NavigationView上的名字
    private ImageView mImgNav;  // NavigationView上的头像

    private IProjectPresenter firesPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initListener();
        initData();

        MySupportFragment fragment = findFragment(HomeTabFragment.class);
        if (fragment == null) {
            loadRootFragment(R.id.fl_container, HomeTabFragment.newInstance());
        }
    }

    private void initData(){
        String curDateStr=DateUtils.Date2String(new Date(),DateUtils.pattern_full);
        String st=getDateStr(curDateStr,5);
        L.i("Date","ST："+curDateStr+" ET"+st);

        //firesPresenter.getFiresList("2018-11-10","2019-11-19");
        firesPresenter.getFiresList(st,curDateStr);
    }

    //Day:日期字符串例如 2015-3-10  Num:需要减少的天数例如 7
    public static String getDateStr(String day,int Num) {
        SimpleDateFormat df = new SimpleDateFormat(DateUtils.pattern_full);
        Date nowDate = null;
        try {
            nowDate = df.parse(day);
        }
        catch (ParseException e) {
            e.printStackTrace();
        } //如果需要向后计算日期 -改为+
        Date newDate2 = new Date(nowDate.getTime() - (long)Num * 24 * 60 * 60 * 1000);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DateUtils.pattern_full);
        String dateOk = simpleDateFormat.format(newDate2);
        return dateOk;
    }


    private void initView() {
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        mDrawer.setDrawerListener(toggle);
        toggle.syncState();

        //TODO
        /*mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);
        mNavigationView.setCheckedItem(R.id.nav_home);

        LinearLayout llNavHeader = (LinearLayout) mNavigationView.getHeaderView(0);
        mTvName = (TextView) llNavHeader.findViewById(R.id.tv_name);
        mImgNav = (ImageView) llNavHeader.findViewById(R.id.img_nav);
        llNavHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawer.closeDrawer(GravityCompat.START);
                mDrawer.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        goLogin();
                    }
                }, 250);
            }
        });*/
    }

    private void initListener(){
        //火点信息保存数据库
        firesPresenter=new ProjectPresenter(this, new IProjectPresenter.ProjectCallback() {
            @Override
            public void onFiresListSuccess(List<FireDateEntity> fireDateEntityList) {
                //插入操作耗时
                DBUtils.getInstance().getmDaoSession().runInTx(() -> {
                    for (FireDateEntity ewellsEntity : fireDateEntityList) {
                        try {
                            DBUtils.getInstance().getmDaoSession().insertOrReplace(ewellsEntity);

                        } catch (Exception e) {
                            L.e(e.toString());
                        }
                    }
                });
            }

            @Override
            public void onFiresListFail(String error) {

            }

            @Override
            public void onSaveSampleInfoSucdess() {

            }

            @Override
            public void onSaveSampleInfoFail(String error) {

            }

            @Override
            public void onFiresListSuccess(String responseString) {

            }
        });
    }

    @Override
    public void onBackPressedSupport() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else {
            ISupportFragment topFragment = getTopFragment();

            // 主页的Fragment
            if (topFragment instanceof BaseMainFragment) {
                //TODO
                //mNavigationView.setCheckedItem(R.id.nav_home);
            }

            if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
                pop();
            } else {
                if (System.currentTimeMillis() - TOUCH_TIME < WAIT_TIME) {
                    finish();
                } else {
                    TOUCH_TIME = System.currentTimeMillis();
                    Toast.makeText(this, R.string.press_again_exit, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(final MenuItem item) {
        mDrawer.closeDrawer(GravityCompat.START);

        mDrawer.postDelayed(new Runnable() {
            @Override
            public void run() {
                int id = item.getItemId();

                final ISupportFragment topFragment = getTopFragment();
                MySupportFragment myHome = (MySupportFragment) topFragment;

                if (id == R.id.nav_home) {

                    HomeTabFragment fragment = findFragment(HomeTabFragment.class);
                    Bundle newBundle = new Bundle();
                    newBundle.putString("from", "From:" + topFragment.getClass().getSimpleName());
                    fragment.putNewBundle(newBundle);

                    myHome.start(fragment, SupportFragment.SINGLETASK);
                } else if (id == R.id.nav_data) {
                    //
                    NavigationUtils.getInstance().jumpTo(DataListActivity.class,null,false);
                    /*DataFragment fragment = findFragment(DataFragment.class);
                    if (fragment == null) {
                        myHome.startWithPopTo(DataFragment.newInstance(), HomeTabFragment.class, false);
                    } else {
                        // 如果已经在栈内,则以SingleTask模式start
                        myHome.start(fragment, SupportFragment.SINGLETASK);
                    }*/
                }/* else if (id == R.id.nav_shop) {
                    ShopFragment fragment = findFragment(ShopFragment.class);
                    if (fragment == null) {
                        myHome.startWithPopTo(ShopFragment.newInstance(), HomeFragment.class, false);
                    } else {
                        // 如果已经在栈内,则以SingleTask模式start,也可以用popTo
                        start(fragment, SupportFragment.SINGLETASK);
                        myHome.popTo(ShopFragment.class, false);
                    }
                }*/ else if (id == R.id.nav_login) {
                    goLogin();
                }
            }
        }, 300);

        return true;
    }

    @Override
    public void onOpenDrawer() {
        //TODO
        /*if (!mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.openDrawer(GravityCompat.START);
        }*/

    }

    @Override
    public void onLoginSuccess(String account) {

    }

    private void goLogin() {
        start(LoginFragment.newInstance());
    }
}
