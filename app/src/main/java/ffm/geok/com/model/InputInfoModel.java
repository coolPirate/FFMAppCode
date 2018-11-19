package ffm.geok.com.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.amap.api.maps.model.LatLng;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class InputInfoModel implements Parcelable{
    private InputInfoType projectType;              //工程类型
    private String projectName;                     //工程名称
    private String lable;                           //输入标签
    private InputInfoModelType inputType;           //类型
    private String defaultHint;                     //默认提示文字
    //private List<IaZAdinfoEntity> defaultAdcd;      //adcd数据集
    //private List<SysEuntlangEntity> defaultEnum;    //工程枚举数据集
    private VerificationType verification;          //验证条件
    private int minLength;                          //最小长度
    private int maxLength;                          //最大长度
    private String inputResultText;                 //结果文字
    private String resultDate;                        //结果日期
    private String radioText;                       //单选组结果文字
    private String radioCode;                       //单选组结果编码
    private String spinnerText;                     //下拉选择结果文字
    private String spinnerCode;                     //下拉选择结果编码
    private List<File> multiMedia;                  //多媒体选择结果集
    private List<String> multiInputResult;          //多文本框结果集
    private LatLng latLng;                          //工程坐标
    private InputInfoModelPattern infoModelPattern; //输入模式
    private String currAdcd;                        //当前定位adcd编码

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.projectType == null ? -1 : this.projectType.ordinal());
        dest.writeString(this.projectName);
        dest.writeString(this.lable);
        dest.writeInt(this.inputType == null ? -1 : this.inputType.ordinal());
        dest.writeString(this.defaultHint);
        //dest.writeList(this.defaultAdcd);
        //dest.writeList(this.defaultEnum);
        dest.writeInt(this.verification == null ? -1 : this.verification.ordinal());
        dest.writeInt(this.minLength);
        dest.writeInt(this.maxLength);
        dest.writeString(this.inputResultText);
        dest.writeString(this.resultDate);
        dest.writeString(this.radioText);
        dest.writeString(this.radioCode);
        dest.writeString(this.spinnerText);
        dest.writeString(this.spinnerCode);
        dest.writeList(this.multiMedia);
        dest.writeStringList(this.multiInputResult);
        dest.writeParcelable(this.latLng, flags);
        dest.writeInt(this.infoModelPattern == null ? -1 : this.infoModelPattern.ordinal());
        dest.writeString(this.currAdcd);
    }

    public InputInfoModel() {
    }
    /**
     * 工程录入信息model
     * @param lable             录入标签label
     * @param inputType         录入类型
     * @param defaultHint       默认hint
     * @param defaultAdcd       默认adcd数据
     * @param defaultData       默认工程枚举
     * @param verification      验证类型
     * @param minLength         录入最小长度
     * @param maxLength         录入最大长度
     * @param pattern           录入类型模式
     */
    public InputInfoModel(String lable, InputInfoModelType inputType, String defaultHint, VerificationType verification, int minLength, int maxLength,InputInfoModelPattern pattern) {
        this.lable = lable;
        this.inputType = inputType;
        this.defaultHint = defaultHint;
        /*this.defaultAdcd = defaultAdcd;
        this.defaultEnum = defaultData;*/
        this.verification = verification;
        this.minLength = minLength;
        this.maxLength = maxLength;
        this.infoModelPattern = pattern;
    }
    protected InputInfoModel(Parcel in) {
        int tmpProjectType = in.readInt();
        this.projectType = tmpProjectType == -1 ? null : InputInfoType.values()[tmpProjectType];
        this.projectName = in.readString();
        this.lable = in.readString();
        int tmpInputType = in.readInt();
        this.inputType = tmpInputType == -1 ? null : InputInfoModelType.values()[tmpInputType];
        this.defaultHint = in.readString();
        /*this.defaultAdcd = new ArrayList<IaZAdinfoEntity>();
        in.readList(this.defaultAdcd, IaZAdinfoEntity.class.getClassLoader());
        this.defaultEnum = new ArrayList<SysEuntlangEntity>();
        in.readList(this.defaultEnum, SysEuntlangEntity.class.getClassLoader());*/
        int tmpVerification = in.readInt();
        this.verification = tmpVerification == -1 ? null : VerificationType.values()[tmpVerification];
        this.minLength = in.readInt();
        this.maxLength = in.readInt();
        this.inputResultText = in.readString();
        this.resultDate = in.readString();
        this.radioText = in.readString();
        this.radioCode=in.readString();
        this.spinnerText = in.readString();
        this.spinnerCode = in.readString();
        this.multiMedia = new ArrayList<File>();
        in.readList(this.multiMedia, File.class.getClassLoader());
        this.multiInputResult = in.createStringArrayList();
        this.latLng = in.readParcelable(LatLng.class.getClassLoader());
        int tmpInfoModelPattern = in.readInt();
        this.infoModelPattern = tmpInfoModelPattern == -1 ? null : InputInfoModelPattern.values()[tmpInfoModelPattern];
        this.currAdcd = in.readString();
    }

    public static final Parcelable.Creator<InputInfoModel> CREATOR = new Parcelable.Creator<InputInfoModel>() {
        @Override
        public InputInfoModel createFromParcel(Parcel source) {
            return new InputInfoModel(source);
        }

        @Override
        public InputInfoModel[] newArray(int size) {
            return new InputInfoModel[size];
        }
    };

    public InputInfoType getProjectType() {
        return projectType;
    }

    public void setProjectType(InputInfoType projectType) {
        this.projectType = projectType;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getLable() {
        return lable;
    }

    public void setLable(String lable) {
        this.lable = lable;
    }

    public InputInfoModelType getInputType() {
        return inputType;
    }

    public void setInputType(InputInfoModelType inputType) {
        this.inputType = inputType;
    }

    public String getDefaultHint() {
        return defaultHint;
    }

    public void setDefaultHint(String defaultHint) {
        this.defaultHint = defaultHint;
    }

    /*public List<IaZAdinfoEntity> getDefaultAdcd() {
        return defaultAdcd;
    }

    public void setDefaultAdcd(List<IaZAdinfoEntity> defaultAdcd) {
        this.defaultAdcd = defaultAdcd;
    }

    public List<SysEuntlangEntity> getDefaultEnum() {
        return defaultEnum;
    }

    public void setDefaultEnum(List<SysEuntlangEntity> defaultEnum) {
        this.defaultEnum = defaultEnum;
    }*/

    public VerificationType getVerification() {
        return verification;
    }

    public void setVerification(VerificationType verification) {
        this.verification = verification;
    }

    public int getMinLength() {
        return minLength;
    }

    public void setMinLength(int minLength) {
        this.minLength = minLength;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    public String getInputResultText() {
        return inputResultText;
    }

    public void setInputResultText(String inputResultText) {
        this.inputResultText = inputResultText;
    }

    public String getResultDate() {
        return resultDate;
    }

    public void setResultDate(String resultDate) {
        this.resultDate = resultDate;
    }

    public String getRadioText() {
        return radioText;
    }

    public void setRadioText(String radioText) {
        this.radioText = radioText;
    }

    public String getRadioCode() {
        return radioCode;
    }

    public void setRadioCode(String radioText) {
        this.radioCode = radioCode;
    }

    public String getSpinnerText() {
        return spinnerText;
    }

    public void setSpinnerText(String spinnerText) {
        this.spinnerText = spinnerText;
    }

    public String getSpinnerCode() {
        return spinnerCode;
    }

    public void setSpinnerCode(String spinnerCode) {
        this.spinnerCode = spinnerCode;
    }

    public List<File> getMultiMedia() {
        return multiMedia;
    }

    public void setMultiMedia(List<File> multiMedia) {
        this.multiMedia = multiMedia;
    }

    public List<String> getMultiInputResult() {
        return multiInputResult;
    }

    public void setMultiInputResult(List<String> multiInputResult) {
        this.multiInputResult = multiInputResult;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public InputInfoModelPattern getInfoModelPattern() {
        return infoModelPattern;
    }

    public void setInfoModelPattern(InputInfoModelPattern infoModelPattern) {
        this.infoModelPattern = infoModelPattern;
    }

    public String getCurrAdcd() {
        return currAdcd;
    }

    public void setCurrAdcd(String currAdcd) {
        this.currAdcd = currAdcd;
    }
}
