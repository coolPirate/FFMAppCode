package ffm.geok.com.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class AddressModel implements Parcelable  {
    private String adcd;
    private String proviencd;
    private String city;
    private String county;
    private String dem;


    public AddressModel() {

    }

    protected AddressModel(Parcel in) {
        adcd = in.readString();
        proviencd = in.readString();
        city = in.readString();
        county = in.readString();
    }

    public static final Creator<AddressModel> CREATOR = new Creator<AddressModel>() {
        @Override
        public AddressModel createFromParcel(Parcel in) {
            return new AddressModel(in);
        }

        @Override
        public AddressModel[] newArray(int size) {
            return new AddressModel[size];
        }
    };

    public String getAdcd() {
        return adcd;
    }

    public void setAdcd(String adcd) {
        this.adcd = adcd;
    }

    public String getProviencd() {
        return proviencd;
    }

    public void setProviencd(String proviencd) {
        this.proviencd = proviencd;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getDem() {
        return dem;
    }

    public void setDem(String dem) {
        this.dem = dem;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(adcd);
        dest.writeString(proviencd);
        dest.writeString(city);
        dest.writeString(county);
    }
}
