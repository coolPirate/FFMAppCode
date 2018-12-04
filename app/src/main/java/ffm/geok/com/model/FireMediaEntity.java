package ffm.geok.com.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class FireMediaEntity {
    @Id
    @Property(nameInDb = "PID")
    private String pid;
    @Property(nameInDb = "FIREID")
    private String fireid;
    @Property(nameInDb = "OBJTP")
    private String objtp;
    @Property(nameInDb = "ADCD")
    private String adcd;
    @Property(nameInDb = "FPATH")
    private String fpath;
    @Property(nameInDb = "LGTD")
    private Double lgtd;
    @Property(nameInDb = "LTTD")
    private Double lttd;
    @Property(nameInDb = "PTIME")
    private String ptime;
    @Property(nameInDb = "FNAME")
    private String fname;
    @Property(nameInDb = "MULTITYPE")
    private String multitype;
    @Property(nameInDb = "STATUS")
    private String status;
    @Property(nameInDb = "MODITIME")
    private String moditime;
    @Property(nameInDb = "REMARK")
    private String remark;
    @Generated(hash = 1132323704)
    public FireMediaEntity(String pid, String fireid, String objtp, String adcd,
            String fpath, Double lgtd, Double lttd, String ptime, String fname,
            String multitype, String status, String moditime, String remark) {
        this.pid = pid;
        this.fireid = fireid;
        this.objtp = objtp;
        this.adcd = adcd;
        this.fpath = fpath;
        this.lgtd = lgtd;
        this.lttd = lttd;
        this.ptime = ptime;
        this.fname = fname;
        this.multitype = multitype;
        this.status = status;
        this.moditime = moditime;
        this.remark = remark;
    }
    @Generated(hash = 492594453)
    public FireMediaEntity() {
    }
    public String getPid() {
        return this.pid;
    }
    public void setPid(String pid) {
        this.pid = pid;
    }
    public String getFireid() {
        return this.fireid;
    }
    public void setFireid(String fireid) {
        this.fireid = fireid;
    }
    public String getObjtp() {
        return this.objtp;
    }
    public void setObjtp(String objtp) {
        this.objtp = objtp;
    }
    public String getAdcd() {
        return this.adcd;
    }
    public void setAdcd(String adcd) {
        this.adcd = adcd;
    }
    public String getFpath() {
        return this.fpath;
    }
    public void setFpath(String fpath) {
        this.fpath = fpath;
    }
    public Double getLgtd() {
        return this.lgtd;
    }
    public void setLgtd(Double lgtd) {
        this.lgtd = lgtd;
    }
    public Double getLttd() {
        return this.lttd;
    }
    public void setLttd(Double lttd) {
        this.lttd = lttd;
    }
    public String getPtime() {
        return this.ptime;
    }
    public void setPtime(String ptime) {
        this.ptime = ptime;
    }
    public String getFname() {
        return this.fname;
    }
    public void setFname(String fname) {
        this.fname = fname;
    }
    public String getMultitype() {
        return this.multitype;
    }
    public void setMultitype(String multitype) {
        this.multitype = multitype;
    }
    public String getStatus() {
        return this.status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getModitime() {
        return this.moditime;
    }
    public void setModitime(String moditime) {
        this.moditime = moditime;
    }
    public String getRemark() {
        return this.remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
}
