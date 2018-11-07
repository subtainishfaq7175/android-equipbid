package equipbid.armentum.com.equip_bid;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.Comparator;

public class CustomGallery implements Parcelable,Comparable<CustomGallery>,Comparator<CustomGallery>{

	public String sdcardPath;
	public boolean isSeleted = false;
    static int index = 0;
    public int idx = 0;

    protected CustomGallery(Parcel in) {
        sdcardPath = in.readString();
        isSeleted = in.readByte() != 0;
        index = in.readInt();
        idx = in.readInt();
    }
     public CustomGallery(){};

    public static final Creator<CustomGallery> CREATOR = new Creator<CustomGallery>() {
        @Override
        public CustomGallery createFromParcel(Parcel in) {
            return new CustomGallery(in);
        }

        @Override
        public CustomGallery[] newArray(int size) {
            return new CustomGallery[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(sdcardPath);
        dest.writeByte((byte) (isSeleted ? 1 : 0));
        dest.writeInt(index);
        dest.writeInt(idx);
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    @Override
    public int compareTo(@NonNull CustomGallery compareidx) {

        int compareQuantity = ((CustomGallery)compareidx).getIdx();
        return this.idx - compareQuantity;
    }

    @Override
    public int compare(CustomGallery o1, CustomGallery o2) {
        int flag = o1.getIdx() - o2.getIdx();
        if(flag==0) flag = o1.sdcardPath.compareTo(o2.sdcardPath);
        return flag;
    }



}
