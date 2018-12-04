package ffm.geok.com.uitls;

public interface ConstantUtils {
    interface global{
        String LOGIN_NAME = "username";                                                             //登录名
        String LOGIN_PASSWORD = "password";                                                         //登录密码
        String LoginModel = "loginModel";                                                           //登录信息
        String RefreshDataStatus = "RefreshDataStatus";                                             //刷新数据状态
        String IS_SYNCHRO_NO = "0";                                                                 //未同步数据标识
        String IS_SYNCHRO_UPDATE = "1";                                                             //已修改待同步数据标识
        int ThumbnailSize = 100;                                                                    //多媒体缩略图默认大小
        int ThumbnailMargin = 10;                                                                   //多媒体缩略图默认边距
        String ProjectDetial = "ProjectDetial";                                                     //工程详情
        String ProjectEntityId="ProjectEntityId";
        int Notification_ID=11;
        String ProjectVertify = "ProjectVertify";                                                     //核查
    }

    interface mapLocation {
        String Location = "location";                                                               //当前定位地址
        String LOCALCITY = "LOCALCITY";                                                             //当前定位城市
        String LATITUDE = "LATITUDE";                                                               //当前定位纬度
        String LONTITUDE = "LONTITUDE";                                                             //当前定位经度
        String TRADEADDRESS = "tradeAddress";
        int LOCATION_LATLANG=20;
        String MapMakerVisible="Visible";
        String LATLNGS="latlngs";
        String POLYLINEISDEL="polyisdelete";
        int showDel=0;//显示删除按钮
        int notShowDel=1;//不显示删除按钮
    }

    interface RequestTag {
        public static final String ADCD = "adcd";
        public static final String EWNM = "ewnm";
        public static final String YEAR = "year";
        public static final String ST = "st";
        public static final String ET = "et";
    }

    interface FIRES_LABELS {
        String CITY = "城市名称";
        String COUNTY="县名称";
        String CREATETIME="开始时间";
        String FINDTIME="发现时间";
        String LAT="纬度";
        String LON="经度";
        String PROVINCE="省份";
        String SATELLITE="卫星名称";
        String TYPE="类型";
        String UPDATETIME="更新时间";

    }


}
