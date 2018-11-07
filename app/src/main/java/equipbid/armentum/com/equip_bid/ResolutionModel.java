package equipbid.armentum.com.equip_bid;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Darshan on 1/17/2018.
 */

public class ResolutionModel implements Parcelable {
    private String selectRe;
    private ArrayList<String> ResolutionList;
    private boolean isChecked = false;

    protected ResolutionModel(Parcel in) {
        selectRe = in.readString();
        ResolutionList = in.createStringArrayList();
        isChecked = in.readByte() != 0;
    }

    public static final Creator<ResolutionModel> CREATOR = new Creator<ResolutionModel>() {
        @Override
        public ResolutionModel createFromParcel(Parcel in) {
            return new ResolutionModel(in);
        }

        @Override
        public ResolutionModel[] newArray(int size) {
            return new ResolutionModel[size];
        }
    };

    public ResolutionModel() {

    }

    public String getSelectRe() {
        return selectRe;
    }

    public void setSelectRe(String selectRe) {
        this.selectRe = selectRe;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public ArrayList<String> getResolutionList() {
        return ResolutionList;
    }

    public void setResolutionList(ArrayList<String> resolutionList) {
        ResolutionList = resolutionList;
    }

    /**
     * Describe the kinds of special objects contained in this Parcelable
     * instance's marshaled representation. For example, if the object will
     * include a file descriptor in the output of {@link #writeToParcel(Parcel, int)},
     * the return value of this method must include the
     * {@link #CONTENTS_FILE_DESCRIPTOR} bit.
     *
     * @return a bitmask indicating the set of special object types marshaled
     * by this Parcelable object instance.
     * @see #CONTENTS_FILE_DESCRIPTOR
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Flatten this object in to a Parcel.
     *
     * @param dest  The Parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written.
     *              May be 0 or {@link #PARCELABLE_WRITE_RETURN_VALUE}.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(selectRe);
        dest.writeStringList(ResolutionList);
        dest.writeByte((byte) (isChecked ? 1 : 0));
    }
}
