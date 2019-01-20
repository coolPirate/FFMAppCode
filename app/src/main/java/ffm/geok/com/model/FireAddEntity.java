package ffm.geok.com.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class FireAddEntity {
    @Id
    @Property(nameInDb="ID")
    private String id;
    @Property(nameInDb="USERNAME")
    private String username;        //上报人姓名
    @Property(nameInDb="SEE")
    private String see;        // 查看状态（1未查看）
    @Property(nameInDb="CREATETIME")
    private String createTime;        // create_time  三个时间相同
    @Property(nameInDb="FINDTIME")
    private String findTime;        // 发现时间  三个时间相同
    @Property(nameInDb="UPDATETIME")
    private String updateTime;        // 更新时间 三个时间相同
    @Property(nameInDb="PROVINCE")
    private String province;        // 省
    @Property(nameInDb="CITY")
    private String city;        // 市
    @Property(nameInDb="COUNTY")
    private String county;        // 县
    @Property(nameInDb="LON")
    private String lon;        // lon
    @Property(nameInDb="LAT")
    private String lat;        // lat
    @Property(nameInDb="DEM")
    private String dem;        // dem
    @Property(nameInDb="ADCD")
    private String adcd;        // adcd
    @Generated(hash = 1840113679)
    public FireAddEntity(String id, String username, String see, String createTime,
            String findTime, String updateTime, String province, String city,
            String county, String lon, String lat, String dem, String adcd) {
        this.id = id;
        this.username = username;
        this.see = see;
        this.createTime = createTime;
        this.findTime = findTime;
        this.updateTime = updateTime;
        this.province = province;
        this.city = city;
        this.county = county;
        this.lon = lon;
        this.lat = lat;
        this.dem = dem;
        this.adcd = adcd;
    }
    @Generated(hash = 1282823359)
    public FireAddEntity() {
    }
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getUsername() {
        return this.username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getSee() {
        return this.see;
    }
    public void setSee(String see) {
        this.see = see;
    }
    public String getCreateTime() {
        return this.createTime;
    }
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
    public String getFindTime() {
        return this.findTime;
    }
    public void setFindTime(String findTime) {
        this.findTime = findTime;
    }
    public String getUpdateTime() {
        return this.updateTime;
    }
    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
    public String getProvince() {
        return this.province;
    }
    public void setProvince(String province) {
        this.province = province;
    }
    public String getCity() {
        return this.city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getCounty() {
        return this.county;
    }
    public void setCounty(String county) {
        this.county = county;
    }
    public String getLon() {
        return this.lon;
    }
    public void setLon(String lon) {
        this.lon = lon;
    }
    public String getLat() {
        return this.lat;
    }
    public void setLat(String lat) {
        this.lat = lat;
    }
    public String getDem() {
        return this.dem;
    }
    public void setDem(String dem) {
        this.dem = dem;
    }
    public String getAdcd() {
        return this.adcd;
    }
    public void setAdcd(String adcd) {
        this.adcd = adcd;
    }

}
