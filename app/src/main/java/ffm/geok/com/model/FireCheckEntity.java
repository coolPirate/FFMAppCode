package ffm.geok.com.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class FireCheckEntity {
    @Id
    @Property(nameInDb="ID")
    private String id;
    @Property(nameInDb="CONFIRMOR")
    private String confirmor;
    @Property(nameInDb="FIREID")
    private String fireid;
    @Property(nameInDb="ISFIRE")
    private String isfire;
    @Property(nameInDb="MODITIME")
    private String moditime;
    @Property(nameInDb="REMARK")
    private String remark;
    @Generated(hash = 1996651165)
    public FireCheckEntity(String id, String confirmor, String fireid,
            String isfire, String moditime, String remark) {
        this.id = id;
        this.confirmor = confirmor;
        this.fireid = fireid;
        this.isfire = isfire;
        this.moditime = moditime;
        this.remark = remark;
    }
    @Generated(hash = 1028676751)
    public FireCheckEntity() {
    }
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getConfirmor() {
        return this.confirmor;
    }
    public void setConfirmor(String confirmor) {
        this.confirmor = confirmor;
    }
    public String getFireid() {
        return this.fireid;
    }
    public void setFireid(String fireid) {
        this.fireid = fireid;
    }
    public String getIsfire() {
        return this.isfire;
    }
    public void setIsfire(String isfire) {
        this.isfire = isfire;
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
