package ffm.geok.com.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class FireDateEntity {
    @Id
    @Property(nameInDb="ID")
    private String id;
    @Property(nameInDb="TYPE")
    private String type;
    @Property(nameInDb="LAT")
    private Double lat;
    @Property(nameInDb="LON")
    private Double lon;
    @Property(nameInDb="PROVINCE")
    private String province;
    @Property(nameInDb="CITY")
    private String city;
    @Property(nameInDb="COUNTY")
    private String county;
    @Property(nameInDb="CREATETIME")
    private String createTime;
    @Property(nameInDb="FINDTIME")
    private String findTime;
    @Property(nameInDb="UPDATETIME")
    private String updateTime;
    @Property(nameInDb="SATELLITE")
    private String satellite;
    @Property(nameInDb="USERNAME")
    private String username;
    @Property(nameInDb="DEM")
    private String dem;
    @Property(nameInDb="SEE")
    private String see;
    @Property(nameInDb="ADCD")
    private String adcd;
    @Generated(hash = 1180671757)
    public FireDateEntity(String id, String type, Double lat, Double lon,
            String province, String city, String county, String createTime,
            String findTime, String updateTime, String satellite, String username,
            String dem, String see, String adcd) {
        this.id = id;
        this.type = type;
        this.lat = lat;
        this.lon = lon;
        this.province = province;
        this.city = city;
        this.county = county;
        this.createTime = createTime;
        this.findTime = findTime;
        this.updateTime = updateTime;
        this.satellite = satellite;
        this.username = username;
        this.dem = dem;
        this.see = see;
        this.adcd = adcd;
    }
    @Generated(hash = 750021700)
    public FireDateEntity() {
    }
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getType() {
        return this.type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public Double getLat() {
        return this.lat;
    }
    public void setLat(Double lat) {
        this.lat = lat;
    }
    public Double getLon() {
        return this.lon;
    }
    public void setLon(Double lon) {
        this.lon = lon;
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
    public String getSatellite() {
        return this.satellite;
    }
    public void setSatellite(String satellite) {
        this.satellite = satellite;
    }
    public String getUsername() {
        return this.username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getDem() {
        return this.dem;
    }
    public void setDem(String dem) {
        this.dem = dem;
    }
    public String getSee() {
        return this.see;
    }
    public void setSee(String see) {
        this.see = see;
    }
    public String getAdcd() {
        return this.adcd;
    }
    public void setAdcd(String adcd) {
        this.adcd = adcd;
    }
}
