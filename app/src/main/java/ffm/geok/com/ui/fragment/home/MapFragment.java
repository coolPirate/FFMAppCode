package ffm.geok.com.ui.fragment.home;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.CoordinateConverter;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.Projection;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MultiPointItem;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.TileOverlay;
import com.amap.api.maps.model.TileOverlayOptions;
import com.amap.api.maps.model.TileProvider;
import com.amap.api.maps.model.UrlTileProvider;
import com.amap.api.maps.model.animation.Animation;
import com.amap.api.maps.model.animation.RotateAnimation;
import com.autonavi.amap.mapcore.DPoint;
import com.orhanobut.logger.Logger;

import org.mozilla.javascript.regexp.SubString;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ffm.geok.com.R;
import ffm.geok.com.base.BaseMainFragment;
import ffm.geok.com.global.XApplication;
import ffm.geok.com.javagen.FireDateEntityDao;
import ffm.geok.com.model.FireDateEntity;
import ffm.geok.com.model.InputInfoModel;
import ffm.geok.com.model.InputInfoModelPattern;
import ffm.geok.com.model.InputInfoModelType;
import ffm.geok.com.model.Message;
import ffm.geok.com.model.VerificationType;
import ffm.geok.com.presenter.IMapLocationPresenter;
import ffm.geok.com.presenter.MapLocationPresenter;
import ffm.geok.com.ui.activity.ProjectDetialActivity;
import ffm.geok.com.uitls.ConstantUtils;
import ffm.geok.com.uitls.Convert;
import ffm.geok.com.uitls.DBUtils;
import ffm.geok.com.uitls.L;
import ffm.geok.com.uitls.MapConverter;
import ffm.geok.com.uitls.NavigationUtils;
import ffm.geok.com.uitls.RxBus;
import ffm.geok.com.uitls.StringUtils;
import ffm.geok.com.uitls.ToastUtils;
import ffm.geok.com.widget.dialog.DialogUtils;
import rx.Observable;
import rx.functions.Action1;


public class MapFragment extends BaseMainFragment implements LocationSource, Toolbar.OnMenuItemClickListener,View.OnClickListener {


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

    private Dialog inputDialog = null;
    Observable<Message> observableMarker;//数据更新
    private ArrayList<InputInfoModel> sourceData = new ArrayList<InputInfoModel>(); //录入模板


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
        ButterKnife.bind(this,view);

        initView(view, savedInstanceState);
        initListener(view);
        initMaker();

        //unbinder = ButterKnife.bind(this, view);
        //ButterKnife.bind(this,view);
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

        /*mToolbar = (Toolbar) view.findViewById(R.id.toolbar);

        mToolbar.setTitle(R.string.home);
        initToolbarNav(mToolbar, true);
        mToolbar.inflateMenu(R.menu.home);
        mToolbar.setOnMenuItemClickListener(this);*/


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

    private void initMaker() {
        //初始化新增数据marker
        //List<FireDateEntity> fireDateEntityList = DBUtils.getInstance().queryAllBySingleWhereConditions(FireDateEntity.class, FireDateEntityDao.Properties.See.eq("1"));
        List<FireDateEntity> fireDateEntityList=DBUtils.getInstance().queryAll(FireDateEntity.class);
        int cnt=fireDateEntityList.size();
        L.i("Marker数", Integer.toString(cnt));
        for (int i = 0; i < fireDateEntityList.size(); i++) {
            FireDateEntity fireDateEntity = fireDateEntityList.get(i);
            Double lgtd = fireDateEntity.getLon();

            Double lttd = fireDateEntity.getLat();
            if (lgtd == null && lttd == null) continue;
            LatLng latLng = new LatLng(lttd, lgtd);
            L.i("LATLNG",latLng.latitude+"......"+latLng.longitude);


            LatLng desLatLng = MapConverter.transformFromWGSToGCJ(latLng);
            L.i("LATLNG111",desLatLng.latitude+"......"+desLatLng.longitude);
            TextView textView = new TextView(getActivity().getApplicationContext());
            textView.setBackgroundResource(R.mipmap.marker4);     //通过View获取BitmapDescriptor对象
            BitmapDescriptor markerIcon = BitmapDescriptorFactory
                    .fromView(textView);
            Marker marker = aMap.addMarker(new MarkerOptions()
                    .position(desLatLng)
                    .title(fireDateEntity.getId())
                    .snippet(fireDateEntity.getCreateTime())
                    .icon(markerIcon)
                    .draggable(false));
            //marker.showInfoWindow();
        }
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

    private void initListener(View view) {
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

        // 定义海量点点击事件

        /*AMap.OnMultiPointClickListener multiPointClickListener = new AMap.OnMultiPointClickListener() {
            // 海量点中某一点被点击时回调的接口
            // 返回 true 则表示接口已响应事件，否则返回false
            @Override
            public boolean onPointClick(MultiPointItem pointItem) {
                //ToastUtils.showShortMsg(MainActivity.this,"clicked!");
                LatLng latLng = pointItem.getLatLng();
                Double lg = latLng.longitude;
                Double lt = latLng.latitude;

                TextView textView = new TextView(getActivity().getApplicationContext());
                textView.setBackgroundResource(R.mipmap.marker2);     //通过View获取BitmapDescriptor对象
                BitmapDescriptor markerIcon = BitmapDescriptorFactory
                        .fromView(textView);

                clearMarkers();

                Marker marker = aMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .icon(markerIcon)
                        .draggable(false));
                marker.setObject(ConstantUtils.mapLocation.MapMakerVisible);

                //initDialogParams();
                return false;

            }
        };
        aMap.setOnMultiPointClickListener(multiPointClickListener);*/

        aMap.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                marker.setInfoWindowEnable(false);

                TextView textView = new TextView(getActivity().getApplicationContext());
                textView.setBackgroundResource(R.mipmap.marker5);     //通过View获取BitmapDescriptor对象
                BitmapDescriptor markerIcon = BitmapDescriptorFactory
                        .fromView(textView);
                marker.setIcon(markerIcon);

                //旋转动画
                /*Animation animation = new RotateAnimation(marker.getRotateAngle(),marker.getRotateAngle()+180,0,0,0);
                long duration = 1000L;
                animation.setDuration(duration);
                animation.setInterpolator(new LinearInterpolator());
                marker.setAnimation(animation);
                marker.startAnimation();*/

                jumpPoint(marker);

                initDialogParams(marker.getTitle());
                return false;
            }
        });

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return false;
            }
        });

    }

    private void clearMarkers() {
        //获取地图上所有Marker
        List<Marker> mapScreenMarkers = aMap.getMapScreenMarkers();
        for (int i = 0; i < mapScreenMarkers.size(); i++) {
            Marker marker = mapScreenMarkers.get(i);
            if (marker.getObject().equals(ConstantUtils.mapLocation.MapMakerVisible)) {
                marker.remove();//移除当前Marker
            }
        }
        aMap.reloadMap();//刷新地图
    }

    private void initDialogParams(String id) {
        FireDateEntity fireDateEntity=(FireDateEntity)DBUtils.getInstance().queryAllBySingleWhereConditions(FireDateEntity.class, FireDateEntityDao.Properties.Id.eq(id)).get(0);
        inputDialog = DialogUtils.getDialog(getActivity(), R.layout.layout_dialog_briefinfo);
        View view=inputDialog.findViewById(R.id.dialog_message);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != fireDateEntity) {
                    sourceData.clear();
                    initInputIaCEwellsTemplate(fireDateEntity);
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList(ConstantUtils.global.ProjectDetial, sourceData);
                    bundle.putString(ConstantUtils.global.ProjectEntityId, fireDateEntity.getId());
                    NavigationUtils.getInstance().jumpTo(ProjectDetialActivity.class, bundle, false);
                }

            }
        });

        /*修改默认窗口高度*/
        Window dialogWindow = inputDialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.BOTTOM);
        Display d = dialogWindow.getWindowManager().getDefaultDisplay(); // 为获取屏幕宽、高
        lp.width = (int) (d.getWidth() * 0.9); // 高度设置为屏幕的0.8
        lp.height = (int) (d.getHeight() * 0.2); // 高度设置
        dialogWindow.setAttributes(lp); // 设置生效
//        content.setText("当前坐标：\n经度："+latLng.longitude+"\n纬度："+latLng.latitude);
        TextView lng = (TextView) inputDialog.findViewById(R.id.tv_project_Lng);
        lng.setText(String.valueOf(fireDateEntity.getLat()));
        setEditTextReadOnly(lng);
        TextView lat = (TextView) inputDialog.findViewById(R.id.tv_project_lat);
        lat.setText(String.valueOf(fireDateEntity.getLon()));
        setEditTextReadOnly(lat);
        TextView name = (TextView) inputDialog.findViewById(R.id.tv_project_name);
        name.setText(String.valueOf(fireDateEntity.getProvince()+fireDateEntity.getCity()+fireDateEntity.getCounty()));
        setEditTextReadOnly(name);
        TextView time = (TextView) inputDialog.findViewById(R.id.tv_project_time);
        time.setText(String.valueOf(fireDateEntity.getFindTime()));
        setEditTextReadOnly(time);

        ImageView closeBtn = (ImageView) inputDialog.findViewById(R.id.img_btn_close);
        closeBtn.setOnClickListener(this);

    }

    private void initInputIaCEwellsTemplate(FireDateEntity fireDateEntity) {
        InputInfoModel infomodel = null;
        infomodel = new InputInfoModel(ConstantUtils.FIRES_LABELS.CREATETIME, InputInfoModelType.INPUT, "请输入开始时间", VerificationType.none, 1, Integer.MAX_VALUE, InputInfoModelPattern.normal);
        infomodel.setInputResultText(String.valueOf(fireDateEntity.getCreateTime()));
        sourceData.add(infomodel);
        infomodel = new InputInfoModel(ConstantUtils.FIRES_LABELS.FINDTIME, InputInfoModelType.INPUT, "请输入发现时间", VerificationType.none, 1, Integer.MAX_VALUE, InputInfoModelPattern.normal);
        infomodel.setInputResultText(String.valueOf(fireDateEntity.getFindTime()));
        sourceData.add(infomodel);
        infomodel = new InputInfoModel(ConstantUtils.FIRES_LABELS.UPDATETIME, InputInfoModelType.INPUT, "请输入更新时间", VerificationType.none, 1, Integer.MAX_VALUE, InputInfoModelPattern.normal);
        infomodel.setInputResultText(String.valueOf(fireDateEntity.getUpdateTime()));
        sourceData.add(infomodel);
        infomodel = new InputInfoModel(ConstantUtils.FIRES_LABELS.PROVINCE, InputInfoModelType.INPUT, "请输入省份", VerificationType.none, 1, Integer.MAX_VALUE, InputInfoModelPattern.normal);
        infomodel.setInputResultText(StringUtils.isEmptyString(fireDateEntity.getProvince()));
        sourceData.add(infomodel);
        infomodel = new InputInfoModel(ConstantUtils.FIRES_LABELS.CITY, InputInfoModelType.INPUT, "请输入城市", VerificationType.none, 1, Integer.MAX_VALUE, InputInfoModelPattern.normal);
        infomodel.setInputResultText(StringUtils.isEmptyString(fireDateEntity.getCity()));
        sourceData.add(infomodel);
        infomodel = new InputInfoModel(ConstantUtils.FIRES_LABELS.COUNTY, InputInfoModelType.INPUT, "请输入县名称", VerificationType.none, 1, Integer.MAX_VALUE, InputInfoModelPattern.normal);
        infomodel.setInputResultText(StringUtils.isEmptyString(fireDateEntity.getCounty()));
        sourceData.add(infomodel);
        infomodel = new InputInfoModel(ConstantUtils.FIRES_LABELS.LAT, InputInfoModelType.INPUT, "请输入纬度", VerificationType.none, 1, Integer.MAX_VALUE, InputInfoModelPattern.normal);
        infomodel.setInputResultText(StringUtils.isEmptyString(String.valueOf(fireDateEntity.getLat())));
        sourceData.add(infomodel);
        infomodel = new InputInfoModel(ConstantUtils.FIRES_LABELS.LON, InputInfoModelType.INPUT, "请输入经度", VerificationType.none, 1, Integer.MAX_VALUE, InputInfoModelPattern.normal);
        infomodel.setInputResultText(StringUtils.isEmptyString(String.valueOf(fireDateEntity.getLon())));
        sourceData.add(infomodel);
        infomodel = new InputInfoModel(ConstantUtils.FIRES_LABELS.SATELLITE, InputInfoModelType.INPUT, "请输入卫星名称", VerificationType.none, 1, Integer.MAX_VALUE, InputInfoModelPattern.normal);
        infomodel.setInputResultText(StringUtils.isEmptyString(fireDateEntity.getSatellite()));
        sourceData.add(infomodel);
        infomodel = new InputInfoModel(ConstantUtils.FIRES_LABELS.TYPE, InputInfoModelType.INPUT, "请输入类型", VerificationType.none, 1, Integer.MAX_VALUE, InputInfoModelPattern.normal);
        infomodel.setInputResultText(StringUtils.isEmptyString(fireDateEntity.getType()));
        sourceData.add(infomodel);
        /*infomodel = new InputInfomodel(ConstantUtils.IA_C_MEDIA_LABELS.Media, InputInfoModelType.Multi_Media, "请选择多媒体", null, null, VerificationType.request, 0, 0, InputInfoModelPattern.normal);
        infomodel.setMultiMedia(getMultiMediaByObjID(iaCEwellsEntity.getPid()));
        sourceData.add(infomodel);*/

    }

    public static void setEditTextReadOnly(TextView view){
        //view.setTextColor(R.color.color_blue);   //设置只读时的文字颜色
        if (view instanceof android.widget.EditText){
            view.setCursorVisible(false);      //设置输入框中的光标不可见
            view.setFocusable(false);           //无焦点
            view.setFocusableInTouchMode(false);     //触摸时也得不到焦点
        }
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
        RxBus.get().unregister("DataSelected",observableMarker);
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        this.mListener = onLocationChangedListener;
    }

    @Override
    public void deactivate() {

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        L.i("LATLON","lat1:");
    }

    @Override
    public void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        L.i("LATLON","lat:");

        observableMarker = RxBus.get().register("DataSelected", Message.class);
        observableMarker.subscribe(new Action1<Message>() {
            @Override
            public void call(Message message) {
                L.d("你传递的消息code = " + message.getMsgCode() + "消息content = " + message.getMsgContent());

                String latlonStr=message.getMsgContent();
                Double lat=Double.valueOf(latlonStr.substring(0,latlonStr.indexOf(",")));
                Double lon=Double.valueOf(latlonStr.substring(latlonStr.indexOf(",")+1,latlonStr.length()));
                L.i("LATLON","lat:"+lat+"Lon"+lon);
                LatLng marker1 = new LatLng(lat, lon);
                //设置中心点和缩放比例
                aMap.moveCamera(CameraUpdateFactory.changeLatLng(marker1));
                aMap.moveCamera(CameraUpdateFactory.zoomTo(12));
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_btn_close:
                if (null != inputDialog) {
                    inputDialog.dismiss();
                }
                break;
        }

    }

    /**
     * marker点击时跳动一下
     */
    public void jumpPoint(final Marker marker) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = aMap.getProjection();
        final LatLng markerLatlng = marker.getPosition();
        Point markerPoint = proj.toScreenLocation(markerLatlng);
        markerPoint.offset(0, -100);
        final LatLng startLatLng = proj.fromScreenLocation(markerPoint);
        final long duration = 1500;

        final Interpolator interpolator = new BounceInterpolator();
        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);
                double lng = t * markerLatlng.longitude + (1 - t)
                        * startLatLng.longitude;
                double lat = t * markerLatlng.latitude + (1 - t)
                        * startLatLng.latitude;
                marker.setPosition(new LatLng(lat, lng));
                if (t < 1.0) {
                    handler.postDelayed(this, 16);
                }
            }
        });
    }
}
