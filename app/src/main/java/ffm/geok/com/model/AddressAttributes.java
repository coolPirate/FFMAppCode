package ffm.geok.com.model;

public class AddressAttributes {
    private String FID;

    private String Shape;

    private String adcd;

    private String province;

    private String city;

    private String county;

    public void setFID(String FID){
        this.FID = FID;
    }
    public String getFID(){
        return this.FID;
    }
    public void setShape(String Shape){
        this.Shape = Shape;
    }
    public String getShape(){
        return this.Shape;
    }
    public void setAdcd(String adcd){
        this.adcd = adcd;
    }
    public String getAdcd(){
        return this.adcd;
    }
    public void setProvince(String province){
        this.province = province;
    }
    public String getProvince(){
        return this.province;
    }
    public void setCity(String city){
        this.city = city;
    }
    public String getCity(){
        return this.city;
    }
    public void setCounty(String county){
        this.county = county;
    }
    public String getCounty(){
        return this.county;
    }
}
