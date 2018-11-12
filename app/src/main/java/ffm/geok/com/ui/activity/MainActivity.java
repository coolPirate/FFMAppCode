package ffm.geok.com.ui.activity;

import android.os.Bundle;
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

import ffm.geok.com.R;
import ffm.geok.com.base.BaseMainFragment;
import ffm.geok.com.base.MySupportActivity;
import ffm.geok.com.base.MySupportFragment;
import ffm.geok.com.ui.fragment.account.LoginFragment;
import ffm.geok.com.ui.fragment.data.DataFragment;
import ffm.geok.com.ui.fragment.home.HomeFragment;
import ffm.geok.com.ui.fragment.home.MapFragment;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MySupportFragment fragment = findFragment(MapFragment.class);
        if (fragment == null) {
            loadRootFragment(R.id.fl_container, MapFragment.newInstance());
        }
        initView();
    }

    private void initView() {
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        mDrawer.setDrawerListener(toggle);
        toggle.syncState();

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
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
                mNavigationView.setCheckedItem(R.id.nav_home);
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

                    MapFragment fragment = findFragment(MapFragment.class);
                    Bundle newBundle = new Bundle();
                    newBundle.putString("from", "From:" + topFragment.getClass().getSimpleName());
                    fragment.putNewBundle(newBundle);

                    myHome.start(fragment, SupportFragment.SINGLETASK);
                } else if (id == R.id.nav_data) {
                    DataFragment fragment = findFragment(DataFragment.class);
                    if (fragment == null) {
                        myHome.startWithPopTo(DataFragment.newInstance(), MapFragment.class, false);
                    } else {
                        // 如果已经在栈内,则以SingleTask模式start
                        myHome.start(fragment, SupportFragment.SINGLETASK);
                    }
                }/* else if (id == R.id.nav_shop) {
                    ShopFragment fragment = findFragment(ShopFragment.class);
                    if (fragment == null) {
                        myHome.startWithPopTo(ShopFragment.newInstance(), HomeFragment.class, false);
                    } else {
                        // 如果已经在栈内,则以SingleTask模式start,也可以用popTo
//                        start(fragment, SupportFragment.SINGLETASK);
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
        if (!mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.openDrawer(GravityCompat.START);
        }

    }

    @Override
    public void onLoginSuccess(String account) {

    }

    private void goLogin() {
        start(LoginFragment.newInstance());
    }
}
