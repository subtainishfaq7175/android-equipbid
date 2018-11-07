package equipbid.armentum.com.equip_bid;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by desktop on 12/5/2016.
 */

public class Gallary implements Parcelable{

    private String imagesUrl ,imgTitle , imgId;

    protected Gallary(Parcel in) {
        imagesUrl = in.readString();
        imgTitle = in.readString();
        imgId = in.readString();
    }
    public Gallary(){};

    public static final Creator<Gallary> CREATOR = new Creator<Gallary>() {
        @Override
        public Gallary createFromParcel(Parcel in) {
            return new Gallary(in);
        }

        @Override
        public Gallary[] newArray(int size) {
            return new Gallary[size];
        }
    };

    public String getImagesUrl() {
        return imagesUrl;
    }

    public void setImagesUrl(String imagesUrl) {
        this.imagesUrl = imagesUrl;
    }

    public String getImgTitle() {
        return imgTitle;
    }

    public void setImgTitle(String imgTitle) {
        this.imgTitle = imgTitle;
    }

    public String getImgId() {
        return imgId;
    }

    public void setImgId(String imgId) {
        this.imgId = imgId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imagesUrl);
        dest.writeString(imgTitle);
        dest.writeString(imgId);
    }
}
