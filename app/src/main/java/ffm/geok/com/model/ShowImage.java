package ffm.geok.com.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by zhanghs on 2017/9/4.
 */

public class ShowImage implements Serializable, Parcelable {
    private int currentIndex;
    private int selectIndex;
    private ArrayList<File> files;
    private boolean isBrowse;   //是否仅浏览

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.currentIndex);
        dest.writeInt(this.selectIndex);
        dest.writeList(this.files);
        dest.writeByte(this.isBrowse ? (byte) 1 : (byte) 0);
    }

    public ShowImage() {
    }

    protected ShowImage(Parcel in) {
        this.currentIndex = in.readInt();
        this.selectIndex = in.readInt();
        this.files = new ArrayList<File>();
        in.readList(this.files, File.class.getClassLoader());
        this.isBrowse = in.readByte() != 0;
    }

    public static final Parcelable.Creator<ShowImage> CREATOR = new Parcelable.Creator<ShowImage>() {
        @Override
        public ShowImage createFromParcel(Parcel source) {
            return new ShowImage(source);
        }

        @Override
        public ShowImage[] newArray(int size) {
            return new ShowImage[size];
        }
    };

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }

    public int getSelectIndex() {
        return selectIndex;
    }

    public void setSelectIndex(int selectIndex) {
        this.selectIndex = selectIndex;
    }

    public ArrayList<File> getFiles() {
        return files;
    }

    public void setFiles(ArrayList<File> files) {
        this.files = files;
    }

    public boolean isBrowse() {
        return isBrowse;
    }

    public void setBrowse(boolean browse) {
        isBrowse = browse;
    }
}
