package ffm.geok.com.uitls;

public interface ConstantUtils {
    interface global{
        String LOGIN_NAME = "username";                                                             //登录名
        String LOGIN_PASSWORD = "password";                                                         //登录密码
        String LoginModel = "loginModel";                                                           //登录信息
        String isLoadSPData = "isLoadSPData";
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
    }


}
