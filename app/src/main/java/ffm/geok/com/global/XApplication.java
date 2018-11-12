package ffm.geok.com.global;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.facebook.stetho.Stetho;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.interceptor.HttpLoggingInterceptor;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import ffm.geok.com.uitls.L;
import ffm.geok.com.uitls.NavigationUtils;
import okhttp3.OkHttpClient;

public class XApplication extends MultiDexApplication {
    private static final String TagStr = "ntsl";
    private static XApplication application;
    private static Context mContext;
    private Map<String,Object> map = new HashMap<String, Object>();

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        this.mContext = getApplicationContext();
        this.application = this;
        initConfig();
    }

    private void initConfig() {
        /*activity加入生命周期控制*/
        registerActivityLifecycleCallbacks(NavigationUtils.getInstance());
        initLogger();
        initOKGo();
        initStetho();
    }

    private void initLogger() {
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(false)  // (Optional) Whether to show thread info or not. Default true
                .methodCount(0)         // (Optional) How many method line to show. Default 2
                .methodOffset(7)        // (Optional) Hides internal method calls up to offset. Default 5
                .tag(TagStr)   // (Optional) Global tag for every log. Default PRETTY_LOGGER
                .build();
        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy));

        ApplicationInfo applicationInfo = null;
        try {
            applicationInfo = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (applicationInfo == null)return;
        String amap_apikey = applicationInfo.metaData.getString("com.amap.api.v2.apikey");
        L.d("amap_apikey=" + amap_apikey);
    }

    private void initOKGo() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("OkGo");
        //log打印级别，决定了log显示的详细程度
        loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);
        //log颜色级别，决定了log在控制台显示的颜色
        loggingInterceptor.setColorLevel(Level.INFO);
        builder.addInterceptor(loggingInterceptor);
        //全局的读取超时时间
        builder.readTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
        //全局的写入超时时间
        builder.writeTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
        //全局的连接超时时间
        builder.connectTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);

        //---------这里给出的是示例代码,告诉你可以这么传,实际使用的时候,根据需要传,不需要就不传-------------//
        HttpHeaders headers = new HttpHeaders();
        headers.put("commonHeaderKey1", "commonHeaderValue1");    //header不支持中文，不允许有特殊字符
        headers.put("commonHeaderKey2", "commonHeaderValue2");
        HttpParams params = new HttpParams();
        params.put("commonParamsKey1", "commonParamsValue1");     //param支持中文,直接传,不要自己编码
        params.put("commonParamsKey2", "这里支持中文参数");
//-------------------------------------------------------------------------------------//

        OkGo.getInstance().init(this)                       //必须调用初始化
                .setOkHttpClient(builder.build())               //建议设置OkHttpClient，不设置将使用默认的
                .setCacheMode(CacheMode.NO_CACHE)               //全局统一缓存模式，默认不使用缓存，可以不传
                .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)   //全局统一缓存时间，默认永不过期，可以不传
                .setRetryCount(3)                               //全局统一超时重连次数，默认为三次，那么最差的情况会请求4次(一次原始请求，三次重连请求)，不需要可以设置为0
                .addCommonHeaders(headers)                      //全局公共头
                .addCommonParams(params);                       //全局公共参数
    }

    private void initStetho() {
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(
                                Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(
                                Stetho.defaultInspectorModulesProvider(this))
                        .build());
    }

    public static XApplication getInstance() {
        return application;
    }
    public static Context getContext() {
        return mContext;
    }

    /**
     * 缓存数据
     * @param key
     * @param value
     */
    public  void put(String key,Object value){
        map.put(key, value);
    }
    @SuppressWarnings("unchecked")
    public  <T> T get(String key){
        T serializableData = null;
        if(map.containsKey(key)){
            serializableData = (T)map.get(key);
        }
        return serializableData;
    }
    /*清楚缓存数据*/
    public  void clear(){
        map.clear();
    }
}