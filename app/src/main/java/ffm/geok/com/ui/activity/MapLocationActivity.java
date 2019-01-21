package ffm.geok.com.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ffm.geok.com.R;
import ffm.geok.com.global.XApplication;
import ffm.geok.com.manager.DialogCallback;
import ffm.geok.com.model.AddressAttributes;
import ffm.geok.com.model.AddressModel;
import ffm.geok.com.model.AddressResults;
import ffm.geok.com.model.FireDateEntity;
import ffm.geok.com.model.LatLngEntity;
import ffm.geok.com.model.LoginModel;
import ffm.geok.com.model.ResponseModelAddress;
import ffm.geok.com.model.ResponseModelLogin;
import ffm.geok.com.presenter.ILoginPresenter;
import ffm.geok.com.presenter.IMapAddressPre;
import ffm.geok.com.presenter.LoginPresenter;
import ffm.geok.com.presenter.MapAddressPre;
import ffm.geok.com.uitls.ConstantUtils;
import ffm.geok.com.uitls.Convert;
import ffm.geok.com.uitls.GeoCoderUtil;
import ffm.geok.com.uitls.L;
import ffm.geok.com.uitls.NavigationUtils;
import ffm.geok.com.uitls.SPManager;
import ffm.geok.com.uitls.ServerUrl;
import ffm.geok.com.uitls.StringUtils;
import ffm.geok.com.uitls.ToastUtils;
import rx.android.schedulers.AndroidSchedulers;

public class MapLocationActivity extends AppCompatActivity implements LocationSource, AMapLocationListener {

    @BindView(R.id.toolbar)
    Toolbar mtoolBar;
    @BindView(R.id.map)
    MapView map;
    @BindView(R.id.map_locationDone)
    Button mapLocationDone;
    @BindView(R.id.map_locationReset)
    Button mapLocationReset;

    //AMap是地图对象
    private AMap aMap;
    private MapView mMapView;
    //声明AMapLocationClient类对象，定位发起端
    private AMapLocationClient mLocationClient = null;
    //声明mLocationOption对象，定位参数
    public AMapLocationClientOption mLocationOption = null;
    //声明mListener对象，定位监听器
    private OnLocationChangedListener mListener = null;
    //标识，用于判断是否只显示一次定位信息和用户重新定位
    private boolean isFirstLoc = true;

    private LatLng targetLatLng;
    private volatile AddressModel address=new AddressModel();
    private IMapAddressPre mapAddressPre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_location);
        ButterKnife.bind(this);

        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.map);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，实现地图生命周期管理
        mMapView.onCreate(savedInstanceState);

        initView();
        initData();
        initListener();

    }

    private void initView() {
        //布局渲染完了之后，才能setSupportActionBar
        setSupportActionBar(mtoolBar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        mtoolBar.setNavigationOnClickListener(v -> finish());

        //获取地图对象
        aMap = mMapView.getMap();
        aMap.setMapType(AMap.MAP_TYPE_NORMAL);

        //设置显示定位按钮 并且可以点击
        UiSettings settings = aMap.getUiSettings();
        //设置定位监听
        aMap.setLocationSource(this);
        // 是否显示定位按钮
        settings.setMyLocationButtonEnabled(true);
        // 是否可触发定位并显示定位层
        aMap.setMyLocationEnabled(true);

        //定位的小图标 默认是蓝点 这里自定义一团火，其实就是一张图片
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        //myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_action_github));
        myLocationStyle.radiusFillColor(android.R.color.transparent);
        myLocationStyle.strokeColor(android.R.color.transparent);
        aMap.setMyLocationStyle(myLocationStyle);

    }

    private void initData() {
        //开始定位
        initLocation();

        mapAddressPre=new MapAddressPre(this, new IMapAddressPre.AddressCallback() {
            @Override
            public void onAddressSuccess(AddressModel addressModel) {
                address=addressModel;

                Intent intent = new Intent();
                L.i("AA",address.getCity());
                intent.putExtra(ConstantUtils.mapLocation.Location, targetLatLng);
                intent.putExtra(ConstantUtils.mapLocation.POINTADDRESS,address);
                setResult(RESULT_OK, intent);//回传数据到主Activity

                finish();

            }

            @Override
            public void onError(String errMsg) {

            }
        });

    }


    private void initListener(){
        Button locDone = (Button) findViewById(R.id.map_locationDone);
        locDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mapAddressPre.getAddress(targetLatLng.longitude,targetLatLng.latitude);

            }
        });

        Button reLoc=(Button)findViewById(R.id.map_locationReset);
        reLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //将地图移动到定位点
                aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(aMap.getMyLocation().getLatitude(), aMap.getMyLocation().getLongitude())));

            }
        });

        aMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {

            }

            @Override
            public void onCameraChangeFinish(CameraPosition cameraPosition) {
                if (null != cameraPosition.target) {
                    L.d("当前定位坐标：" + cameraPosition.target.toString());
                    /*经纬度精确到小数点后7位*/
                    String longitude = StringUtils.reservedDecimalPlace(cameraPosition.target.longitude, 7);
                    String latitude = StringUtils.reservedDecimalPlace(cameraPosition.target.latitude, 7);
                    LatLng tempPoint = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude), false);
                    targetLatLng = tempPoint;
                }
            }
        });



    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息
                aMapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见官方定位类型表
                aMapLocation.getLatitude();//获取纬度
                aMapLocation.getLongitude();//获取经度
                aMapLocation.getAccuracy();//获取精度信息
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(aMapLocation.getTime());
                df.format(date);//定位时间
                aMapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                aMapLocation.getCountry();//国家信息
                aMapLocation.getProvince();//省信息
                aMapLocation.getCity();//城市信息
                aMapLocation.getDistrict();//城区信息
                aMapLocation.getStreet();//街道信息
                aMapLocation.getStreetNum();//街道门牌号信息
                aMapLocation.getCityCode();//城市编码
                aMapLocation.getAdCode();//地区编码

                // 如果不设置标志位，此时再拖动地图时，它会不断将地图移动到当前的位置
                if (isFirstLoc) {
                    //设置缩放级别
                    aMap.moveCamera(CameraUpdateFactory.zoomTo(17));
                    //将地图移动到定位点
                    aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude())));
                    //点击定位按钮 能够将地图的中心移动到定位点
                    mListener.onLocationChanged(aMapLocation);
                    //添加图钉
                    //aMap.addMarker(getMarkerOptions(aMapLocation));
                    //获取定位信息
                    StringBuffer buffer = new StringBuffer();
                    buffer.append(aMapLocation.getCountry() + "" + aMapLocation.getProvince() + "" + aMapLocation.getCity() + "" + aMapLocation.getProvince() + "" + aMapLocation.getDistrict() + "" + aMapLocation.getStreet() + "" + aMapLocation.getStreetNum());
                    Toast.makeText(getApplicationContext(), buffer.toString(), Toast.LENGTH_LONG).show();

                    Marker marker = aMap.addMarker(new MarkerOptions()
                            .position(new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude()))
                            .title("新增")
                            .icon(BitmapDescriptorFactory
                                    .defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                            .draggable(true));
                    marker.showInfoWindow();

                    isFirstLoc = false;
                }

            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                L.e("AmapError", "location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());

                Toast.makeText(getApplicationContext(), "定位失败", Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;

    }

    @Override
    public void deactivate() {

    }

    private void initLocation() {
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(this);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(false);
        //设置是否强制刷新WIFI，默认为强制刷新
        mLocationOption.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(10000);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }

    // 将经纬度getLng， getLat   通过getAmapByLngAndLat方法转换地址
    public void getAmapByLngAndLat(Double getLng, Double getLat){
        String geometry="{{\"x\":"+getLng+", \"y\":"+getLat+"}}";
        L.i("GEOMETRY",geometry);
        OkGo.<String>get(ServerUrl.Address)
                .tag(this)
                .params("returnGeometry", "false")
                .params("f", "json")
                .params("imageDisplay", "1434,366,96")
                .params("sr", "4326")
                .params("geometry", geometry)
                .params("geometryType", "esriGeometryPoint")
                .params("mapExtent", "111,26,113,24")
                .params("tolerance", "3")
                .params("layers", "all")
                .params("tdsourcetag", "s_pcqq_aiomsg")
                .execute(new DialogCallback(MapLocationActivity.this) {
                    @Override
                    public void onSuccess(Response<String> response) {
                        String responseString = response.body();
                        L.d("test11", "请求成功：" + responseString);
                        ResponseModelAddress responseModel = Convert.fromJson(responseString,ResponseModelAddress.class);
                        if (null != responseModel){
                            AddressAttributes addressAttributes=responseModel.getResults().get(0).getAttributes();
                            address.setAdcd(addressAttributes.getAdcd());
                            address.setCity(addressAttributes.getCity());
                            address.setCounty(addressAttributes.getCounty());
                            address.setProviencd(addressAttributes.getProvince());
                            L.e("Address",addressAttributes.getCity());
                            L.e("Address111",address.getCity());
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        L.e("test", "请求失败：" + response.message());
                    }
                });

    }
}
