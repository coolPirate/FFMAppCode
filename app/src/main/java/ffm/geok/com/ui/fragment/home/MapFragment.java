package ffm.geok.com.ui.fragment.home;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.TileOverlay;
import com.amap.api.maps.model.TileOverlayOptions;
import com.amap.api.maps.model.TileProvider;
import com.amap.api.maps.model.UrlTileProvider;
import com.orhanobut.logger.Logger;

import java.net.MalformedURLException;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;
import ffm.geok.com.R;
import ffm.geok.com.base.BaseMainFragment;
import ffm.geok.com.global.XApplication;
import ffm.geok.com.presenter.IMapLocationPresenter;
import ffm.geok.com.presenter.MapLocationPresenter;
import ffm.geok.com.uitls.ConstantUtils;
import ffm.geok.com.uitls.StringUtils;
import ffm.geok.com.uitls.ToastUtils;
import ffm.geok.com.widget.dialog.DialogUtils;


public class MapFragment extends BaseMainFragment implements LocationSource, Toolbar.OnMenuItemClickListener {


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.map)
    MapView mMapView;
    @BindView(R.id.googleMap)
    CheckBox googleMap;
    private Toolbar mToolbar;

    Context mContext = getContext();

    private AMap aMap;
    private OnLocationChangedListener mListener = null;//定位监听器
    private IMapLocationPresenter mapLocationPresenter;
    private String locationAddress;
    private MyLocationStyle myLocationStyle;

    private TileOverlay tileOverlay;

    public static MapFragment newInstance() {
        return new MapFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        initView(view, savedInstanceState);
        initListener();

        //unbinder = ButterKnife.bind(this, view);
        return view;
    }

    private void initView(View view, Bundle savedInstanceState) {
        mMapView = (MapView) view.findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);
        if (null != mMapView) {
            aMap = mMapView.getMap();
        }

        /*初始化地图*/
        mapLocationPresenter = new MapLocationPresenter(getContext(), locationCallBack);
        mapLocationPresenter.initAMap(getActivity());

        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);

        mToolbar.setTitle(R.string.home);
        initToolbarNav(mToolbar, true);
        mToolbar.inflateMenu(R.menu.home);
        mToolbar.setOnMenuItemClickListener(this);


        //设置显示定位按钮 并且可以点击
        UiSettings settings = aMap.getUiSettings();
        //指北针
        settings.setCompassEnabled(true);
        //设置定位监听
        aMap.setLocationSource(this);
        // 是否显示定位按钮
        settings.setMyLocationButtonEnabled(true);
        // 是否可触发定位并显示定位层
        aMap.setMyLocationEnabled(true);
        settings.setZoomPosition(AMapOptions.ZOOM_POSITION_RIGHT_CENTER);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }

    IMapLocationPresenter.LocationCallBack locationCallBack = new IMapLocationPresenter.LocationCallBack() {
        @Override
        public void onInitFinish() {
            mapLocationPresenter.startLocation();
        }

        @Override
        public void onLocationSuccess(AMapLocation location) {
            if (TextUtils.isEmpty(location.getLongitude() + "") || TextUtils.isEmpty(location.getLatitude() + "")) {
                DialogUtils.ShowConfirmDialog(mContext, StringUtils.getResourceString(mContext, R.string.location_error), new DialogUtils.DialogConfirmCallback() {
                    @Override
                    public void onConfirm(DialogInterface dialog) {
                        mapLocationPresenter.startLocation();
                    }
                });
            } else {
                String tradeAddress = XApplication.getInstance().get(ConstantUtils.mapLocation.TRADEADDRESS);
                Logger.i(tradeAddress);
                locationAddress = "x=" + location.getLongitude() + "&y=" + location.getLatitude() + "&p=" + location.getAddress();
                XApplication.getInstance().put(ConstantUtils.mapLocation.Location, locationAddress);
//                ToastUtils.showShortToast(mContext, "定位成功："+locationAddress);
                Logger.d("定位成功：" + locationAddress);

                myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
                myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);
                myLocationStyle.interval(5000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
                myLocationStyle.showMyLocation(true);//显示定位点
                aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
                //aMap.getUiSettings().setMyLocationButtonEnabled(true);设置默认定位按钮是否显示，非必需设置。
                aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。

                //设置缩放级别
                aMap.moveCamera(CameraUpdateFactory.zoomTo(17));
                //将地图移动到定位点
                aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
                //点击定位按钮 能够将地图的中心移动到定位点
                mListener.onLocationChanged(location);
                //添加图钉
                //aMap.addMarker(getMarkerOptions(location));
                //获取定位信息
                StringBuffer buffer = new StringBuffer();
                buffer.append(location.getCountry() + "" + location.getProvince() + "" + location.getCity() + "" + location.getProvince() + "" + location.getDistrict() + "" + location.getStreet() + "" + location.getStreetNum());
                //ToastUtils.showLongMsg(mContext, buffer.toString());
            }
        }

        @Override
        public void onError(String returncon) {
            ToastUtils.showShortMsg(mContext, returncon);
        }
    };

    private void initListener() {
        googleMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (googleMap.isChecked()) {
                    //显示google瓦片
                    final String url = "http://mt0.google.cn/vt/lyrs=y@198&hl=zh-CN&gl=cn&src=app&x=%d&y=%d&z=%d&s=";
                    TileProvider tileProvider = new UrlTileProvider(256, 256) {
                        public URL getTileUrl(int x, int y, int zoom) {
                            try {
                                return new URL(String.format(url, x, y, zoom));
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            }
                            return null;
                        }
                    };
                    if (tileProvider != null) {
                        tileOverlay = aMap.addTileOverlay(new TileOverlayOptions()
                                .tileProvider(tileProvider)
                                .diskCacheEnabled(true)
                                .diskCacheDir("/storage/emulated/0/amap/cache")
                                .diskCacheSize(100000)
                                .memoryCacheEnabled(true)
                                .memCacheSize(100000))
                        ;
                    }
                } else {
                    tileOverlay.remove();
                }
            }
        });


    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }

    /**
     * 类似于 Activity的 onNewIntent()
     */
    @Override
    public void onNewBundle(Bundle args) {
        super.onNewBundle(args);

        Toast.makeText(_mActivity, args.getString("from"), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //unbinder.unbind();
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        this.mListener = onLocationChangedListener;
    }

    @Override
    public void deactivate() {

    }
}
