package equipbid.armentum.com.equip_bid;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by meet_darshan on 04-02-2017.
 */
public class Add_Detail implements Parcelable {
    private String id, name , parent;
    boolean selected;
    public boolean AddList;

    protected Add_Detail(Parcel in) {
        id = in.readString();
        name = in.readString();
        parent = in.readString();
        selected = in.readByte() != 0;
        AddList = in.readByte() != 0;
    }

    public  Add_Detail(){};
    public static final Creator<Add_Detail> CREATOR = new Creator<Add_Detail>() {
        @Override
        public Add_Detail createFromParcel(Parcel in) {
            return new Add_Detail(in);
        }

        @Override
        public Add_Detail[] newArray(int size) {
            return new Add_Detail[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public boolean isSelected() {
        return selected;
    }
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(parent);
        dest.writeByte((byte) (selected ? 1 : 0));
        dest.writeByte((byte) (AddList ? 1 : 0));
    }
}
